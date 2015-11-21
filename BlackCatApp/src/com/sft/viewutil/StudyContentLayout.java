package com.sft.viewutil;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;

import com.sft.blackcatapp.R;

@SuppressLint("InflateParams")
public class StudyContentLayout extends LinearLayout {

	private Context context;
	private LayoutInflater inflater;
	private List<String> contentList;

	public interface StudyContentSelectChangeListener {
		void onStudyContentChange(int index, boolean isChecked, String content);
	}

	public StudyContentLayout(Context context) {
		super(context);
		init(context);
	}

	public StudyContentLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public StudyContentLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	public void setContent(List<String> contentList) {
		if (contentList == null)
			return;
		this.contentList = contentList;
		int length = contentList.size();
		int rowSize = (int) Math.ceil(length / 2f);

		for (int i = 0; i < rowSize; i++) {
			View view = this.inflater.inflate(R.layout.study_content_layout,
					null);
			int curIndex = i * 2;
			CheckBox leftCk = (CheckBox) view
					.findViewById(R.id.studycontent_left_ck);
			leftCk.setText(contentList.get(curIndex));
			leftCk.setOnCheckedChangeListener(new MyCheckChangeListener(
					curIndex));
			if (curIndex + 1 < length) {
				// 此行有2个选项
				CheckBox rightCk = (CheckBox) view
						.findViewById(R.id.studycontent_right_ck);
				rightCk.setText(contentList.get(curIndex + 1));
				rightCk.setVisibility(View.VISIBLE);
				rightCk.setOnCheckedChangeListener(new MyCheckChangeListener(
						curIndex + 1));
			}
			addView(view);
		}
	}

	private class MyCheckChangeListener implements OnCheckedChangeListener {

		private int index;

		public MyCheckChangeListener(int index) {
			this.index = index;
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (context instanceof StudyContentSelectChangeListener) {
				StudyContentSelectChangeListener listener = (StudyContentSelectChangeListener) context;
				listener.onStudyContentChange(index, isChecked,
						contentList.get(index));
			}
		}

	}
}
