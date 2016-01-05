package com.sft.blackcatapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.baseactivity.util.MyHandler;
import cn.sft.infinitescrollviewpager.CropRoundView;

import com.edmodo.cropper.CropImageView;
import com.edmodo.cropper.PreviewListener;
import com.sft.common.Config;
import com.sft.util.Util;
import com.sft.viewutil.ZProgressHUD;

/**
 * 更换头像界面
 * 
 * @author Administrator
 * 
 */
@SuppressLint("SimpleDateFormat")
public class CropImageActivity extends BaseActivity implements OnClickListener,
		PreviewListener {

	private static final String changeHead = "changeHead";
	private CropImageView cropImageView;
	private TextView cropText;
	private CropRoundView cropImage;
	private Button picBtn, cameraBtn;

	private int CAMERA = 1;
	private int PICTURE = 2;

	public static final int selectCamera = 3;
	public static final int selectPic = 4;

	private Uri imageFilePath;
	// 拍照的存储路径
	private static final String cameraPath = Config.PICPATH + File.separator
			+ "camera.jpg";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_crop_image);
		initView();
		setListener();
	}

	@Override
	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	}

	private void initView() {
		setTitleText(R.string.change_headpic);

		cropImageView = (CropImageView) findViewById(R.id.cropimageview);
		cropText = (TextView) findViewById(R.id.croptext);
		cropImage = (CropRoundView) findViewById(R.id.cropImage);
		picBtn = (Button) findViewById(R.id.cropimage_pic_btn);
		cameraBtn = (Button) findViewById(R.id.cropimage_camera_btn);

		RelativeLayout.LayoutParams imageParams = (RelativeLayout.LayoutParams) cropImage
				.getLayoutParams();
		imageParams.width = (int) (screenWidth * 0.45);
		imageParams.height = imageParams.width;

		cropText.setText("拖动选择框预览头像");
		cropText.setVisibility(View.INVISIBLE);
	}

	private void setListener() {
		cropImageView.setOnPreviewListener(this);
		picBtn.setOnClickListener(this);
		cameraBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.base_left_btn:
			finish();
			break;
		case R.id.cropimage_camera_btn:
			byCamera();
			break;
		case R.id.cropimage_pic_btn:
			byPicture();
			break;
		case R.id.base_right_tv:
			try {
				upload();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
	}

	private void byCamera() {
		File filePath = new File(Config.PICPATH + File.separator);
		if (!filePath.exists()) {
			filePath.mkdirs();
		}
		File file = new File(cameraPath); // 创建一个文件
		this.imageFilePath = Uri.fromFile(file);
		Intent getImageByCamera = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, this.imageFilePath);
		startActivityForResult(getImageByCamera, CAMERA);
	}

	private void byPicture() {
		Intent intent;
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
		} else {
			intent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					"image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
		}
		startActivityForResult(intent, PICTURE);
	}

	private Bitmap cameraReturn() {
		BitmapFactory.Options op = new BitmapFactory.Options();
		op.inSampleSize = 2;
		Bitmap bitmap = BitmapFactory.decodeFile(cameraPath, op);
		return bitmap;
	}

	private Bitmap picReturn(Intent data) {
		Uri uri = data.getData();
		ContentResolver cr = this.getContentResolver();
		Bitmap bitmap = null;
		InputStream is = null;
		try {
			is = cr.openInputStream(uri);
			bitmap = BitmapFactory.decodeStream(is);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bitmap;
	}

	private Bitmap scaleBitmap(Bitmap src, float w) {
		Bitmap bitmap = null;
		final int width = src.getWidth(); // 获取图像宽
		final int height = src.getHeight(); // 获取图像高
		final float sx = w / width; // 计算X轴伸缩因子
		Matrix m = new Matrix();
		m.setScale(sx, sx);
		bitmap = Bitmap.createBitmap(src, 0, 0, width, height, m, false); // 返回目的图像
		return bitmap;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		Bitmap img = null;
		if (resultCode == RESULT_OK) {
			if (requestCode == CAMERA) {
				// 拍照获取图片
				img = cameraReturn();
			} else if (requestCode == PICTURE) {
				// 本地图片
				img = picReturn(data);
			}
			cropText.setVisibility(View.VISIBLE);
		}
		try {
			// 首先取得屏幕对象
			DisplayMetrics dm = getResources().getDisplayMetrics();
			// 获取屏幕的宽和高
			int dw = dm.widthPixels;
			int dh = dm.heightPixels;
			// 手机屏幕的宽高比
			float phoneRadio = (float) dw / dh;
			// 图像宽高比
			float imageRadio = (float) img.getWidth() / img.getHeight();

			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) cropImageView
					.getLayoutParams();

			// 手机剩余的高度
			int maxHeight = (int) (dh - statusbarHeight
					- dp2px(139 + 5 + 10 + 20 + 44) - dw * 0.45);
			if (phoneRadio <= imageRadio) {
				// 图像太宽
				params.width = dw * 3 / 5;
				params.height = params.width * img.getHeight() / img.getWidth();
				if (params.height > maxHeight) {
					params.height = maxHeight;
					params.width = params.height * img.getWidth()
							/ img.getHeight();
				}
			} else {
				params.width = params.height;
				if (params.width >= dw - px2dp(80f)) {
					params.width = dw - px2dp(80f);
					params.height = params.width * img.getHeight()
							/ img.getWidth();
				}
			}

			cropImageView.setLayoutParams(params);
			cropImageView.setImageBitmap(scaleBitmap(img, params.width));
			new MyHandler(200) {
				@Override
				public void run() {
					showTitlebarText(BaseActivity.SHOW_RIGHT_TEXT);
					setText(0, R.string.finish);
					headPicChanged();
				}
			};
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 把得到的图片绑定在控件上显示
		img = null;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onImageSelectChanged() {
		headPicChanged();
	}

	private void headPicChanged() {
		try {
			cropImage.setBackgroundColor(Color.TRANSPARENT);
			cropImage.setBkground(cropImageView.getCroppedImage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 删除拍照后的图片
		util.deleteFile(new File(cameraPath));
	}

	/**
	 * 上传头像
	 */
	protected void upload() {
		try {
			Date date = new Date();
			SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat format2 = new SimpleDateFormat("HHmmssSSS");
			String path = format1.format(date);
			String fileName = format2.format(date) + "-"
					+ app.userVO.getUserid() + ".png";

			File file = Util.savePic(cropImageView.getCroppedImage(), path,
					fileName);
			app.uploadPic(file, path + "/" + fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void forOperResult(Intent intent) {
		String info = intent.getStringExtra("info");
		if (!TextUtils.isEmpty(info)) {
			if (info.contains("error:null")) {
				// 上传七牛成功，将地址发送给服务器
				String key = intent.getStringExtra("res");
				if (!TextUtils.isEmpty(key)) {
					changeHeadPic(key);
				} else {
					toast.setText("上传失败！");
				}
			}
		}
	}

	private void changeHeadPic(String key) {
		try {
			JSONObject jsonObject = new JSONObject(key);
			app.userVO.getHeadportrait().setOriginalpic(
					"http://7xnjg0.com1.z0.glb.clouddn.com/"
							+ jsonObject.getString("key"));
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("userid", app.userVO.getUserid());
			paramMap.put("headportrait", app.userVO.getHeadportrait()
					.toString());

			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("authorization", app.userVO.getToken());
			HttpSendUtils.httpPostSend(changeHead, this, Config.IP
					+ "api/v1/userinfo/updateuserinfo", paramMap, 10000,
					headerMap);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		if (type.equals(changeHead)) {
			if (dataString != null) {
				ZProgressHUD.getInstance(this).show();
				ZProgressHUD.getInstance(this).dismissWithSuccess("上传成功");
				new MyHandler(500) {
					@Override
					public void run() {
						finish();
					}
				};
			}
		}
		return true;
	}

	private int dp2px(float dpValue) {
		final float scale = getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public int px2dp(float pxValue) {
		final float scale = getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
}
