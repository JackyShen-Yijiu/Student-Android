package com.sft.vo.commentvo;

import cn.sft.sqlhelper.DBVO;

import com.sft.vo.commonvo.CarModel;
import com.sft.vo.commonvo.HeadPortrait;
import com.sft.vo.uservo.ApplyClassTypeInfo;

public class CommentUser extends DBVO {

	private static final long serialVersionUID = 1L;

	private String _id;
	private HeadPortrait headportrait;
	private String name;
	private ApplyClassTypeInfo applyclasstypeinfo;
	private CarModel carmodel;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public HeadPortrait getHeadportrait() {
		return headportrait == null ? new HeadPortrait() : headportrait;
	}

	public void setHeadportrait(HeadPortrait headportrait) {
		this.headportrait = headportrait;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ApplyClassTypeInfo getApplyclasstypeinfo() {
		return applyclasstypeinfo;
	}

	public void setApplyclasstypeinfo(ApplyClassTypeInfo applyclasstypeinfo) {
		this.applyclasstypeinfo = applyclasstypeinfo;
	}

	public CarModel getCarmodel() {
		return carmodel;
	}

	public void setCarmodel(CarModel carmodel) {
		this.carmodel = carmodel;
	}

	@Override
	public boolean equals(Object obj) {
		return this._id.equals(((CommentUser) obj)._id);
	}
}
