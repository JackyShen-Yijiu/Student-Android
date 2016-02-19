package com.sft.fragment;

import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.sft.view.WeekView;

public class AppointmentWeekFragment extends BaseHomeFragment {

	private List<Integer> list;
	private int pagePosition;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LinearLayout ret = new LinearLayout(getActivity());
		ret.setBackgroundColor(Color.parseColor("#eeeeee"));
		ret.setLayoutParams(new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
		WeekView weekView = new WeekView(getActivity());
		weekView.initMonthAdapter(pagePosition);
		ret.addView(weekView);
		return ret;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		// EventBus.getDefault().unregister(this);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	public void setData(int position) {
		pagePosition = position;

	}
}
