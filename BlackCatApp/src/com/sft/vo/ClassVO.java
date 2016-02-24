package com.sft.vo;

import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;
import cn.sft.sqlhelper.DBVO;

import com.sft.vo.uservo.VipServerListVO;

public class ClassVO extends DBVO {

	public static final String ENROLL_USER_SELECTED = "1";
	private static final long serialVersionUID = 1L;

	private String db_userid;
	private String db_classStyle;
	private String calssid;
	private SchoolVO schoolinfo;
	private String classname;
	private String begindate;
	private String enddate;
	private String is_vip;
	private String classdesc;
	private String price;
	private CarModelVO carmodel;
	private String cartype;
	private List<VipServerListVO> vipserverlist;
	private String classchedule;
	private String applycount;
	private String onsaleprice;// 优惠后的价格
	private String _id;

	public String getOnsaleprice() {
		return onsaleprice;
	}

	public void setOnsaleprice(String onsaleprice) {
		this.onsaleprice = onsaleprice;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getDb_userid() {
		return db_userid;
	}

	public void setDb_userid(String db_userid) {
		this.db_userid = db_userid;
	}

	public String getDb_classStyle() {
		return db_classStyle;
	}

	public void setDb_classStyle(String db_classStyle) {
		this.db_classStyle = db_classStyle;
	}

	public String getCalssid() {
		return calssid;
	}

	public void setCalssid(String calssid) {
		this.calssid = calssid;
	}

	public SchoolVO getSchoolinfo() {
		return schoolinfo == null ? new SchoolVO() : schoolinfo;
	}

	public void setSchoolinfo(SchoolVO schoolinfo) {
		this.schoolinfo = schoolinfo;
	}

	public String getName() {
		if (classname == null) {
			return "班级不详";
		}
		return TextUtils.isEmpty(classname) ? "null" : classname;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String getBegindate() {
		return begindate;
	}

	public void setBegindate(String begindate) {
		this.begindate = begindate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public String getIs_vip() {
		return is_vip;
	}

	public void setIs_vip(String is_vip) {
		this.is_vip = is_vip;
	}

	public String getClassdesc() {
		return classdesc;
	}

	public void setClassdesc(String classdesc) {
		this.classdesc = classdesc;
	}

	public String getPrice() {
		return price == null ? "0" : price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public CarModelVO getCarmodel() {
		return carmodel == null ? new CarModelVO() : carmodel;
	}

	public void setCarmodel(CarModelVO carmodel) {
		this.carmodel = carmodel;
	}

	public List<VipServerListVO> getVipserverlist() {
		return vipserverlist == null ? new ArrayList<VipServerListVO>()
				: vipserverlist;
	}

	public void setVipserverlist(List<VipServerListVO> vipserverlist) {
		this.vipserverlist = vipserverlist;
	}

	public String getCartype() {
		return cartype;
	}

	public void setCartype(String cartype) {
		this.cartype = cartype;
	}

	public String getClasschedule() {
		return classchedule;
	}

	public void setClasschedule(String classchedule) {
		this.classchedule = classchedule;
	}

	public String getApplycount() {
		return applycount;
	}

	public void setApplycount(String applycount) {
		this.applycount = applycount;
	}

}
