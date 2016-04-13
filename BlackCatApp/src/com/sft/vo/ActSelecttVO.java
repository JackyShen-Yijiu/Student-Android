package com.sft.vo;

import cn.sft.sqlhelper.DBVO;

public class ActSelecttVO extends DBVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String _id;
	private String productname;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getProductname() {
		return productname;
	}

	public void setProductname(String productname) {
		this.productname = productname;
	}

}
