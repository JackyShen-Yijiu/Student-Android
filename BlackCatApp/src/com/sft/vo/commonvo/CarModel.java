package com.sft.vo.commonvo;

import cn.sft.sqlhelper.DBVO;

public class CarModel extends DBVO {

	private static final long serialVersionUID = 1L;

	private String name;

	private String code;

	private String modelsid;

	public String getName() {
		return name == null ? "" : name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code == null ? "" : code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getModelsid() {
		return modelsid;
	}

	public void setModelsid(String modelsid) {
		this.modelsid = modelsid;
	}

}
