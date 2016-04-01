package com.sft.util;

import android.view.View;
import android.widget.Button;

import com.jzjf.app.R;
import com.sft.common.BlackCatApplication;
import com.sft.common.Config.EnrollResult;

/**
 * 报名 条件 统一 控制
 * @author pengdonghua
 *
 */
public class EnrollUtils {

	/**
	 * 报名状态 改变
	 * @param entrollBut
	 */
	public  static void  doEnroll(Button entrollBut){
		BlackCatApplication app = BlackCatApplication.getInstance();
//		LogUtil.print(app.isLogin+"<<login>>"+app.userVO.getApplystate());
		if(app.isLogin){//已经登录了
			if(EnrollResult.SUBJECT_NONE.getValue().equals(app.userVO.getApplystate())){//没有登录， 或者未报名
				entrollBut.setText("报名");
				entrollBut.setTextColor(app.getResources().getColor(
						R.color.white));
				entrollBut.setVisibility(View.VISIBLE);
				entrollBut.setEnabled(true);
			}else{//已经报名的，隐藏掉
				entrollBut.setVisibility(View.GONE);
			}
		}else{//未登录
			entrollBut.setText("报名");
			entrollBut.setTextColor(app.getResources().getColor(
					R.color.white));
			entrollBut.setVisibility(View.VISIBLE);
			entrollBut.setEnabled(true);
		}
		
		
		
		
	}
	
	
}
