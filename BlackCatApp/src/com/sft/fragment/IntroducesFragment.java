package com.sft.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sft.blackcatapp.R;

public class IntroducesFragment extends BaseFragment {

	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.main_view_one, container, false);

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

}
