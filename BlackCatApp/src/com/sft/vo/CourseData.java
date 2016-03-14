package com.sft.vo;

import cn.sft.sqlhelper.DBVO;

import com.sft.vo.coursevo.CourseTime;

public class CourseData extends DBVO {

	private static final long serialVersionUID = 1L;

	private String __v;
	private String _id;// 课程id
	private String carmodelid; // 教练所教车型
	private String coachid;
	private String coachname;
	private String coursebegintime;// 课程开始时间
	private String coursedate;
	private String courseendtime;// 课程结束时间
	private String[] coursereservation;// 预约订单列表
	private int coursestudentcount;// 课程共有几个学生选择
	private CourseTime coursetime;
	private String[] courseuser;// 选择学生id列表
	private String createtime; // 创建时间
	private String driveschool;// 所在驾校 新
	private int selectedstudentcount; // 已经选择学生数量
	private int signinstudentcount;// 签到学生数量
	private String subjectid;// 教练科目
	private String platenumber; // 教练车牌号

	public String get__v() {
		return __v;
	}

	public void set__v(String __v) {
		this.__v = __v;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getCarmodelid() {
		return carmodelid;
	}

	public void setCarmodelid(String carmodelid) {
		this.carmodelid = carmodelid;
	}

	public String getCoachid() {
		return coachid;
	}

	public void setCoachid(String coachid) {
		this.coachid = coachid;
	}

	public String getCoachname() {
		return coachname;
	}

	public void setCoachname(String coachname) {
		this.coachname = coachname;
	}

	public String getCoursebegintime() {
		return coursebegintime;
	}

	public void setCoursebegintime(String coursebegintime) {
		this.coursebegintime = coursebegintime;
	}

	public String getCoursedate() {
		return coursedate;
	}

	public void setCoursedate(String coursedate) {
		this.coursedate = coursedate;
	}

	public String getCourseendtime() {
		return courseendtime;
	}

	public void setCourseendtime(String courseendtime) {
		this.courseendtime = courseendtime;
	}

	public String[] getCoursereservation() {
		return coursereservation;
	}

	public void setCoursereservation(String[] coursereservation) {
		this.coursereservation = coursereservation;
	}

	public int getCoursestudentcount() {
		return coursestudentcount;
	}

	public void setCoursestudentcount(int coursestudentcount) {
		this.coursestudentcount = coursestudentcount;
	}

	public CourseTime getCoursetime() {
		return coursetime;
	}

	public void setCoursetime(CourseTime coursetime) {
		this.coursetime = coursetime;
	}

	public String[] getCourseuser() {
		return courseuser;
	}

	public void setCourseuser(String[] courseuser) {
		this.courseuser = courseuser;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getDriveschool() {
		return driveschool;
	}

	public void setDriveschool(String driveschool) {
		this.driveschool = driveschool;
	}

	public int getSelectedstudentcount() {
		return selectedstudentcount;
	}

	public void setSelectedstudentcount(int selectedstudentcount) {
		this.selectedstudentcount = selectedstudentcount;
	}

	public int getSigninstudentcount() {
		return signinstudentcount;
	}

	public void setSigninstudentcount(int signinstudentcount) {
		this.signinstudentcount = signinstudentcount;
	}

	public String getSubjectid() {
		return subjectid;
	}

	public void setSubjectid(String subjectid) {
		this.subjectid = subjectid;
	}

	public String getPlatenumber() {
		return platenumber;
	}

	public void setPlatenumber(String platenumber) {
		this.platenumber = platenumber;
	}

}
