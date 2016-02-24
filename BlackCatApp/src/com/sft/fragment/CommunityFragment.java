package com.sft.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.sft.blackcatapp.R;

public class CommunityFragment extends BaseFragment implements OnClickListener {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_apply, container,
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
