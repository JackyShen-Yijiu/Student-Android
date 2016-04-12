package com.sft.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sft.fragment.ExciseFragment;
import com.sft.util.LogUtil;
import com.sft.vo.ExerciseVO;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
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
	
	
//	private void init(ExerciseVO vo,String po){
//		if(fragsMap.size()<5){
//			ExciseFragment frag = ExciseFragment.newInstance(vo, "321");
//			fragsMap.put(po, frag);
//		}else{
//		}	
//	}
	
	public void setData(List<ExerciseVO> data){
		this.data = data;
		notifyDataSetChanged();
//		for(int i=0;i<5;i++){
//			frags.add(ExciseFragment.newInstance(data.get(i), ""));
//		}
		
		
	}

//	@Override
//	public void destroyItem(ViewGroup container, int position, Object object) {
//		Fragment fragment = (Fragment)object;
//
//		if (tran == null) {
//			tran = fm.beginTransaction();
//		}
////		if (DEBUG) Log.v(TAG, “Removing item #” + position + “: f=” + object
////		+ ” v=” + ((Fragment)object).getView());
////		while (mSavedState.size() mSavedState.add(null);
////		}
////		mSavedState.set(position, mFragmentManager.saveFragmentInstanceState(fragment)); //保存fragment的状态，如果有设置的话
////		frags.set(position, null);//清空mFragments列表中该位置的fragment
////
////		tran.remove(fragment);//把fragment移除
//		
//		super.destroyItem(container, position, object);
//	}

//	@Override
//	public void finishUpdate(ViewGroup container) {
//		super.finishUpdate(container);
//	}

	@Override
	public Fragment getItem(int arg0) {
		return ExciseFragment.newInstance(data.get(arg0), arg0+"321");
	}

//	@Override
//	public Object instantiateItem(ViewGroup arg0, int arg1) {
//		 
////		if (tran == null) {
////			tran = fm.beginTransaction();
////			}
////
////			final long itemId = getItemId(arg1); //获取id
////
////			// Do we already have this fragment?
////			String name = makeFragmentName(container.getId(), itemId);
////			//生成fragment Tag，格式”android:switcher:” + viewId + “:” + id;也可以通过这个来获取一个Fragment的实例
////			Fragment fragment = mFragmentManager.findFragmentByTag(name);
////			//下面判断是否有该Fragment，如果有就直接Attach到Activity里面去，没有的话就通过getItem方法获取新的实例
////			if (fragment != null) {
////			if (DEBUG) Log.v(TAG, “Attaching item #” + itemId + “: f=” + fragment);
////			mCurTransaction.attach(fragment);
////			} else {
////			fragment = getItem(arg1);
////			if (DEBUG) Log.v(TAG, “Adding item #” + itemId + “: f=” + fragment);
////			mCurTransaction.add(container.getId(), fragment,
////			makeFragmentName(container.getId(), itemId));
////			}
////			if (fragment != mCurrentPrimaryItem) {
////			FragmentCompat.setMenuVisibility(fragment, false);
////			FragmentCompat.setUserVisibleHint(fragment, false);
////			}
////
////			return fragment;
////		return super.instantiateItem(arg0, arg1);
//		ExciseFragment frag = ExciseFragment.newInstance(data.get(arg1), "");
//		LogUtil.print("instantiateItem----"+data.get(arg1).getQuestion());
////		tran.add(arg0, frag);
//		return frag;
//	}

//	@Override
//	public boolean isViewFromObject(View view, Object object) {
////		return super.isViewFromObject(view, object);
//		return false;
//	}

//	@Override
//	public void startUpdate(ViewGroup container) {
//		// TODO Auto-generated method stub
//		super.startUpdate(container);
//	}

	@Override
	public int getCount() {
		return data.size();
	}

}
