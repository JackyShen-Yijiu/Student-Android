package com.sft.vo;

import cn.sft.sqlhelper.DBVO;

import com.sft.vo.commonvo.Subject;

public class VideoVO extends DBVO {

	private static final long serialVersionUID = 1L;

	private String _id;
	private String seqindex;
	private String videourl;
	private String name;
	private String pictures;
	private Subject subject;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getSeqindex() {
		return seqindex;
	}

	public void setSeqindex(String seqindex) {
		this.seqindex = seqindex;
	}

	public String getVideourl() {
		return videourl;
	}

	public void setVideourl(String videourl) {
		this.videourl = videourl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPictures() {
		return pictures;
	}

	public void setPictures(String pictures) {
		this.pictures = pictures;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

}
