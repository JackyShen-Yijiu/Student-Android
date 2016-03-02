package com.sft.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jzjf.app.R;

public class AsyncProgressDialog extends ProgressDialog {

	private Context context;
	private TextView tvMsg;
	private RelativeLayout rlDialogView;

	public AsyncProgressDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
		rlDialogView = (RelativeLayout) LayoutInflater.from(context).inflate(
				R.layout.apply_dialog_progress, null, false);
		tvMsg = (TextView) rlDialogView.findViewById(R.id.progress_msg);
	}

	public AsyncProgressDialog(Context context) {
		this(context, 0);
	}

	public void setContent(int resid) {
		if (resid > 0) {
			String content = context.getResources().getString(resid);
			setContent(content);
		}
	}

	public void setContent(String content) {
		if (content != null && !"".equals(content.trim())) {
			if (tvMsg != null) {
				tvMsg.setText(content);
			}
		}
	}

	@Override
	public void show() {
		super.show();
		setContentView(rlDialogView);
	}
}
