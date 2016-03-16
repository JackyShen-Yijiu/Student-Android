package com.sft.dialog;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import cn.sft.infinitescrollviewpager.BitmapManager;

import com.joooonho.SelectableRoundedImageView;
import com.jzjf.app.R;
import com.sft.util.LogUtil;
import com.sft.vo.MyAppointmentVO;

/**
 * 微评论 对话框
 * 
 * @author sun 2016-2-2 下午5:11:57
 * 
 */
public class NoCommentDialog extends Dialog implements
		android.view.View.OnClickListener {

	List<MyAppointmentVO> list = new ArrayList<MyAppointmentVO>();

	private Context context;
	private Button moreBtn, commitBtn;

	private SelectableRoundedImageView coachPicIv;

	private RatingBar ratingBar;

	private TextView wordNumTv;

	private EditText commentEdit;

	private ClickListenerInterface clickListenerInterface;

	private TextView coachNameTv;

	private TextView commentTitleTv;

	private LinearLayout commentStarsLl;

	private RatingBar totalStar;

	private RatingBar abilityStar;

	private RatingBar attitudeStar;

	private RatingBar punctualStar;

	private LinearLayout commentBtns;

	private Button moreCommitBtn;

	public NoCommentDialog(Context context) {
		super(context, R.style.dialog);
		this.context = context;
		create(this.context);
	}

	public void setData(MyAppointmentVO myAppointmentVO) {
		coachNameTv.setText(myAppointmentVO.getCoachid().getName());
	}

	private void create(Context context) {
		LogUtil.print("dialog---------");
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.dialog_comment, null);

		commentTitleTv = (TextView) view
				.findViewById(R.id.dialog_comment_title_tv);
		coachPicIv = (SelectableRoundedImageView) view
				.findViewById(R.id.dialog_comment_coach_im1);
		coachPicIv.setScaleType(ScaleType.CENTER_CROP);
		coachPicIv.setImageResource(R.drawable.login_head);
		coachPicIv.setOval(true);

		coachNameTv = (TextView) view
				.findViewById(R.id.dialog_comment_coach_name_tv1);
		ratingBar = (RatingBar) view
				.findViewById(R.id.dialog_comment_ratingBar);

		commentStarsLl = (LinearLayout) view
				.findViewById(R.id.dialog_comment_stars_ll);
		punctualStar = (RatingBar) view
				.findViewById(R.id.dialog_comment_punctual_star);
		attitudeStar = (RatingBar) view
				.findViewById(R.id.dialog_comment_attitude_star);
		abilityStar = (RatingBar) view
				.findViewById(R.id.dialog_comment_ability_star);
		totalStar = (RatingBar) view
				.findViewById(R.id.dialog_comment_total_star);

		wordNumTv = (TextView) view.findViewById(R.id.dialog_comment_words_num);
		commentEdit = (EditText) view.findViewById(R.id.dialog_comment_edit_et);

		commentBtns = (LinearLayout) view
				.findViewById(R.id.dialog_comment_btns);
		moreBtn = (Button) view.findViewById(R.id.dialog_comment_more_btn);
		commitBtn = (Button) view.findViewById(R.id.dialog_comment_commit_btn);

		moreCommitBtn = (Button) view
				.findViewById(R.id.dialog_comment_more_commit_btn);
		// setTextAndImage();
		setContentView(view);
		DisplayMetrics d = context.getResources().getDisplayMetrics();
		Window dialogWindow = getWindow();
		WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
		p.width = (int) (d.widthPixels * 0.75); // 宽度设置为屏幕宽度的0.9
		dialogWindow.setAttributes(p);
		setCanceledOnTouchOutside(true);// 设置点击Dialog外部任意区域关闭Dialog
		moreBtn.setOnClickListener(this);
		// commitBtn.setOnClickListener(this);
		// moreBtn.setOnClickListener(new clickListener());
		commitBtn.setOnClickListener(new clickListener());
		moreCommitBtn.setOnClickListener(new clickListener());
		commentEdit.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;
			private int selectionStart;
			private int selectionEnd;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				temp = s;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				wordNumTv.setText(s.length() + "/40");
				selectionStart = commentEdit.getSelectionStart();
				selectionEnd = commentEdit.getSelectionEnd();
				if (temp.length() > 40) {
					s.delete(selectionStart - 1, selectionEnd);
					int tempSelection = selectionEnd;
					commentEdit.setText(s);
					commentEdit.setSelection(tempSelection);// 设置光标在最后
				}
			}
		});

		// commentEdit.setOnFocusChangeListener(new OnFocusChangeListener() {
		//
		// @Override
		// public void onFocusChange(View v, boolean hasFocus) {
		// if (hasFocus) {
		// editHintTv.setVisibility(View.GONE);
		// } else {
		// editHintTv.setVisibility(View.VISIBLE);
		// }
		// }
		// });
	}

	public RatingBar getRatingBar() {
		return ratingBar;
	}

	public RatingBar getPunctualStar() {
		return punctualStar;
	}

	public RatingBar getAttitudeStar() {
		return attitudeStar;
	}

	public RatingBar getAbilityStar() {
		return abilityStar;
	}

	public RatingBar getTotalStar() {
		return totalStar;
	}

	public EditText getEditText() {
		return commentEdit;
	}

	public void showErrorHint(boolean isShow) {
		// if (isShow) {
		// errorHintTv.setVisibility(View.VISIBLE);
		// } else {
		// errorHintTv.setVisibility(View.GONE);
		// }
	}

	public void setImage(String url) {
		LinearLayout.LayoutParams headpicParams = (LinearLayout.LayoutParams) coachPicIv
				.getLayoutParams();

		if (TextUtils.isEmpty(url)) {
			coachPicIv.setBackgroundResource(R.drawable.login_head);
		} else {
			BitmapManager.INSTANCE.loadBitmap2(url, coachPicIv,
					headpicParams.width, headpicParams.height);
		}
	}

	// @Override
	// public void onClick(View v) {
	// switch (v.getId()) {
	// case R.id.dialog_comment_more_btn:
	// dismiss();
	// break;
	// case R.id.dialog_no_login_cancel_btn:
	//
	// // context.sendBroadcast(new Intent(MyAppointmentActivity.class
	// // .getName()).putExtra("isApplyExam", true));
	// dismiss();
	// break;
	// }
	// }
	public void setClicklistener(ClickListenerInterface clickListenerInterface) {
		this.clickListenerInterface = clickListenerInterface;
	}

	private class clickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {
			case R.id.dialog_comment_more_btn:
				// clickListenerInterface.getMoreClick();
				break;
			case R.id.dialog_comment_commit_btn:
				clickListenerInterface.commintClick();
				break;
			}
		}

	};

	public interface ClickListenerInterface {

		public void getMoreClick();

		public void commintClick();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_comment_more_btn:
			commentTitleTv.setVisibility(View.GONE);
			ratingBar.setVisibility(View.GONE);
			commentBtns.setVisibility(View.GONE);

			commentStarsLl.setVisibility(View.VISIBLE);
			moreCommitBtn.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
	}
}
