package com.sft.dialog;

import java.util.Calendar;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TextView;

import com.jzjf.app.R;

public class DateTimeDialog extends Dialog implements
		android.view.View.OnClickListener {

	private Context context;
	private TextView timeTv;
	private DatePicker datePicker;
	private TextView dateConfirm;
	private TextView dateCancel;

	public DateTimeDialog(Context context) {
		super(context, R.style.dialog);
		this.context = context;
		create(this.context);
	}

	private void create(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.date_picker_dialog, null);
		timeTv = (TextView) view.findViewById(R.id.time_tv);
		datePicker = (DatePicker) view.findViewById(R.id.datePicker);
		dateCancel = (TextView) view.findViewById(R.id.date_cancel);
		dateConfirm = (TextView) view.findViewById(R.id.date_confirm);
		setContentView(view);
		DisplayMetrics d = context.getResources().getDisplayMetrics();
		Window dialogWindow = getWindow();
		WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
		p.width = (int) (d.widthPixels * 0.7); // 宽度设置为屏幕宽度的0.9
		p.height = p.width;
		dialogWindow.setAttributes(p);
		setCanceledOnTouchOutside(true);// 设置点击Dialog外部任意区域关闭Dialog

		dateCancel.setOnClickListener(this);
		dateConfirm.setOnClickListener(this);
	}

	public void setData(String tag, Calendar date,
			OnDateChangedListener listener) {
		timeTv.setText(tag);
		datePicker.init(date.get(Calendar.YEAR), date.get(Calendar.MONTH),
				date.get(Calendar.DAY_OF_MONTH), listener);
	}

	public void setConfirmListener(View.OnClickListener listener) {
		dateConfirm.setOnClickListener(listener);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.date_cancel) {
			this.dismiss();
		} else if (v.getId() == R.id.date_confirm) {
			this.dismiss();
		}
	}
}
