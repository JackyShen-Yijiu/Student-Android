package com.sft.vo;

import cn.sft.sqlhelper.DBVO;

import com.sft.vo.commentvo.CommentContent;
import com.sft.vo.commentvo.CommentUser;

public class CoachCommentVO extends DBVO {

	private static final long serialVersionUID = 1L;
	private String _id;
	private String finishtime;
	private CommentUser userid;
	private CommentContent comment;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getFinishtime() {
		return finishtime;
	}

	public void setFinishtime(String finishtime) {
		this.finishtime = finishtime;
	}

	public CommentUser getUserid() {
		return userid == null ? new CommentUser() : userid;
	}

	public void setUserid(CommentUser userid) {
		this.userid = userid;
	}

	public CommentContent getComment() {
		return comment == null ? new CommentContent() : comment;
	}

	public void setComment(CommentContent comment) {
		this.comment = comment;
	}

}
