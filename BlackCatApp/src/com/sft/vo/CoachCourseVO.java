package com.sft.vo;

import cn.sft.sqlhelper.DBVO;

import com.sft.vo.coursevo.CourseTime;

public class CoachCourseVO extends DBVO {

	private static final long serialVersionUID = 1L;

	private String coursedate;
	private String coachid;
	private String _id;
	private String selectedstudentcount;
	private String coursestudentcount;
	private String createtime;
	private String[] courseuser;
	private String[] coursereservation;
	private CourseTime coursetime;
	private String coursebegintime;
	private String coursendtime;

	public String getCoursedate() {
		return coursedate;
	}

	public void setCoursedate(String coursedate) {
		this.coursedate = coursedate;
	}

	public String getCoachid() {
		return coachid;
	}

	public void setCoachid(String coachid) {
		this.coachid = coachid;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getSelectedstudentcount() {
		return selectedstudentcount;
	}

	public void setSelectedstudentcount(String selectedstudentcount) {
		this.selectedstudentcount = selectedstudentcount;
	}

	public String getCoursestudentcount() {
		return coursestudentcount;
	}

	public void setCoursestudentcount(String coursestudentcount) {
		this.coursestudentcount = coursestudentcount;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String[] getCourseuser() {
		return courseuser;
	}

	public void setCourseuser(String[] courseuser) {
		this.courseuser = courseuser;
	}

	public String[] getCoursereservation() {
		return coursereservation;
	}

	public void setCoursereservation(String[] coursereservation) {
		this.coursereservation = coursereservation;
	}

	public CourseTime getCoursetime() {
		return coursetime == null ? new CourseTime() : coursetime;
	}

	public void setCoursetime(CourseTime coursetime) {
		this.coursetime = coursetime;
	}

	public String getCoursebegintime() {
		return coursebegintime;
	}

	public void setCoursebegintime(String coursebegintime) {
		this.coursebegintime = coursebegintime;
	}

	public String getCoursendtime() {
		return coursendtime;
	}

	public void setCoursendtime(String coursendtime) {
		this.coursendtime = coursendtime;
	}

}
