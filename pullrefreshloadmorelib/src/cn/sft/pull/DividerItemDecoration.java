package cn.sft.pull;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {


	private int mOrientation = LinearLayoutManager.VERTICAL;


	private int mItemSize = 1;


	private Paint mPaint;


	public DividerItemDecoration(Context context, int orientation,
			int deviderWidth, int deviderColor) {
		this.mOrientation = orientation;
		if (orientation != LinearLayoutManager.VERTICAL
				&& orientation != LinearLayoutManager.HORIZONTAL) {
			throw new IllegalArgumentException("�봫����ȷ�Ĳ���");
		}
		// mItemSize = (int) TypedValue.applyDimension((int) deviderWidth,
		// TypedValue.COMPLEX_UNIT_DIP, context.getResources()
		// .getDisplayMetrics());
		mItemSize = deviderWidth;
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(deviderColor);
		/* ������� */
		mPaint.setStyle(Paint.Style.FILL);
	}

	@Override
	public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
		if (mOrientation == LinearLayoutManager.VERTICAL) {
			drawVertical(c, parent);
		} else {
			drawHorizontal(c, parent);
		}
	}


	private void drawVertical(Canvas canvas, RecyclerView parent) {
		final int left = parent.getPaddingLeft();
		final int right = parent.getMeasuredWidth() - parent.getPaddingRight();
		final int childSize = parent.getChildCount();
		for (int i = 0; i < childSize; i++) {
			final View child = parent.getChildAt(i);
			RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child
					.getLayoutParams();
			final int top = child.getBottom() + layoutParams.bottomMargin;
			final int bottom = top + mItemSize;
			canvas.drawRect(left, top, right, bottom, mPaint);
		}
	}


	private void drawHorizontal(Canvas canvas, RecyclerView parent) {
		final int top = parent.getPaddingTop();
		final int bottom = parent.getMeasuredHeight()
				- parent.getPaddingBottom();
		final int childSize = parent.getChildCount();
		for (int i = 0; i < childSize; i++) {
			final View child = parent.getChildAt(i);
			RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child
					.getLayoutParams();
			final int left = child.getRight() + layoutParams.rightMargin;
			final int right = left + mItemSize;
			canvas.drawRect(left, top, right, bottom, mPaint);
		}
	}

	@Override
	public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
			RecyclerView.State state) {
		if (mOrientation == LinearLayoutManager.VERTICAL) {
			outRect.set(0, 0, 0, mItemSize);
		} else {
			outRect.set(0, 0, mItemSize, 0);
		}
	}
}