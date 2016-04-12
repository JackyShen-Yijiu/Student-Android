package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;


import com.jzjf.app.R;
import com.sft.adapter.ExerciseAdapter;
import com.sft.util.LogUtil;
import com.sft.util.Util;
import com.sft.vo.ExerciseAnswerVO;
import com.sft.vo.ExerciseVO;
import com.sft.vo.questionbank.web_note;

/**
 * 练习  顺序
 * @author pengdonghua
 *
 */
public class ExerciseOrderAct extends BaseFragmentAct{
	
	private final static int  DATA_SIZE = 50 ;

	private ViewPager viewpager;
	
	private ExerciseAdapter adapter;
	
	/**缓存数据*/
	private List<web_note> data1 = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_exercise_order);
		initData();
		initView();
		
	}

	private void initView() {
		viewpager = (ViewPager) findViewById(R.id.act_exer_order_vp);
		adapter = new ExerciseAdapter(getSupportFragmentManager(),data1);
		LogUtil.print("size--->"+data1.size());
		viewpager.setAdapter(adapter);
		
	}
	
	private void initData(){
		getIntent().getStringExtra("chartId");
		data1 = Util.getAllSubjectFourBank();
		
	}
	
//	public void getData()[
	
	
	public void onClick(View view){
		switch(view.getId()){
		case R.id.base_left_btn://干掉页面
			finish();
			break;
		}
	}

	
	
	
}
