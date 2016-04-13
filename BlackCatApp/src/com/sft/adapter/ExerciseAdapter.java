package com.sft.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

import com.sft.fragment.ExciseFragment;
import com.sft.vo.ExerciseVO;
import com.sft.vo.questionbank.web_note;
/**
 * 练习 适配器
 * @author pengdonghua
 *FragmentStatePagerAdapter
 */
public class ExerciseAdapter extends FragmentPagerAdapter{
	
//	private Map<String,ExciseFragment> fragsMap = new HashMap<String,ExciseFragment>();
	
//	private List<ExciseFragment> frags = new ArrayList<ExciseFragment>(5);
	
	private List<ExerciseVO> data = new ArrayList<ExerciseVO>();
	
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
	}
	
	
	
	public void setData(List<ExerciseVO> data){
		this.data = data;
		notifyDataSetChanged();
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
