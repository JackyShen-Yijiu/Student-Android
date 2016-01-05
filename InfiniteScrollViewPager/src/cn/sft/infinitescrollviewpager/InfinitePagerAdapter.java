package cn.sft.infinitescrollviewpager;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * A PagerAdapter that wraps around another PagerAdapter to handle paging
 * wrap-around.
 */
public class InfinitePagerAdapter extends PagerAdapter {

	private PagerAdapter adapter;
	private int[] imageIds;
	private String[] imageUrls;
	private ViewPageAdapter viewPageAdapter;
	private ViewPageUrlAdapter viewPageUrlAdapter;

	public InfinitePagerAdapter(Context context, int[] imageIds) {
		viewPageAdapter = new ViewPageAdapter(context, imageIds);
		this.imageIds = imageIds;
		this.adapter = viewPageAdapter;
	}

	public InfinitePagerAdapter(Context context, String[] imageUrls, int width,
			int height) {
		viewPageUrlAdapter = new ViewPageUrlAdapter(context, imageUrls, width,
				height);
		this.imageUrls = imageUrls;
		this.adapter = viewPageUrlAdapter;
	}

	public InfinitePagerAdapter(PagerAdapter adapter) {
		this.adapter = adapter;
	}

	public void setURLErrorListener(BitMapURLExcepteionListner listener) {
		if (viewPageUrlAdapter != null)
			viewPageUrlAdapter.setURlErrorListener(listener);
	}

	public void setPageClickListener(PageClickListener listener) {
		if (viewPageAdapter != null)
			viewPageAdapter.setPageClickListener(listener);
		if (viewPageUrlAdapter != null)
			viewPageUrlAdapter.setPageClickListener(listener);
	}

	public int getSize() {
		return imageIds != null ? imageIds.length : imageUrls.length;
	}

	@Override
	public int getCount() {
		// warning: scrolling to very high values (1,000,000+) results in
		// strange drawing behaviour
		return Integer.MAX_VALUE;
	}

	/**
	 * @return the {@link #getCount()} result of the wrapped adapter
	 */
	public int getRealCount() {
		return adapter.getCount();
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		int virtualPosition = Math.abs(position - 100000) % getRealCount();
		// only expose virtual position to the inner adapter
		return adapter.instantiateItem(container, virtualPosition);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		int virtualPosition = position % getRealCount();
		// only expose virtual position to the inner adapter
		adapter.destroyItem(container, virtualPosition, object);
	}

	/*
	 * Delegate rest of methods directly to the inner adapter.
	 */

	@Override
	public void finishUpdate(ViewGroup container) {
		adapter.finishUpdate(container);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return adapter.isViewFromObject(view, object);
	}

	@Override
	public void restoreState(Parcelable bundle, ClassLoader classLoader) {
		adapter.restoreState(bundle, classLoader);
	}

	@Override
	public Parcelable saveState() {
		return adapter.saveState();
	}

	@Override
	public void startUpdate(ViewGroup container) {
		adapter.startUpdate(container);
	}
}
