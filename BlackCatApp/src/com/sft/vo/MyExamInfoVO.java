package com.sft.vo;

import cn.sft.sqlhelper.DBVO;

public class MyExamInfoVO extends DBVO {

	private String examinationstate;// 0 未申请 ， 1 申请中， 2 申请拒绝 ， 3 已安排
	private String applydate;
	private String applyenddate;
	private String examinationdate;
	private String examaddress;

	public String getExaminationstate() {
		return examinationstate;
	}

	public void setExaminationstate(String examinationstate) {
		this.examinationstate = examinationstate;
	}

	public String getApplydate() {
		return applydate;
	}

	public void setApplydate(String applydate) {
		this.applydate = applydate;
	}

	public String getApplyenddate() {
		return applyenddate;
	}

	public void setApplyenddate(String applyenddate) {
		this.applyenddate = applyenddate;
	}

	public String getExaminationdate() {
		return examinationdate;
	}

	public void setExaminationdate(String examinationdate) {
		this.examinationdate = examinationdate;
	}

	public String getExamaddress() {
		return examaddress;
	}

	public void setExamaddress(String examaddress) {
		this.examaddress = examaddress;
	}

}
