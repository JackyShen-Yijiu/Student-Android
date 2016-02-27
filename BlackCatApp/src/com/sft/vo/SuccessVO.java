package com.sft.vo;

import java.io.Serializable;

public class SuccessVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String scanauditurl;
	public String applynotes;
	public String name;
	public String mobile;
	public String endtime;
	public String userid;
	public int applystate;
	public String applytime;
	public applyclasstypeinfo applyclasstypeinfo;
	public Applyschoolinfo applyschoolinfo;
	public Applycoachinfo applycoachinfo;
	public String paytype;
	public int paytypestatus;//0 未支付  20 支付成功 30 支付失败
	public String schoollogoimg;

	public Carmodel carmodel;

	public class Carmodel  implements Serializable{

		public String code;
		public String name;
		public int modelsid;
	}

	public class applyclasstypeinfo  implements Serializable{
		public String id;
		public String name;
		public String price;
		public String onsaleprice;
		
	}

	public class Applycoachinfo  implements Serializable{

		public String name;
		public String id;
	}

	public class Applyschoolinfo  implements Serializable{

		public String name;
		public String id;
	}

}
