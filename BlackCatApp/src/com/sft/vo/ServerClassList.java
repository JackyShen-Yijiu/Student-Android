package com.sft.vo;

import cn.sft.sqlhelper.DBVO;

public class ServerClassList extends DBVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String _id;
	private CarModelVO carmodel;
	private String cartype;
	private String classdesc;
	private String classname;
	private String onsaleprice;// 优惠后的价格
	private String price; // 原价

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public CarModelVO getCarmodel() {
		return carmodel;
	}

	public void setCarmodel(CarModelVO carmodel) {
		this.carmodel = carmodel;
	}

	public String getCartype() {
		return cartype;
	}

	public void setCartype(String cartype) {
		this.cartype = cartype;
	}

	public String getClassdesc() {
		return classdesc;
	}

	public void setClassdesc(String classdesc) {
		this.classdesc = classdesc;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String getOnsaleprice() {
		return onsaleprice;
	}

	public void setOnsaleprice(String onsaleprice) {
		this.onsaleprice = onsaleprice;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

}
