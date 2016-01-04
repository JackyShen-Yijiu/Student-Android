package com.sft.vo;

import java.util.List;

import cn.sft.sqlhelper.DBVO;

public class AmountInCashVO extends DBVO {

	private static final long serialVersionUID = 1L;
	private int money;
	private String userid;
	private String fcode;
	private List<MoneyListVO> moneylist;

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getFcode() {
		return fcode;
	}

	public void setFcode(String fcode) {
		this.fcode = fcode;
	}

	public List<MoneyListVO> getMoneylist() {
		return moneylist;
	}

	public void setMoneylist(List<MoneyListVO> moneylist) {
		this.moneylist = moneylist;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
