package com.sft.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sft.blackcatapp.OldMainActivity;
import com.sft.blackcatapp.R;

@SuppressLint("InflateParams")
public class NoLoginDialog extends Dialog implements
		android.view.View.OnClickListener {

	private Context context;
	private ImageView image;
	private TextView content;
	private Button confirmBtn, cancelBtn;

	public NoLoginDialog(Context context) {
		super(context, R.style.dialog);
		this.context = context;
		create(this.context);
	}

	private void create(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.dialog_no_login, null);
		image = (ImageView) view.findViewById(R.id.dialog_no_login_im);
		content = (TextView) view.findViewById(R.id.dialog_no_login_content);
		confirmBtn = (Button) view
				.findViewById(R.id.dialog_no_login_confirm_btn);
		cancelBtn = (Button) view.findViewById(R.id.dialog_no_login_cancel_btn);
		setTextAndImage();
		setContentView(view);
		DisplayMetrics d = context.getResources().getDisplayMetrics();
		Window dialogWindow = getWindow();
		WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
		p.width = (int) (d.widthPixels * 0.9); // 宽度设置为屏幕宽度的0.9
		dialogWindow.setAttributes(p);
		setCanceledOnTouchOutside(true);// 设置点击Dialog外部任意区域关闭Dialog

		confirmBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
	}

	private void setTextAndImage() {
		image.setBackgroundResource(R.drawable.appointment_time_error);
		content.setText("请先登录");
		confirmBtn.setText("登录");
		cancelBtn.setText("稍后再说");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_no_login_confirm_btn:
			context.sendBroadcast(new Intent(OldMainActivity.class.getName())
					.putExtra("isRequestLogin", true));
			dismiss();
			break;
		case R.id.dialog_no_login_cancel_btn:
			dismiss();
			break;
		}
	}
}
