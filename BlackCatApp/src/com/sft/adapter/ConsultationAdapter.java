package com.sft.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.sft.infinitescrollviewpager.BitmapManager;

import com.joooonho.SelectableRoundedImageView;
import com.jzjf.app.R;
import com.sft.util.UTC2LOC;
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
			holder.studentPic = (SelectableRoundedImageView) convertView
					.findViewById(R.id.consultation_student_headpic_im);
			holder.studentPic.setScaleType(ScaleType.CENTER_CROP);
			holder.studentPic.setImageResource(R.drawable.login_head);
			holder.studentPic.setOval(true);
			holder.studentName = (TextView) convertView
					.findViewById(R.id.consultation_student_name);
			holder.studentAnswerTime = (TextView) convertView
					.findViewById(R.id.consultation_student_answer_time);
			holder.studentAnswerContent = (TextView) convertView
					.findViewById(R.id.consultation_student_answer_content);

			holder.coachPic = (SelectableRoundedImageView) convertView
					.findViewById(R.id.consultation_coach_headpic_im);
			holder.coachPic.setScaleType(ScaleType.CENTER_CROP);
			holder.coachPic.setImageResource(R.drawable.head_driving);
			holder.coachPic.setOval(true);
			holder.coachName = (TextView) convertView
					.findViewById(R.id.consultation_coach_name);
			holder.coachAnswerTime = (TextView) convertView
					.findViewById(R.id.consultation_coach_answer_time);
			holder.coachAnswerContent = (TextView) convertView
					.findViewById(R.id.consultation_coach_answer_content);
			convertView.setTag(holder);
		} else {

			holder = (ViewHolder) convertView.getTag();
		}

		ConsultationVO consultationVO = mData.get(position);

		LinearLayout.LayoutParams headParams = (LinearLayout.LayoutParams) holder.studentPic
				.getLayoutParams();

		String url = consultationVO.getUserid().getHeadportrait()
				.getOriginalpic();
		if (TextUtils.isEmpty(url)) {
			holder.studentPic.setImageResource(R.drawable.login_head);
		} else {
			BitmapManager.INSTANCE.loadBitmap2(url, holder.studentPic,
					headParams.width, headParams.height);
		}
		holder.studentName.setText(consultationVO.getUserid().getName());
		holder.studentAnswerTime.setText(UTC2LOC.instance.getDate(
				consultationVO.getCreatetime(), "MM/dd HH:mm:ss"));
		holder.studentAnswerContent.setText(consultationVO.getContent());

		holder.coachPic.setImageResource(R.drawable.head_driving);
		if (TextUtils.isEmpty(consultationVO.getReplyuser())) {
			holder.coachName.setText("驾校回复");

		} else {
			holder.coachName.setText(consultationVO.getReplyuser() + "回复");
		}
		holder.coachAnswerTime.setText(UTC2LOC.instance.getDate(
				consultationVO.getReplytime(), "MM/dd HH:mm:ss"));

		if (TextUtils.isEmpty(consultationVO.getReplycontent())) {
			holder.coachAnswerContent.setText("等待驾校回复中...");

		} else {
			holder.coachAnswerContent.setText(consultationVO.getReplyuser());
		}

		return convertView;
	}

	private class ViewHolder {
		public SelectableRoundedImageView studentPic;
		public TextView studentName;
		public TextView studentAnswerTime;
		public TextView studentAnswerContent;

		public SelectableRoundedImageView coachPic;
		public TextView coachName;
		public TextView coachAnswerTime;
		public TextView coachAnswerContent;

	}
}
