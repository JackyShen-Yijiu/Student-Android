package cn.sft.infinitescrollviewpager;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ViewPageAdapter extends PagerAdapter {

	private int[] imageIds;
	private Context context;
	private PageClickListener pageClickListener;

	public ViewPageAdapter(Context context, int[] imageIds) {
		this.context = context;
		this.imageIds = imageIds;
	}

	public void setPageClickListener(PageClickListener listener) {
		this.pageClickListener = listener;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public int getCount() {
		return imageIds.length;
	}

	@Override
	public Object instantiateItem(ViewGroup view, final int position) {
		View imageLayout = LayoutInflater.from(context).inflate(
				R.layout.screenshot_item, view, false);
		ImageView imageView = (ImageView) imageLayout
				.findViewById(R.id.si_image);

		imageView.setBackgroundResource(imageIds[position]);

		view.addView(imageLayout, 0);

		imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (pageClickListener != null) {
					pageClickListener.onPageClick(position);
				}
			}
		});

		return imageLayout;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

}