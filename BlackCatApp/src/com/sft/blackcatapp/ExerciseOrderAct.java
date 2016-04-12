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
import com.sft.vo.ExerciseAnswerVO;
import com.sft.vo.ExerciseVO;

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
	private List<ExerciseVO> data1 = new ArrayList<ExerciseVO>(DATA_SIZE);
	
	private List<ExerciseVO> data2 = new ArrayList<ExerciseVO>(DATA_SIZE);
	
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
		
		ExerciseVO vo ;
		List<ExerciseAnswerVO> answers = new ArrayList<ExerciseAnswerVO>();
		
		for(int i =0;i<30;i++){
			vo = new ExerciseVO();
			vo.setQuestion("问题sdjljsdafjojaifojdojasdaojds撒旦法撒旦法双方的 "+i);
			answers = new ArrayList<ExerciseAnswerVO>();
			for(int j=0;j<4;j++){
				answers.add(new ExerciseAnswerVO(j+"答a案"+i));
			}
			vo.setAnswers(answers);
			LogUtil.print("int--->"+new Random().nextInt(4));
			vo.setType(new Random().nextInt(4));
			vo.setId(i);
			data1.add(vo);
		}
	}
	
//	public void 
	
	public void onClick(View view){
		switch(view.getId()){
		case R.id.base_left_btn://干掉页面
			finish();
			break;
		}
	}

	
	
	
}
