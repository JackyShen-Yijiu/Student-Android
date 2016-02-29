package com.sft.vo;

import cn.sft.sqlhelper.DBVO;

public class ProductVO extends DBVO {

	private static final long serialVersionUID = 1L;

	private String productid;
	private String productname;
	private String productprice;
	private String productimg;
	private String productdesc;
	private String detailurl;
	private String viewcount;
	private int buycount;
	private String detailsimg;
	private String address;
	private String cityname;
	private String county;
	private int distinct;
	private String is_scanconsumption;
	private String merchantid;
	private int productcount;
	private String enddate;

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public String getProductid() {
		return productid;
	}

	public void setProductid(String productid) {
		this.productid = productid;
	}

	public String getDetailurl() {
		return detailurl;
	}

	public void setDetailurl(String detailurl) {
		this.detailurl = detailurl;
	}

	public String getProductname() {
		return productname;
	}

	public void setProductname(String productname) {
		this.productname = productname;
	}

	public String getProductprice() {
		return productprice;
	}

	public void setProductprice(String productprice) {
		this.productprice = productprice;
	}

	public String getProductimg() {
		return productimg;
	}

	public void setProductimg(String productimg) {
		this.productimg = productimg;
	}

	public String getProductdesc() {
		return productdesc;
	}

	public void setProductdesc(String productdesc) {
		this.productdesc = productdesc;
	}

	public String getViewcount() {
		return viewcount;
	}

	public void setViewcount(String viewcount) {
		this.viewcount = viewcount;
	}

	public int getBuycount() {
		return buycount;
	}

	public void setBuycount(int buycount) {
		this.buycount = buycount;
	}

	public String getDetailsimg() {
		return detailsimg;
	}

	public void setDetailsimg(String detailsimg) {
		this.detailsimg = detailsimg;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCityname() {
		return cityname;
	}

	public void setCityname(String cityname) {
		this.cityname = cityname;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getDistinct() {

		return (distinct / 1000) + "km";
	}

	public void setDistinct(int distinct) {
		this.distinct = distinct;
	}

	public String getIs_scanconsumption() {
		return is_scanconsumption;
	}

	public void setIs_scanconsumption(String is_scanconsumption) {
		this.is_scanconsumption = is_scanconsumption;
	}

	public String getMerchantid() {
		return merchantid;
	}

	public void setMerchantid(String merchantid) {
		this.merchantid = merchantid;
	}

	public int getProductcount() {
		return productcount;
	}

	public void setProductcount(int productcount) {
		this.productcount = productcount;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
