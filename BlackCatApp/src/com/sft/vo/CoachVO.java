package com.sft.vo;

import java.util.ArrayList;
import java.util.List;

import com.sft.vo.coachvo.TrainFieldlInfo;
import com.sft.vo.coachvo.WorkTimeSpace;
import com.sft.vo.commonvo.ApplySchoolInfo;
import com.sft.vo.commonvo.HeadPortrait;
import com.sft.vo.commonvo.Subject;

import cn.sft.sqlhelper.DBVO;

public class CoachVO extends DBVO {

	public static final String ENROLL_USER_SELECTED = "1";
	public static final String APPOINTMENT_COACH = "2";

	private static final long serialVersionUID = 1L;

	private String db_userid;
	private String db_coachStyle;

	private ApplySchoolInfo driveschoolinfo;
	private String coachid;
	private String mobile;
	private String name;
	private String createtime;
	private String email;
	private HeadPortrait headportrait;
	private String distance;
	private String latitude;
	private String longitude;
	private String logintime;
	private String invitationcode;
	private String displaycoachid;
	private String is_lock;
	private String address;
	private String introduction;
	private String Gender;
	private String is_validation;
	private String studentcoount;
	private String commentcount;
	private String Seniority;
	private List<Subject> subject;
	private CarModelVO carmodel;
	private TrainFieldlInfo trainfieldlinfo;
	private String coachnumber;
	private String is_shuttle;
	private String starlevel;
	private String token;
	private String passrate;
	private String platenumber;
	private String shuttlemsg;
	private String worktimedesc;
	private String[] workweek;
	private WorkTimeSpace worktimespace;

	@Override
	public boolean equals(Object o) {
		if (o instanceof CoachVO) {
			CoachVO coachVO = (CoachVO) o;
			if (coachid.equals(coachVO.getCoachid())) {
				return true;
			}
		}
		return false;
	}

	public String getDb_userid() {
		return db_userid;
	}

	public void setDb_userid(String db_userid) {
		this.db_userid = db_userid;
	}

	public String getDb_coachStyle() {
		return db_coachStyle;
	}

	public void setDb_coachStyle(String db_coachStyle) {
		this.db_coachStyle = db_coachStyle;
	}

	public boolean isGeneral() {
		if (subject != null && subject.size() > 1) {
			return true;
		}
		return false;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getCoachid() {
		return coachid;
	}

	public void setCoachid(String coachid) {
		this.coachid = coachid;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude.equals("true") ? "1" : "0";
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public HeadPortrait getHeadportrait() {
		return headportrait == null ? new HeadPortrait() : headportrait;
	}

	public void setHeadportrait(HeadPortrait headportrait) {
		this.headportrait = headportrait;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ApplySchoolInfo getDriveschoolinfo() {
		return driveschoolinfo == null ? new ApplySchoolInfo() : driveschoolinfo;
	}

	public void setDriveschoolinfo(ApplySchoolInfo driveschoolinfo) {
		this.driveschoolinfo = driveschoolinfo;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLogintime() {
		return logintime;
	}

	public void setLogintime(String logintime) {
		this.logintime = logintime;
	}

	public String getInvitationcode() {
		return invitationcode;
	}

	public void setInvitationcode(String invitationcode) {
		this.invitationcode = invitationcode;
	}

	public String getDisplaycoachid() {
		return displaycoachid;
	}

	public void setDisplaycoachid(String displaycoachid) {
		this.displaycoachid = displaycoachid;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getGender() {
		return Gender;
	}

	public void setGender(String gender) {
		Gender = gender;
	}

	public String getIs_lock() {
		return is_lock;
	}

	public void setIs_lock(String is_lock) {
		this.is_lock = is_lock;
	}

	public String getIs_validation() {
		return is_validation;
	}

	public void setIs_validation(String is_validation) {
		this.is_validation = is_validation;
	}

	public String getIs_shuttle() {
		return is_shuttle;
	}

	public void setIs_shuttle(String is_shuttle) {
		this.is_shuttle = is_shuttle;
	}

	public String getStudentcoount() {
		return studentcoount;
	}

	public void setStudentcoount(String studentcoount) {
		this.studentcoount = studentcoount;
	}

	public String getCommentcount() {
		return commentcount;
	}

	public void setCommentcount(String commentcount) {
		this.commentcount = commentcount;
	}

	public String getSeniority() {
		return Seniority;
	}

	public void setSeniority(String seniority) {
		Seniority = seniority;
	}

	public List<Subject> getSubject() {
		return subject == null ? new ArrayList<Subject>() : subject;
	}

	public void setSubject(List<Subject> subject) {
		this.subject = subject;
	}

	public CarModelVO getCarmodel() {
		return carmodel == null ? new CarModelVO() : carmodel;
	}

	public void setCarmodel(CarModelVO carmodel) {
		this.carmodel = carmodel;
	}

	public TrainFieldlInfo getTrainfieldlinfo() {
		return trainfieldlinfo == null ? new TrainFieldlInfo() : trainfieldlinfo;
	}

	public void setTrainfieldlinfo(TrainFieldlInfo trainfieldlinfo) {
		this.trainfieldlinfo = trainfieldlinfo;
	}

	public String getCoachnumber() {
		return coachnumber;
	}

	public void setCoachnumber(String coachnumber) {
		this.coachnumber = coachnumber;
	}

	public String getStarlevel() {
		return starlevel;
	}

	public void setStarlevel(String starlevel) {
		this.starlevel = starlevel;
	}

	public String getPassrate() {
		return passrate;
	}

	public void setPassrate(String passrate) {
		this.passrate = passrate;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPlatenumber() {
		return platenumber;
	}

	public void setPlatenumber(String platenumber) {
		this.platenumber = platenumber;
	}

	public String getShuttlemsg() {
		return shuttlemsg;
	}

	public void setShuttlemsg(String shuttlemsg) {
		this.shuttlemsg = shuttlemsg;
	}

	public String getWorktimedesc() {
		return worktimedesc;
	}

	public void setWorktimedesc(String worktimedesc) {
		this.worktimedesc = worktimedesc;
	}

	public String[] getWorkweek() {
		return workweek;
	}

	public void setWorkweek(String[] workweek) {
		this.workweek = workweek;
	}

	public WorkTimeSpace getWorktimespace() {
		return worktimespace;
	}

	public void setWorktimespace(WorkTimeSpace worktimespace) {
		this.worktimespace = worktimespace;
	}

}
