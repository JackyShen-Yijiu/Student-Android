package com.sft.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sft.blackcatapp.R;

/**
 * 报名   驾校/教练
 * @author sun  2016-2-18 下午3:27:10
 *
 */
public class EnrollFragment extends BaseFragment{
	
	public EnrollCoachFragment coachFragment = null;
	
	public EnrollSchoolFragament schoolFragment = null;
	
	public int type = 0;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_enroll_base, null);
		coachFragment = new EnrollCoachFragment();
		schoolFragment = new EnrollSchoolFragament();
		showSchool();
//		initView(view);
		return view;
	}

//	private void initView(View view) {
//		
//	}
	
	public void switchSchoolOrCoach(){
		if(type==0){
			showCoach();
		}else if(type == 1){
			showSchool();
		}
	}
	
	private void showSchool(){
		show(schoolFragment,"school");
		hide(coachFragment,"coach");
		type = 0;
	}
	
	private void showCoach(){
		hide(schoolFragment,"school");
		show(coachFragment,"coach");
		type = 1;
	}
	
	private void show(Fragment frag,String tag) {
		
		if(frag==null){
			if(tag.equals("school")){
				frag = new EnrollSchoolFragament();
			}else{
				frag = new EnrollCoachFragment();
			}
			
		}
		FragmentTransaction ft = getChildFragmentManager().beginTransaction();
		if(frag.isAdded()){
			ft.show(frag);
		}else{
			ft.add(R.id.frag_enroll_base,frag,tag);
		}
		ft.commitAllowingStateLoss();
	}
	
	private void hide(Fragment frag,String tag){
		if(frag==null){
			if(tag.equals("school")){
				frag = new EnrollSchoolFragament();
			}else{
				frag = new EnrollCoachFragment();
			}
		}
		FragmentTransaction ft = getChildFragmentManager().beginTransaction();
		if(frag.isAdded()){
			ft.hide(frag);
		}
		ft.commitAllowingStateLoss();
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data){
//		if(type == 0){
//			
//			schoolFragment.onActivityResult(requestCode, resultCode, data);
//		}else{
//			coachFragment.onActivityResult(requestCode, resultCode, data);
//		}
	}

	
	

}
