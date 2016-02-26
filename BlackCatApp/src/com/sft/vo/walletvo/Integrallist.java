package com.sft.vo.walletvo;

import cn.sft.sqlhelper.DBVO;

public class Integrallist extends DBVO {
	private static final long serialVersionUID = 1L;
	private String amount;
	private String createtime;
	private String seqindex;
	private String type;

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getSeqindex() {
		return seqindex;
	}

	public void setSeqindex(String seqindex) {
		this.seqindex = seqindex;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
