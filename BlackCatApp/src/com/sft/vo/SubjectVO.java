package com.sft.vo;

import cn.sft.sqlhelper.DBVO;

public class SubjectVO extends DBVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int subjecyId;
	private String name;

	public int getSubjecyId() {
		return subjecyId;
	}

	public void setSubjecyId(int subjecyId) {
		this.subjecyId = subjecyId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
