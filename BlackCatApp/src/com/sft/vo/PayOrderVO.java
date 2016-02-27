package com.sft.vo;

import java.io.Serializable;

import cn.sft.sqlhelper.DBVO;

/**
 * 我的 支付订单
 * 
 * @author sun 2016-2-1 下午2:22:00
 * 
 */
public class PayOrderVO extends DBVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String _id;
	public String paymoney;// 最终支付价格，
	public String payendtime;
	public String creattime;
	public String userid;
	public String __v;
	public String discountmoney;// 折扣减免xx(元)
	/** 支付渠道 0 暂未选择 1 支付宝 2 微信 */
	public String paychannel;
	/** 0 订单生成 2 支付成功 3 支付失败 4 订单取消 */
	public String userpaystate;

	public SchoolVO applyschoolinfo;
	public OrderExchangeVO applyclasstypeinfo;// name id 错误

	public String activitycoupon;

	public String couponcode;

	// {"type":1,"msg":"","data":[{"_id":"56af11ce9ba0d4530524b6cb","userpaystate":0,"creattime":
	// "2016-02-01T08:05:34.823Z","payendtime":"2016-02-04T08:05:34.823Z","paychannel":0,
	// "applyschoolinfo":{"id":"562dcc3ccb90f25c3bde40da","name":"一步互联网驾校"},
	// "applyclasstypeinfo":{"id":"56a9ba41fe60f807363001c9","name":"新春特惠班","price":
	// 4980,"onsaleprice":4680},"discountmoney":0,"paymoney":4680,"activitycoupon":"","couponcode":""}]}

}
