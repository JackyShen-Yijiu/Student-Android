package com.sft.vo.coachvo;

import cn.sft.sqlhelper.DBVO;

public class TrainFieldlInfo extends DBVO {

	private static final long serialVersionUID = 1L;

	private String name;
	private String id;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
