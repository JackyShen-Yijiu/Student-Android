package com.sft.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.umeng.analytics.MobclickAgent;

/*
 * Fragment基类
 */
public class BaseHomeFragment extends Fragment {
	protected Activity mActivity;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
	}

	@Override
	public void onResume() {
		super.onResume();
		// 统计页面
		MobclickAgent.onPageStart(this.getClass().getSimpleName());
	}

	@Override
	public void onPause() {
		super.onPause();
		// 统计页面
		MobclickAgent.onPageStart(this.getClass().getSimpleName());
	}
}
