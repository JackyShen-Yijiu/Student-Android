package com.sft.vo;

import cn.sft.sqlhelper.DBVO;

public class ActivitiesVO extends DBVO {

	public static final String ENROLL_USER_SELECTED = "1";
	public static final String APPOINTMENT_COACH = "2";

	private String id;
	private String name;
	private String titleimg;
	private String enddate;
	private String contenturl;
	private String begindate;
	private String address;
	private String activitystate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getActivitystate() {
		return activitystate;
	}

	public void setActivitystate(String activitystate) {
		this.activitystate = activitystate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitleimg() {
		return titleimg;
	}

	public void setTitleimg(String titleimg) {
		this.titleimg = titleimg;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public String getContenturl() {
		return contenturl;
	}

	public void setContenturl(String contenturl) {
		this.contenturl = contenturl;
	}

	public String getBegindate() {
		return begindate;
	}

	public void setBegindate(String begindate) {
		this.begindate = begindate;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public static String getEnrollUserSelected() {
		return ENROLL_USER_SELECTED;
	}

	public static String getAppointmentCoach() {
		return APPOINTMENT_COACH;
	}

}
