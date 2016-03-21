package com.sft.vo;

import java.util.ArrayList;
import java.util.List;

import cn.sft.sqlhelper.DBVO;

/**
 * 班车路线 新的
 * @author pengdonghua
 *
 */
public class SchoolBusRouteNew extends DBVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String index;
	private String time;
	private String longitude;
	private String latitude;
	private String stationname;
	
	private String endtime;
	/**线路名称*/
	private String routename;
	
	private List<StationInfoVO> stationinfo = new ArrayList<StationInfoVO>();
	
	
	public String getRoutename() {
		return routename;
	}

	public void setRoutename(String routename) {
		this.routename = routename;
	}

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
	
	
	
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getStationname() {
		return stationname;
	}
	public void setStationname(String stationname) {
		this.stationname = stationname;
	}
	

	

}
