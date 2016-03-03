package com.sft.vo;

import cn.sft.sqlhelper.DBVO;

public class QuestionLibVO extends DBVO {

	private static final long serialVersionUID = 1L;

	private String questionerrorurl;
	private String questiontesturl;
	private String questionlisturl;
	private String kemusichengjidanurl;

	public String getQuestionerrorurl() {
		return questionerrorurl;
	}

	public void setQuestionerrorurl(String questionerrorurl) {
		this.questionerrorurl = questionerrorurl;
	}

	public String getQuestiontesturl() {
		return questiontesturl;
	}

	public void setQuestiontesturl(String questiontesturl) {
		this.questiontesturl = questiontesturl;
	}

	public String getQuestionlisturl() {
		return questionlisturl;
	}

	public void setQuestionlisturl(String questionlisturl) {
		this.questionlisturl = questionlisturl;
	}

	public String getKemusichengjidanurl() {
		return kemusichengjidanurl;
	}

	public void setKemusichengjidanurl(String kemusichengjidanurl) {
		this.kemusichengjidanurl = kemusichengjidanurl;
	}

}
