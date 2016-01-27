package com.sft.dialog;

import java.io.InputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sft.blackcatapp.R;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

@SuppressLint("InflateParams")
public class BonusDialog extends Activity {
	private ImageView closeIv;
	// private Button send;
	private TextView title;

	// 微信
	private ImageButton weixinBtn;
	// 微博
	private ImageButton weiboBtn;
	// qq
	private ImageButton qqBtn;
	// 短信
	private ImageButton messageBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.dialog_bonus);
		closeIv = (ImageView) findViewById(R.id.dialog_bonus_close_im);
		// send = (Button) view.findViewById(R.id.dialog_bonus_send_btn);
		title = (TextView) findViewById(R.id.dialog_bonus_title_tv);

		closeIv.setOnClickListener(new MyOnClickListener());
		// send.setOnClickListener(new MyOnClickListener());
		weixinBtn = (ImageButton) findViewById(R.id.send_invite_weixin_btn);
		weiboBtn = (ImageButton) findViewById(R.id.send_invite_weibo_btn);
		qqBtn = (ImageButton) findViewById(R.id.send_invite_qq_btn);
		messageBtn = (ImageButton) findViewById(R.id.send_invite_message_btn);

		weixinBtn.setOnClickListener(new MyOnClickListener());
		weiboBtn.setOnClickListener(new MyOnClickListener());
		qqBtn.setOnClickListener(new MyOnClickListener());
		messageBtn.setOnClickListener(new MyOnClickListener());

		setFinishOnTouchOutside(false);
		// DisplayMetrics d = getResources().getDisplayMetrics();
		// Window dialogWindow = getWindow();
		// WindowManager.LayoutParams p = dialogWindow.getAttributes(); //
		// 获取对话框当前的参数值
		// p.width = (int) (d.widthPixels * 0.95); // 宽度设置为屏幕宽度的0.9
		// dialogWindow.setAttributes(p);
	}

	// private void create(Context context) {
	//
	// setCanceledOnTouchOutside(true);// 设置点击Dialog外部任意区域关闭Dialog
	// }

	private class MyOnClickListener implements
			android.view.View.OnClickListener {

		@SuppressWarnings("deprecation")
		@Override
		public void onClick(View v) {
			InputStream inputStream = getResources().openRawResource(
					R.drawable.ybxch_official_website_qrcode);
			UMImage image = new UMImage(BonusDialog.this, new BitmapDrawable(
					inputStream).getBitmap());
			// UMusic music = new UMusic(
			// "http://music.huoxing.com/upload/20130330/1364651263157_1085.mp3");
			// music.setTitle("sdasdasd");
			// music.setThumb(new UMImage(BonusDialog.this,
			// "http://www.umeng.com/images/pic/social/chart_1.png"));
			// UMVideo video = new UMVideo(
			// "http://video.sina.com.cn/p/sports/cba/v/2013-10-22/144463050817.html");

			switch (v.getId()) {
			case R.id.dialog_bonus_close_im:
				finish();
				break;
			// case R.id.dialog_bonus_send_btn:
			// context.sendBroadcast(new Intent(MyWalletActivity.class
			// .getName()).putExtra("sendInvite", true));
			// dismiss();
			// break;
			case R.id.send_invite_weixin_btn:
				new ShareAction(BonusDialog.this)
						.setPlatform(SHARE_MEDIA.WEIXIN)
						.setCallback(umShareListener)
						.withText("一步学车，就是让你so esay的通过驾照考试！ ")
						.withTitle("一步学车")
						.withTargetUrl("http://www.ybxch.com").withMedia(image)
						.share();
				break;
			case R.id.send_invite_weibo_btn:
				/**
				 * shareaction need setplatform , callbacklistener,and
				 * content(text,image).then share it
				 **/
				new ShareAction(BonusDialog.this).setPlatform(SHARE_MEDIA.SINA)
						.setCallback(umShareListener)
						.withText("一步学车，就是让你so esay的通过驾照考试！ ")
						.withTitle("一步学车")
						.withTargetUrl("http://www.ybxch.com").withMedia(image)
						.share();
				break;
			case R.id.send_invite_qq_btn:
				new ShareAction(BonusDialog.this).setPlatform(SHARE_MEDIA.QQ)
						.setCallback(umShareListener)
						.withText("一步学车，就是让你so esay的通过驾照考试！ ")
						.withTitle("一步学车")
						.withTargetUrl("http://www.ybxch.com").withMedia(image)
						.share();
				break;
			case R.id.send_invite_message_btn:
				new ShareAction(BonusDialog.this)
						.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
						.setCallback(umShareListener)
						.withText("一步学车，就是让你so esay的通过驾照考试！ ")
						.withTitle("一步学车")
						.withTargetUrl("http://www.ybxch.com").withMedia(image)
						.share();
				break;
			}
		}
	}

	private UMShareListener umShareListener = new UMShareListener() {
		@Override
		public void onResult(SHARE_MEDIA platform) {
			Toast.makeText(BonusDialog.this, platform + " 分享成功啦",
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onError(SHARE_MEDIA platform, Throwable t) {
			Toast.makeText(BonusDialog.this, platform + " 分享失败啦",
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onCancel(SHARE_MEDIA platform) {
			Toast.makeText(BonusDialog.this, platform + " 分享取消了",
					Toast.LENGTH_SHORT).show();
		}
	};
	private ShareBoardlistener shareBoardlistener = new ShareBoardlistener() {

		@Override
		public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
			new ShareAction(BonusDialog.this).setPlatform(share_media)
					.setCallback(umShareListener).withText("多平台分享").share();
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/** attention to this below ,must add this **/
		UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

	}
}
