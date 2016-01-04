package cn.sft.infinitescrollviewpager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * A {@link ViewPager} that allows pseudo-infinite paging with a wrap-around
 * effect. Should be used with an {@link InfinitePagerAdapter}.
 */
@SuppressLint("ClickableViewAccessibility")
public class InfiniteViewPager extends ViewPager implements OnTouchListener {

	private MyHandler handler;
	private int changeTime = 4000;
	private PageChangeListener pageChangeListener;

	public InfiniteViewPager(Context context) {
		super(context);
	}

	public InfiniteViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private void stopChange() {
		if (handler != null) {
			handler.cancle();
		}
	}

	private void startChange() {
		stopChange();
		handler = new MyHandler(changeTime, true, changeTime) {
			@Override
			public void run() {
				setCurrentItem(getCurrentItem() + 1);
			}
		};
	}

	public void setChangeTime(int changeTime) {
		this.changeTime = changeTime;
		if (handler != null) {
			startChange();
		}
	}

	@Override
	public void setAdapter(final PagerAdapter adapter) {
		super.setAdapter(adapter);
		setOnTouchListener(this);
		setOnPageChangeListener(new GuidePageChangeListener());
		startChange();
		// offset first element so that we can scroll to the left
		setCurrentItem(100000);

	}

	public void setPageChangeListener(PageChangeListener listener) {
		this.pageChangeListener = listener;
	}

	@Override
	public void setCurrentItem(int item) {
		// offset the current item to ensure there is space to scroll
		item = (item % getAdapter().getCount());
		super.setCurrentItem(item);
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			stopChange();
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			stopChange();
		} else if (event.getAction() == MotionEvent.ACTION_UP
				|| event.getAction() == MotionEvent.ACTION_CANCEL) {
			startChange();
		}
		return false;
	}

	public void onResume() {
		startChange();
	}

	public void onPause() {
		stopChange();
	}

	private final class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int position) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int position) {
			position = Math.abs(position - 100000)
					% ((InfinitePagerAdapter) getAdapter()).getSize();
			if (pageChangeListener != null) {
				pageChangeListener.onPageChanged(position);
			}
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
			getParent().requestDisallowInterceptTouchEvent(true);
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			getParent().requestDisallowInterceptTouchEvent(false);
		}
		return super.onTouchEvent(event);
	}
}
