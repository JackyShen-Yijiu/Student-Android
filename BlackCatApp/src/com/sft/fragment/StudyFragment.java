package com.sft.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.sft.blackcatapp.R;
import com.sft.util.LogUtil;

public class StudyFragment extends BaseHomeFragment implements OnClickListener {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LogUtil.print("StudyFragment----");
		View rootView = inflater.inflate(R.layout.fragment_study, container,
				false);
		initViews(rootView);
		return rootView;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		// EventBus.getDefault().unregister(this);
	}

	private void initViews(View rootView) {
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
