package com.sft.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sft.blackcatapp.R;
import com.sft.vo.ClassVO;

public class ApplyClassTypeRowLayout extends LinearLayout implements
		OnCheckedChangeListener {

	// 状态 1选中 2未选中 0 其他
	public static final int selected = 1;
	public static final int noSelected = 2;
	public static final int over = 0;

	private ClassTypeSelectedChangeListener selectedListener;

	public TextView className;
	private CheckBox ck;

	private ClassVO classVO;

	public interface ClassTypeSelectedChangeListener {
		public void onClassTypeSelectedChange(ApplyClassTypeRowLayout layout,
				ClassVO classVO, boolean selected);
	}

	public void setSelectedChangeListener(
			ClassTypeSelectedChangeListener selectedListener) {
		this.selectedListener = selectedListener;
	}

	public ApplyClassTypeRowLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public ApplyClassTypeRowLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public ApplyClassTypeRowLayout(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater
				.inflate(R.layout.apply_class_type_row_layout, null);

		className = (TextView) view
				.findViewById(R.id.apply_class_type_row_name);
		ck = (CheckBox) view.findViewById(R.id.apply_class_type_row_ck);

		ck.setOnCheckedChangeListener(this);

		addView(view);
	}

	public void setValue(ClassVO classVO) {
		this.classVO = classVO;
		if (classVO == null) {
			ck.setOnCheckedChangeListener(null);
			ck.setChecked(false);
			ck.setOnCheckedChangeListener(this);
			className.setText("");
			return;
		}

		String name = classVO.getClassname() + "班¥" + classVO.getPrice();
		className.setText(name);

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			setTextColor(selected);
		} else {
			setTextColor(noSelected);
		}

		selectedListener.onClassTypeSelectedChange(this, classVO, isChecked);
	}

	public void setTextColor(int style) {
		className.setTextColor(Color.parseColor("#333333"));
		switch (style) {
		case selected:
			className.setTextColor(Color.parseColor("#ff6633"));
			break;
		case noSelected:
			className.setTextColor(Color.parseColor("#333333"));
			break;
		}
	}
}
