package com.sft.vo.uservo;

import cn.sft.sqlhelper.DBVO;

public class UserSettingVO extends DBVO {

	private static final long serialVersionUID = 1L;

	private String reservationreminder;
	private String newmessagereminder;
	private String classremind;

	public String getReservationreminder() {
		return reservationreminder;
	}

	public void setReservationreminder(String reservationreminder) {
		this.reservationreminder = reservationreminder;
	}

	public String getNewmessagereminder() {
		return newmessagereminder;
	}

	public void setNewmessagereminder(String newmessagereminder) {
		this.newmessagereminder = newmessagereminder;
	}

	public String getClassremind() {
		return classremind;
	}

	public void setClassremind(String classremind) {
		this.classremind = classremind;
	}

}
