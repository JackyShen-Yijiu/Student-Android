package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.TextView;

import com.jzjf.app.R;
import com.sft.adapter.ExerciseAdapter;
import com.sft.util.LogUtil;
import com.sft.util.Util;
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
	
	private TextView tvTime,tvPercentage,tvWrong,tvRight,tvTotal;
	
	private ExerciseAdapter adapter;
	
	/**缓存数据*/
//	private List<web_note> data1 = null;
	
	private List<ExerciseVO> data1 = null;
	
	Handler handler =new Handler(){

		@Override
		public void handleMessage(Message msg) {
			//刷新 显示
			adapter = new ExerciseAdapter(getSupportFragmentManager(),data1);
			LogUtil.print("size--->"+data1.size());
			viewpager.setAdapter(adapter);
			tvTotal.setText("1/"+data1.size());
			
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_exercise_order);
		initData();
//		getData();
		initView();
		
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
				tvTotal.setText(arg0+"/"+data1.size());
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
	
	private void initData(){
		data1 = new ArrayList<ExerciseVO>();
		getIntent().getStringExtra("chartId");
		new Thread(){

			@Override
			public void run() {
				List<web_note> d = Util.getAllSubjectOneBank();
				data1 = doData(d);
				handler.sendEmptyMessage(1);
				for(int i=0;i<d.size();i++){
					LogUtil.print(d.get(i).getType()+"size---000>"+d.get(i).getAnswer_true());
					if(d.get(i).getAnswer_true().length()>1){
						LogUtil.print(d.get(i).getType()+"size---333>"+i);
					}
				}
			
			}
			
		}.start();
		
		
	}
	
	/**
	 * 读取数据库，保存为可以使用的数据
	 * @param data1
	 * @return
	 */
	private List<ExerciseVO> doData(List<web_note> data1){
		List<ExerciseVO> data = new ArrayList<ExerciseVO>();
		ExerciseVO vo = null;
		for(int i=0;i<data1.size();i++){
			vo = new ExerciseVO();
			vo.setWebnote(data1.get(i));
			data.add(vo);
		}
		return data;
	}
	
//	public void getData(){
//		web_note a = new web_note();
//		for(int i=0;i<30;i++){
//			a = new web_note();
//			a.setId(i);
//			a.setQuestion("问题"+i);
//			a.setAnswer1("答案465"+i);
//			a.setAnswer2("答案465"+i);
//			a.setAnswer3("答案465"+i);
//			a.setAnswer4("答案465"+i);
//			a.setAnswer5("答案465"+i);
//			a.setAnswer_true("2");
//			a.setType(1);
//			data1.add(a);
//		}
//	}
	
	
	public void onClick(View view){
		switch(view.getId()){
		case R.id.base_left_btn://干掉页面
			finish();
			break;
		}
	}
	
	/**
	 * 自动下一页
	 */
	public void next(){
		viewpager.setCurrentItem(viewpager.getCurrentItem()+1, true);
	}
	
	/**
	 * 增加一个
	 * @param right
	 */
	public void addRight(){
		right++;
		tvRight.setText(String.valueOf(right));
		setPercentage();
	}
	
	/**
	 * 错题增加一个
	 */
	public void addWrong(){
		wrong++;
		tvWrong.setText(String.valueOf(wrong));
		setPercentage();
	}
	
	/**
	 * 正确率
	 */
	private void setPercentage(){
		tvPercentage.setText(((int)(100*right/(right+wrong)))+"%");
	}
	
	private int right = 0;
	private int wrong = 0;
	
}
