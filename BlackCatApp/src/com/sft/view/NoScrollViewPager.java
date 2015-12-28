package com.sft.view;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.sft.blackcatapp.ApplyActivity;
import com.sft.common.BlackCatApplication;
import com.sft.common.Config.EnrollResult;

public class NoScrollViewPager extends ViewPager {

	private boolean canScroll;
	private Context context;

	public NoScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public NoScrollViewPager(Context context) {
		super(context);
		this.context = context;
	}

	public boolean isCanScroll() {
		return canScroll;
	}

	public void setCanScroll(boolean canScroll) {
		this.canScroll = canScroll;
	}

	private boolean isOnly = false;

	@Override
	public void scrollTo(int x, int y) {

		if (getCurrentItem() == 1) {
			if (!isOnly) {

				String enrollState = BlackCatApplication.app.userVO
						.getApplystate();
				if (!EnrollResult.SUBJECT_ENROLL_SUCCESS.getValue().equals(
						enrollState)) {
					Intent intent = new Intent(context, ApplyActivity.class);
					context.startActivity(intent);
				}
				isOnly = true;
			}
		} else {
			super.scrollTo(x, y);

		}
	}
}
