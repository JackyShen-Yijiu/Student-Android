package com.sft.vo;

import cn.sft.sqlhelper.DBVO;
/**
 * 答案
 * @author pengdonghua
 *
 */
public class ExerciseAnswerVO extends DBVO{

	public ExerciseAnswerVO(String content){
		this.content = content;
	}
	
	private String content;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getChecked() {
		return checked;
	}
	public void setChecked(int checked) {
		this.checked = checked;
	}
	/**是否选中 0:未选中  1*/
	private int checked;
//	private 
	
}
