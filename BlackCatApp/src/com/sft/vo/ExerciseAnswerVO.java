package com.sft.vo;

import java.util.ArrayList;
import java.util.List;

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
	/**是否选中      0:未选中  1:已经选择且正确  2: 已经选择 但是错误*/
	private int checked;
	
	/***我的答案，*/
	public List<Integer> myAnswers = new ArrayList<Integer>();
	
}
