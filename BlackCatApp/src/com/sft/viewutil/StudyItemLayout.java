package com.sft.viewutil;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sft.blackcatapp.R;

public class StudyItemLayout extends RelativeLayout {

	private Drawable mIcon;
	private String mTitle, mDescirbe;

	private ImageView mIconIv;
	private TextView mTitleTv, mDescribeTv;

	public StudyItemLayout(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init(context, attrs);
	}

	public StudyItemLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	public StudyItemLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public StudyItemLayout(Context context) {
		super(context);

	}

	private void init(Context context, AttributeSet attrs) {

		// 获取属性
		TypedArray type = context.obtainStyledAttributes(attrs,
				R.styleable.StudyItemLayout);
		mIcon = type.getDrawable(R.styleable.StudyItemLayout_itemIcon);
		mTitle = type.getString(R.styleable.StudyItemLayout_itemTitle);
		mDescirbe = type.getString(R.styleable.StudyItemLayout_itemDescribe);

		// 设值
		View view = View.inflate(context, R.layout.layout_study_item, null);
		mIconIv = (ImageView) view.findViewById(R.id.study_item_icon);
		mTitleTv = (TextView) view.findViewById(R.id.study_item_title);
		mDescribeTv = (TextView) view.findViewById(R.id.study_item_describe);
		mIconIv.setBackgroundDrawable(mIcon);
		mTitleTv.setText(mTitle);
		mDescribeTv.setText(mDescirbe);
		addView(view);
		type.recycle();

	}

}
