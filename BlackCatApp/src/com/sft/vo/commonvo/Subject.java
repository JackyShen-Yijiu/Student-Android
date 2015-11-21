package com.sft.vo.commonvo;

import cn.sft.sqlhelper.DBVO;

public class Subject extends DBVO {

	private static final long serialVersionUID = 1L;
	private String _id;
	// 报考状态 0 未报名1 报名中 2报名成功
	private String subjectid;
	private String name;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getSubjectid() {
		return subjectid;
	}

	public void setSubjectid(String subjectid) {
		this.subjectid = subjectid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
