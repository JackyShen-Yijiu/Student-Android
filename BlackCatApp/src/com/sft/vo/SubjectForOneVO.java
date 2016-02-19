package com.sft.vo;

import cn.sft.sqlhelper.DBVO;

public class SubjectForOneVO extends DBVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int finishcourse;
	private int missingcourse;
	private int officialhours;
	private int reservation;
	private int totalcourse;
	private String progress;

	public int getFinishcourse() {
		return finishcourse;
	}

	public void setFinishcourse(int finishcourse) {
		this.finishcourse = finishcourse;
	}

	public int getMissingcourse() {
		return missingcourse;
	}

	public void setMissingcourse(int missingcourse) {
		this.missingcourse = missingcourse;
	}

	public int getOfficialhours() {
		return officialhours;
	}

	public void setOfficialhours(int officialhours) {
		this.officialhours = officialhours;
	}

	public int getReservation() {
		return reservation;
	}

	public void setReservation(int reservation) {
		this.reservation = reservation;
	}

	public int getTotalcourse() {
		return totalcourse;
	}

	public void setTotalcourse(int totalcourse) {
		this.totalcourse = totalcourse;
	}

	public String getProgress() {
		return progress;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}

}
