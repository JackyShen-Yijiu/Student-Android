package com.sft.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.sft.util.BaseUtils;

public class NoTouchRelativeLayout extends RelativeLayout {

	private boolean isOnTouch = false;
	private Context context;

	public NoTouchRelativeLayout(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {

		super(context, attrs, defStyleAttr, defStyleRes);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public NoTouchRelativeLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public NoTouchRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public NoTouchRelativeLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	// 拦截事件
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (isOnTouch) {
			int screenHeight = BaseUtils.getScreenHeight(context);
			// int screenHeight - (int) ev.getRawX();
			return true;
		} else {
			return super.dispatchTouchEvent(ev);
		}
	}

	public void setIsOnTouch(boolean isOnTouch) {
		this.isOnTouch = isOnTouch;
	}
}
