package com.sft.vo;

import cn.sft.sqlhelper.DBVO;

public class QuestionVO extends DBVO {

	private static final long serialVersionUID = 1L;
	private QuestionLibVO subjectone;
	private QuestionLibVO subjectfour;

	public QuestionLibVO getSubjectone() {
		return subjectone == null ? new QuestionLibVO() : subjectone;
	}

	public void setSubjectone(QuestionLibVO subjectone) {
		this.subjectone = subjectone;
	}

	public QuestionLibVO getSubjectfour() {
		return subjectfour == null ? new QuestionLibVO() : subjectfour;
	}

	public void setSubjectfour(QuestionLibVO subjectfour) {
		this.subjectfour = subjectfour;
	}

}
