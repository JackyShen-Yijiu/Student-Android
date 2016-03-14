package com.sft.vo.commentvo;

import cn.sft.sqlhelper.DBVO;

public class CommentContent extends DBVO {

	private static final long serialVersionUID = 1L;

	private String commentcontent;
	private String starlevel;
	private String commenttime;

	public String getCommentcontent() {
		return commentcontent;
	}

	public void setCommentcontent(String commentcontent) {
		this.commentcontent = commentcontent;
	}

	public String getStarlevel() {
		return starlevel;
	}

	public void setStarlevel(String starlevel) {
		this.starlevel = starlevel;
	}

	public String getCommenttime() {
		return commenttime;
	}

	public void setCommenttime(String commenttime) {
		this.commenttime = commenttime;
	}

}
