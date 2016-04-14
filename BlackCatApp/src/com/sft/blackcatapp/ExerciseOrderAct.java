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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

	static int minute = -1;
	static int second = -1;
	/** 章节id */
	public int chartId;
	public int kemu;
	/** 种类 0：练习 1：考试 2:错题 */
	private int flag = 0;

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.arg1 == 1) {// 倒计时
				handler();
			} else {
				// 刷新 显示
				adapter = new ExerciseAdapter(getSupportFragmentManager(),
						data1);
				LogUtil.print("size--->" + data1.size());
				viewpager.setAdapter(adapter);
				tvTotal.setText("1/" + data1.size());
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
		// 练习模式
		chartId = getIntent().getIntExtra("chartId", 0);
		kemu = getIntent().getIntExtra("kemu", 0);
		// 考试模式

		new Thread() {

			@Override
			public void run() {
				List<web_note> d = Util.getAllSubjectFourBank();
				data1 = doData(d);
				handler.sendEmptyMessage(1);
				for (int i = 0; i < d.size(); i++) {
					LogUtil.print(d.get(i).getType() + "size---000>"
							+ d.get(i).getAnswer_true());
					if (d.get(i).getAnswer_true().length() > 1) {
						LogUtil.print(d.get(i).getType() + "size---333>" + i);
					}
				}

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

	// public void getData(){
	// web_note a = new web_note();
	// for(int i=0;i<30;i++){
	// a = new web_note();
	// a.setId(i);
	// a.setQuestion("问题"+i);
	// a.setAnswer1("答案465"+i);
	// a.setAnswer2("答案465"+i);
	// a.setAnswer3("答案465"+i);
	// a.setAnswer4("答案465"+i);
	// a.setAnswer5("答案465"+i);
	// a.setAnswer_true("2");
	// a.setType(1);
	// data1.add(a);
	// }
	// }

	//
	// public void onClick(View view){
	// switch(view.getId()){
	// case R.id.base_left_btn://干掉页面
	// =======
	// adapter = new ExerciseAdapter(getSupportFragmentManager(), data1);
	// LogUtil.print("size--->" + data1.size());
	// viewpager.setAdapter(adapter);
	//
	// }

	// private void initData() {
	// getIntent().getStringExtra("chartId");
	//
	// new Thread(new Runnable() {
	// @Override
	// public void run() {
	// data1 = Util.getAllSubjectOneBank();
	// LogUtil.print("web_note--" + data1.size());
	// // 耗时的方法
	// handler.sendEmptyMessage(1);
	// // 执行耗时的方法之后发送消给handler
	// }
	// }).start();
	//
	// }

	// Handler handler = new Handler() {
	// @Override
	// public void handleMessage(Message msg) {
	// // handler接收到消息后就会执行此方法
	// switch (msg.what) {
	// case 1:
	// // initView();
	// // progressDialog.dismiss();
	// // 关闭ProgressDialog
	// break;
	// default:
	// break;
	// }
	// super.handleMessage(msg);
	// }
	// };

	// public void getData()[

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
		tvPercentage.setText((100 * right / (right + wrong)) + "%");
	}

	private void unzip() {
		UnZipUtils zip = new UnZipUtils();
		File f = new File(zip.targetPath);
		if (f.exists()) {
			LogUtil.print(zip.targetPath + "video--copy--fhdht>snvjsdl");
			return;
		} else {
			zip.createDir();
		}

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

	private int right = 0;
	private int wrong = 0;

	@Override
	public void do1() {
	}

	private void getRandWeb(List<ExerciseVO> list, int type) {
		Integer[] result = null;
		if (type == 1) {
			result = getRandomInt(list.size(), 100);
		} else {
			result = getRandomInt(list.size(), 50);
		}

	}

	/**
	 * 获取随机数
	 */
	private Integer[] getRandomInt(int max, int lenght) {
		int[] num = new int[lenght];
		for (int i = 0; i < lenght; i++) {
			num[i] = i;
		}
		Integer[] result = getRandomNum(num, lenght, max);
		System.out.println(Arrays.toString(result));
		return result;
	}

	private Integer[] getRandomNum(int[] num, int n, int max) {
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
