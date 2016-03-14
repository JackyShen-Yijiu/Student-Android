package com.sft.common;

import java.io.File;

import android.os.Environment;

public class Config {

	/**
	 * 微信appId
	 */
	public static final String APP_ID_WEIXIN = "wxb815a53dcb2faf06";

	// 友盟appkey
	public static final String UMENG_APPKEY = "56243b3d67e58eb1ae00419b";
	// 友盟appkey
	public static final String UMENG_CHANNELID = "ceshi";
	//
	// public static final String IP = "http://101.200.204.240:8181/";
	public static final String IP = "http://jzapi.yibuxueche.com/";

	/** 学员须知 */
	public static final String STUDENT_KNOW = IP + "xueyuanxuzhi.html";

	/** 支付宝回掉接口 */
	public static final String RECALL_URL = IP + "paynotice/alipay";
	

	/** 当前版本号 */
	// public static final String VERSION =
	// "http://yibuxueche.com/about.html?ver=";
	public static final String VERSION = "http://www.yibuxueche.com/jzjfabout.html?ver=";

	// 默认头像下载地址
	public static final String HEAD_URL = "http://7xnjg0.com1.z0.glb.clouddn.com/";
	// 上次登录用户的手机号
	public static final String LAST_CHANGE_PASSWORD_PHONE = "lastchangepasswordphone";
	// 上次登录用户的手机号
	public static final String LAST_LOGIN_PHONE = "lastloginphone";
	// 用户上次登录的(用户名)
	public static final String LAST_LOGIN_ACCOUNT = "lastloginaccount";
	// 用户上次登录的密码
	public static final String LAST_LOGIN_PASSWORD = "lastloginpassword";
	// 用户上次登陆的信息
	public static final String LAST_LOGIN_MESSAGE = "lastloginmessage";
	// 用户自动登录
	public static final String USER_AUTO_LOGIN = "userautologin";
	// 用户当前所在城市
	public static final String USER_CITY = "usercity";
	// 用户报名信息
	public static final String USER_ENROLL_INFO = "userenrollinfo";

	// 聊天对方的头像url字段
	public static final String CHAT_HEAD_RUL = "headUrl";
	// 聊天对方的昵称
	public static final String CHAT_NICK_NAME = "nickName";
	// 聊天对方的userid
	public static final String CHAT_USERID = "userId";
	// 聊天对方的类型
	public static final String CHAT_USERTYPE = "userType";
	// 聊天对方的名称（对方没有回复用）
	public static final String CHAT_NICK_NAME_NOANSWER = "nickNamenoanswer";
	// 聊天对方的头像（对方没有回复用）
	public static final String CHAT_HEAD_RUL_NOANSWER = "headUrlnoanswer";
	// 聊天对方的头像（对方没有回复用）
	public static final String CHAT_USERTYPE_NOANSWER = "userTypenoanswer";
	// 聊天对方的id（对方没有回复用）
	public static final String CHAT_USERID_NOANSWER = "userIdnoanswer";

	public static final String SYSTEM_PUSH = "systempush";

	public static String path = getSDPath() + File.separator + "BlackCat"
			+ File.separator;
	/**
	 * 用户头像保存的位置
	 */
	public static final String PICPATH = path + "picture";
	/**
	 * apk保存的位置
	 */
	public static final String APKPATH = path + "apk";

	/**
	 * 最近一次预约过的教练id
	 */
	public static final String LAST_APPOINTMENT_COACH = "last_appointment_coach";

	public enum AppointmentResult {

		// 预约中
		applying("1"),
		// 学员预约取消
		applycancel("2"),
		// 教练同意
		applyconfirm("3"),
		// 教练拒绝取消
		applyrefuse("4"),
		//
		unconfirmfinish("5"),
		//
		ucomments("6"),
		//
		finish("7"),
		// 系统取消
		systemcancel("8"),
		// 已签到
		signfinish("9"),
		// 漏课
		missclass("10");

		private String index;

		private AppointmentResult(String index) {
			this.index = index;
		}

		public String getValue() {
			return index;
		}

	}

	public enum EnrollResult {
		// 报考状态 0 未报名；1 报名中 ；2报名成功
		SUBJECT_NONE("0"), SUBJECT_ENROLLING("1"), SUBJECT_ENROLL_SUCCESS("2");
		private String index;

