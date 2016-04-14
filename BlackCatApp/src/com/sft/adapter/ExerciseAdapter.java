package com.sft.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

import com.sft.fragment.ExciseFragment;
import com.sft.util.LogUtil;
import com.sft.vo.ExerciseVO;
/**
 * 练习 适配器
 * @author pengdonghua
 *FragmentStatePagerAdapter
 */
public class ExerciseAdapter extends FragmentPagerAdapter{
	
	private Map<String,ExciseFragment> fragsMap = new HashMap<String,ExciseFragment>();
	
	Stack<ExciseFragment> statck = null;
	
//	private List<ExciseFragment> frags = new ArrayList<ExciseFragment>(5);
	
	private List<ExerciseVO> data = new ArrayList<ExerciseVO>();
	
	private int lastId;
	
	
    private FragmentTransaction tran;
    private FragmentManager fm;
	
	@SuppressLint("CommitTransaction") 
	public ExerciseAdapter(FragmentManager fm) {
		super(fm);
		tran = fm.beginTransaction();
		this.fm = fm;
	}
	
	@SuppressLint("CommitTransaction") 
	public ExerciseAdapter(FragmentManager fm,List<ExerciseVO> data) {
		super(fm);
		tran = fm.beginTransaction();
		this.fm = fm;
		this.data = data;
		 statck = new Stack<ExciseFragment>();
		statck.setSize(2);
	}
	
	
	public ExciseFragment getByTag(String tag){
		if(statck.size()>0){
			ExciseFragment vo0 = statck.get(0);
			if(vo0!=null && tag.equals(vo0.getMTag())){//
				return vo0;
			}
		}
		if(statck.size()>1){
			ExciseFragment vo1 = statck.get(1);
			if(vo1!=null && tag.equals(vo1.getMTag())){//
				return vo1;
			}
		}
		return null;
	}
	
	private void put(ExciseFragment vo){
		statck.push(vo);
	}
	
	public void setData(List<ExerciseVO> data){
		this.data = data;
		notifyDataSetChanged();
	}


	@Override
	public Fragment getItem(int arg0) {
		LogUtil.print("getItem-->"+arg0);
		ExciseFragment t = ExciseFragment.newInstance(data.get(arg0), arg0+"");
		put(t);
		return t;
	}


	@Override
	public int getCount() {
		return data.size();
	}

}
