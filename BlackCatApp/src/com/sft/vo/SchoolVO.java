package com.sft.vo;

import android.text.TextUtils;
import cn.sft.sqlhelper.DBVO;

import com.sft.vo.commonvo.HeadPortrait;

public class SchoolVO extends DBVO {

	public static final String ENROLL_USER_SELECTED = "1";
	private static final long serialVersionUID = 1L;

	private String db_userid;
	private String db_schoolStyle;
	private String schoolid;
	private String name;
	private String latitude;
	private String longitude;
	private String hours;
	private String introduction;
	private String registertime;
	private String address;
	private String responsible;
	private String phone;
	private String websit;
	private HeadPortrait logoimg;
	private String[] pictures;
	private String passingrate;
	private String distance;
	private String maxprice;
	private String minprice;
	private int coachcount;
	private int commentcount;
	private String id;
	private String schoollevel;

	public int getCoachcount() {
		return coachcount;
	}

	public void setCoachcount(int coachcount) {
		this.coachcount = coachcount;
	}

	public int getCommentcount() {
		return commentcount;
	}

	public void setCommentcount(int commentcount) {
		this.commentcount = commentcount;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSchoollevel() {
		return schoollevel;
	}

	public void setSchoollevel(String schoollevel) {
		this.schoollevel = schoollevel;
	}

	public void setPictures(String[] pictures) {
		this.pictures = pictures;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof SchoolVO) {
			return ((SchoolVO) o).getSchoolid().equals(schoolid);
		}
		return false;
	}

	public String getDb_userid() {
		return db_userid;
	}

	public void setDb_userid(String db_userid) {
		this.db_userid = db_userid;
	}

	public String getDb_schoolStyle() {
		return db_schoolStyle;
	}

	public void setDb_schoolStyle(String db_schoolStyle) {
		this.db_schoolStyle = db_schoolStyle;
	}

	public String getSchoolid() {
		return TextUtils.isEmpty(schoolid) ? "null" : schoolid;
	}

	public void setSchoolid(String schoolid) {
		this.schoolid = schoolid;
	}

	public String getName() {
		return TextUtils.isEmpty(name) ? "null" : name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getHours() {
		return hours;
	}

	public void setHours(String hours) {
		this.hours = hours;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getRegistertime() {
		return registertime;
	}

	public void setRegistertime(String registertime) {
		this.registertime = registertime;
	}

	public String getAddress() {
		return TextUtils.isEmpty(address) ? "null" : address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getResponsible() {
		return responsible;
	}

	public void setResponsible(String responsible) {
		this.responsible = responsible;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getWebsit() {
		return websit;
	}

	public void setWebsit(String websit) {
		this.websit = websit;
	}

	public HeadPortrait getLogoimg() {
		return logoimg == null ? new HeadPortrait() : logoimg;
	}

	public void setLogoimg(HeadPortrait logoimg) {
		this.logoimg = logoimg;
	}

	public String[] getPictures() {
		return pictures;
	}

	public void setPictures(String pictures) {
		if (pictures.contains("[")) {
			pictures.replace("[", "");
		}
		if (pictures.contains("]")) {
			pictures.replace("]", "");
		}
		this.pictures = pictures.split(",");
	}

	public String getPassingrate() {
		return TextUtils.isEmpty(passingrate) ? "null" : passingrate;
	}

	public void setPassingrate(String passingrate) {
		this.passingrate = passingrate;
	}

	public String getPrice() {
		return "¥" + minprice + "-¥" + maxprice;
	}

	public String getMaxprice() {
		return TextUtils.isEmpty(maxprice) ? "null" : maxprice;
	}

	public void setMaxprice(String maxprice) {
		this.maxprice = maxprice;
	}

	public String getMinprice() {
		return TextUtils.isEmpty(minprice) ? "null" : minprice;
	}

	public void setMinprice(String minprice) {
		this.minprice = minprice;
	}

	public String getDistance() {
		return TextUtils.isEmpty(distance) ? "null" : distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

}
