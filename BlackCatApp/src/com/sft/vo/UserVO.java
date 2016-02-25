package com.sft.vo;

import java.util.List;

import cn.sft.sqlhelper.DBVO;

import com.sft.vo.commonvo.ApplySchoolInfo;
import com.sft.vo.commonvo.CarModel;
import com.sft.vo.commonvo.HeadPortrait;
import com.sft.vo.commonvo.Subject;
import com.sft.vo.uservo.ApplyClassTypeInfo;
import com.sft.vo.uservo.ApplyCoachInfo;
import com.sft.vo.uservo.StudentSubject;
import com.sft.vo.uservo.UserSettingVO;
import com.sft.vo.uservo.VipServerListVO;

public class UserVO extends DBVO {

	private static final long serialVersionUID = 1L;
	private String mobile;
	private String name;
	private String nickname;
	private String creattime;
	private String email;
	private HeadPortrait headportrait;
	private CarModel carmodel;
	private Subject subject;
	private String logintime;
	private String invitationcode;
	private String applystate;
	private ApplySchoolInfo applyschoolinfo;
	private String displayuserid;
	private String is_lock;
	private String telephone;
	private ApplyCoachInfo applycoachinfo;
	private ApplyClassTypeInfo applyclasstypeinfo;

	private String[] addresslist;
	private String address;
	private String token;
	private String displaymobile;
	private String userid;
	private String gender;
	private String signature;
	private String idcardnumber;
	private StudentSubject subjecttwo;
	private StudentSubject subjectthree;
	private UserSettingVO usersetting;
	private List<VipServerListVO> vipserverlist;
	private PayOrderVO payOrderVO;

	public void setHeadPortrait(HeadPortrait headPortrait) {
		this.headportrait = headPortrait;
	}

	public HeadPortrait getHeadportrait() {
		return headportrait == null ? new HeadPortrait() : headportrait;
	}

	public CarModel getCarmodel() {
		return carmodel == null ? new CarModel() : carmodel;
	}

	public void setCarmodel(CarModel carmodel) {
		this.carmodel = carmodel;
	}

	public Subject getSubject() {
		return subject == null ? new Subject() : subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public ApplySchoolInfo getApplyschoolinfo() {
		return applyschoolinfo == null ? new ApplySchoolInfo()
				: applyschoolinfo;
	}

	public void setApplyschoolinfo(ApplySchoolInfo applyschoolinfo) {
		this.applyschoolinfo = applyschoolinfo;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getCreatetime() {
		return creattime;
	}

	public void setCreatetime(String createtime) {
		this.creattime = createtime;
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

	public String getApplystate() {
		return applystate;
	}

	public void setApplystate(String applystate) {
		this.applystate = applystate;
	}

	public String getIs_lock() {
		return is_lock;
	}

	public void setIs_lock(String is_lock) {
		this.is_lock = is_lock;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getDisplaymobile() {
		return displaymobile;
	}

	public void setDisplaymobile(String displaymobile) {
		this.displaymobile = displaymobile;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getDisplayuserid() {
		return displayuserid;
	}

	public void setDisplayuserid(String displayuserid) {
		this.displayuserid = displayuserid;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public ApplyClassTypeInfo getApplyclasstypeinfo() {
		return applyclasstypeinfo == null ? new ApplyClassTypeInfo()
				: applyclasstypeinfo;
	}

	public void setApplyclasstypeinfo(ApplyClassTypeInfo applyclasstypeinfo) {
		this.applyclasstypeinfo = applyclasstypeinfo;
	}

	public String[] getAddresslist() {
		return addresslist;
	}

	public void setAddresslist(String[] addresslist) {
		this.addresslist = addresslist;
	}

	public ApplyCoachInfo getApplycoachinfo() {
		return applycoachinfo == null ? new ApplyCoachInfo() : applycoachinfo;
	}

	public void setApplycoachinfo(ApplyCoachInfo applycoachinfo) {
		this.applycoachinfo = applycoachinfo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getIdcardnumber() {
		return idcardnumber;
	}

	public void setIdcardnumber(String idcardnumber) {
		this.idcardnumber = idcardnumber;
	}

	public StudentSubject getSubjecttwo() {
		return subjecttwo == null ? new StudentSubject() : subjecttwo;
	}

	public void setSubjecttwo(StudentSubject subjecttwo) {
		this.subjecttwo = subjecttwo;
	}

	public StudentSubject getSubjectthree() {
		return subjectthree == null ? new StudentSubject() : subjectthree;
	}

	public void setSubjectthree(StudentSubject subjectthree) {
		this.subjectthree = subjectthree;
	}

	public UserSettingVO getUsersetting() {
		return usersetting == null ? new UserSettingVO() : usersetting;
	}

	public void setUsersetting(UserSettingVO usersetting) {
		this.usersetting = usersetting;
	}

	public List<VipServerListVO> getVipserverlist() {
		return vipserverlist;
	}

	public void setVipserverlist(List<VipServerListVO> vipserverlist) {
		this.vipserverlist = vipserverlist;
	}

	public PayOrderVO getPayOrderVO() {
		return payOrderVO;
	}

	public void setPayOrderVO(PayOrderVO payOrderVO) {
		this.payOrderVO = payOrderVO;
	}

}
