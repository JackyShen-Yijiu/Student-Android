package com.sft.vo.questionbank;

import java.util.ArrayList;
import java.util.List;

import cn.sft.sqlhelper.DBVO;

import com.sft.vo.ExerciseAnswerVO;

public class web_note extends DBVO {

	private int id;
	private int type;
	private String intNumber;
	private String strTppe;
	private String strType_l;
	private String license_type;
	private String question;
	private String answer1;
	private String answer2;
	private String answer3;
	private String answer4;
	private String answer5;
	private String answer6;
	private String answer7;
	private String answer_true;
	private String explain;
	private int kemu;
	private String explain_form;
	private String moretypes;
	private int chapterid;
	private String img_url;
	private String video_url;
	private int diff_degree;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getIntNumber() {
		return intNumber;
	}

	public void setIntNumber(String intNumber) {
		this.intNumber = intNumber;
	}

	public String getStrTppe() {
		return strTppe;
	}

	public void setStrTppe(String strTppe) {
		this.strTppe = strTppe;
	}

	public String getStrType_l() {
		return strType_l;
	}

	public void setStrType_l(String strType_l) {
		this.strType_l = strType_l;
	}

	public String getLicense_type() {
		return license_type;
	}

	public void setLicense_type(String license_type) {
		this.license_type = license_type;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer1() {
		return answer1;
	}

	public void setAnswer1(String answer1) {
		this.answer1 = answer1;
	}

	public String getAnswer2() {
		return answer2;
	}

	public void setAnswer2(String answer2) {
		this.answer2 = answer2;
	}

	public String getAnswer3() {
		return answer3;
	}

	public void setAnswer3(String answer3) {
		this.answer3 = answer3;
	}

	public String getAnswer4() {
		return answer4;
	}

	public void setAnswer4(String answer4) {
		this.answer4 = answer4;
	}

	public String getAnswer5() {
		return answer5;
	}

	public void setAnswer5(String answer5) {
		this.answer5 = answer5;
	}

	public String getAnswer6() {
		return answer6;
	}

	public void setAnswer6(String answer6) {
		this.answer6 = answer6;
	}

	public String getAnswer7() {
		return answer7;
	}

	public void setAnswer7(String answer7) {
		this.answer7 = answer7;
	}

	public String getAnswer_true() {
		return answer_true;
	}

	public void setAnswer_true(String answer_true) {
		this.answer_true = answer_true;
	}

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

	public int getKemu() {
		return kemu;
	}

	public void setKemu(int kemu) {
		this.kemu = kemu;
	}

	public String getExplain_form() {
		return explain_form;
	}

	public void setExplain_form(String explain_form) {
		this.explain_form = explain_form;
	}

	public String getMoretypes() {
		return moretypes;
	}

	public void setMoretypes(String moretypes) {
		this.moretypes = moretypes;
	}

	public int getChapterid() {
		return chapterid;
	}

	public void setChapterid(int chapterid) {
		this.chapterid = chapterid;
	}

	public String getImg_url() {
		return img_url;
	}

	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}

	public String getVideo_url() {
		return video_url;
	}

	public void setVideo_url(String video_url) {
		this.video_url = video_url;
	}

	public int getDiff_degree() {
		return diff_degree;
	}

	public void setDiff_degree(int diff_degree) {
		this.diff_degree = diff_degree;
	}
	
	/**
	 * 获取 答案列表
	 * @return
	 */
	public List<ExerciseAnswerVO> getAnswers(){
		List<ExerciseAnswerVO> list = new ArrayList<ExerciseAnswerVO>();
		ExerciseAnswerVO vo = null;
		for(int i=0;i<7;i++){
			switch(i){
			case 0:
				vo = getAn(answer1);
				break;
			case 1:
				vo = getAn(answer2);
				break;
			case 2:
				vo = getAn(answer3);
				break;
			case 3:
				vo = getAn(answer4);
				break;
			case 4:
				vo = getAn(answer5);
				break;
			case 5:
				vo = getAn(answer6);
				break;
			case 6:
				vo = getAn(answer7);
				break;
			default:
				vo = null;
				break;
					
			}
			if(vo!=null){
				list.add(vo);
			}
		}
		return list;
	}
	
	private ExerciseAnswerVO getAn(String str){
		ExerciseAnswerVO vo = null;
		if(null == str){
			
		}else{
			vo = new ExerciseAnswerVO(str);
		}
		return vo;
	}

}