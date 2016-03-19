package com.sft.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jzjf.app.R;
import com.sft.vo.ConsultationVO;

@SuppressLint({ "Inflatconsultation_student_headpic_imeParams",
		"ResourceAsColor" })
public class ConsultationAdapter extends BaseAdapter {

	private List<ConsultationVO> mData;
	private Context context;
	private int width;
	private int heigth;

	public ConsultationAdapter(Context context, List<ConsultationVO> mData,
			int width) {
		this.mData = mData;
		this.context = context;
		this.width = width;
		this.heigth = width / 2;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mData.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;

		if (convertView == null) {
			convertView = View.inflate(context, R.layout.consultation_item,
					null);
			holder = new ViewHolder();
			holder.questionTv = (TextView) convertView
					.findViewById(R.id.consultation_question_tv);
			holder.answerTv = (TextView) convertView
					.findViewById(R.id.consultation_answer_tv);

			convertView.setTag(holder);
		} else {

			holder = (ViewHolder) convertView.getTag();
		}

		ConsultationVO consultationVO = mData.get(position);
		holder.questionTv.setText(consultationVO.getContent());
		if (!TextUtils.isEmpty(consultationVO.getReplycontent())) {
			holder.answerTv.setText(consultationVO.getReplycontent());

		} else {
			holder.answerTv.setText("正在等待回复中...");

		}

		if (consultationVO != null) {

		}
		return convertView;
	}

	private class ViewHolder {
		public TextView questionTv;
		public TextView answerTv;

	}
}
