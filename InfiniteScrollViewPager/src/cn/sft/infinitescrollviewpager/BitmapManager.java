package cn.sft.infinitescrollviewpager;

import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

/**
 * 图片下载 枚举类型 1.使用线程池下载图片，对线程进行管理 2.使用Map存放图片信息，保证图片显示不错位 3.利用ListView回收机制
 * 4.将图片放入缓存，快速获取 5.单例模式创建BitmapManager 6.创建单例模式最好使用枚举类型
 * 
 * @author Jet
 * 
 */
public enum BitmapManager {
	INSTANCE;
	private final Map<String, SoftReference<Bitmap>> cache;
	private final ExecutorService pool;
	private Map<View, String> imageViews = Collections.synchronizedMap(new WeakHashMap<View, String>());
	// private Bitmap placeholder;
	private final Map<String, SoftReference<Bitmap>> cache2;
	private BitMapURLExcepteionListner listener;

	public BitmapManager setUrLErrorListener(BitMapURLExcepteionListner listener) {
		this.listener = listener;
		return this;
	}

	BitmapManager() {

		cache = new HashMap<String, SoftReference<Bitmap>>();
		cache2 = new HashMap<String, SoftReference<Bitmap>>();
		pool = Executors.newFixedThreadPool(5);

	}

	public Bitmap getBitmapFromCache(String url) {

		if (cache.containsKey(url)) {
			return cache.get(url).get();
		}
		return null;
	}

	@SuppressLint("HandlerLeak")
	public void queueJob(final String url, final CropRoundView imageView, final int width, final int height,
			final int id) {

		final Handler handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				String tag = imageViews.get(imageView);
				if (tag != null && tag.equals(url + width + height)) {
					if (msg.obj != null) {
						imageView.setBackgroundColor(Color.TRANSPARENT);
						imageView.setBkground((Bitmap) msg.obj, id);
					} else {
						// imageView.setImageBitmap(placeholder);
					}
				}
			}
		};

		pool.submit(new Runnable() {

			@Override
			public void run() {
				final Bitmap bmp = downloadBitmap(url, width, height);
				Message message = Message.obtain();
				message.obj = bmp;
				handler.sendMessage(message);
			}
		});
	}

	/**
	 * 加载图片
	 * 
	 * @param url
	 * @param imageView
	 * @param width
	 * @param height
	 */
	public void loadBitmap(final String url, final CropRoundView imageView, final int width, final int height,
			final int id) {
		// 将listview中的image放入Map
		imageViews.put(imageView, url + width + height);
		// 从内存中获取
		Bitmap bitmap = getBitmapFromCache(url + width + height);
		// check in UI thread, so no concurrency issues
		if (bitmap != null) {
			imageView.setBackgroundColor(Color.TRANSPARENT);
			imageView.setBkground(bitmap, id);
		} else {
			// imageView.setImageBitmap(placeholder);
			queueJob(url, imageView, width, height, id);
		}
	}

	/**
	 * 下载图片
	 * 
	 * @param url
	 *            下载地址
	 * @param width
	 *            图片像素宽度，越大越清晰
	 * @param height
	 *            图片像素高度，越大越清晰
	 * @return
	 */
	private Bitmap downloadBitmap(String url, int width, int height) {

		try {
			String encodeUrl = url.substring(0, 7) + URLEncoder.encode(url.substring(7), "utf-8");
			encodeUrl = encodeUrl.replace("%2F", "/").replace("%2f", "/");
			Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(encodeUrl).getContent());
			if (width > 0 && height > 0) {
				bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
			}
			cache.put(url + width + height, new SoftReference<Bitmap>(bitmap));
			return bitmap;

		} catch (Exception e) {
			if (listener != null) {
				listener.onURlError(e);
			}
		}

		return null;

	}

	/**
	 * 加载图片
	 * 
	 * @param url
	 * @param imageView
	 * @param width
	 * @param height
	 */
	public void loadBitmap2(final String url, final ImageView imageView, final int width, final int height) {
		// 将listview中的image放入Map
		imageViews.put(imageView, url + width + height);
		// 从内存中获取
		Bitmap bitmap = getBitmapFromCache2(url + width + height);
		// check in UI thread, so no concurrency issues
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
		} else {
			// imageView.setImageBitmap(placeholder);
			queueJob2(url, imageView, width, height);
		}
	}

	/**
	 * 下载图片
	 * 
	 * @param url
	 *            下载地址
	 * @param width
	 *            图片像素宽度，越大越清晰
	 * @param height
	 *            图片像素高度，越大越清晰
	 * @return
	 */
	public Bitmap downloadBitmap2(String url, int width, int height) {

		try {
			String encodeUrl = url.substring(0, 7) + URLEncoder.encode(url.substring(7), "utf-8");
			encodeUrl = encodeUrl.replace("%2F", "/").replace("%2f", "/");
			Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(encodeUrl).getContent());
			if (width > 0 && height > 0) {
				bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
			}
			cache2.put(url + width + height, new SoftReference<Bitmap>(bitmap));

			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
			if (listener != null) {
				listener.onURlError(e);
			}
		}

		return null;
	}

	@SuppressLint("HandlerLeak")
	public void queueJob2(final String url, final ImageView imageView, final int width, final int height) {

		final Handler handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				String tag = imageViews.get(imageView);
				if (tag != null && tag.equals(url + width + height)) {
					if (msg.obj != null) {
						imageView.setImageBitmap((Bitmap) msg.obj);
					} else {
						// imageView.setImageBitmap(placeholder);
					}
				}
			}
		};

		pool.submit(new Runnable() {

			@Override
			public void run() {
				final Bitmap bmp = downloadBitmap2(url, width, height);
				Message message = Message.obtain();
				message.obj = bmp;
				handler.sendMessage(message);
			}
		});
	}

	public Bitmap getBitmapFromCache2(String url) {

		if (cache2.containsKey(url)) {
			return cache2.get(url).get();
		}
		return null;
	}
}
