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

	public Carmodel carmodel;

	public class Carmodel {

		public String code;
		public String name;
		public int modelsid;
	}

	public class applyclasstypeinfo {
		public String id;
		public String name;
		public String price;
	}

	public class Applycoachinfo {

		public String name;
		public String id;
	}

	public class Applyschoolinfo {

		public String name;
		public String id;
	}

}
