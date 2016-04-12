package com.sft.vo;

public class ComplaintListVO {
	public String _id;
	public CoachVO coachid;
	public ComplaintVO complaint;
	public ComplaintHandInfoVO complainthandinfo;

	public String createtime;
	public int feedbacktype;// 1,教练 2，驾校
	public String feedbackmessage;
	public String becomplainedname;
	public String[] piclist;

	public String[] getPiclist() {
		return piclist;
	}

	public void setPiclist(String[] piclist) {
		this.piclist = piclist;
	}

}
