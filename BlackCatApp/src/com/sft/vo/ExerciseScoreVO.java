package com.sft.vo;

import cn.sft.sqlhelper.DBVO;
/**
 * 成绩
 * @author pengdonghua
 *
 */
public class ExerciseScoreVO extends DBVO{

	private int id;
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
	public String getUsertime() {
		return usertime;
	}
	public void setUsertime(String usertime) {
		this.usertime = usertime;
	}
	public String getRiqi() {
		return riqi;
	}
	public void setRiqi(String riqi) {
		this.riqi = riqi;
	}
	private int kemu;
	private String chenji;
	private int uid;
	private String usertime;
	private String riqi;
	
}
