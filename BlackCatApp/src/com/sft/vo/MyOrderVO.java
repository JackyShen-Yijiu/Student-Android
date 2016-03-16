package com.sft.vo;

import cn.sft.sqlhelper.DBVO;

/**
 * getmyorder
 * @author pengdonghua
 *
 */
public class MyOrderVO extends DBVO{
	
	public int applystate;
	public String schoollogoimg;
	public String schooladdress;
	public CoachVO applycoachinfo;
	public ClassVO applyclasstypeinfo;
	public SchoolVO applyschoolinfo;
	
	public String applytime;
	public String endapplytime;
	public String scanauditurl;
	public String orderid;
	public String name;
	public String mobile;
	public int paytype;
	public int paytypestatus;
	
	
}
