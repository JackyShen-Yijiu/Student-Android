package com.sft.vo;

import cn.sft.sqlhelper.DBVO;

public class CarModelVO extends DBVO {

	public static final String ENROLL_USER_SELECTED = "1";
	private static final long serialVersionUID = 1L;

	private String modelsid;
	// 类型
	private String code;
	// 名称
	private String name;

	private String db_userid;
	private String db_carmodelStyle;

	public String getDb_userid() {
		return db_userid;
	}

	public void setDb_userid(String db_userid) {
		this.db_userid = db_userid;
	}

	public String getDb_carmodelStyle() {
		return db_carmodelStyle;
	}

	public void setDb_carmodelStyle(String db_carmodelStyle) {
		this.db_carmodelStyle = db_carmodelStyle;
	}

	public String getModelsid() {
		return modelsid;
	}

	public void setModelsid(String modelsid) {
		this.modelsid = modelsid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		String string = "{\"modelsid\":\"" + modelsid + "\",\"name\":\"" + name
				+ "\",\"code\":\"" + code + "\"}";
		return string;
	}
}
