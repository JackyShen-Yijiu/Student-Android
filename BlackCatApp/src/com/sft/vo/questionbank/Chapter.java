package com.sft.vo.questionbank;

import cn.sft.sqlhelper.DBVO;

public class Chapter extends DBVO {
	private int id;
	private int mid;
	private String title;
	private int fid;
	private int kemu;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMid() {
		return mid;
	}

	public void setMid(int mid) {
		this.mid = mid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getFid() {
		return fid;
	}

	public void setFid(int fid) {
		this.fid = fid;
	}

	public int getKemu() {
		return kemu;
	}

	public void setKemu(int kemu) {
		this.kemu = kemu;
	}

}
