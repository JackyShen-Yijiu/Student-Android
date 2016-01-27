package com.sft.util;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * 通过这个获得拍照 的intent
 */
public class PhotoUtil {
	public static final int PICTRUE_FROM_CAMERA = 1;
	public static final int PICTRUE_FROM_GALLERY = 2;
	public static final int PICTRUE_FROM_CROP = 3;
	public static final String PHOTOFILE_NAME = "image.jpg";

	public static File getPhotoFile(Context context) {
		File file;
		if (BaseUtils.isMounted()) {
			String dirPath = Constants.SD_APP_DIR;
			file = new File(dirPath);
			if (!file.exists()) {
				file.mkdirs();
			}
		} else {
			file = context.getCacheDir();
		}
		File photoFile = new File(file, PHOTOFILE_NAME);
		if (photoFile.exists()) {
			photoFile.delete();
		}
		try {
			photoFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return photoFile;
	}

	/**
	 * from camera
	 * 
	 * @param f
	 *            存储图片的file
	 * @return
	 */
	public static Intent getTakePickIntent(File f) {
		Uri uri = Uri.fromFile(f);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		return intent;
	}

	/**
	 * 获取从gallery获取图片并裁减的intent
	 * 
	 * @param f
	 *            存储图片的file
	 * @param outputX
	 *            这里默认是1：1宽高 如果设置为0 会有默认赋值600
	 * @param outputY
	 * @return
	 */
	public static Intent getPictureFromGalleryAndCrop(File f, int outputX,
			int outputY) {
		Uri uri = Uri.fromFile(f);
		if (outputX == 0 || outputY == 0) {
			outputY = 600;
			outputX = 600;
		}
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		intent.putExtra("crop", "true");

		// 裁剪框比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);

		// 图片输出大小
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("scale", true);
		// 如果为true会在onactivityresult的时候返回值 但不会往给定的file里存照片
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

		// 不启用人脸识别
		intent.putExtra("noFaceDetection", false);
		return intent;
	}

	/**
	 * uri是URI.fromFile来的 存储图片用的 <br/>
	 * 返回之后通过BitmapFactory.decodeStream(getContentResolver().openInputStream(uri
	 * ));获取bitmap
	 * 
	 * @param f
	 * @param outputX
	 *            输出图片的宽高 这里默认比例是1：1 所以x y值应该相等 如果设置为0 xy为默认值600
	 * @param outputY
	 * @return
	 */
	public static Intent getCropIntent(File f, int outputX, int outputY) {
		Uri uri = Uri.fromFile(f);
		if (outputX == 0 || outputY == 0) {
			outputY = 600;
			outputX = 600;
		}
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");// 可裁剪
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra("return-data", false);// 若为false则表示不返回数据
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true);
		return intent;
	}
}
