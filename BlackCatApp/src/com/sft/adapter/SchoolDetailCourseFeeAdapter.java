package com.sft.adapter;

import java.util.List;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.sft.blackcatapp.R;
import com.sft.common.BlackCatApplication;
import com.sft.util.LogUtil;
import com.sft.vo.ClassVO;

public class SchoolDetailCourseFeeAdapter extends BaseAdapter {

	private Context mContext;
	private List<ClassVO> mList;
	private MyClickListener mListener;
	private String enrollBtnName;

	public SchoolDetailCourseFeeAdapter(List<ClassVO> list, Context context,
			MyClickListener listener, String enrollBtnName) {
		this.mContext = context;
		this.mList = list;
		mListener = listener;
		LogUtil.print("enrollBtnName====" + enrollBtnName);
		this.enrollBtnName = enrollBtnName;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public ClassVO getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		convertView = View.inflate(mContext, R.layout.school_detail_course_fee,
				null);
		TextView title = (TextView) convertView
				.findViewById(R.id.course_fee_title_tv);
		TextView intro = (TextView) convertView
				.findViewById(R.id.course_fee_intro_tv);
		Button entrollBut = (Button) convertView
				.findViewById(R.id.course_fee_enroll_btn);

		if (!TextUtils.isEmpty(enrollBtnName)) {
			entrollBut.setText(enrollBtnName);
		}
		if (BlackCatApplication.app.userVO == null) {
			// collectCk.setEnabled(false);
			entrollBut.setVisibility(View.GONE);
		} else {
			entrollBut.setVisibility(View.VISIBLE);
		}
		entrollBut.setTag(position);
		entrollBut.setOnClickListener(mListener);

		ClassVO serverClassList = mList.get(position);
		title.setText(serverClassList.getClassname());

		intro.setText(serverClassList.getClassdesc() + "¥"
				+ serverClassList.getOnsaleprice());
		SpannableStringBuilder builder = new SpannableStringBuilder(intro
				.getText().toString());

		// ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
		ForegroundColorSpan span = new ForegroundColorSpan(mContext
				.getResources().getColor(R.color.app_main_color));

		builder.setSpan(span, serverClassList.getClassdesc().length(),
				serverClassList.getClassdesc().length()
						+ serverClassList.getOnsaleprice().length() + 1,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		intro.setText(builder);
		return convertView;
	}

	/**
	 * 用于回调的抽象类
	 * 
	 * @author Ivan Xu 2014-11-26
	 */
	public static abstract class MyClickListener implements OnClickListener {
		/**
		 * 基类的onClick方法
		 */
		@Override
		public void onClick(View v) {
			myOnClick((Integer) v.getTag(), v);
		}

		public abstract void myOnClick(int position, View v);
	}
}
