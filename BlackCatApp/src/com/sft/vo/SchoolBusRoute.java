package com.sft.vo;

import java.util.ArrayList;
import java.util.List;

import cn.sft.sqlhelper.DBVO;

public class SchoolBusRoute extends DBVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String __v;
	private String _id;
	private String routecontent;
	private String routename;
	private String schoolid;
	
	private String endtime;
	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getBegintime() {
		return begintime;
	}

	public void setBegintime(String begintime) {
		this.begintime = begintime;
	}

	public String getTrainingfieldid() {
		return trainingfieldid;
	}

	public void setTrainingfieldid(String trainingfieldid) {
		this.trainingfieldid = trainingfieldid;
	}

	public List<StationInfoVO> getStationinfo() {
		return stationinfo;
	}

	public void setStationinfo(List<StationInfoVO> stationinfo) {
		this.stationinfo = stationinfo;
	}

	private String begintime;
	private String trainingfieldid;
	
	private List<StationInfoVO> stationinfo = new ArrayList<StationInfoVO>();
	

	public String get__v() {
		return __v;
	}

	public void set__v(String __v) {
		this.__v = __v;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getRoutecontent() {
		return routecontent;
	}

	public void setRoutecontent(String routecontent) {
		this.routecontent = routecontent;
	}

	public String getRoutename() {
		return routename;
	}

	public void setRoutename(String routename) {
		this.routename = routename;
	}

	public String getSchoolid() {
		return schoolid;
	}

	public void setSchoolid(String schoolid) {
		this.schoolid = schoolid;
	}

}
