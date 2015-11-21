package com.sft.vo;

import java.util.List;

import cn.sft.sqlhelper.DBVO;

public class MyWalletVO extends DBVO {

	private static final long serialVersionUID = 1L;

	private String wallet;

	private List<IncomeVO> list;

	public String getWallet() {
		return wallet;
	}

	public void setWallet(String wallet) {
		this.wallet = wallet;
	}

	public List<IncomeVO> getList() {
		return list;
	}

	public void setList(List<IncomeVO> list) {
		this.list = list;
	}

}
