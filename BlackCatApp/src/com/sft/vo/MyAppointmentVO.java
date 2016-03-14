package com.sft.vo;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.os.ParcelableCompat;
import cn.sft.sqlhelper.DBVO;

import com.sft.vo.coachvo.TrainFieldlInfo;
import com.sft.vo.commonvo.Subject;

public class MyAppointmentVO extends DBVO  {

	private static final long serialVersionUID = 1L;

	private String coursehour;
	private String endtime;
	private String begintime;
	private String shuttleaddress;
	private CoachVO coachid;
	private String userid;
	private String _id;
	private String isshuttle;
	private String iscomplaint;
	private String iscoachcomment;
	private String iscomment;
	private String reservationcreatetime;
	private String reservationstate;
	private Subject subject;
	private String courseprocessdesc;
	private String classdatetimedesc;
	private String learningcontent;
	private TrainFieldlInfo trainfieldlinfo;

	public String getCoursehour() {
		return coursehour;
	}

	public void setCoursehour(String coursehour) {
		this.coursehour = coursehour;
	}

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

	public String getShuttleaddress() {
		return shuttleaddress;
	}

	public void setShuttleaddress(String shuttleaddress) {
		this.shuttleaddress = shuttleaddress;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getIsshuttle() {
		return isshuttle;
	}

	public void setIsshuttle(String isshuttle) {
		this.isshuttle = isshuttle;
	}

	public String getIscomplaint() {
		return iscomplaint;
	}

	public void setIscomplaint(String iscomplaint) {
		this.iscomplaint = iscomplaint;
	}

	public String getIscoachcomment() {
		return iscoachcomment;
	}

	public void setIscoachcomment(String iscoachcomment) {
		this.iscoachcomment = iscoachcomment;
	}

	public String getIscomment() {
		return iscomment;
	}

	public void setIscomment(String iscomment) {
		this.iscomment = iscomment;
	}

	public String getReservationcreatetime() {
		return reservationcreatetime;
	}

	public void setReservationcreatetime(String reservationcreatetime) {
		this.reservationcreatetime = reservationcreatetime;
	}

	public String getReservationstate() {
		return reservationstate;
	}

	public void setReservationstate(String reservationstate) {
		this.reservationstate = reservationstate;
	}

	public Subject getSubject() {
		return subject == null ? new Subject() : subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public CoachVO getCoachid() {
		return coachid == null ? new CoachVO() : coachid;
	}

	public void setCoachid(CoachVO coachid) {
		this.coachid = coachid;
	}

	public String getCourseprocessdesc() {
		return courseprocessdesc;
	}

	public void setCourseprocessdesc(String courseprocessdesc) {
		this.courseprocessdesc = courseprocessdesc;
	}

	public String getClassdatetimedesc() {
		return classdatetimedesc;
	}

	public void setClassdatetimedesc(String classdatetimedesc) {
		this.classdatetimedesc = classdatetimedesc;
	}

	public TrainFieldlInfo getTrainfieldlinfo() {
		return trainfieldlinfo == null ? new TrainFieldlInfo()
				: trainfieldlinfo;
	}

	public void setTrainfieldlinfo(TrainFieldlInfo trainfieldlinfo) {
		this.trainfieldlinfo = trainfieldlinfo;
	}

	public String getLearningcontent() {
		return learningcontent;
	}

	public void setLearningcontent(String learningcontent) {
		this.learningcontent = learningcontent;
	}


}
