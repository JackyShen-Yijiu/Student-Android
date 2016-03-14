package com.sft.vo;

import cn.sft.sqlhelper.DBVO;

public class CoachCourseV2VO extends DBVO {

	private static final long serialVersionUID = 1L;

	private String begintime;
	private String endtime;
	private int coachcount; // 可以预约教练的数量
	private int is_outofdate; // 0 过期 1 正常
	private int is_reservation; // 0没有预约 1 已经预约
	private int is_rest; // 0休息 1 不休息
	private String reservationcoachname;// 预约教练名字
	private String timeid;
	private String timespace;
	private CourseData coursedata; // 这个时段有教练课程安排 下面 是课程安排信息

	public String getBegintime() {
		return begintime;
	}

	public void setBegintime(String begintime) {
		this.begintime = begintime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public int getCoachcount() {
		return coachcount;
	}

	public void setCoachcount(int coachcount) {
		this.coachcount = coachcount;
	}

	public int getIs_outofdate() {
		return is_outofdate;
	}

	public void setIs_outofdate(int is_outofdate) {
		this.is_outofdate = is_outofdate;
	}

	public int getIs_reservation() {
		return is_reservation;
	}

	public void setIs_reservation(int is_reservation) {
		this.is_reservation = is_reservation;
	}

	public int getIs_rest() {
		return is_rest;
	}

	public void setIs_rest(int is_rest) {
		this.is_rest = is_rest;
	}

	public String getReservationcoachname() {
		return reservationcoachname;
	}

	public void setReservationcoachname(String reservationcoachname) {
		this.reservationcoachname = reservationcoachname;
	}

	public String getTimeid() {
		return timeid;
	}

	public void setTimeid(String timeid) {
		this.timeid = timeid;
	}

	public String getTimespace() {
		return timespace;
	}

	public void setTimespace(String timespace) {
		this.timespace = timespace;
	}

	public CourseData getCoursedata() {
		return coursedata;
	}

	public void setCoursedata(CourseData coursedata) {
		this.coursedata = coursedata;
	}

}
