package com.sft.vo;

import cn.sft.sqlhelper.DBVO;

public class LocationShowTypeVO extends DBVO {

	public static final String ENROLL_USER_SELECTED = "1";
	public static final String APPOINTMENT_COACH = "2";

	private String name;
	private int showtype;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getShowtype() {
		return showtype;
	}

	public void setShowtype(int showtype) {
		this.showtype = showtype;
	}

}
