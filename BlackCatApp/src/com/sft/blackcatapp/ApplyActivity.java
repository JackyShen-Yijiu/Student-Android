package com.sft.blackcatapp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;

import com.sft.common.Config;
import com.sft.common.Config.EnrollResult;
import com.sft.dialog.EnrollSelectConfilctDialog;
import com.sft.dialog.EnrollSelectConfilctDialog.OnSelectConfirmListener;
import com.sft.util.CommonUtil;
import com.sft.util.JSONUtil;
import com.sft.util.LogUtil;
import com.sft.util.SharedPreferencesUtil;
import com.sft.util.UTC2LOC;
import com.sft.util.Util;
import com.sft.view.ApplyClassTypeLayout;
import com.sft.view.ApplyClassTypeLayout.OnClassTypeSelectedListener;
import com.sft.viewutil.MultipleTextViewGroup;
import com.sft.viewutil.MultipleTextViewGroup.MultipleTextViewVO;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.CarModelVO;
import com.sft.vo.ClassVO;
import com.sft.vo.CoachVO;
import com.sft.vo.PayOrderVO;
import com.sft.vo.SchoolVO;
import com.sft.vo.UserVO;

/**
 * 报名
 * 
 * @author sun 2016-1-29 下午1:36:14
 * 
 */
public class ApplyActivity extends BaseActivity implements
		OnClassTypeSelectedListener, OnSelectConfirmListener {

//	private ApplyClassTypeLayout applyClassTypeLayout;
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
	private int multipleTextViewGroupWidth;
	private MultipleTextViewGroup multipleTextViewGroup;
	/** 身份证号 */
	private EditText etYCodeCard;
	private String price;
	private String classType;
	private String schoolId;
//	private SchoolVO school;

	private ClassVO classe;
	// private String classID;
	/** 实际价格 */
	private TextView tvOnSale;

	private RadioButton radioOnLine, radioOffLine;
	/**
	 * 0：驾校详情 1：教练详情 2.活动详情 ，，待定
	 */
	private int from;
	private TextView tvNameError,tvPhoneError;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addView(R.layout.new_apply);

		initView();
//		initData();
		setListener();
//		obtainEnrollCarStyle();
		initApplyData();
		// View v;
		// v.setBackgroundColor(Color.)
	}

	private String enCoachId;
	private String enSchoolId;
	private String enclassTypeId;
	private String encarmodel;
	String schoolName = "";

	private void initApplyData() {

		from = getIntent().getIntExtra("from", 0);

		// boolean isFromMenu = getIntent().getBooleanExtra("isFromMenu",
		// false);
		// boolean isFromEnroll = getIntent().getBooleanExtra(
		// SearchCoachActivity.from_searchCoach_enroll, false);
		// // 活动页面
		// boolean isFromPlay = getIntent().getBooleanExtra("isFromPlay",
		// false);

		// if(classe!=null){
		// classID = classe.get_id();
		// }
		// if (school != null) {//从驾校跳转过来
		// setDefaultData(school);
		// }

		// coach = (CoachVO) getIntent().getSerializableExtra("coach");
		// if (classe != null) {
		// licenseType.setText(classe.getClassname() + "￥"
		// + classe.getOnsaleprice());
		// tvOnSale.setText(classe.getOnsaleprice() + "元");
		// classID = classe.get_id();
		// }
		
		switch (from) {
		case 0:// 驾校详情
			classe = (ClassVO) getIntent().getSerializableExtra("class");
			school = (SchoolVO) getIntent().getSerializableExtra("school");
			enSchoolId = classe.getSchoolinfo().getSchoolid();

			
			schoolName = classe.getSchoolinfo().getName();
			LogUtil.print("school--id:" + enSchoolId + "id::" + school.getId());
			enCoachId = "";// 智能分配
			enclassTypeId = classe.getCalssid();
			encarmodel = classe.getCarmodel().toString();
			LogUtil.print("classTypeId---000>"+enclassTypeId);
			// coachId 智能分配
			break;
		case 1:// 教练详情
			coach = (CoachVO) getIntent().getSerializableExtra("coach");
			classe = (ClassVO) getIntent().getSerializableExtra("class");

			enSchoolId = coach.getDriveschoolinfo().getId();
			
			schoolName = coach.getDriveschoolinfo().getName();
			enCoachId = coach.getCoachid();// 智能分配
			
			enclassTypeId = classe.get_id();
			LogUtil.print("classTypeId--->"+enclassTypeId);
			encarmodel = classe.getCarmodel().toString();
			break;
		case 2:// 活动详情
			break;
		}
//		LogUtil.print("class--name::>>>" + classe.getSchoolinfo().getSchoolid());
		initDefaultData(schoolName, coach, classe);

		// // 从查找驾校处报名
		// if (isFromMenu) {
		// SchoolVO schoolVO = (SchoolVO) getIntent().getSerializableExtra(
		// "school");
		// CoachVO coachVO = (CoachVO) getIntent().getSerializableExtra(
		// "coach");
		// if (schoolVO != null) {
		// setDefaultData(schoolVO);
		// }
		// if (coachVO != null) {
		// coachTv.setText(coachVO.getName());
		// }
		// // 从查找教练处报名
		// } else if (isFromEnroll) {
		//
		// if (coach != null) {
		// schoolRl.setClickable(false);
		// coachTv.setText(coach.getName());
		// obtainSchoolById(coach.getDriveschoolinfo().getId());
		// }
		// } else if (isFromPlay) {
		//
		// } else {
		obtainNearBySchool();
		// }
	}

	private void initDefaultData(String schoolName, CoachVO coach,
			ClassVO classe) {
		coachTv.setText(null == coach ? "智能匹配" : coach.getName());
		schoolTv.setText(schoolName);
		licenseType.setText(classe.getClassname() + "￥"
				+ classe.getOnsaleprice());
		tvOnSale.setText("需支付  "+classe.getOnsaleprice() + "元");
	}

	private void obtainSchoolById(String schoolId) {
		HttpSendUtils.httpGetSend(schoolDetail, this, Config.IP
				+ "api/v1/driveschool/getschoolinfo/" + schoolId);
	}

	private void obtainEnrollCarStyle() {
		HttpSendUtils.httpGetSend(carStyleString, this, Config.IP
				+ "api/v1/info/carmodel");
	}

	/**
	 * 获取y码，判断是否正确
	 */
	private void obtainYCode() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("userid", app.userVO.getUserid());
		paramMap.put("fcode", etYCodeCard.getText().toString());
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("authorization", app.userVO.getToken());
		HttpSendUtils.httpGetSend(ycode, this, Config.IP
				+ "api/v1/verifyfcodecorrect", paramMap, 10000, headerMap);
	}

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

