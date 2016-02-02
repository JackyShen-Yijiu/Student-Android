package com.sft.dialog;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.sft.blackcatapp.MyAppointmentActivity;
import com.sft.vo.MyAppointmentVO;
/**
 *  微评论  对话框
 * @author sun  2016-2-2 下午5:11:57
 *
 */
public class NoCommentDialog extends CheckApplyDialog{

	List<MyAppointmentVO> list = new ArrayList<MyAppointmentVO>();

	
	public NoCommentDialog(Context context) {
		super(context);
	}
	
	@Override
	public void onClick(View v) {
		Intent i = new Intent(v.getContext(),MyAppointmentActivity.class);
		i.putExtra("flag", 1);//未评论 列表
		v.getContext().startActivity(i);
//		dismiss();
		
	}
	
	

}
