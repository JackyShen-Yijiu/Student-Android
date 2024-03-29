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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sft.blackcatapp.R;

@SuppressLint("InflateParams")
public class CustomDialog extends Dialog implements
		android.view.View.OnClickListener {

	public static final int APPOINTMENT_TIME_ERROR = 1;
	public static final int APPOINTMENT_TIME_SUCCESS = 2;
	public static final int ENROLL_SUCCESS = 3;
	public static final int VERTIFY_ENROLL = 4;
	public static final int APPLY_EXAM = 5;
	public static final int APPOINTMENT_COMPLAIN = 6;
	public static final int ERROR_COACH = 7;

	private Context context;
	private ImageView image;
	private TextView title, content;
	private LinearLayout buttonsLayout;
	private Button confirmBtn;
	private Button cancelBtn;

	public CustomDialog(Context context, int style) {
		super(context, R.style.dialog);
		this.context = context;
		create(this.context, style);
	}

	private void create(Context context, int style) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.dialog_enroll_success, null);
		image = (ImageView) view.findViewById(R.id.dialog_im);
		title = (TextView) view.findViewById(R.id.dialog_title);
		content = (TextView) view.findViewById(R.id.dialog_content);

		buttonsLayout = (LinearLayout) view.findViewById(R.id.dialog_buttons);
		confirmBtn = (Button) view.findViewById(R.id.dialog_confirm_btn);
		cancelBtn = (Button) view.findViewById(R.id.dialog_cancel_btn);

		buttonsLayout.setVisibility(View.GONE);
		setTextAndImage(style);
		setContentView(view);
		DisplayMetrics d = context.getResources().getDisplayMetrics();
		Window dialogWindow = getWindow();
		WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
		p.width = (int) (d.widthPixels * 0.9); // 宽度设置为屏幕宽度的0.9
		dialogWindow.setAttributes(p);
		setCanceledOnTouchOutside(true);// 设置点击Dialog外部任意区域关闭Dialog
	}

	private void setTextAndImage(int style) {
		switch (style) {
		case APPOINTMENT_TIME_ERROR:
			image.setBackgroundResource(R.drawable.appointment_time_error);
			title.setText(R.string.appointment_time_incontinuity);
			content.setText(R.string.appointment_time_separate);
			break;
		case APPOINTMENT_TIME_SUCCESS:
			image.setBackgroundResource(R.drawable.appointment_success);
			title.setText(R.string.appointment_time_success);
			content.setText(R.string.coach_confirm_appointment_time);

			// 显示分享按钮
			buttonsLayout.setVisibility(View.VISIBLE);
			confirmBtn.setOnClickListener(this);
			cancelBtn.setOnClickListener(this);
			break;
		case ENROLL_SUCCESS:
			image.setBackgroundResource(R.drawable.appointment_success);
			title.setText(R.string.enroll_success);
			content.setText(R.string.we_contact_yout);
			break;
		case VERTIFY_ENROLL:
			image.setBackgroundResource(R.drawable.appointment_success);
			title.setText(R.string.commit_success);
			content.setText(R.string.system_vertify);
			break;
		case APPLY_EXAM:
			image.setBackgroundResource(R.drawable.appointment_success);
			title.setText(R.string.commit_success);
			content.setText(R.string.apply_exam_success);
			break;
		case APPOINTMENT_COMPLAIN:
			image.setBackgroundResource(R.drawable.appointment_success);
			title.setText(R.string.complain_success);
			content.setVisibility(View.GONE);
			break;
		case ERROR_COACH:
			image.setBackgroundResource(R.drawable.appointment_time_error);
			title.setText(R.string.driveing_school_error);
			content.setText(R.string.select_right_driveing_school);
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_confirm_btn:
			Intent intent = new Intent(context, BonusDialog.class);
			context.startActivity(intent);
			dismiss();
			break;
		case R.id.dialog_cancel_btn:
			dismiss();
			break;

		default:
			break;
		}
	}
}
