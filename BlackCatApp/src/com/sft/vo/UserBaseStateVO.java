package com.sft.vo;

import cn.sft.sqlhelper.DBVO;

import com.sft.vo.basestatevo.ApplyInfoVO;

public class UserBaseStateVO extends DBVO {

	private static final long serialVersionUID = 1L;

	private String _id;

	private String applystate;

	private ApplyInfoVO applyinfo;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getApplystate() {
		return applystate;
	}

	public void setApplystate(String applystate) {
		this.applystate = applystate;
	}

	public ApplyInfoVO getApplyinfo() {
		return applyinfo;
	}

	public void setApplyinfo(ApplyInfoVO applyinfo) {
		this.applyinfo = applyinfo;
	}

}
