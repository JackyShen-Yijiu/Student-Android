package com.sft.vo;

import cn.sft.sqlhelper.DBVO;

public class IncomeVO extends DBVO {

	private static final long serialVersionUID = 1L;

	// 日期
	private String createtime;
	private String amount;
	private String type;
	private String seqindex;

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSeqindex() {
		return seqindex;
	}

	public void setSeqindex(String seqindex) {
		this.seqindex = seqindex;
	}

}
