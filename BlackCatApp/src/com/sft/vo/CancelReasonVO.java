package com.sft.vo;

import cn.sft.sqlhelper.DBVO;

public class CancelReasonVO extends DBVO {

	private static final long serialVersionUID = 1L;
	private String reason;
	private String cancelcontent;

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getCancelcontent() {
		return cancelcontent;
	}

	public void setCancelcontent(String cancelcontent) {
		this.cancelcontent = cancelcontent;
	}

}
