package com.sft.vo;

import cn.sft.sqlhelper.DBVO;

import com.sft.vo.commentvo.CommentUser;

public class ConsultationVO extends DBVO {

	private static final String ENROLL_USER_SELECTED = "1";
	private static final String APPOINTMENT_COACH = "2";

	private String replyuser;
	private String createtime;
	private String replytime;
	private String replycontent;
	private String _id;
	private String content;
	private CommentUser userid;

	public String getReplyuser() {
		return replyuser;
	}

	public void setReplyuser(String replyuser) {
		this.replyuser = replyuser;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getReplytime() {
		return replytime;
	}

	public void setReplytime(String replytime) {
		this.replytime = replytime;
	}

	public String getReplycontent() {
		return replycontent;
	}

	public void setReplycontent(String replycontent) {
		this.replycontent = replycontent;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public CommentUser getUserid() {
		return userid;
	}

	public void setUserid(CommentUser userid) {
		this.userid = userid;
	}

}
