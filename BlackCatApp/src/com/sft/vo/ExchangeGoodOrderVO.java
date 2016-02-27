package com.sft.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 兑换商品订单
 * @author sun  2016-2-26 下午4:08:38
 *
 */
public class ExchangeGoodOrderVO implements Serializable{

	public List<ExchangeOrderItemVO> ordrelist = new ArrayList<ExchangeOrderItemVO>();
	
	public UserVO userdata;
	
}
