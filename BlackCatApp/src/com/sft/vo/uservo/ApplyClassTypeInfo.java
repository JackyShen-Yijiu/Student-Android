package com.sft.vo.uservo;

import cn.sft.sqlhelper.DBVO;

public class ApplyClassTypeInfo extends DBVO {

	private static final long serialVersionUID = 1L;

	private String price;
	private String name;
	private String id;

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

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
