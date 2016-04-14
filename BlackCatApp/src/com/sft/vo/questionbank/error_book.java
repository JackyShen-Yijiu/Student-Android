package com.sft.vo.questionbank;

import cn.sft.sqlhelper.DBVO;

public class error_book extends DBVO {

	private int id;
	private int chapterid;
	private int webnoteid;
	private int kemu;
	private String userid;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getChapterid() {
		return chapterid;
	}

	public void setChapterid(int chapterid) {
		this.chapterid = chapterid;
	}

	public int getWebnoteid() {
		return webnoteid;
	}

	public void setWebnoteid(int webnoteid) {
		this.webnoteid = webnoteid;
	}

	public int getKemu() {
		return kemu;
	}

	public void setKemu(int kemu) {
		this.kemu = kemu;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

}
