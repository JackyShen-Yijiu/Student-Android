package com.sft.vo.questionbank;

import cn.sft.sqlhelper.DBVO;

public class score extends DBVO {

	private int id;
	private int kemu;
	private String chenji;
	private int uid;
	private String usetime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getKemu() {
		return kemu;
	}

	public void setKemu(int kemu) {
		this.kemu = kemu;
	}

	public String getChenji() {
		return chenji;
	}

	public void setChenji(String chenji) {
		this.chenji = chenji;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getUsetime() {
		return usetime;
	}

	public void setUsetime(String usetime) {
		this.usetime = usetime;
	}

	public String getRiqi() {
		return riqi;
	}

	public void setRiqi(String riqi) {
		this.riqi = riqi;
	}

	private String riqi;

}
