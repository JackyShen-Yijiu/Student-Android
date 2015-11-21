package com.sft.vo.uservo;

import cn.sft.sqlhelper.DBVO;

public class VipServerListVO extends DBVO{

	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String _id;
	private String coclor;

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

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getCoclor() {
		return coclor;
	}

	public void setCoclor(String coclor) {
		this.coclor = coclor;
	}

}
