package com.sft.adapter;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class HomePageAdapter extends FragmentStatePagerAdapter {

	private List<Class<? extends Fragment>> mData = new LinkedList<Class<? extends Fragment>>();
	private Context mContext;

	public HomePageAdapter(FragmentManager fm, Context mContext) {
		super(fm);
		this.mContext = mContext;
	}

	@Override
	public Fragment getItem(int arg0) {
		Class<? extends Fragment> fragClass = mData.get(arg0);
		return Fragment.instantiate(mContext, fragClass.getName(), null);
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	public void addFragmentClass(Class<? extends Fragment> fragClass) {
		mData.add(fragClass);
	}
}