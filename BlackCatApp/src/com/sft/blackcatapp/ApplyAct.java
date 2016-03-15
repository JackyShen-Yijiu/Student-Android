package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;

import com.jzjf.app.R;
import com.sft.common.Config;
import com.sft.dialog.EnrollSelectConfilctDialog;
import com.sft.dialog.EnrollSelectConfilctDialog.OnSelectConfirmListener;
import com.sft.util.CommonUtil;
import com.sft.util.JSONUtil;
import com.sft.util.LogUtil;
import com.sft.util.SharedPreferencesUtil;
import com.sft.util.UTC2LOC;
import com.sft.util.Util;
import com.sft.view.ApplyClassTypeLayout.OnClassTypeSelectedListener;
import com.sft.viewutil.MultipleTextViewGroup;
import com.sft.viewutil.MultipleTextViewGroup.MultipleTextViewVO;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.CarModelVO;
import com.sft.vo.ClassVO;
import com.sft.vo.CoachVO;
import com.sft.vo.MyCodeVO;
import com.sft.vo.SchoolVO;
import com.sft.vo.UserVO;

/**
 * 报名,新版
 * 
 * @author tan 2016-3-14 下午1:36:14
 * 
 */
public class ApplyAct extends BaseActivity implements
		OnClassTypeSelectedListener, OnSelectConfirmListener {

	// private ApplyClassTypeLayout applyClassTypeLayout;
	private static final String realName = "realName";
	private static final String contact = "contact";
	private static final String enroll = "enroll";
	private final static String carStyleString = "carStyle";
	private final static String firstSchool = "firstSchool";
	private final static String ycode = "ycode";
	private final static String schoolDetail = "schoolDetail";

	String[] tempStrings;
	// 姓名输入框
	private EditText nameEt;
	// 联系方式输入框
	private EditText contactEt;
	// 报考驾校文本
	private TextView schoolTv;
	// 报考教练文本
	private TextView coachTv;
	// 报考车型文本
	private TextView carStyleTv;
	// 报考班级文本
	private TextView classTv;
	// 用户选择的驾校
	private SchoolVO school;
	// 班型id
	private ClassVO classId;
	// 车型
	private CarModelVO carStyle;
	// 用户选择的教练id
	private CoachVO coach;
	// 报名状态
	private String enrollState;

	private RelativeLayout enroll_rootlayout;
	private Button commitBtn;
	private static final String myCode = "myCode";
	private static final String classInfo = "classInfo";
	private TextView licenseTypeC1;
	private TextView licenseTypeC2;
	private TextView licenseType;
	private List<CarModelVO> listCarModelVOs;
	private TextView styleTv;
	private TextView dataTv;
	private TextView weekTv;
	private TextView brandTv;
	private TextView priceTv;
	private TextView countTv;
	private TextView introductionTv;
	// private int multipleTextViewGroupWidth;
	// private MultipleTextViewGroup multipleTextViewGroup;
	/** 身份证号 */
	// private SchoolVO school;

	private ClassVO classe;
	// private String classID;

	/**
	 * 0：驾校详情 1：教练详情 2.活动详情 ，，待定
	 */
	private int from;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addView(R.layout.act_apply);

		initView();
		setListener();
		initApplyData();
		initData();
	}

	private String enCoachId;
	private String enSchoolId;
	private String enclassTypeId;
	private String encarmodel;
	String schoolName = "";
	private TextView tv_class_content;
	private TextView tv_class_time;
	private ImageView check_btn;
	private RelativeLayout rl_Ycode;
	private TextView apply_Ycode;

	private void initApplyData() {

		from = getIntent().getIntExtra("from", 0);

		switch (from) {
		case 0:// 驾校详情
			classe = (ClassVO) getIntent().getSerializableExtra("class");
			school = (SchoolVO) getIntent().getSerializableExtra("school");
			enSchoolId = classe.getSchoolinfo().getSchoolid();
			schoolName = classe.getSchoolinfo().getName();
			enCoachId = "";// 智能分配
			enclassTypeId = classe.getCalssid();
			encarmodel = classe.getCarmodel().toString();
			break;
		case 1:// 教练详情
			coach = (CoachVO) getIntent().getSerializableExtra("coach");
			classe = (ClassVO) getIntent().getSerializableExtra("class");

			enSchoolId = coach.getDriveschoolinfo().getId();

			schoolName = coach.getDriveschoolinfo().getName();
			enCoachId = coach.getCoachid();// 智能分配

			enclassTypeId = classe.get_id();
			// LogUtil.print("classTypeId--->"+enclassTypeId);
			encarmodel = classe.getCarmodel().toString();
			break;
		case 2:// 活动详情
			break;
		}

		initDefaultData(schoolName, coach, classe);
		obtainNearBySchool();
	}

	private void initDefaultData(String schoolName, CoachVO coach,
			ClassVO classe) {
		coachTv.setText(null == coach ? "智能匹配" : coach.getName());
		schoolTv.setText(schoolName);
		licenseType.setText(classe.getClassname() + "￥"
				+ classe.getOnsaleprice());
		tv_class_content.setText(classe.getClassdesc());
		tv_class_time.setText(classe.getClasschedule());

	}

	/**
	 * 获取y码，判断是否正确
	 */

	private void obtainNearBySchool() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("latitude", app.latitude);
		paramMap.put("longitude", app.longtitude);
		paramMap.put("radius", "10000");
		paramMap.put("index", "1");
		paramMap.put("count", "10");
		HttpSendUtils.httpGetSend(firstSchool, this, Config.IP
				+ "api/v1/searchschool", paramMap);
	}

	private void initView() {
		setTitleText(R.string.enroll_info_table);
		setBg(getResources().getColor(R.color.main_bg));

		enroll_rootlayout = (RelativeLayout) findViewById(R.id.enroll_rootlayout);
		enroll_rootlayout.setFocusable(true);
		enroll_rootlayout.setFocusableInTouchMode(true);
		enroll_rootlayout.requestFocus();

		tv_class_content = (TextView) findViewById(R.id.tv_class_content);
		tv_class_time = (TextView) findViewById(R.id.tv_class_time);
		check_btn = (ImageView) findViewById(R.id.check_btn);
		rl_Ycode = (RelativeLayout) findViewById(R.id.rl_Ycode);
		apply_Ycode = (TextView) findViewById(R.id.apply_Ycode);

		schoolTv = (TextView) findViewById(R.id.new_apply_school);
		coachTv = (TextView) findViewById(R.id.new_apply_coach);
		coachRl = (RelativeLayout) findViewById(R.id.enroll_coach_rl);
		nameEt = (EditText) findViewById(R.id.enroll_name_et);
		contactEt = (EditText) findViewById(R.id.enroll_contact_et);

		commitBtn = (Button) findViewById(R.id.enroll_commit_btn);

		licenseType = (TextView) findViewById(R.id.new_apply_classtype);

		classTypeLayout = (RelativeLayout) findViewById(R.id.enroll_class_rl);
		// classDetailLayout = (RelativeLayout)
		// findViewById(R.id.apply_class_detail);
		// classDetailLayout.measure(0, 0);
		// targetHeight = classDetailLayout.getMeasuredHeight();
		// classDetailLayout.getLayoutParams().height = 0;
		// classDetailLayout.requestLayout();
		// 班级详情
		styleTv = (TextView) findViewById(R.id.class_detail_style_tv);
		dataTv = (TextView) findViewById(R.id.class_detail_date_tv);
		weekTv = (TextView) findViewById(R.id.class_detail_week_tv);
		brandTv = (TextView) findViewById(R.id.class_detail_brand_tv);
		priceTv = (TextView) findViewById(R.id.class_detail_price_tv);
		countTv = (TextView) findViewById(R.id.class_detail_count_tv);
		introductionTv = (TextView) findViewById(R.id.class_detail_introduction_content_tv);

		selectCochRl = (RelativeLayout) findViewById(R.id.rl_coach);

	}

	private void initData() {
		// LogUtil.print("initData-->"+app.userVO);
		enrollState = app.userVO.getApplystate();
		if (app.userVO.getPayState() == 20) {
			commitBtn.setText("已支付");
		}

		String name = SharedPreferencesUtil.getString(this, realName
				+ app.userVO.getUserid(), "");
		String contac = SharedPreferencesUtil.getString(this, contact
				+ app.userVO.getUserid(), "");
		// String name = util.readParam(realName + app.userVO.getUserid());
		if (!TextUtils.isEmpty(name)) {
			nameEt.setText(name);
		} else {
			nameEt.setText(app.userVO.getName());
		}
		// String contac = util.readParam(contact + app.userVO.getUserid());
		if (!TextUtils.isEmpty(contac)) {
			contactEt.setText(contac);
		} else {
			contactEt.setText(app.userVO.getMobile());
		}

	}

	private void setListener() {
		commitBtn.setOnClickListener(this);
		check_btn.setOnClickListener(this);
		rl_Ycode.setOnClickListener(this);
		selectCochRl.setOnClickListener(this);
	}

	boolean isClick = true;

	@Override
	public void onClick(View v) {
		if (!onClickSingleView()) {
			return;
		}
		if (v.getId() == R.id.base_left_btn) {
			finish();
			return;
		}
		Intent intent = null;
		switch (v.getId()) {
		case R.id.rl_coach:
			// 智能匹配
			intent = new Intent(this, AppointmentMoreCoachActivity.class);
			intent.putExtra("isFromApply", true);
			break;
		case R.id.rl_class:// 整个布局点击
		case R.id.check_btn:
			if (isClick) {
				tv_class_content.setVisibility(View.VISIBLE);
				tv_class_time.setVisibility(View.VISIBLE);
				check_btn.setImageResource(R.drawable.icon_more_down);
			} else {
				check_btn.setImageResource(R.drawable.icon_more);
				tv_class_content.setVisibility(View.GONE);
				tv_class_time.setVisibility(View.GONE);
			}
			isClick = !isClick;
			break;
		case R.id.rl_Ycode:
			intent = new Intent(this, YCodeListActivity.class);
			break;
		case R.id.enroll_carstyle_tv:
			if (school == null) {
				ZProgressHUD.getInstance(this).show();
				ZProgressHUD.getInstance(this).dismissWithFailure("先选择驾校");
			} else {
				intent = new Intent(this, EnrollCarStyleActivity.class);
				if (carStyle != null)
					intent.putExtra("carStyle", carStyle);
			}
			break;
		case R.id.enroll_commit_btn:
			String checkResult = checkEnrollInfo();
			// LogUtil.print("checkResult---->"+checkResult);
			if (null != checkResult) {
				ZProgressHUD.getInstance(this).show();
				ZProgressHUD.getInstance(this).dismissWithFailure(checkResult);
			} else {
				// 验证Y码
				if (null == apply_Ycode.getText().toString()
						|| TextUtils.isEmpty(apply_Ycode.getText().toString()
								.trim())) {

					enroll(null, enCoachId, enSchoolId, enclassTypeId,
							encarmodel);
				} else {
					obtainMyCode();// 正式
				}
			}
			break;

		case R.id.pop_window_one:
			isSystemAdd = true;
			coachTv.setText(getResources().getString(
					R.string.apply_system_distribute));
			if (popupWindow != null) {
				popupWindow.dismiss();
			}
			break;
		case R.id.pop_window_two:
			isSystemAdd = false;
			coachTv.setText(getResources().getString(R.string.apply_add_byself));
			intent = new Intent(this, EnrollCoachActivity.class);
			intent.putExtra("schoolId", school.getSchoolid());
			if (coach != null)
				intent.putExtra("coach", coach);

			if (popupWindow != null) {
				popupWindow.dismiss();
			}
			break;

		}
		if (intent != null) {
			startActivityForResult(intent, v.getId());
		}
		// }
	}

	private void obtainMyCode() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userid", app.userVO.getUserid());
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpGetSend(myCode, this, Config.IP
				+ "api/v1/userinfo/getUserAvailableFcode", paramMap, 10000,
				headerMap);
	}

	/**
	 * 
	 * @return
	 */
	// private boolean check(){
	//
	// }

	private boolean isSystemAdd = true;
	private PopupWindow popupWindow;

	private void showPopupWindow(View parent) {
		if (popupWindow == null) {
			View view = View.inflate(this, R.layout.pop_window, null);

			TextView addSystem = (TextView) view
					.findViewById(R.id.pop_window_one);
			addSystem.setText(R.string.apply_system_distribute);
			TextView addByself = (TextView) view
					.findViewById(R.id.pop_window_two);
			addByself.setText(R.string.apply_add_byself);
			addSystem.setOnClickListener(this);
			addByself.setOnClickListener(this);

			popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
		}
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		// WindowManager windowManager = (WindowManager)
		// getSystemService(Context.WINDOW_SERVICE);
		// int xPos = -popupWindow.getWidth() / 2
		// + getCustomTitle().getCenter().getWidth() / 2;

		popupWindow.showAsDropDown(parent);

	}

	private void changeLicenseTextColor(int tag) {
		licenseTypeC1.setTextColor(CommonUtil.getColor(this,
				R.color.default_text_color));
		licenseTypeC2.setTextColor(CommonUtil.getColor(this,
				R.color.default_text_color));
		switch (tag) {
		case 1:
			licenseTypeC1.setTextColor(CommonUtil.getColor(this,
					R.color.app_main_color));
			break;
		case 2:
			licenseTypeC2.setTextColor(CommonUtil.getColor(this,
					R.color.app_main_color));

			break;

		default:
			break;
		}
	}

	private void enroll(String checkResult, String coachId, String SchoolId,
			String classTypeId, String carModel) {

		if (checkResult == null) {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("name", nameEt.getText().toString());
			paramMap.put("telephone", contactEt.getText().toString());
			paramMap.put("userid", app.userVO.getUserid());

			// LogUtil.print("enrollll--->" + coachId + "schoolId;:>" + SchoolId
			// + "classType:>" + classTypeId + "CarModel-->" + carModel);

			paramMap.put("coachid", coachId);
			paramMap.put("schoolid", SchoolId);// school.getSchoolid()
			paramMap.put("classtypeid", classTypeId);// classId.getCalssid()
			paramMap.put("carmodel", carModel);// carStyle.toString()
			paramMap.put("idcardnumber", "");
			paramMap.put("address", "");

			paramMap.put("applyagain", 1 + "");
			// }

			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put("authorization", app.userVO.getToken());
			HttpSendUtils.httpPostSend(enroll, this, Config.IP
					+ "api/v1/userinfo/userapplyschool", paramMap, 10000,
					headerMap);
		} else {
			ZProgressHUD.getInstance(this).show();
			ZProgressHUD.getInstance(this).dismissWithFailure(checkResult);
		}
	}

	private String checkEnrollInfo() {

		String name = nameEt.getText().toString();
		if (TextUtils.isEmpty(name)) {
			return "姓名为空";
		}

		String phone = contactEt.getText().toString();
		if (TextUtils.isEmpty(phone)) {
			return "手机号为空";
		} else {
			if (!CommonUtil.isMobile(phone)) {
				return "手机号格式不正确";
			}
		}

		return null;
	}

	private boolean check() {
		String name = nameEt.getText().toString();
		if (TextUtils.isEmpty(name)) {
			Toast("真实姓名不能为空");
			return false;
		}

		String phone = contactEt.getText().toString();
		if (TextUtils.isEmpty(phone)) {
			Toast("手机号不能为空");
			return false;
		} else {
			if (!CommonUtil.isMobile(phone)) {
				Toast("手机号格式不正确");
				return false;
			}
		}

		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Toast(requestCode + "Activity--->" + resultCode + "<<----->>" +
		// data);
		if (data == null) {
			return;
		}
		LogUtil.print("=====---111---===" + requestCode);
		switch (requestCode) {
		case 9:// 报名成功
			setResult(9, getIntent());
			finish();
			break;
		case R.id.rl_Ycode:
			MyCodeVO codeVO = (MyCodeVO) data.getSerializableExtra("code");
			apply_Ycode.setText(codeVO.getYcode());
			break;
		case R.id.pop_window_two:
			// 报名页面选择教练
			coach = (CoachVO) data.getSerializableExtra("coach");
			coachTv.setText(coach.getName());
			break;
		case R.id.main_my_layout:
			// 我喜欢的教练，驾校
			school = null;
			// schoolTv.setText("");
			coach = null;
			coachTv.setText("");
			classId = null;
			classTv.setText("");
			carStyle = null;
			carStyleTv.setText("");

			coach = (CoachVO) data.getSerializableExtra("coach");

			if (coach != null) {
				coachTv.setText(coach.getName());
				school = Util.getEnrollUserSelectedSchool(this);
				// if (school != null) {
				// schoolTv.setText(school.getName());
				// }
			} else {
				school = (SchoolVO) data.getSerializableExtra("school");
				// if (school != null) {
				// schoolTv.setText(school.getName());
				// }
			}

			carStyle = Util.getEnrollUserSelectedCarStyle(this);
			if (carStyle != null) {
				carStyleTv.setText(carStyle.getCode());
			}

			classId = Util.getEnrollUserSelectedClass(this);
			if (classId != null) {
				classTv.setText(classId.getClassname());
			}

			break;

		case R.id.rl_coach:
			LogUtil.print("=====------===");
			CoachVO coachVO = (CoachVO) data.getSerializableExtra("coach");
			if (null != coachVO) {
				coachTv.setText(coachVO.getName());
			}
			break;
		}
	}

	private void obtainEnrollClass() {
		if (school != null) {
			String schoolId = school.getSchoolid();
			HttpSendUtils.httpGetSend(classInfo, this, Config.IP
					+ "api/v1/driveschool/schoolclasstype/" + schoolId);
		}
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}

		try {
			if (type.equals(classInfo)) {
				if (dataArray != null) {
					int length = dataArray.length();
					List<ClassVO> list = new ArrayList<ClassVO>();
					for (int i = 0; i < length; i++) {
						ClassVO classVO = JSONUtil.toJavaBean(ClassVO.class,
								dataArray.getJSONObject(i));
						list.add(classVO);
					}
				}
			} else if (type.equals(enroll)) {
				if ("success".equals(dataString)) {
					// 更新本地数据
					SharedPreferencesUtil.putString(this,
							realName + app.userVO.getUserid(), nameEt.getText()
									.toString());
					SharedPreferencesUtil.putString(this,
							contact + app.userVO.getUserid(), contactEt
									.getText().toString());
					// LogUtil.print("success--->" + jsonString);

					app.isEnrollAgain = true;
					//

					// 报名成功
					//
				} else {
					// 不成功
					ZProgressHUD.getInstance(this).show();
					ZProgressHUD.getInstance(this)
							.dismissWithFailure(msg, 2000);
				}
			} else if (type.equals(carStyleString)) {
				if (dataArray != null) {
					int length = dataArray.length();
					listCarModelVOs = new ArrayList<CarModelVO>();
					for (int i = 0; i < length; i++) {
						CarModelVO carStyleVO = JSONUtil.toJavaBean(
								CarModelVO.class, dataArray.getJSONObject(i));
						listCarModelVOs.add(carStyleVO);
					}
					// setLienseType(listCarModelVOs);
				}
			} else if (type.equals(firstSchool)) {
				if (dataArray != null) {
					try {
						SchoolVO schoolVO;
						schoolVO = JSONUtil.toJavaBean(SchoolVO.class,
								dataArray.getJSONObject(0));

						// setDefaultData(schoolVO);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else if (type.equals(schoolDetail)) {
				if (data != null) {
					try {
						SchoolVO schoolVO;
						schoolVO = JSONUtil.toJavaBean(SchoolVO.class, data);

						// setDefaultData(schoolVO);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else if (type.equals(ycode)) {
				if (result != null && result.equals("0")) {
					EnrollSelectConfilctDialog dialog = new EnrollSelectConfilctDialog(
							ApplyAct.this, "您的Y码不正确，是否要继续报名？");
					dialog.setBtnText("继续报名", "修改Y码");
					dialog.show();
				}
				if (result != null && result.equals("1")) {
					enroll(null, enCoachId, enSchoolId, enclassTypeId,
							encarmodel);
				}
			} else if (type.equals("reLogin")) {
				try {
					// Toast("登录成功");
					app.userVO = JSONUtil.toJavaBean(UserVO.class, data);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	private void reLogin() {
		String lastLoginPhone = util.readParam(Config.LAST_LOGIN_ACCOUNT);
		String password = util.readParam(Config.LAST_LOGIN_PASSWORD);

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("mobile", lastLoginPhone);
		paramMap.put("usertype", "1");
		paramMap.put("password", util.MD5(password));
		HttpSendUtils.httpPostSend("reLogin", this, Config.IP
				+ "api/v1/userinfo/userlogin", paramMap);
	}

	// private RelativeLayout classDetailLayout;
	private int targetHeight;

	private boolean isFirstOpen = false;
	private boolean isExtend = false;

	@Override
	public void ClassTypeSelectedListener(ClassVO seleClassVO) {

		classId = seleClassVO;
		setClassDetailData(seleClassVO);
		if (!isFirstOpen) {
			setClassDetailAnimator();
			isFirstOpen = true;
		}
	}

	private void closeClassDetail() {
		// animator = ValueAnimator.ofInt(targetHeight, 0);
		// animator.addUpdateListener(new AnimatorUpdateListener() {
		// @Override
		// public void onAnimationUpdate(ValueAnimator animator) {
		// classDetailLayout.getLayoutParams().height = (Integer) animator
		// .getAnimatedValue();
		// classDetailLayout.requestLayout();
		// }
		// });
		// animator.setDuration(300);
		// animator.addListener(new AnimatorListener() {
		// @Override
		// public void onAnimationStart(Animator arg0) {
		//
		// }
		//
		// @Override
		// public void onAnimationRepeat(Animator arg0) {
		// }
		//
		// @Override
		// public void onAnimationEnd(Animator arg0) {
		// isFirstOpen = false;
		// isExtend = false;
		// }
		//
		// @Override
		// public void onAnimationCancel(Animator arg0) {
		// }
		// });
		// animator.start();
	}

	private ValueAnimator animator;
	private RelativeLayout classTypeLayout;
	private RelativeLayout coachRl;
	private RelativeLayout selectCochRl;

	// private EditText yCodeEt;

	private void setClassDetailAnimator() {
		// animator = ValueAnimator.ofInt(0, targetHeight);
		// animator.addUpdateListener(new AnimatorUpdateListener() {
		// @Override
		// public void onAnimationUpdate(ValueAnimator animator) {
		// classDetailLayout.getLayoutParams().height = (Integer) animator
		// .getAnimatedValue();
		// classDetailLayout.requestLayout();
		// }
		// });
		// animator.addListener(new AnimatorListener() {
		// @Override
		// public void onAnimationStart(Animator arg0) {
		//
		// }
		//
		// @Override
		// public void onAnimationRepeat(Animator arg0) {
		// }
		//
		// @Override
		// public void onAnimationEnd(Animator arg0) {
		// isExtend = true;
		// }
		//
		// @Override
		// public void onAnimationCancel(Animator arg0) {
		// }
		// });
		// animator.setDuration(300);
		// animator.start();
	}

	private void setClassDetailData(ClassVO seleClassVO) {
		styleTv.setText(getString(R.string.license)
				+ seleClassVO.getCarmodel().getCode());
		weekTv.setText(getString(R.string.course_date)
				+ seleClassVO.getClasschedule());
		brandTv.setText(getString(R.string.car_brand)
				+ seleClassVO.getCartype());
		priceTv.setText(seleClassVO.getPrice());
		countTv.setText(getString(R.string.enroll_count)
				+ seleClassVO.getApplycount());

		String beginDate = seleClassVO.getBegindate();
		beginDate = UTC2LOC.instance.getDate(beginDate, "yyyy.MM.dd");
		String endDate = seleClassVO.getBegindate();
		endDate = UTC2LOC.instance.getDate(endDate, "yyyy.MM.dd");
		dataTv.setText(getString(R.string.active_date) + beginDate + "-"
				+ endDate);

		introductionTv.setText(seleClassVO.getClassdesc());

		List<MultipleTextViewVO> multipleList = new ArrayList<MultipleTextViewGroup.MultipleTextViewVO>();
		int length = seleClassVO.getVipserverlist().size();
		// for (int i = 0; i < length; i++) {
		// MultipleTextViewVO vo = multipleTextViewGroup.new
		// MultipleTextViewVO();
		// vo.setText(seleClassVO.getVipserverlist().get(i).getName());
		//
		// String color = seleClassVO.getVipserverlist().get(i).getCoclor();
		// if (color != null && color.length() == 6) {
		// vo.setColor(Color.parseColor("#" + color));
		// } else {
		// vo.setColor(Color.parseColor("#ff6633"));
		// }
		// multipleList.add(vo);
		// }

		// multipleTextViewGroup.setTextViews(multipleList,
		// multipleTextViewGroupWidth);
	}

	@Override
	public void selectConfirm(boolean isConfirm, boolean isFreshAll) {

		if (isConfirm) {
			enroll(null, enCoachId, enSchoolId, enclassTypeId, encarmodel);
		} else {

		}
	}

}
