package com.sft.vo;

import cn.sft.sqlhelper.DBVO;
/**
 * 章节
 * @author pengdonghua
 *
 */
public class ExerciseChapterVO extends DBVO{

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
	private int id;
	private int mid;
	private String title;
	private int fid;
	private int kemu;
	
}
