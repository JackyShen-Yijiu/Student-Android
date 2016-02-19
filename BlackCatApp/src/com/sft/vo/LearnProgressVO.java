package com.sft.vo;

import cn.sft.sqlhelper.DBVO;

public class LearnProgressVO extends DBVO {

	private static final long serialVersionUID = 1L;

	private String _id;
	private SubjectVO subject;
	private SubjectForOneVO subjectone;
	private SubjectForOneVO subjectthree;
	private SubjectForOneVO subjecttwo;
	private SubjectForOneVO subjectfour;
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public SubjectVO getSubject() {
		return subject;
	}
	public void setSubject(SubjectVO subject) {
		this.subject = subject;
	}
	public SubjectForOneVO getSubjectone() {
		return subjectone;
	}
	public void setSubjectone(SubjectForOneVO subjectone) {
		this.subjectone = subjectone;
	}
	public SubjectForOneVO getSubjectthree() {
		return subjectthree;
	}
	public void setSubjectthree(SubjectForOneVO subjectthree) {
		this.subjectthree = subjectthree;
	}
	public SubjectForOneVO getSubjecttwo() {
		return subjecttwo;
	}
	public void setSubjecttwo(SubjectForOneVO subjecttwo) {
	}
	public SubjectForOneVO getSubjectfour() {
		return subjectfour;
	}
	public void setSubjectfour(SubjectForOneVO subjectfour) {
		this.subjectfour = subjectfour;
	}
	
	

}
