package com.sft.vo;

import java.util.ArrayList;
import java.util.List;

import cn.sft.sqlhelper.DBVO;

import com.sft.vo.questionbank.web_note;
/**
 * 试题
 * @author pengdonghua
 *
 */
public class ExerciseVO extends DBVO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**是否提交    0：未提交  1：提交     单选后 直接提交， 多选需要点击提交按钮*/
	public int submit = 0;
	
	private web_note webnote;
	
	
	public web_note getWebnote() {
		return webnote;
	}
	public void setWebnote(web_note webnote) {
		this.webnote = webnote;
	}
	public void setAnswers(List<ExerciseAnswerVO> answers ){
		this.answers = answers;
	}
	
	
	List<ExerciseAnswerVO> answers = null;
	/**
	 * 获取 答案列表
	 * @return
	 */
	public List<ExerciseAnswerVO> getAnswers(){
		if(answers!=null)
			return answers;
		answers = new ArrayList<ExerciseAnswerVO>();
		ExerciseAnswerVO vo = null;
		for(int i=0;i<7;i++){
			switch(i){
			case 0:
				vo = getAn(webnote.getAnswer1());
				break;
			case 1:
				vo = getAn(webnote.getAnswer2());
				break;
			case 2:
				vo = getAn(webnote.getAnswer3());
				break;
			case 3:
				vo = getAn(webnote.getAnswer4());
				break;
			case 4:
				vo = getAn(webnote.getAnswer5());
				break;
			case 5:
				vo = getAn(webnote.getAnswer6());
				break;
			case 6:
				vo = getAn(webnote.getAnswer7());
				break;
			default:
				vo = null;
				break;
					
			}
			if(vo!=null){
				answers.add(vo);
			}
		}
		return answers;
	}
	
	private ExerciseAnswerVO getAn(String str){
		ExerciseAnswerVO vo = null;
		if(null == str){
			
		}else{
			vo = new ExerciseAnswerVO(str);
		}
		return vo;
	}
	
//	private List<ExerciseAnswerVO> answers = new ArrayList<ExerciseAnswerVO>();
//	public List<ExerciseAnswerVO> getAnswers() {
//		return answers;
//	}
//	public void setAnswers(List<ExerciseAnswerVO> answers) {
//		this.answers = answers;
//	}
	
}
