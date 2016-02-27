package com.sft.vo.walletvo;

import java.util.List;

import cn.sft.sqlhelper.DBVO;

public class IntegralVO extends DBVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String wallet;
	private List<Integrallist> list;

	public String getWallet() {
		return wallet;
	}

	public void setWallet(String wallet) {
		this.wallet = wallet;
	}

	public List<Integrallist> getList() {
		return list;
	}

	public void setList(List<Integrallist> list) {
		this.list = list;
	}

}
