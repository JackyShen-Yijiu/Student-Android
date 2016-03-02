package com.sft.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sft.adapter.ViewPagerAdapter;
import com.jzjf.app.R;
import com.sft.util.LogUtil;
import com.sft.view.PagerSlidingTab;

/**
 * 报名 - 驾校\教练
 * @author sun  2016-2-18 上午9:29:40
 *
 */
public class EnrollSchoolFragament extends BaseFragment{

	PagerSlidingTab slidingTab;
	
	ViewPager viewPager;
	
	private String[] title = {"评分","价格","综合"};
	private int[] orderType = {2,3,0};
	//评分：2  价格3  综合0
	
//	public String cityname = "北京";
	
	private SchoolsFragment[] schools = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_enroll, null);
		initView(view);
		return view;
	}

	private void initView(View view) {
		// TODO Auto-generated method stub
		slidingTab = (PagerSlidingTab) view.findViewById(R.id.act_student1_sliding_tab);
        viewPager = (ViewPager) view.findViewById(R.id.act_student1_view_pager);
        viewPager.setOffscreenPageLimit(2);
        /**普通学员列表 0 群发短信学员列表 1*/
        int flag = getActivity().getIntent().getFlags();                
        schools = new  SchoolsFragment[3];//{StudentFragment1.getInstance(0),StudentFragment1.getInstance(1)};
        for(int i =0;i<3;i++){
        	schools[i] =new SchoolsFragment();
            Bundle b = new Bundle();
            b.putInt("type",orderType[i]);
            schools[i].setArguments(b);
        }

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager(),title,schools);
        viewPager.setAdapter(adapter);
        slidingTab.setViewPager(viewPager);
	}
	
	public void requestByCity(String name){
		for(int i=0;i<3;i++){
			schools[i].getSchoolByCity(name);//viewPager.getCurrentItem()
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		LogUtil.print("onActiivty---->666"+resultCode);
//		if(data!=null){
//			if(resultCode == 9){//选择城市
//				
//			}
//		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	

	
}
