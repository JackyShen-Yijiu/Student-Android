package com.sft.vo.basestatevo;

import cn.sft.sqlhelper.DBVO;

public class ApplyInfoVO extends DBVO {

	private static final long serialVersionUID = 1L;
	private String[] handelmessage;
	private String handelstate;
	private String applytime;

	public String[] getHandelmessage() {
		return handelmessage;
	}

	public void setHandelmessage(String[] handelmessage) {
		this.handelmessage = handelmessage;
	}

	public String getHandelstate() {
		return handelstate;
	}

	public void setHandelstate(String handelstate) {
		this.handelstate = handelstate;
	}

	public String getApplytime() {
		return applytime;
	}

	public void setApplytime(String applytime) {
		this.applytime = applytime;
	}

}
