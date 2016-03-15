package com.sft.vo.commonvo;

import cn.sft.sqlhelper.DBVO;

public class ApplySchoolInfo extends DBVO {

	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private String mobile;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name == null ? "" : name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

}
