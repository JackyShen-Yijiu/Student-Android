package com.sft.vo.coursevo;

import cn.sft.sqlhelper.DBVO;

public class CourseTime extends DBVO {

	private static final long serialVersionUID = 1L;

	private String endtime;
	private String begintime;
	private String timespace;
	private String timeid;

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getBegintime() {
		return begintime;
	}

	public void setBegintime(String begintime) {
		this.begintime = begintime;
	}

	public String getTimespace() {
		return timespace;
	}

	public void setTimespace(String timespace) {
		this.timespace = timespace;
	}

	public String getTimeid() {
		return timeid;
	}

	public void setTimeid(String timeid) {
		this.timeid = timeid;
	}

}
