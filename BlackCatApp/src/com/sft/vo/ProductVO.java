package com.sft.vo;

import cn.sft.sqlhelper.DBVO;

public class ProductVO extends DBVO {

	private static final long serialVersionUID = 1L;

	private String productid;
	private String productname;
	private String productprice;
	private String productimg;
	private String productdesc;
	private String viewcount;
	private String buycount;
	private String detailsimg;

	public String getProductid() {
		return productid;
	}

	public void setProductid(String productid) {
		this.productid = productid;
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

	public String getBuycount() {
		return buycount;
	}

	public void setBuycount(String buycount) {
		this.buycount = buycount;
	}

	public String getDetailsimg() {
		return detailsimg;
	}

	public void setDetailsimg(String detailsimg) {
		this.detailsimg = detailsimg;
	}

}
