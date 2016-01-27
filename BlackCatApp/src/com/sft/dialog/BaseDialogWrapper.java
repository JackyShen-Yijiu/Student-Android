package com.sft.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface.OnDismissListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;

import com.sft.blackcatapp.R;

public class BaseDialogWrapper {
	private Activity mActivity;
	private Dialog mDialog;
	private View mContent;
	private OnDismissListener mOnDialogDismissListener;

	public BaseDialogWrapper(Activity act, int resId) {
		this.mActivity = act;
		mContent = loadLayout(resId);
		initDialog();
	}

	public BaseDialogWrapper(Activity act, View view) {
		this.mActivity = act;
		mContent = view;
		initDialog();
	}

	protected void onFinishInflate(View view) {
		// Should be override if init with #DialogView(Context context, int
		// layoutId)
	}

	private View loadLayout(int layoutId) {
		View v = LayoutInflater.from(mActivity).inflate(layoutId, null);
		onFinishInflate(v);
		return v;
	}

	public boolean isShowing() {
		if (mDialog != null && mDialog.isShowing()) {
			return true;
		}
		return false;
	}

	protected void initDialog() {
		mDialog = new Dialog(mActivity, R.style.DialogViewTheme);
		// 设置dialog显示的位置
		mDialog.getWindow().setGravity(Gravity.CENTER);
		// 设置dialog进出动画
		mDialog.getWindow().setWindowAnimations(R.style.DialogViewAnimation);
		// 设置dialog 点击返回键的时候可以消失
		mDialog.setCancelable(true);
		// 设置在点击dialog外面的时候 消失dialog
		mDialog.setCanceledOnTouchOutside(true);
		// 设置dialog的内容 这里已经通过theme 把dialog的title去掉了
		mDialog.setContentView(mContent);
	}

	/**
	 * 留下一个设置显示位置的方法 用于扩展<br/>
	 * 默认是GRAVITY.CENTER 在屏幕居中显示
	 * 
	 * @param gravity
	 *            参考android.view.Gravity 的值
	 */
	public void setGravity(int gravity) {
		mDialog.getWindow().setGravity(gravity);
	}

	public void setFullWidth(boolean isFull) {
		if (isFull && mDialog != null) {
			LayoutParams lp = mDialog.getWindow().getAttributes();
			lp.width = LayoutParams.MATCH_PARENT;
			lp.height = LayoutParams.WRAP_CONTENT;
		}
	}

	public void setFullScreen(boolean isFull) {
		if (isFull && mDialog != null) {
			LayoutParams lp = mDialog.getWindow().getAttributes();
			lp.width = LayoutParams.MATCH_PARENT;
			lp.height = LayoutParams.MATCH_PARENT;
		}
	}

	public void showDialog() {
		if (mDialog != null && !mDialog.isShowing()) {
			mDialog.show();
			// 在stackoverflow上面查到的 解决办法 但是不明白为什么show之后再设置 才能控制dialog的宽度
			// show之前设置没用
			// LayoutParams lp = dialog.getWindow().getAttributes();
			// lp.width = LayoutParams.MATCH_PARENT;
			// lp.height = LayoutParams.WRAP_CONTENT;
			// dialog.getWindow().setAttributes(lp);
		}
	}

	public void dismissDialog() {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
		}
	}

	public void setCancelable(boolean cancel) {
		if (mDialog != null) {
			mDialog.setCancelable(cancel);
		}
	}

	public void setCanceledOnTouchOutside(boolean cancel) {
		if (mDialog != null) {
			mDialog.setCanceledOnTouchOutside(cancel);
		}
	}

	/**
	 * @return the mOnDialogDismissListener
	 */
	public OnDismissListener getOnDialogDismissListener() {
		return mOnDialogDismissListener;
	}

	/**
	 * @param onDialogDismissListener
	 *            the mOnDialogDismissListener to set
	 */
	public void setOnDialogDismissListener(
			OnDismissListener onDialogDismissListener) {
		this.mOnDialogDismissListener = onDialogDismissListener;
		if (mDialog != null) {
			mDialog.setOnDismissListener(mOnDialogDismissListener);
		}
	}
}
