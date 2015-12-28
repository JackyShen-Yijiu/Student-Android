package com.sft.vo;

public class MyMoneyVO {

	// "userid": "56766efb98945d9922d63b3a",
	// "wallet": 150,
	// "fcode": "",
	// "money": 0,
	// "couponcount":
	private String userid;
	private String wallet;
	private String fcode;
	private String money;
	private String couponcount;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getWallet() {
		return wallet == null ? "" : wallet;
	}

	public void setWallet(String wallet) {
		this.wallet = wallet;
	}

	public String getFcode() {
		return fcode == null ? "" : fcode;
	}

	public void setFcode(String fcode) {
		this.fcode = fcode;
	}

	public String getMoney() {
		return money == null ? "" : money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getCouponcount() {
		return couponcount == null ? "" : couponcount;
	}

	public void setCouponcount(String couponcount) {
		this.couponcount = couponcount;
	}

}
