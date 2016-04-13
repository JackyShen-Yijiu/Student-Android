package com.sft.vo;

import java.util.List;

import cn.sft.sqlhelper.DBVO;

public class MyCuponVO extends DBVO {

	private static final long serialVersionUID = 1L;

	private String _id;
	private String userid;
	// 优惠卷状态// 0未领取 1领取 2过期 3作废 4 已消费
	private String state;
	// 是否可以兑换现金
	private boolean is_forcash;
	// 优惠券来源 1 报名奖励 2 活动奖励
	private String couponcomefrom;
	private String createtime;
	// 二维码
	private String orderscanaduiturl;
	// 活动列表
	private List<ActSelecttVO> useproductidlist;

	public String getOrderscanaduiturl() {
		return orderscanaduiturl;
	}

	public void setOrderscanaduiturl(String orderscanaduiturl) {
		this.orderscanaduiturl = orderscanaduiturl;
	}

	public List<ActSelecttVO> getUseproductidlist() {
		return useproductidlist;
	}

	public void setUseproductidlist(List<ActSelecttVO> useproductidlist) {
		this.useproductidlist = useproductidlist;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getState() {
		// 0未领取 1领取 2过期 3作废 4 已消费
		if ("0".equals(state)) {
			return "未领取兑换券1张";
		} else if ("1".equals(state)) {
			return "已领取兑换券1张";
		} else if ("2".equals(state)) {
			return "已过期兑换券1张";
		} else if ("3".equals(state)) {
			return "已作废兑换券1张";
		} else if ("4".equals(state)) {
			return "已消费兑换券1张";
		}
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public boolean isIs_forcash() {
		return is_forcash;
	}

	public void setIs_forcash(boolean is_forcash) {
		this.is_forcash = is_forcash;
	}

	public String getCouponcomefrom() {
		return couponcomefrom;
	}

	public void setCouponcomefrom(String couponcomefrom) {
		this.couponcomefrom = couponcomefrom;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
