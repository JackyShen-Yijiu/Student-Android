package com.sft.vo;

import cn.sft.sqlhelper.DBVO;

public class MyCodeVO extends DBVO {
	private static final long serialVersionUID = 1L;

	private String name;
	private String Ycode;
	private String date;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getYcode() {
		return Ycode;
	}

	public void setYcode(String ycode) {
		Ycode = ycode;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
