package com.sft.dialog;

import java.io.InputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jzjf.app.R;
import com.sft.event.AppointmentSuccessEvent;
import com.umeng.socialize.Config;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.ShareBoardlistener;

import de.greenrobot.event.EventBus;

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
		appointmentSuccess = getIntent().getBooleanExtra("appointmentSuccess",
				false);
		closeIv = (ImageView) findViewById(R.id.dialog_bonus_close_im);
		// send = (Button) view.findViewById(R.id.dialog_bonus_send_btn);
		title = (TextView) findViewById(R.id.dialog_bonus_title_tv);

		closeIv.setOnClickListener(new MyOnClickListener());
		// send.setOnClickListener(new MyOnClickListener());
		weixinBtn = (ImageButton) findViewById(R.id.send_invite_weixin_btn);
		weiboBtn = (ImageButton) findViewById(R.id.send_invite_weibo_btn);
		qqBtn = (ImageButton) findViewById(R.id.send_invite_qq_btn);
		messageBtn = (ImageButton) findViewById(R.id.send_invite_message_btn);

		weixinLl = (LinearLayout) findViewById(R.id.send_invite_weixin_ll);
		messageLl = (LinearLayout) findViewById(R.id.send_invite_message_ll);
		weiboLl = (LinearLayout) findViewById(R.id.send_invite_weibo_ll);
		qqLl = (LinearLayout) findViewById(R.id.send_invite_qq_ll);

		weixinBtn.setOnClickListener(new MyOnClickListener());
		weiboBtn.setOnClickListener(new MyOnClickListener());
		qqBtn.setOnClickListener(new MyOnClickListener());
		messageBtn.setOnClickListener(new MyOnClickListener());
		Log.LOG = false;
		Config.IsToastTip = false;
		setFinishOnTouchOutside(false);
		ProgressDialog dialog = new ProgressDialog(this);
		dialog.setTitle("");
		dialog.setMessage("加载中...");
		Config.dialog = dialog;

		showShareButton();
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

	private void showShareButton() {
		if (!UMShareAPI.get(this).isInstall(this, SHARE_MEDIA.WEIXIN)) {
			weixinLl.setVisibility(View.GONE);
		}
		if (!UMShareAPI.get(this).isInstall(this, SHARE_MEDIA.WEIXIN_CIRCLE)) {
			messageLl.setVisibility(View.GONE);
		}
		if (!UMShareAPI.get(this).isInstall(this, SHARE_MEDIA.QQ)) {
			qqLl.setVisibility(View.GONE);
		}
		if (!UMShareAPI.get(this).isInstall(this, SHARE_MEDIA.SINA)) {
			weiboLl.setVisibility(View.GONE);
		}
	}

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
						.withText("极致驾服，就是让你so esay的通过驾照考试！ ")
						.withTitle("极致驾服")
						.withTargetUrl("http://jizhijiafu.cn/")
						.withMedia(image).share();
				break;
			case R.id.send_invite_weibo_btn:
				/**
				 * shareaction need setplatform , callbacklistener,and
				 * content(text,image).then share it
				 **/
				new ShareAction(BonusDialog.this).setPlatform(SHARE_MEDIA.SINA)
						.setCallback(umShareListener)
						.withText("极致驾服，就是让你so esay的通过驾照考试！ ")
						.withTitle("极致驾服")
						.withTargetUrl("http://jizhijiafu.cn/")
						.withMedia(image).share();
				break;
			case R.id.send_invite_qq_btn:
				new ShareAction(BonusDialog.this).setPlatform(SHARE_MEDIA.QQ)
						.setCallback(umShareListener)
						.withText("极致驾服，就是让你so esay的通过驾照考试！ ")
						.withTitle("极致驾服")
						.withTargetUrl("http://jizhijiafu.cn/")
						.withMedia(image).share();
				break;
			case R.id.send_invite_message_btn:
				new ShareAction(BonusDialog.this)
						.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
						.setCallback(umShareListener)
						.withText("极致驾服，就是让你so esay的通过驾照考试！ ")
						.withTitle("极致驾服")
						.withTargetUrl("http://jizhijiafu.cn/")
						.withMedia(image).share();
				break;
			}
		}
	}

	private UMShareListener umShareListener = new UMShareListener() {
		@Override
		public void onResult(SHARE_MEDIA platform) {
			Toast.makeText(BonusDialog.this, " 分享成功啦", Toast.LENGTH_SHORT)
					.show();
			BonusDialog.this.finish();
			if (appointmentSuccess) {
				EventBus.getDefault().post(new AppointmentSuccessEvent());

			}
		}

		@Override
		public void onError(SHARE_MEDIA platform, Throwable t) {
			Toast.makeText(BonusDialog.this, " 分享失败啦", Toast.LENGTH_SHORT)
					.show();
			if (appointmentSuccess) {
				EventBus.getDefault().post(new AppointmentSuccessEvent());

			}
		}

		@Override
		public void onCancel(SHARE_MEDIA platform) {
			Toast.makeText(BonusDialog.this, " 分享取消啦", Toast.LENGTH_SHORT)
					.show();
		}
	};
	private ShareBoardlistener shareBoardlistener = new ShareBoardlistener() {

		@Override
		public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
			new ShareAction(BonusDialog.this).setPlatform(share_media)
					.setCallback(umShareListener).withText("多平台分享").share();
		}
	};
	private LinearLayout weixinLl;
	private LinearLayout messageLl;
	private LinearLayout weiboLl;
	private LinearLayout qqLl;
	private boolean appointmentSuccess;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/** attention to this below ,must add this **/
		UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

	}

}
