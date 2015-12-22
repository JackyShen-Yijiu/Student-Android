package com.sft.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sft.blackcatapp.R;

public class IntroducesFragment extends Fragment {

	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.introduces_fragment, container, false);

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

}
