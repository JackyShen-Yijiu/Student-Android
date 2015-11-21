package com.sft.vo;

import cn.sft.sqlhelper.DBVO;

public class PushVO extends DBVO {

	private static final long serialVersionUID = 1L;

	private String alert;
	private String title;
	private String builder_id;
	private PushInnerVO extras;

	public String getAlert() {
		return alert;
	}

	public void setAlert(String alert) {
		this.alert = alert;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBuilder_id() {
		return builder_id;
	}

	public void setBuilder_id(String builder_id) {
		this.builder_id = builder_id;
	}

	public PushInnerVO getExtras() {
		return extras;
	}

	public void setExtras(PushInnerVO extras) {
		this.extras = extras;
	}

}
