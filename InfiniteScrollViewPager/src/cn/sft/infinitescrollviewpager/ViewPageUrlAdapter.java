package cn.sft.infinitescrollviewpager;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ViewPageUrlAdapter extends PagerAdapter {

	private String[] imageUrls;
	private Context context;
	private PageClickListener pageClickListener;
	private BitMapURLExcepteionListner urlErrorListner;
	private int width;
	private int height;

	public ViewPageUrlAdapter(Context context, String[] imageUrls, int width,
			int height) {
		this.context = context;
		this.imageUrls = imageUrls;
		this.width = width;
		this.height = height;
	}

	public void setPageClickListener(PageClickListener listener) {
		this.pageClickListener = listener;
	}

	public void setURlErrorListener(BitMapURLExcepteionListner listener) {
		this.urlErrorListner = listener;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public int getCount() {
		return imageUrls.length;
	}

	@Override
	public Object instantiateItem(ViewGroup view, final int position) {
		View imageLayout = LayoutInflater.from(context).inflate(
				R.layout.screenshot_item, view, false);
		ImageView imageView = (ImageView) imageLayout
				.findViewById(R.id.si_image);
		BitmapManager.INSTANCE.setUrLErrorListener(urlErrorListner)
				.loadBitmap2(imageUrls[position], imageView, width, height);

		view.addView(imageLayout, 0);

		imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (pageClickListener != null) {
					pageClickListener.onPageClick(position);
				}
			}
		});

		return imageLayout;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

}