package com.sft.vo;

public class MoneyListVO {

	private int income;
	private String createtime;
	// 0 支出 1 报名奖励 2 邀请奖励 3 下线分红
	private int type;

	public String getIncome() {
		return "+" + income + "元";
	}

	public void setIncome(int income) {
		this.income = income;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getType() {
		String typeString = "";
		switch (type) {
		case 0:
			typeString = "支出 ";
			break;
		case 1:
			typeString = "报名奖励  ";
			break;
		case 2:
			typeString = " 邀请奖励 ";
			break;
		case 3:
			typeString = " 下线分红 ";
			break;

		}

		return typeString;
	}

	public void setType(int type) {
		this.type = type;
	}

}
