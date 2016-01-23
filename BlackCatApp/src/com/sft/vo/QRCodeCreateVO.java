package com.sft.vo;

import cn.sft.sqlhelper.DBVO;

public class QRCodeCreateVO extends DBVO {

	private static final long serialVersionUID = 1L;

	private String studentId;
	private String studentName;
	private String reservationId;
	private String createTime;
	private String locationAddress;
	private String latitude;
	private String longitude;
	private String coachName;
	private String courseProcessDesc;

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getReservationId() {
		return reservationId;
	}

	public void setReservationId(String reservationId) {
		this.reservationId = reservationId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getLocationAddress() {
		return locationAddress;
	}

	public void setLocationAddress(String locationAddress) {
		this.locationAddress = locationAddress;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getCoachName() {
		return coachName;
	}

	public void setCoachName(String coachName) {
		this.coachName = coachName;
	}

	public String getCourseProcessDesc() {
		return courseProcessDesc;
	}

	public void setCourseProcessDesc(String courseProcessDesc) {
		this.courseProcessDesc = courseProcessDesc;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
