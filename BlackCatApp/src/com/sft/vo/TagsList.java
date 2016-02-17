package com.sft.vo;

import cn.sft.sqlhelper.DBVO;

public class TagsList extends DBVO {

	private static final long serialVersionUID = 1L;
	private String _id;
	private String color;
	private String tagname;
	private String tagtype;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getTagname() {
		return tagname;
	}

	public void setTagname(String tagname) {
		this.tagname = tagname;
	}

	public String getTagtype() {
		return tagtype;
	}

	public void setTagtype(String tagtype) {
		this.tagtype = tagtype;
	}

}
