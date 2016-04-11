package com.sft.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jzjf.app.R;
import com.sft.common.BlackCatApplication;
import com.sft.common.Config.EnrollResult;
import com.sft.util.EnrollUtils;
import com.sft.util.LogUtil;
import com.sft.vo.ClassVO;

public class SchoolDetailCourseFeeAdapter extends BaseAdapter {

	private Context mContext;
	private List<ClassVO> mList;
	private MyClickListener mListener;
	private String enrollstate;

	private String schoolName;

	private BlackCatApplication app;

	public SchoolDetailCourseFeeAdapter(List<ClassVO> list, Context context,
			MyClickListener listener, String enrollstate) {
		this.mContext = context;
		this.mList = list;
		mListener = listener;
		this.enrollstate = enrollstate;
		if (app == null) {
			app = BlackCatApplication.getInstance();
		}
	}

	public void setName(String name) {
		schoolName = name;
	}

	/**
	 * ' 设置支付状态
	 * 
	 * @param enrollstate
	 */
	public void setPayState(String enrollstate) {
		this.enrollstate = enrollstate;
		notifyDataSetChanged();
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

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LogUtil.print("course--adapter--00>" + position);
		ClassVO serverClassList = mList.get(position);
		convertView = View.inflate(mContext, R.layout.school_detail_course_fee,
				null);
		TextView title = (TextView) convertView
				.findViewById(R.id.course_fee_title_tv);
		TextView intro = (TextView) convertView
				.findViewById(R.id.course_fee_intro_tv);
		TextView price = (TextView) convertView
				.findViewById(R.id.course_fee_price_tv);
		TextView onsaleprice = (TextView) convertView
				.findViewById(R.id.course_fee_onsaleprice_tv);

		TextView timeTv = (TextView) convertView
				.findViewById(R.id.course_fee_time_tv);
		ImageView tehuiIv = (ImageView) convertView
				.findViewById(R.id.course_fee_tehui_iv);
		tehuiIv.setVisibility(View.GONE);
		Button entrollBut = (Button) convertView
				.findViewById(R.id.course_fee_enroll_btn);

		// EnrollUtils.doEnroll(entrollBut);
		// BaomingBtn(entrollBut, serverClassList);

		entrollBut.setTag(position);
		entrollBut.setOnClickListener(mListener);

		if ("true".equals(serverClassList.getIs_vip())) {
			tehuiIv.setVisibility(View.VISIBLE);
		}
		timeTv.setText(serverClassList.getClasschedule());

		EnrollUtils.doEnroll(entrollBut);

		if (!TextUtils.isEmpty(serverClassList.getPrice())) {
			price.setText("¥" + serverClassList.getPrice());
			price.setVisibility(View.VISIBLE);
		} else {
			price.setVisibility(View.GONE);

		}
		price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); // 中划线
		onsaleprice.setText("学生专享¥" + serverClassList.getOnsaleprice());
		title.setText(serverClassList.getClassname());

		intro.setText(serverClassList.getClassdesc());// + "¥"
		// + serverClassList.getOnsaleprice()
		SpannableStringBuilder builder = new SpannableStringBuilder(intro
				.getText().toString());

		return convertView;
	}

	private void BaomingBtn(Button entrollBut, ClassVO classVO) {
		if (!app.isLogin) {// 未登录
			entrollBut.setText("报名");
			entrollBut.setTextColor(mContext.getResources().getColor(
					R.color.white));
			entrollBut.setVisibility(View.VISIBLE);
		} else if (app.userVO != null
				&& app.userVO.getApplyclasstypeinfo() != null
				&& classVO.getCalssid() != null
		// && classVO.getCalssid().equals(
		// app.userVO.getApplyclasstypeinfo().getId())
		) {// 如果相同
			// 则显示

			if (EnrollResult.SUBJECT_NONE.getValue().equals(
					app.userVO.getApplystate())) {// 未报名
				entrollBut.setText("报名");
				entrollBut.setTextColor(mContext.getResources().getColor(
						R.color.white));
				entrollBut.setVisibility(View.VISIBLE);
				entrollBut.setEnabled(true);
			} else if (EnrollResult.SUBJECT_ENROLLING.getValue().equals(
					app.userVO.getApplystate())) {// 报名中，审核

				if (app.userVO.getPayState() == 20) {// 已支付
					entrollBut.setVisibility(View.VISIBLE);
					entrollBut.setEnabled(false);

					entrollBut.setBackgroundColor(Color.parseColor("#cecece"));
				} else {
					entrollBut.setText("报名");
					entrollBut.setTextColor(mContext.getResources().getColor(
							R.color.white));
					entrollBut.setVisibility(View.VISIBLE);
					entrollBut.setEnabled(true);
				}

			} else {// 报名成功
				entrollBut.setVisibility(View.VISIBLE);
				entrollBut.setEnabled(false);
				entrollBut.setBackgroundColor(Color.parseColor("#cecece"));
				LogUtil.print("123213123" + app.userVO.getPayState());
			}

		}
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
