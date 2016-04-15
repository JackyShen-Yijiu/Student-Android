package com.sft.viewutil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * 重写ScrollView,以解决ScrollView与水平listView滑动时冲突
 */
public class MyScrollView extends ScrollView {
	private GestureDetector mGestureDetector;
	View.OnTouchListener mGestureListener;

	@SuppressWarnings("deprecation")
	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mGestureDetector = new GestureDetector(new YScrollDetector());
		setFadingEdgeLength(0);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev)
				&& mGestureDetector.onTouchEvent(ev);
	}

	class YScrollDetector extends SimpleOnGestureListener {
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			if (Math.abs(distanceY) > Math.abs(distanceX)) {
				return true;
			}
			return false;
		}
	}

	/** 监听滑动位置 */
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		if (listener != null)
			listener.onScrollChanged(t, oldt);
		super.onScrollChanged(l, t, oldl, oldt);
	}

	public interface scrollStateListener {
		public void onScrollChanged(int t, int oldt);
	}

	public void setOnStateListener(scrollStateListener listener) {
		this.listener = listener;
	}

	scrollStateListener listener;

}
