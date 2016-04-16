package com.sft.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.jzjf.app.R;
import com.sft.adapter.ExamAdapter;
import com.sft.blackcatapp.ExamSussess;
import com.sft.blackcatapp.ExerciseOrderAct;
import com.sft.common.BlackCatApplication;
import com.sft.jieya.UnZipUtils;
import com.sft.util.BaseUtils;
import com.sft.util.CommonUtil;
import com.sft.util.LogUtil;
import com.sft.util.Util;
import com.sft.vo.ExerciseAnswerVO;
import com.sft.vo.ExerciseVO;
import com.sft.vo.questionbank.error_book;
import com.sft.vo.questionbank.score;
import com.sft.vo.questionbank.web_note;

/**
 * 考试每一项
 * 
 * @author pengdonghua
 * 
 */
public class ExciseFragment extends Fragment implements OnItemClickListener,
		OnClickListener {

	private final static String PARAM1 = "p1";
	private final static String PARAM2 = "p2";

	private String param2;

	private ExerciseVO param1;

	private final String test = "         ";

	private TextView tvTitle;
	private ImageView imgType;
	private ImageView img;
	private Button btnSubmit;
	private ListView lv;
	private VideoView videoView;

	private LinearLayout llAnswer;
	/** 解析答案 */
	private TextView tvAnswer;

	private TextView tvRightAnswer;

	private ExamAdapter adapter;

	// private String video_url = "";
	/** 本地文件的路径 */
	private String localPath = UnZipUtils.localPath + "/ggtkFile/resources/";

	public static ExciseFragment newInstance(ExerciseVO param1, String param2) {
		ExciseFragment fragment = new ExciseFragment();
		Bundle b = new Bundle();
		b.putSerializable(PARAM1, param1);
		b.putString(PARAM2, param2);
		LogUtil.print("instantiateItem----params2>>" + param2);
		fragment.setArguments(b);

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			param1 = (ExerciseVO) getArguments().getSerializable(PARAM1);
			param2 = getArguments().getString(PARAM2);
		}
		localPath = Environment.getExternalStorageDirectory().toString();
		// video_url = localPath+"/test.mp4";
		localPath = Environment.getExternalStorageDirectory()
				+ "/jzjf/img/a/ggtkFile/resources/";
		// LogUtil.print("instantiateItem---onCreate->" + param1.getWebnote());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LogUtil.print("instantiateItem---onCreateView-->Excise");
		View view = View
				.inflate(getActivity(), R.layout.frag_excise_item, null);
		initData();
		initView(view);

		if (view == null) {
			view = inflater.inflate(R.layout.frag_excise_item, null);
		}
		// 缓存的view需要判断是否已经被加过parent，
		// 如果有parent需要从parent删除，要不然会发生这个view已经有parent的错误。
		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
			parent.removeView(view);
		}
		LogUtil.print("excise--111>>" + param1.getWebnote().getId()
				+ "position---checked-->" + param1.getAnswers());
		return view;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		LogUtil.print("onResume--->");
		super.onResume();
	}

	private void initData() {

	}

	private void initView(View view) {
		tvTitle = (TextView) view.findViewById(R.id.frag_excise_item_title);
		imgType = (ImageView) view.findViewById(R.id.frag_excise_item_type);
		btnSubmit = (Button) view.findViewById(R.id.frag_excise_btn);
		lv = (ListView) view.findViewById(R.id.frag_excise_item_lv);
		img = (ImageView) view.findViewById(R.id.frag_excise_img);
		videoView = (VideoView) view.findViewById(R.id.frag_excise_videov);
		llAnswer = (LinearLayout) view
				.findViewById(R.id.frag_excise_item_answer_ll);
		tvAnswer = (TextView) view
				.findViewById(R.id.frag_excise_item_answer_content);
		tvRightAnswer = (TextView) view
				.findViewById(R.id.frag_excise_item_answer_right);

		btnSubmit.setOnClickListener(this);

		lv.setOnItemClickListener(this);

		tvTitle.setText(test + param1.getWebnote().getQuestion());
		LogUtil.print("int---21465>" + param1.getWebnote().getType());

		switch (param1.getWebnote().getType()) {
		case 0:
		case 1:// 判断
			imgType.setImageResource(R.drawable.ic_study_judge);
			if (param1.getAnswers().size() == 0) {
				List<ExerciseAnswerVO> answers = new ArrayList<ExerciseAnswerVO>();
				answers.add(new ExerciseAnswerVO("正确"));
				answers.add(new ExerciseAnswerVO("错误"));
				param1.setAnswers(answers);
			}
			LogUtil.print(param1.getAnswers().size() + "answeer--->"
					+ param1.getWebnote().getQuestion());
			break;
		case 2:// 单选
			imgType.setImageResource(R.drawable.ic_study_single_select);
			break;
		case 3:// 多选
			imgType.setImageResource(R.drawable.ic_study_mulity_select);
			btnSubmit.setVisibility(View.VISIBLE);
			break;

		}

		adapter = new ExamAdapter(getActivity(), param1.getAnswers());
		adapter.setRightAnswer(param1.getWebnote().getAnswer_true());
		adapter.setSubmit(param1.submit);
		adapter.setType(param1.getWebnote().getType());
		lv.setAdapter(adapter);
		LogUtil.print("adapter--->size-->" + param1.getAnswers().size());
		BaseUtils.setListViewHeightBasedOnChildren(lv);
		// 显示图片
		doImage(param1.getWebnote().getImg_url());
		// 显示视频
		doVideo(param1.getWebnote().getVideo_url());
		if (param1.submit == 1) {// 已经提交了
			showAnalysy();
		}

	}

	private void doImage(String name) {
		if (null == name) {
			img.setVisibility(View.GONE);
		} else {
			int height = (int) (CommonUtil.getWindowsWidth(getActivity()) * 0.4);
			Uri uri = Uri.parse(localPath + name);
			// img.setImageURI(uri);
			LayoutParams p = new LayoutParams(LayoutParams.MATCH_PARENT, height);
			Drawable d = Drawable.createFromPath(localPath + name);
			Bitmap b = BitmapFactory.decodeFile(localPath + name);
			int w = 0, h = 0;
			if (b != null) {
				w = b.getWidth() * 2;
				h = b.getHeight() * 2;
				b.recycle();
				b = null;
			}
			LogUtil.print("width::>" + w + "height::>" + h);
			if (w - h > 20) {
				img.setLayoutParams(p);
				img.setBackgroundDrawable(d);
				img.setVisibility(View.VISIBLE);
			} else {
				p = new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT);
				// img.set
				img.setLayoutParams(p);
				img.setImageDrawable(d);
				img.setVisibility(View.VISIBLE);
			}

		}
		// } else {
		// int height = (int) (CommonUtil.getWindowsWidth(getActivity()) * 0.4);
		// Uri uri = Uri.parse(localPath + name);
		// // img.setImageURI(uri);
		// LayoutParams p = new LayoutParams(LayoutParams.MATCH_PARENT, height);
		// Drawable d = Drawable.createFromPath(localPath + name);
		// img.setLayoutParams(p);
		// img.setBackgroundDrawable(d);
		// img.setVisibility(View.VISIBLE);
		// }

	}

	private void doVideo(String name) {
		if (null == name) {
			videoView.setVisibility(View.GONE);
			return;
		}
		int height = (int) (CommonUtil.getWindowsWidth(getActivity()) * 0.4);
		LayoutParams p = new LayoutParams(LayoutParams.MATCH_PARENT, height);
		videoView.setLayoutParams(p);
		LogUtil.print("video-->" + localPath + name);
		videoView.setVisibility(View.VISIBLE);
		videoView.setVideoPath(localPath + name);
		videoView.start();
		videoView
				.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer mp) {
						if (null != videoView) {
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							videoView.seekTo(0);
							videoView.start();
						}
					}
				});
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		LogUtil.print("onItemClick-->" + param1.submit);
		if (param1.submit == 1) {// 已经提交了
			btnSubmit.setEnabled(false);
			return;
		}

		List<ExerciseAnswerVO> data = adapter.getData();
		switch (param1.getWebnote().getType()) {
		case 0:
		case 1:// 判断
		case 2:
			// 单选
			// 已经选择了
			param1.submit = 1;
			for (int i = 0; i < data.size(); i++) {
				data.get(i).setChecked(0);
			}
			data.get(arg2).setChecked(1);
			adapter.setSubmit(param1.submit);
			adapter.notifyDataSetChanged();
			if (checkAnswerSingle(arg2)) {// 正确
				onRight();
			} else {// 错误
				onError();
			}
			break;
		case 3:
			// 多选
			data.get(arg2).setChecked(data.get(arg2).getChecked() == 0 ? 1 : 0);

			if (getSeletedSize() > 1) {// 按钮可用
				btnSubmit.setEnabled(true);
			} else {// 不可用
				btnSubmit.setEnabled(false);
			}
			adapter.notifyDataSetChanged();
			break;
		}

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		// case R.id.
		case R.id.frag_excise_btn:// 提交
			param1.submit = 1;
			adapter.setSubmit(param1.submit);
			adapter.notifyDataSetChanged();
			if (checkMulity()) {// 正确
				onRight();
			} else {// 错误
				onError();
			}
			btnSubmit.setEnabled(false);
			break;
		}
	}

	/**
	 * 判断 单选 答案是否正确
	 * 
	 * @param pos
	 * @return
	 */
	private boolean checkAnswerSingle(int pos) {
		int trueAn = Integer.parseInt(param1.getWebnote().getAnswer_true());
		// 与数据库 单位 一致
		pos++;
		if (trueAn == pos) {// 正确
			return true;
		} else {// 错误
			return false;
		}
	}

	/**
	 * 多项选择 是否正确
	 * 
	 * @return
	 */
	private boolean checkMulity() {

		List<ExerciseAnswerVO> list = param1.getAnswers();
		for (int i = 0; i < list.size(); i++) {// 所有答案， 我的答案
			if (list.get(i).getChecked() == 1) {// 选择了
				if (param1.getWebnote().getAnswer_true().contains((i + 1) + "")) {// 是正确的

				} else {// 不正确
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 回答正确
	 */
	private void onRight() {
		((ExerciseOrderAct) getActivity()).addRight();
		((ExerciseOrderAct) getActivity()).next();
		// 如果是错题，删除错题
		if (((ExerciseOrderAct) getActivity()).flag == 2) {// 错题
			error_book book = new error_book();
			book.setWebnoteid(param1.getWebnote().getId());
			Util.deleteErrorBook(book);
			// LogUtil.print("delete----.>>"+);
		} else if (((ExerciseOrderAct) getActivity()).flag == 1) {// 考试
			onEnd();
		}
	}

	private void onError() {
		showAnalysy();
		((ExerciseOrderAct) getActivity()).addWrong();
		if (((ExerciseOrderAct) getActivity()).flag != 2) {
			// 插入数据库
			insertError();
		}
		// 考试
		if (((ExerciseOrderAct) getActivity()).flag == 1) {// 考试
			if (((ExerciseOrderAct) getActivity()).wrong > 10) {
				showDialogFinish();
			}
			onEnd();
		}
	}

	private void onEnd() {
		if (((ExerciseOrderAct) getActivity()).isEnd()) {// 是否结束
			Toast.makeText(getActivity(), "end", Toast.LENGTH_SHORT).show();
			((ExerciseOrderAct) getActivity()).requestExam();
			Intent i = new Intent(getActivity(), ExamSussess.class);
			if (((ExerciseOrderAct) getActivity()).kemu == 1) {// 科目一
				i.putExtra("score", ((ExerciseOrderAct) getActivity()).right);
			} else {
				i.putExtra("score",
						((ExerciseOrderAct) getActivity()).right * 2);
			}
			startActivity(i);
		}
	}

	/**
	 * 显示解析
	 */
	private void showAnalysy() {
		llAnswer.setVisibility(View.VISIBLE);
		tvAnswer.setText(param1.getWebnote().getExplain());
		String temp = param1.getWebnote().getAnswer_true();
		String temp1 = temp.replace("1", "A ").replace("2", "B ")
				.replace("3", "C ").replace("4", "D ");
		LogUtil.print(temp1 + "right::" + param1.getWebnote().getAnswer_true());
		tvRightAnswer.setText("正确答案:  " + temp1);
	}

	/**
	 * 重新设置图片的大小
	 */
	private void resizeImg() {
		// Bitmap b = BitmapFactory.decodeFile(pathName)
	}

	/*
	 * 获取选择的数量
	 */
	private int getSeletedSize() {
		List<ExerciseAnswerVO> list = param1.getAnswers();
		int result = 0;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getChecked() == 1) {
				result++;
			}
		}
		return result;
	}

	/**
	 * 播放视频
	 */
	public void playVideo() {
		// if(null!=param1.getWebnote().getVideo_url()){
		// videoView.seekTo(0);
		// videoView.start();
		// }
	}

	public interface doConnect {
		public void do1();
	}

	public String getMTag() {
		return param2;
	}

	BlackCatApplication app = null;

	/**
	 * 插入错题本 数据库
	 */
	private void insertError() {
		if (app == null) {
			app = BlackCatApplication.getInstance();
		}
		error_book error = new error_book();
		if (app.userVO != null) {
			error.setUserid(app.userVO.getUserid());
		}
		error.setChapterid(((ExerciseOrderAct) getActivity()).chartId);
		error.setKemu(((ExerciseOrderAct) getActivity()).kemu);
		error.setWebnoteid(param1.getWebnote().getId());
		LogUtil.print(param1.getWebnote().getQuestion() + "delete-->"
				+ param1.getWebnote().getId());
		Util.insertErrorBank(error);
		List<web_note> list = Util.getAllSubjectFourErrorQuestion();
		LogUtil.print("error---size>" + list.size());
	}

	/**
	 * 插入考试信息
	 */
	private void insertExam() {
		score sc = new score();
		sc.setChenji("");

		// Util.insertErrorBank(error)
	}

	/**
	 * 考试结束,未通过
	 */
	private void showDialogFinish() {
		final Dialog dialog = new Dialog(getActivity(), R.style.dialog);
		View view = View.inflate(getActivity(), R.layout.pop_back, null);
		TextView tvTitle = (TextView) view.findViewById(R.id.textView1);
		TextView tvContent = (TextView) view.findViewById(R.id.textView2);
		tvTitle.setText("考试不通过");
		tvContent.setText("非常抱歉，您已经答错了十一道题目，模拟考试未通过，请再接再厉!");
		view.findViewById(R.id.pay_cancel).setVisibility(View.INVISIBLE);
		dialog.setContentView(view);

		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		view.findViewById(R.id.pay_ok).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// 跳转
						dialog.dismiss();
						getActivity().finish();
						// 退出支付流程,干掉之前的
					}
				});
		view.findViewById(R.id.pay_cancel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						dialog.dismiss();
					}
				});
		dialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface paramDialogInterface) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				getActivity().finish();
			}
		});
		dialog.show();
	}
}
