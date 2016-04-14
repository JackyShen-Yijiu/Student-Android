package com.sft.blackcatapp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import com.jzjf.app.R;
import com.sft.adapter.ExerciseAdapter;
import com.sft.fragment.ExciseFragment;
import com.sft.fragment.ExciseFragment.doConnect;
import com.sft.jieya.UnZipUtils;
import com.sft.jieya.ZipCall;
import com.sft.util.LogUtil;
import com.sft.util.Util;
import com.sft.vo.ExerciseVO;
import com.sft.vo.questionbank.web_note;

/**
 * 练习 顺序
 * 
 * @author pengdonghua
 * 
 */
public class ExerciseOrderAct extends BaseFragmentAct implements doConnect {

	private final static int DATA_SIZE = 50;

	private ViewPager viewpager;

	private TextView tvTime, tvPercentage, tvWrong, tvRight, tvTotal;

	private ExerciseAdapter adapter;

	/** 缓存数据 */
	private List<ExerciseVO> data1 = null;
	
	private List<ExerciseVO> dataExam = null;
	

	static int minute = -1;
	static int second = -1;
	/**章节id*/
	public int chartId;
	public int kemu;
	/**种类  0：练习     1：考试   2:错题*/
	private int flag = 0;

 	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.arg1 == 1) {// 倒计时
				handler();
			} else {
				
				if(flag == 1){//考试
					adapter = new ExerciseAdapter(getSupportFragmentManager(),
							dataExam);
					tvTotal.setText("1/" + dataExam.size());
				}else{
					adapter = new ExerciseAdapter(getSupportFragmentManager(),
							data1);
					tvTotal.setText("1/" + data1.size());
				}
				// 刷新 显示
//				LogUtil.print("size--->" + dataExam.size());
				viewpager.setAdapter(adapter);
				
			}

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_exercise_order);
		initData();
		// getData();
		initView();
		unzip();
		Exam();
	}

	private void initView() {
		viewpager = (ViewPager) findViewById(R.id.act_exer_order_vp);
		tvTime = (TextView) findViewById(R.id.act_exercise_time);
		tvPercentage = (TextView) findViewById(R.id.act_exercise_percentage);
		tvWrong = (TextView) findViewById(R.id.act_exercise_wrong);
		tvRight = (TextView) findViewById(R.id.act_exercise_right);
		tvTotal = (TextView) findViewById(R.id.act_exercise_total);

		viewpager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				tvTotal.setText((arg0 + 1) + "/" + data1.size());
				ExciseFragment t = adapter.getByTag(arg0 + "");
				if (t != null)
					t.playVideo();
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	private void initData() {
		data1 = new ArrayList<ExerciseVO>();
		
		flag = getIntent().getIntExtra("flag", 0);
		//练习模式
		chartId = getIntent().getIntExtra("id",0);
		kemu = getIntent().getIntExtra("subjectid",1);
		if(flag == 1){//考试模式
			if(kemu == 1){
				dataExam  = new ArrayList<ExerciseVO>(100);
			}else if(kemu == 4){
				dataExam  = new ArrayList<ExerciseVO>(50);
			}
		}else if(flag == 0){//练习模式
			
		}
		
		//考试模式
		new Thread() {

			@Override
			public void run() {
				List<web_note> d = null;
				switch(flag){
				case 0://练习模式
					if(kemu == 1){
						d = Util.getSubjectOneQuestionWithChapter("0"+chartId);
					}else{
						d = Util.getSubjectFourQuestionWithChapter("0"+chartId);
					}
					data1 = doData(d);
					break;
				case 1://考试模式
					if(kemu == 1){
						d = Util.getAllSubjectOneBank();
					}else{
						d = Util.getAllSubjectFourBank();
					}
					LogUtil.print("Kaoshi--->"+d.size());
					data1 = doData(d);
					LogUtil.print("Kaoshi---1111>"+data1.size());
					getRandWeb(data1, kemu);
					LogUtil.print("Kaoshi---2222>"+dataExam.size());
					
					break;
				case 2://错题模式
					if(kemu == 1){
						d = Util.getAllSubjectOneErrorQuestion();
					}else{
						d = Util.getAllSubjectFourErrorQuestion();
					}
					data1 = doData(d);
					break;
				}
				LogUtil.print(data1.size() + "size---0123>"+chartId
						);
				
				handler.sendEmptyMessage(0);

			}

		}.start();

	}

	/**
	 * 读取数据库，保存为可以使用的数据
	 * 
	 * @param data1
	 * @return
	 */
	private List<ExerciseVO> doData(List<web_note> data1) {
		List<ExerciseVO> data = new ArrayList<ExerciseVO>();
		ExerciseVO vo = null;
		for (int i = 0; i < data1.size(); i++) {
			vo = new ExerciseVO();
			vo.setWebnote(data1.get(i));
			data.add(vo);
		}
		return data;
	}

	

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.base_left_btn:// 干掉页面
			finish();
			break;
		}
	}

	/**
	 * 自动下一页
	 */
	public void next() {
		viewpager.setCurrentItem(viewpager.getCurrentItem() + 1, true);
	}

	/**
	 * 增加一个
	 * 
	 * @param right
	 */
	public void addRight() {
		right++;
		tvRight.setText(String.valueOf(right));
		setPercentage();
	}

	/**
	 * 错题增加一个
	 */
	public void addWrong() {
		wrong++;
		tvWrong.setText(String.valueOf(wrong));
		setPercentage();
	}

	/**
	 * 正确率
	 */
	private void setPercentage() {
		tvPercentage.setText(((int) (100 * right / (right + wrong))) + "%");
	}

	private void unzip() {
		UnZipUtils zip = new UnZipUtils();
		File f = new File(zip.targetPath);
		if (f.exists())
			return;
		zip.CopyFileThread(this, UnZipUtils.assertName, UnZipUtils.targetPath,
				new Handler() {

					@Override
					public void handleMessage(Message msg) {

						try {
							new UnZipUtils().doZip(ExerciseOrderAct.this,
									UnZipUtils.targetPath,
									UnZipUtils.localPath, new ZipCall() {

										@Override
										public void unzipSuccess() {
											// 解压成功
											Toast.makeText(
													ExerciseOrderAct.this,
													"success",
													Toast.LENGTH_SHORT).show();
										}

										@Override
										public void unzipFailed() {

										}

									});
						} catch (IOException e) {
							e.printStackTrace();
						}

					}

				});
	}
	
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Toast.makeText(this, "keydown-->", Toast.LENGTH_SHORT).show();
			showDialogBack(50);
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}



	private int right = 0;
	private int wrong = 0;

	@Override
	public void do1() {
	}
	
	/**
	 * 中途退出
	 */
	private void showDialogBack(int last){
		final PopupWindow pop = new PopupWindow(this);
		pop.setHeight(LayoutParams.MATCH_PARENT);
		pop.setWidth(LayoutParams.MATCH_PARENT);
		View view = View.inflate(this, R.layout.pop_back, null);
		TextView tvTitle = (TextView) view.findViewById(R.id.textView1);
		TextView tvContent = (TextView) view.findViewById(R.id.textView2);
		tvTitle.setText("退出模拟考试");
		tvContent.setText("还有"+last+"道题目没做呢，确定要退出模拟考试吗?");
		view.setFocusable(true);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				pop.dismiss();
			}
		});
		pop.setContentView(view);
		view.findViewById(R.id.pay_ok).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// 跳转
						pop.dismiss();
						finish();
						// 退出支付流程,干掉之前的
					}
				});
		view.findViewById(R.id.pay_cancel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						pop.dismiss();
					}
				});
		pop.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
	}
	
	/**
	 * 考试结束,未通过
	 */
	private void showDialogFinish(){
		final PopupWindow pop = new PopupWindow(this);
		pop.setHeight(LayoutParams.MATCH_PARENT);
		pop.setWidth(LayoutParams.MATCH_PARENT);
		View view = View.inflate(this, R.layout.pop_back, null);
		TextView tvTitle = (TextView) view.findViewById(R.id.textView1);
		TextView tvContent = (TextView) view.findViewById(R.id.textView2);
		tvTitle.setText("考试不通过");
		tvContent.setText("非常抱歉，您已经答错了十一道题目，模拟考试未通过，请再接再厉!");
		view.findViewById(R.id.pay_cancel).setVisibility(View.GONE);
		view.setFocusable(true);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				pop.dismiss();
			}
		});
		pop.setContentView(view);
		view.findViewById(R.id.pay_ok).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// 跳转
						pop.dismiss();
						finish();
						// 退出支付流程,干掉之前的
					}
				});
		view.findViewById(R.id.pay_cancel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						pop.dismiss();
					}
				});
		pop.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
	}
	
	
	/**
	 * 获取指定 数量 的  题目
	 * @param list
	 * @param type
	 */
	private void getRandWeb(List<ExerciseVO> list,int type){
		Integer[] result = null;
		if(type==1){
			result = getRandomInt(list.size(), 100);
			for(int i=0;i<100;i++){
				dataExam.add(i, list.get(result[i]));
			}
		}else{
			result = getRandomInt(list.size(), 50);
			for(int i=0;i<50;i++){
				dataExam.add(i, list.get(result[i]));
			}
		}
		
		
	}
	
	/**
	 * 获取随机数
	 */
	private Integer[] getRandomInt(int max,int lenght){
		int[] num = new int[lenght];
		for (int i = 0; i < lenght; i++) {
			num[i] = i;
		}
		Integer[] result = getRandomNum(num, lenght,max);
		System.out.println(Arrays.toString(result));
		return result;
	}
	
	private Integer[] getRandomNum(int[] num, int n,int max) {
		Set<Integer> sets = new HashSet<Integer>();
		Random random = new Random();
		while (sets.size() < n) {
			sets.add(random.nextInt(max));
		}

	return sets.toArray(new Integer[n]);

	}

	/**
	 * 考试模式 1.倒计时 2.写入数据库 3.退出提示 4.答错题数量提示，
	 * 
	 */
	private void Exam() {// 考试
		minute = 30;
		second = 0;
		timerTask = new TimerTask() {

			@Override
			public void run() {
				Message msg = new Message();
				msg.what = 0;
				msg.arg1 = 1;
				handler.sendMessage(msg);
			}
		};

		timer = new Timer();
		timer.schedule(timerTask, 0, 1000);
	}

	Timer timer = null;

	TimerTask timerTask;

	private void handler() {
		if (minute == 0) {
			if (second == 0) {
				tvTime.setText("Time out !");
				if (timer != null) {
					timer.cancel();
					timer = null;
				}
				if (timerTask != null) {
					timerTask = null;
				}
			} else {
				second--;
				if (second >= 10) {
					tvTime.setText("0" + minute + ":" + second);
				} else {
					tvTime.setText("0" + minute + ":0" + second);
				}
			}
		} else {
			if (second == 0) {
				second = 59;
				minute--;
				if (minute >= 10) {
					tvTime.setText(minute + ":" + second);
				} else {
					tvTime.setText("0" + minute + ":" + second);
				}
			} else {
				second--;
				if (second >= 10) {
					if (minute >= 10) {
						tvTime.setText(minute + ":" + second);
					} else {
						tvTime.setText("0" + minute + ":" + second);
					}
				} else {
					if (minute >= 10) {
						tvTime.setText(minute + ":0" + second);
					} else {
						tvTime.setText("0" + minute + ":0" + second);
					}
				}
			}
		}
	}

}
