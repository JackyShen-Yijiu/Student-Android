package com.sft.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sft.adapter.ViewPagerAdapter;
import com.sft.blackcatapp.R;
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
        /**普通学员列表 0 群发短信学员列表 1*/
        int flag = getActivity().getIntent().getFlags();
        schools = new  SchoolsFragment[3];//{StudentFragment1.getInstance(0),StudentFragment1.getInstance(1)};
        for(int i =0;i<3;i++){
        	schools[i] =new SchoolsFragment();
            Bundle b = new Bundle();
            b.putInt("type",i+1);
            schools[i].setArguments(b);
        }

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager(),title,schools);
        viewPager.setAdapter(adapter);
        slidingTab.setViewPager(viewPager);
	}

	
}