		private EnrollResult(String index) {
			this.index = index;
		}

		public String getValue() {
			return index;
		}
	}

	public enum MoneyType {
		// 报考状态 0 积分收益；1 兑换券 ；2现金额
		// coin certificate("1"), amount_in_cash("2")
		INTEGRAL_RETURN("0"), COIN_CERTIFICATE("1"), AMOUNT_IN_CASH("2");
		private String index;

		private MoneyType(String index) {
			this.index = index;
		}

		public String getValue() {
			return index;
		}
	}

	public enum SubjectStatu {
		SUBJECT_NONE("0"), SUBJECT_ONE("1"), SUBJECT_THREE("3"), SUBJECT_TWO(
				"2"), SUBJECT_FOUR("4");
		private String index;

		private SubjectStatu(String index) {
			this.index = index;
		}

		public String getValue() {
			return index;
		}
	}

	public enum UserType {
		USER(1), COACH(2);
		private int index;

		private UserType(int index) {
			this.index = index;
		}

		public int getValue() {
			return index;
		}
	}

	public enum WalletType {
		REGISTER("1"), INVATE("2"), BUY("3");
		private String index;

		private WalletType(String index) {
			this.index = index;
		}

		public String getValue() {
			return index;
		}
	}

	public enum PushType {
		userapplysuccess("userapplysuccess"), reservationsucess(
				"reservationsucess"), walletupdate("walletupdate"), newversion(
				"newversion"), reservationcancel("reservationcancel"), reservationcoachcomment(
				"reservationcoachcomment"), systemmsg("systemmsg");
		private String index;

		private PushType(String index) {
			this.index = index;
		}

		public String getValue() {
			return index;
		}
	}

	public static String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取根目录
			return sdDir.toString();
		}
		return null;
	}

	/**
	 * 科目二 “倒车入库” 技巧大揭秘
	 */
	public static final String CHEATS_BACKING = "http://mp.weixin.qq.com/s?__biz=MzIxNTEyNzY4OA==&mid=402027666&idx=2&sn=cd027f28f9395328dca99cddccd941c8&scene=1&srcid=0227PsJt9MhhtlC1UxbNK8rd#wechat_redirect";
	/**
	 * 科目二 “侧方停车” 技巧大揭秘
	 */
	public static final String CHEATS_PARKING = "http://mp.weixin.qq.com/s?__biz=MzIxNTEyNzY4OA==&mid=402027666&idx=3&sn=8a37868cd4abdc99102e5431701a99b4&scene=1&srcid=0227gf0yphhGiI2xvu54Is8r#wechat_redirect";
	/**
	 * 科目二 “半坡起步” 技巧大揭秘
	 */
	public static final String CHEATS_START = "http://mp.weixin.qq.com/s?__biz=MzIxNTEyNzY4OA==&mid=402027666&idx=4&sn=88035999123229b82a72f672c11b93e8&scene=1&srcid=0227vPi1F8jMEiLYGHhrbf7X#wechat_redirect";
	/**
	 * 科目二 “直接转弯” 技巧大揭秘
	 */
	public static final String CHEATS_TURN_CORNER = "http://mp.weixin.qq.com/s?__biz=MzIxNTEyNzY4OA==&mid=402027666&idx=5&sn=02a10d6b03795ec3d725e9e22b7aa541&scene=1&srcid=02276riS53LAdoeiPiksDwFT#wechat_redirect";
	/**
	 * 科目二 “S弯道” 技巧大揭秘
	 */
	public static final String CHEATS_S_CURVE = "http://mp.weixin.qq.com/s?__biz=MzIxNTEyNzY4OA==&mid=402027666&idx=6&sn=1730ae458c88cee6572f9df4b2a01aa4&scene=1&srcid=0227SBzpyLCZnVA86vePxpB8#wechat_redirect";
	/**
	 * 科目三考试技巧口诀大揭秘
	 */
	public static final String THREE_EXAM_FORMULA = "http://mp.weixin.qq.com/s?__biz=MzIxNTEyNzY4OA==&mid=402027666&idx=7&sn=a476a88c0fe6d016840f9a6b2e38e3a0&scene=1&srcid=0227yDo5HLeoi5N8HXWQ6ha7#wechat_redirect";
}
