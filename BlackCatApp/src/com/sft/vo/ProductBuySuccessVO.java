package com.sft.vo;

import cn.sft.sqlhelper.DBVO;

public class ProductBuySuccessVO extends DBVO {

	private static final long serialVersionUID = 1L;

	private String orderid;
	private String orderscanaduiturl;
	private String finishorderurl;

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getOrderscanaduiturl() {
		return orderscanaduiturl;
	}

	public void setOrderscanaduiturl(String orderscanaduiturl) {
		this.orderscanaduiturl = orderscanaduiturl;
	}

	public String getFinishorderurl() {
		return finishorderurl;
	}

	public void setFinishorderurl(String finishorderurl) {
		this.finishorderurl = finishorderurl;
	}

}
