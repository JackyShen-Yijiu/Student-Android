package cn.sft.pull;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.cardview.R;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class RefreshLoadMoreView extends LinearLayout {
	static final int VERTICAL = LinearLayoutManager.VERTICAL;
	static final int HORIZONTAL = LinearLayoutManager.HORIZONTAL;
	private RecyclerView recyclerView;
	private SwipeRefreshLayout swipeRfl;
	private LinearLayoutManager layoutManager;
	private OnRefreshListener mRefreshListener;
	private RecyclerView.OnScrollListener mScrollListener;
	private RefreshLoadMoreListener mRefreshLoadMoreListner;
	private boolean hasMore = true;
	private boolean isRefresh = false;
	private boolean isLoadMore = false;
	private Context context;
	private int deviderWidth = 0;
	private int deviderColor;

	private AutoScrollListener autoScrollListener;

	public RefreshLoadMoreView(Context context) {
		super(context, null);
	}

	public RefreshLoadMoreView(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.CardView);
		deviderWidth = a.getDimensionPixelSize(
				R.styleable.CardView_deviderWidth, 0);
		deviderColor = a.getColor(R.styleable.CardView_deviderColor,
				Color.parseColor("#00000000"));

		a.recycle();

		this.context = context;
		LinearLayout rootLl = new LinearLayout(context);
		rootLl.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		swipeRfl = new SwipeRefreshLayout(context);
		swipeRfl.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		recyclerView = new RecyclerView(context);
		recyclerView.setVerticalScrollBarEnabled(true);
		recyclerView.setHorizontalScrollBarEnabled(true);
		recyclerView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		swipeRfl.addView(recyclerView);
		rootLl.addView(swipeRfl);
		this.addView(rootLl);

		mScrollListener = new RecyclerView.OnScrollListener() {
			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				int lastVisibleItem = layoutManager
						.findLastVisibleItemPosition();
				int firstCompleteVisibleItem = layoutManager
						.findFirstCompletelyVisibleItemPosition();
				if (firstCompleteVisibleItem == 0) {
					setPullRefreshEnable(true);
				} else {
					setPullRefreshEnable(false);
				}

				int totalItemCount = layoutManager.getItemCount();
				if (hasMore && (lastVisibleItem >= totalItemCount - 1)
						&& !isLoadMore) {
					isLoadMore = true;
					loadMore();
				}

			}

			@Override
			public void onScrollStateChanged(RecyclerView recyclerView,
					int newState) {
				super.onScrollStateChanged(recyclerView, newState);
				if (newState == RecyclerView.SCROLL_STATE_IDLE) {
					if (autoScrollListener != null) {
						autoScrollListener.autoScroll(recyclerView);
					}
				}
			}
		};

		mRefreshListener = new OnRefreshListener() {

			@Override
			public void onRefresh() {
				if (!isRefresh) {
					isRefresh = true;
					refresh();
				}
			}
		};

		swipeRfl.setOnRefreshListener(mRefreshListener);
		recyclerView.setHasFixedSize(true);
		layoutManager = new LinearLayoutManager(context);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.addItemDecoration(new DividerItemDecoration(context,
				getOrientation(), deviderWidth, deviderColor));
		recyclerView.setOnScrollListener(mScrollListener);
	}

	public void scrollTo(int x, int y) {
		recyclerView.scrollTo(x, y);
	}
	
	public void setOrientation(int orientation) {
		if (orientation != 0 && orientation != 1) {
			layoutManager.setOrientation(VERTICAL);
		} else {
			layoutManager.setOrientation(orientation);
		}
		recyclerView.addItemDecoration(new DividerItemDecoration(context,
				getOrientation(), deviderWidth, deviderColor));
	}

	public void setAutoScrollListener(AutoScrollListener autoScrollListener) {
		this.autoScrollListener = autoScrollListener;
	}

	public int getOrientation() {
		return layoutManager.getOrientation();
	}

	public void setVertical() {
		layoutManager.setOrientation(VERTICAL);
		recyclerView.addItemDecoration(new DividerItemDecoration(context,
				getOrientation(), deviderWidth, deviderColor));
	}

	public void setHorizontal() {
		layoutManager.setOrientation(HORIZONTAL);
	}

	public void setPullLoadMoreEnable(boolean enable) {
		this.hasMore = enable;
	}

	public void setPullRefreshEnable(boolean enable) {
		swipeRfl.setEnabled(enable);
	}

	public void setLoadMoreListener() {
		recyclerView.setOnScrollListener(mScrollListener);
	}

	public void loadMore() {
		if (mRefreshLoadMoreListner != null && hasMore) {
			mRefreshLoadMoreListner.onLoadMore();
		}
	}

	public void setLoadMoreCompleted() {
		isLoadMore = false;
	}

	public void stopRefresh() {
		isRefresh = false;
		swipeRfl.setRefreshing(false);
	}

	public void setRefreshLoadMoreListener(RefreshLoadMoreListener listener) {
		mRefreshLoadMoreListner = listener;
	}

	public void refresh() {
		recyclerView.setVisibility(View.VISIBLE);
		if (mRefreshLoadMoreListner != null) {
			mRefreshLoadMoreListner.onRefresh();
		}
	}

	public void scrollToTop() {
		recyclerView.scrollToPosition(0);
	}

	public void scrollToIndex(int index) {
		recyclerView.scrollToPosition(index);
	}

	public void smoothscrollToIndex(int index) {
		recyclerView.smoothScrollToPosition(index);
	}

	public void setAdapter(RecyclerView.Adapter<?> adapter) {
		if (adapter != null)
			recyclerView.setAdapter(adapter);
	}

	public interface RefreshLoadMoreListener {
		public void onRefresh();

		public void onLoadMore();
	}
}
