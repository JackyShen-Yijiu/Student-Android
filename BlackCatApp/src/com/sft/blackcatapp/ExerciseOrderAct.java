package com.sft.blackcatapp;

import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.jzjf.app.R;
import com.sft.adapter.ExerciseAdapter;
import com.sft.util.LogUtil;
import com.sft.util.Util;
import com.sft.vo.questionbank.web_note;

/**
 * 练习 顺序
 * 
 * @author pengdonghua
 * 
 */
public class ExerciseOrderAct extends BaseFragmentAct {

	private final static int DATA_SIZE = 50;

	private ViewPager viewpager;

	private ExerciseAdapter adapter;

	/** 缓存数据 */
	private List<web_note> data1 = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_exercise_order);
		initData();
		// initView();

	}

	private void initView() {
		viewpager = (ViewPager) findViewById(R.id.act_exer_order_vp);
		adapter = new ExerciseAdapter(getSupportFragmentManager(), data1);
		LogUtil.print("size--->" + data1.size());
		viewpager.setAdapter(adapter);

	}

	private void initData() {
		getIntent().getStringExtra("chartId");

		new Thread(new Runnable() {
			@Override
			public void run() {
				data1 = Util.getAllSubjectOneBank();
				LogUtil.print("web_note--" + data1.size());
				// 耗时的方法
				handler.sendEmptyMessage(1);
				// 执行耗时的方法之后发送消给handler
			}
		}).start();

	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// handler接收到消息后就会执行此方法
			switch (msg.what) {
			case 1:
				// initView();
				// progressDialog.dismiss();
				// 关闭ProgressDialog
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	// public void getData()[

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.base_left_btn:// 干掉页面
			finish();
			break;
		}
	}

}
