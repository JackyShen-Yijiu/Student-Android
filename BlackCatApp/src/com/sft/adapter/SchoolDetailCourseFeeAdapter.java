package com.sft.adapter;

import java.util.List;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.sft.blackcatapp.R;
import com.sft.common.Config.EnrollResult;
import com.sft.vo.ClassVO;

public class SchoolDetailCourseFeeAdapter extends BaseAdapter {

	private Context mContext;
	private List<ClassVO> mList;
	private MyClickListener mListener;
	private String enrollstate;
	
	private String schoolName;

	public SchoolDetailCourseFeeAdapter(List<ClassVO> list, Context context,
			MyClickListener listener, String enrollstate) {
		this.mContext = context;
		this.mList = list;
		mListener = listener;
		this.enrollstate = enrollstate;
	}
	
	public void setName(String name){
		schoolName = name;
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
		TextView price = (TextView) convertView
				.findViewById(R.id.course_fee_price_tv);
		
		TextView total = (TextView) convertView
				.findViewById(R.id.course_fee_name_title_price_tv);
		
		
		Button entrollBut = (Button) convertView
				.findViewById(R.id.course_fee_enroll_btn);

		if (EnrollResult.SUBJECT_NONE.getValue().equals(enrollstate)) {
			entrollBut.setText("报名");
		} else if (EnrollResult.SUBJECT_ENROLLING.getValue()
				.equals(enrollstate)) {
			entrollBut.setText("报名审核中");
			entrollBut.setEnabled(false);
			entrollBut.setBackgroundColor(mContext.getResources().getColor(
					R.color.txt_9));

		} else {
			entrollBut.setText("已报名");
			entrollBut.setEnabled(false);
			entrollBut.setBackgroundColor(mContext.getResources().getColor(
					R.color.txt_9));
		}
		
		
		entrollBut.setTag(position);
		entrollBut.setOnClickListener(mListener);

		ClassVO serverClassList = mList.get(position);
		
		total.setText(schoolName+" "+serverClassList.getClassname()+" "+serverClassList.getPrice());
		
		price.setText("￥"+ serverClassList.getOnsaleprice());

		title.setText(serverClassList.getClassname());

		intro.setText(serverClassList.getClassdesc() );//+ "¥"
//				+ serverClassList.getOnsaleprice()
		SpannableStringBuilder builder = new SpannableStringBuilder(intro
				.getText().toString());

		// ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
//		ForegroundColorSpan span = new ForegroundColorSpan(mContext
//				.getResources().getColor(R.color.app_main_color));
//
//		builder.setSpan(span, serverClassList.getClassdesc().length(),
//				serverClassList.getClassdesc().length()
//						+ serverClassList.getOnsaleprice().length() + 1,
//				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//		intro.setText(builder);
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
