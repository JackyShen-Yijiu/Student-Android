package com.sft.vo.coachvo;

import cn.sft.sqlhelper.DBVO;

public class TrainFieldlInfo extends DBVO {

	private static final long serialVersionUID = 1L;

	private String fieldname;
	private String _id;
	private String phone;
	private String[] pictures;

	private String id;
	private String name;

	public String getFieldname() {
		return fieldname == null ? "" : fieldname;
	}

	public void setFieldname(String fieldname) {
		this.fieldname = fieldname;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String[] getPictures() {
		return pictures;
	}

	public void setPictures(String[] pictures) {
		this.pictures = pictures;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
