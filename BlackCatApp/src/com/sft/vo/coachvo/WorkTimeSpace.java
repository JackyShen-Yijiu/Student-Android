package com.sft.vo.coachvo;

import cn.sft.sqlhelper.DBVO;

public class WorkTimeSpace extends DBVO {

	private static final long serialVersionUID = 1L;

	private String endtimeint;
	private String begintimeint;

	public String getEndtimeint() {
		return endtimeint;
	}

	public void setEndtimeint(String endtimeint) {
		this.endtimeint = endtimeint;
	}

	public String getBegintimeint() {
		return begintimeint;
	}

	public void setBegintimeint(String begintimeint) {
		this.begintimeint = begintimeint;
	}

}