//		applyClassTypeLayout = (ApplyClassTypeLayout) findViewById(R.id.apply_class_type_ll);
		tvNameError = (TextView) findViewById(R.id.new_apply_name_error);
		schoolTv = (TextView) findViewById(R.id.new_apply_school);
		schoolRl = (RelativeLayout) findViewById(R.id.enroll_school_rl);
		coachTv = (TextView) findViewById(R.id.new_apply_coach);
		coachRl = (RelativeLayout) findViewById(R.id.enroll_coach_rl);
		// carStyleTv = (TextView) findViewById(R.id.enroll_carstyle_tv);
		nameEt = (EditText) findViewById(R.id.enroll_name_et);
		contactEt = (EditText) findViewById(R.id.enroll_contact_et);

		etYCodeCard = (EditText) findViewById(R.id.enroll_ycode_et);
		// yCodeEt = (EditText) findViewById(R.id.enroll_ycode_et);

		commitBtn = (Button) findViewById(R.id.enroll_commit_btn);

//		licenseTypeC1 = (TextView) findViewById(R.id.apply_license_type_c1);
//		licenseTypeC2 = (TextView) findViewById(R.id.apply_license_type_c2);
		licenseType = (TextView) findViewById(R.id.new_apply_classtype);

		classTypeLayout = (RelativeLayout) findViewById(R.id.enroll_class_rl);
		classDetailLayout = (RelativeLayout) findViewById(R.id.apply_class_detail);
		// classDetailLayout.setVisibility(View.GONE);
		classDetailLayout.measure(0, 0);
		targetHeight = classDetailLayout.getMeasuredHeight();
		classDetailLayout.getLayoutParams().height = 0;
		classDetailLayout.requestLayout();
		// 班级详情
		styleTv = (TextView) findViewById(R.id.class_detail_style_tv);
		dataTv = (TextView) findViewById(R.id.class_detail_date_tv);
		weekTv = (TextView) findViewById(R.id.class_detail_week_tv);
		brandTv = (TextView) findViewById(R.id.class_detail_brand_tv);
		priceTv = (TextView) findViewById(R.id.class_detail_price_tv);
		countTv = (TextView) findViewById(R.id.class_detail_count_tv);
		introductionTv = (TextView) findViewById(R.id.class_detail_introduction_content_tv);
		multipleTextViewGroup = (MultipleTextViewGroup) findViewById(R.id.class_detail_multiple_tv);

		tvOnSale = (TextView) findViewById(R.id.new_apply_money);

		radioOnLine = (RadioButton) findViewById(R.id.radio1);
		radioOffLine = (RadioButton) findViewById(R.id.radio2);

		final ViewTreeObserver vto = multipleTextViewGroup
				.getViewTreeObserver();
		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			@Override
			public boolean onPreDraw() {
				if (multipleTextViewGroupWidth <= 0) {
					multipleTextViewGroupWidth = multipleTextViewGroup
							.getMeasuredWidth();
					initData();
				}
				return true;
			}
		});
		
		nameEt.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				if(s.length()>0){
					tvNameError.setVisibility(View.GONE);
				}else{
					tvNameError.setVisibility(View.VISIBLE);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});

	}

	private void initData() {
		LogUtil.print("initData-->"+app.userVO);
		enrollState = app.userVO.getApplystate();

		if (EnrollResult.SUBJECT_ENROLLING.getValue().equals(enrollState)) {
			commitBtn.setText("报名审核中...");
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

		// 1.2版 获取新的数据
		//

		// 1.2版 获取新的数据
		//

		//
		// Intent intent = getIntent();
		// if (intent.getBooleanExtra("userselect", false)) {
		// onActivityResult(intent.getIntExtra("requestCode", 0),
		// intent.getIntExtra("resultCode", 0), intent);
		// } else {
		// if (EnrollResult.SUBJECT_NONE.getValue().equals(enrollState)) {
		// // 没有报过名，读取数据库中保存的用户选择信息填充
		// school = Util.getEnrollUserSelectedSchool(this);
		// if (school != null) {
		// schoolTv.setText(school.getName());
		// }
		//
		// carStyle = Util.getEnrollUserSelectedCarStyle(this);
		// if (carStyle != null) {
		// carStyleTv.setText(carStyle.getCode());
		// }
		//
		// classId = Util.getEnrollUserSelectedClass(this);
		// if (classId != null) {
		// classTv.setText(classId.getClassname());
		// }
		//
		// coach = Util.getEnrollUserSelectedCoach(this);
		// if (coach != null) {
		// coachTv.setText(coach.getName());
		// }
		// } else {
		// // 用户已经报过名
		// if (EnrollResult.SUBJECT_ENROLLING.getValue().equals(
		// enrollState)) {
		// commitBtn.setText("报名审核中...");
		// } else {
		// commitBtn.setText("已成功报名");
		// }
		// schoolTv.setText(app.userVO.getApplyschoolinfo().getName());
		// coachTv.setText(app.userVO.getApplycoachinfo().getName());
		// carStyleTv.setText(app.userVO.getCarmodel().getCode());
		// classTv.setText(app.userVO.getApplyclasstypeinfo().getName());
		//
		// nameEt.setEnabled(false);
		// contactEt.setEnabled(false);
		// commitBtn.setEnabled(false);
		// }
		// }
		// classVO = (ClassVO) getIntent().getSerializableExtra("class");

		// schoolNameTv.setText(classVO.getSchoolinfo().getName());
		// schoolAddressTv.setText(classVO.getSchoolinfo().getAddress());

	}

	private void setListener() {
		commitBtn.setOnClickListener(this);
//		schoolRl.setOnClickListener(this);
		// carStyleTv.setOnClickListener(this);
//		coachRl.setOnClickListener(this);
		// classTv.setOnClickListener(this);

		// nameEt.addTextChangedListener(new MyEditChangedListener(realName));
		// contactEt.addTextChangedListener(new MyEditChangedListener(contact));

//		licenseTypeC1.setOnClickListener(this);
//		licenseTypeC2.setOnClickListener(this);
//		applyClassTypeLayout.setOnClassTypeSelectedListener(this);
//		classTypeLayout.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View paramView) {
//				if (isExtend) {
//
//					closeClassDetail();
//				}
//			}
//		});
	}

	@Override
	public void onClick(View v) {
		if (!onClickSingleView()) {
			return;
		}
		if (v.getId() == R.id.base_left_btn) {
			finish();
			return;
		}
		// if (EnrollResult.SUBJECT_NONE.getValue().equals(enrollState)) {
		Intent intent = null;
		switch (v.getId()) {
		// case R.id.apply_license_type_c1:
		// carStyle = listCarModelVOs.get(0);
		// changeLicenseTextColor(1);
		// break;
		// case R.id.apply_license_type_c2:
		// carStyle = listCarModelVOs.get(1);
		// changeLicenseTextColor(2);
		// break;
		// case R.id.enroll_school_rl:
		// intent = new Intent(this, EnrollSchoolActivity.class);
		// if (school != null)
		// intent.putExtra("school", school);
		// break;
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
		case R.id.enroll_coach_rl:
			// if (school == null) {
			// ZProgressHUD.getInstance(this).show();
			// ZProgressHUD.getInstance(this).dismissWithFailure("先选择驾校");
			// } else {
			// // intent = new Intent(this, EnrollCoachActivity.class);
			// // intent.putExtra("schoolId", school.getSchoolid());
			// // if (coach != null)
			// // intent.putExtra("coach", coach);
			// showPopupWindow(coachTv);
			// }
			break;
		// case R.id.enroll_class_tv:
		// if (school == null) {
		// ZProgressHUD.getInstance(this).show();
		// ZProgressHUD.getInstance(this).dismissWithFailure("先选择驾校");
		// } else {
		// intent = new Intent(this, EnrollClassActivity.class);
		// intent.putExtra("schoolId", school.getSchoolid());
		// if (classId != null)
		// intent.putExtra("class", classId);
		// }
		// break;
		case R.id.enroll_commit_btn:
			String checkResult = checkEnrollInfo();
			LogUtil.print("checkResult---->"+checkResult);
			if(null != checkResult){
				ZProgressHUD.getInstance(this).show();
				ZProgressHUD.getInstance(this)
						.dismissWithFailure(checkResult);
			}else{
				// 验证Y码
				if (null == etYCodeCard.getText().toString()
						|| TextUtils.isEmpty(etYCodeCard.getText().toString()
								.trim())) {
					
					enroll(null, enCoachId, enSchoolId, enclassTypeId, encarmodel);
				} else {
					obtainYCode();// 正式
				}
			}
			

			// Intent intent1 = new Intent(this, ConfirmOrderActivity.class);
			// intent1.putExtra("class", classe);
			// intent1.putExtra("schoolName", school.getName());
			// intent1.putExtra("phone", contactEt.getText().toString());
			// startActivity(intent1);
			// finish();

			 
			// // if (checkResult == null) {
			// // enroll(checkResult);
			// // } else {
			// enroll(checkResult);
			// // }
			// // 保存数据

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

			LogUtil.print("enrollll--->" + coachId + "schoolId;:>" + SchoolId
					+ "classType:>" + classTypeId + "CarModel-->" + carModel);

			paramMap.put("coachid", coachId);
			paramMap.put("schoolid", SchoolId);// school.getSchoolid()
			paramMap.put("classtypeid", classTypeId);// classId.getCalssid()
			paramMap.put("carmodel", carModel);// carStyle.toString()
			paramMap.put("idcardnumber", "");
			paramMap.put("address", "");
			paramMap.put("paytype", radioOnLine.isChecked() ? "2" : "1");// 1
																			// 线下支付
																			// 2.线上支付

			if (TextUtils.isEmpty(etYCodeCard.getText().toString())) {
				paramMap.put("fcode", "");
			} else {
				paramMap.put("fcode", etYCodeCard.getText().toString());
			}
			LogUtil.print("重复报名:--->	" + app.isEnrollAgain);
			if (app.isEnrollAgain) {
				paramMap.put("applyagain", "1");
			}

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
//			nameEt.setError("姓名不能为空");
			tvNameError.setVisibility(View.VISIBLE);
			return "姓名为空";
		}
		

		String phone = contactEt.getText().toString();
		if (TextUtils.isEmpty(phone)) {
//			contactEt.setError("手机号不能为空");
			return "手机号为空";
		} else {
			if (!CommonUtil.isMobile(phone)) {
//				contactEt.setError("手机号格式不正确");
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
		// else {
		// if (!CommonUtil.isRightName(name)) {
		// return "姓名不能包含特殊字符";
		// }
		// }

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
		// if(TextUtils.isEmpty(etIdCard.getText().toString())){
		// Toast("身份证号不能为空");
		// return false;
		// }else if(!CommonUtil.isIdCardOk(etIdCard.getText().toString())){
		// Toast("身份证号不正确");
		// return false;
		// }

		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		Toast("Activity--->"+data);
		if (data == null) {
			return;
		}
		switch (requestCode) {
		case 9://报名成功
			setResult(9,getIntent());
			finish();
			break;
		case R.id.enroll_school_rl:
			// 报名页面选择学校
			SchoolVO tempSchool = (SchoolVO) data
					.getSerializableExtra("school");
			if (school == null || !school.equals(tempSchool)) {
				// 更换驾校，并删除了相应的冲突信息
				school = tempSchool;
				// schoolTv.setText(school.getName());
				coach = null;
				coachTv.setText(R.string.apply_system_distribute);
				obtainEnrollClass();
				// classId = null;
				// classTv.setText("");
				// carStyle = null;
				// carStyleTv.setText("");
			}

			coach = (CoachVO) data.getSerializableExtra("coach");
			if (coach != null) {
				coachTv.setText(coach.getName());
			}
			break;
		// case R.id.enroll_carstyle_tv:
		// // 报名页面选择车型，在选择车型的页面进行了保存
		// carStyle = (CarModelVO) data.getSerializableExtra("carStyle");
		// carStyleTv.setText(carStyle.getCode());
		// break;
		case R.id.pop_window_two:
			// 报名页面选择教练
			coach = (CoachVO) data.getSerializableExtra("coach");
			coachTv.setText(coach.getName());
			break;
		// case R.id.enroll_class_tv:
		// // 报名页面选择班级
		// classId = (ClassVO) data.getSerializableExtra("class");
		// classTv.setText(classId.getClassname());
		// break;
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
					// setclassName(list);
//					applyClassTypeLayout.clearData();
//					applyClassTypeLayout.setData(list);
				}
				// {"type":1,"msg":"","data":"success","extra":{"__v":0,"paymoney":4680,"payendtime":"2016-02-04T15:46:52.002Z",
				// "creattime":"2016-02-01T15:46:52.002Z","userid":"56a74ca04cc338e971ca226a",
				// "_id":"56af7dec1b27e71413067afe","discountmoney":0,"couponcode":"","applyclasstypeinfo":
				// {"onsaleprice":4680,"price":4980,"name":"新春特惠班","id":"56a9ba41fe60f807363001c9"},
				// "applyschoolinfo":{"name":"一步互联网驾校","id":"562dcc3ccb90f25c3bde40da"},"paychannel":0,"userpaystate":0}}
				// LogUtil
			} else if (type.equals(enroll)) {
				if ("success".equals(dataString)) {
					// 更新本地数据
					SharedPreferencesUtil.putString(this,
							realName + app.userVO.getUserid(), nameEt.getText()
									.toString());
					SharedPreferencesUtil.putString(this,
							contact + app.userVO.getUserid(), contactEt
									.getText().toString());
					LogUtil.print("success--->" + jsonString);

					app.isEnrollAgain = true;
					if (radioOnLine.isChecked()) {// 线上支付
						jsonObject = new JSONObject(jsonString.toString());
						JSONObject extra = jsonObject.getJSONObject("extra");

						PayOrderVO pay = JSONUtil.toJavaBean(PayOrderVO.class,
								extra);
						Intent intent = new Intent(this,
								ConfirmOrderActivity.class);
						intent.putExtra("class", classe);
						intent.putExtra("schoolName", schoolName);
						intent.putExtra("phone", contactEt.getText().toString());
						intent.putExtra("bean", pay);
//						startActivity(intent);
						startActivityForResult(intent, 9);
					} else {// 线下支付
						Intent intent1 = new Intent(this,
								EnrollSuccessActivity.class);
//						startActivity(intent1);
						startActivityForResult(intent1, 9);
//						reLogin();
					}
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
							ApplyActivity.this, "您的Y码不正确，是否要继续报名？");
					dialog.setBtnText("继续报名", "修改Y码");
					dialog.show();
				}
				if (result != null && result.equals("1")) {
					enroll(null, enCoachId, enSchoolId, enclassTypeId,
							encarmodel);
				}
			}else if(type.equals("reLogin")){
				try {
//					Toast("登录成功");
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
	
	private void reLogin(){
		String lastLoginPhone = util.readParam(Config.LAST_LOGIN_ACCOUNT);
		String password = util.readParam(Config.LAST_LOGIN_PASSWORD);
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("mobile", lastLoginPhone);
		paramMap.put("usertype", "1");
		paramMap.put("password", util.MD5(password));
		HttpSendUtils.httpPostSend("reLogin", this, Config.IP
				+ "api/v1/userinfo/userlogin", paramMap);
	}

	// 设置默认驾校
	// private void setDefaultData(SchoolVO schoolVO) {
	// if (schoolVO != null) {
	// school = schoolVO;
	// schoolTv.setText(schoolVO.getName());
	// obtainEnrollClass();
	// }
	// }

	// private void setLienseType(List<CarModelVO> listCarModelVOs) {
	// licenseTypeC1.setText(listCarModelVOs.get(0).getCode() + "手动档");
	// licenseTypeC2.setText(listCarModelVOs.get(1).getCode() + "自动档");
	// }

	private RelativeLayout classDetailLayout;
	private int targetHeight;
	private RelativeLayout schoolRl;

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
		animator = ValueAnimator.ofInt(targetHeight, 0);
		animator.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animator) {
				classDetailLayout.getLayoutParams().height = (Integer) animator
						.getAnimatedValue();
				classDetailLayout.requestLayout();
			}
		});
		animator.setDuration(300);
		animator.addListener(new AnimatorListener() {
			@Override
			public void onAnimationStart(Animator arg0) {

			}

			@Override
			public void onAnimationRepeat(Animator arg0) {
			}

			@Override
			public void onAnimationEnd(Animator arg0) {
				isFirstOpen = false;
				isExtend = false;
			}

			@Override
			public void onAnimationCancel(Animator arg0) {
			}
		});
		animator.start();
	}

	private ValueAnimator animator;
	private RelativeLayout classTypeLayout;
	private RelativeLayout coachRl;

	// private EditText yCodeEt;

	private void setClassDetailAnimator() {
		animator = ValueAnimator.ofInt(0, targetHeight);
		animator.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animator) {
				classDetailLayout.getLayoutParams().height = (Integer) animator
						.getAnimatedValue();
				classDetailLayout.requestLayout();
			}
		});
		animator.addListener(new AnimatorListener() {
			@Override
			public void onAnimationStart(Animator arg0) {

			}

			@Override
			public void onAnimationRepeat(Animator arg0) {
			}

			@Override
			public void onAnimationEnd(Animator arg0) {
				isExtend = true;
			}

			@Override
			public void onAnimationCancel(Animator arg0) {
			}
		});
		animator.setDuration(300);
		animator.start();
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
		for (int i = 0; i < length; i++) {
			MultipleTextViewVO vo = multipleTextViewGroup.new MultipleTextViewVO();
			vo.setText(seleClassVO.getVipserverlist().get(i).getName());

			String color = seleClassVO.getVipserverlist().get(i).getCoclor();
			if (color != null && color.length() == 6) {
				vo.setColor(Color.parseColor("#" + color));
			} else {
				vo.setColor(Color.parseColor("#ff6633"));
			}
			multipleList.add(vo);
		}

		multipleTextViewGroup.setTextViews(multipleList,
				multipleTextViewGroupWidth);
	}

	@Override
	public void selectConfirm(boolean isConfirm, boolean isFreshAll) {

		if (isConfirm) {
			enroll(null, enCoachId, enSchoolId, enclassTypeId, encarmodel);
		} else {

		}
	}

}
