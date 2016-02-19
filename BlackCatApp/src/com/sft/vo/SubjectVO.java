package com.sft.vo;

import cn.sft.sqlhelper.DBVO;

public class SubjectVO extends DBVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int subjectId;
	private String name;

	public int getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
