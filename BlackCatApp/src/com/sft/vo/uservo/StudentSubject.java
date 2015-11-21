package com.sft.vo.uservo;

import cn.sft.sqlhelper.DBVO;

public class StudentSubject extends DBVO {
	private static final long serialVersionUID = 1L;
	private int totalcourse;
	private int reservation;
	private int finishcourse;
	private String progress;
	private String reservationid;

	public int getTotalcourse() {
		return totalcourse;
	}

	public void setTotalcourse(int totalcourse) {
		this.totalcourse = totalcourse;
	}

	public int getReservation() {
		return reservation;
	}

	public void setReservation(int reservation) {
		this.reservation = reservation;
	}

	public int getFinishcourse() {
		return finishcourse;
	}

	public void setFinishcourse(int finishcourse) {
		this.finishcourse = finishcourse;
	}

	public String getProgress() {
		return progress;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}

	public String getReservationid() {
		return reservationid;
	}

	public void setReservationid(String reservationid) {
		this.reservationid = reservationid;
	}

}
