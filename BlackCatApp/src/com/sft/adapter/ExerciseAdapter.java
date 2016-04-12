package com.sft.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

import com.sft.fragment.ExciseFragment;
import com.sft.vo.questionbank.web_note;
/**
 * 练习 适配器
 * @author pengdonghua
 *FragmentStatePagerAdapter
 */
public class ExerciseAdapter extends FragmentPagerAdapter{
	
//	private Map<String,ExciseFragment> fragsMap = new HashMap<String,ExciseFragment>();
	
//	private List<ExciseFragment> frags = new ArrayList<ExciseFragment>(5);
	
	private List<web_note> data = new ArrayList<web_note>();
	
    private FragmentTransaction tran;
    private FragmentManager fm;
	
	@SuppressLint("CommitTransaction") 
	public ExerciseAdapter(FragmentManager fm) {
		super(fm);
		tran = fm.beginTransaction();
		this.fm = fm;
	}
	
	@SuppressLint("CommitTransaction") 
	public ExerciseAdapter(FragmentManager fm,List<web_note> data) {
		super(fm);
		tran = fm.beginTransaction();
		this.fm = fm;
		this.data = data;
	}
	
	
//	private void init(web_note vo,String po){
//		if(fragsMap.size()<5){
//			ExciseFragment frag = ExciseFragment.newInstance(vo, "321");
//			fragsMap.put(po, frag);
//		}else{
//		}	
//	}
	
	public void setData(List<web_note> data){
		this.data = data;
		notifyDataSetChanged();
//		for(int i=0;i<5;i++){
//			frags.add(ExciseFragment.newInstance(data.get(i), ""));
//		}
		
		
	}


	@Override
	public Fragment getItem(int arg0) {
		return ExciseFragment.newInstance(data.get(arg0), arg0+"321");
	}


	@Override
	public int getCount() {
		return data.size();
	}

}
