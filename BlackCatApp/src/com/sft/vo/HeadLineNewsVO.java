package com.sft.vo;

import cn.sft.sqlhelper.DBVO;

import com.sft.vo.commonvo.HeadPortrait;

public class HeadLineNewsVO extends DBVO {

	private static final long serialVersionUID = 1L;

	private String newtype;
	private String _id;
	private String is_using;
	private String linkurl;
	private String createtime;
	private String newsname;
	private HeadPortrait headportrait;

	public String getNewtype() {
		return newtype;
	}

	public void setNewtype(String newtype) {
		this.newtype = newtype;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getIs_using() {
		return is_using;
	}

	public void setIs_using(String is_using) {
		this.is_using = is_using;
	}

	public String getLinkurl() {
		return linkurl;
	}

	public void setLinkurl(String linkurl) {
		this.linkurl = linkurl;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getNewsname() {
		return newsname;
	}

	public void setNewsname(String newsname) {
		this.newsname = newsname;
	}

	public HeadPortrait getHeadportrait() {
		return headportrait == null ? new HeadPortrait() : headportrait;
	}

	public void setHeadportrait(HeadPortrait headportrait) {
		this.headportrait = headportrait;
	}

}
