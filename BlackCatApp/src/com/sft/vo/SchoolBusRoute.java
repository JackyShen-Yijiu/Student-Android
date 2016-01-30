package com.sft.vo;

import cn.sft.sqlhelper.DBVO;

public class SchoolBusRoute extends DBVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String __v;
	private String _id;
	private String routecontent;
	private String routename;
	private String schoolid;

	public String get__v() {
		return __v;
	}

	public void set__v(String __v) {
		this.__v = __v;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getRoutecontent() {
		return routecontent;
	}

	public void setRoutecontent(String routecontent) {
		this.routecontent = routecontent;
	}

	public String getRoutename() {
		return routename;
	}

	public void setRoutename(String routename) {
		this.routename = routename;
	}

	public String getSchoolid() {
		return schoolid;
	}

	public void setSchoolid(String schoolid) {
		this.schoolid = schoolid;
	}

}
