package com.sft.fragment;

import java.util.ArrayList;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.Toast;
import cn.sft.baseactivity.util.Util;
import cn.sft.listener.ICallBack;

import com.sft.common.BlackCatApplication;
import com.sft.viewutil.ZProgressHUD;
import com.sft.vo.CarModelVO;
import com.sft.vo.ClassVO;
import com.sft.vo.CoachVO;
import com.sft.vo.QuestionVO;
import com.sft.vo.SchoolVO;
import com.sft.vo.UserVO;
import com.umeng.analytics.MobclickAgent;

public class BaseFragment extends Fragment implements ICallBack {

	protected BlackCatApplication app;
	protected static Util util;

	protected String result = "";
	protected String msg = "";
	protected JSONObject data = null;
	protected JSONArray dataArray = null;
	protected String dataString = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (util == null) {
			util = new Util(getActivity());
		}
		if (savedInstanceState != null) {
			if (app == null) {
				app = BlackCatApplication.getInstance();
			}
			app.userVO = (UserVO) savedInstanceState.getSerializable("userVO");
			app.questionVO = (QuestionVO) savedInstanceState
					.getSerializable("questionVO");
			app.subjectTwoContent = savedInstanceState
					.getStringArrayList("subjectTwoContent");
			app.subjectThreeContent = savedInstanceState
					.getStringArrayList("subjectThreeContent");
			app.selectEnrollSchool = (SchoolVO) savedInstanceState
					.getSerializable("selectEnrollSchool");
			app.selectEnrollCoach = (CoachVO) savedInstanceState
					.getSerializable("selectEnrollCoach");
			app.selectEnrollCarStyle = (CarModelVO) savedInstanceState
					.getSerializable("selectEnrollCarStyle");
			app.selectEnrollClass = (ClassVO) savedInstanceState
					.getSerializable("selectEnrollClass");
			app.userVO = (UserVO) savedInstanceState.getSerializable("userVO");
			app.qiniuToken = savedInstanceState.getString("qiniuToken");

			Bundle favouriteCoach = savedInstanceState
					.getBundle("favouriteCoach");
			if (favouriteCoach != null) {
				Set<String> favouriteCoachKey = favouriteCoach.keySet();
				if (app.favouriteCoach == null)
					app.favouriteCoach = new ArrayList<CoachVO>();
				for (String key : favouriteCoachKey) {
					app.favouriteCoach.add((CoachVO) favouriteCoach.get(key));
				}
			}

			Bundle favouriteSchool = savedInstanceState
					.getBundle("favouriteSchool");
			if (favouriteSchool != null) {
				Set<String> favouriteSchoolKey = favouriteSchool.keySet();
				if (app.favouriteSchool == null)
					app.favouriteSchool = new ArrayList<SchoolVO>();
				for (String key : favouriteSchoolKey) {
					app.favouriteSchool
							.add((SchoolVO) favouriteSchool.get(key));
				}
			}

			// Intent intent = new Intent(getActivity(), LoginActivity.class);
			// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// startActivity(intent);
			// finish();
		}
		if (app == null) {
			app = BlackCatApplication.getInstance();
		}
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		try {
			util.print("json=" + jsonString + " type= " + type);

			JSONObject jsonObject = new JSONObject(jsonString.toString());
			result = jsonObject.getString("type");
			msg = jsonObject.getString("msg");
			
			try {
				Object o = jsonObject.get("data");
				if(o instanceof JSONObject){
					data = jsonObject.getJSONObject("data");
				}else if( o instanceof JSONArray){
					dataArray = jsonObject.getJSONArray("data");
				}else if(o instanceof String){
					dataString = jsonObject.getString("data");
				}
			} catch (Exception e2) {
				try {
					dataArray = jsonObject.getJSONArray("data");
				} catch (Exception e3) {
					dataString = jsonObject.getString("data");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!TextUtils.isEmpty(msg)) {
			ZProgressHUD.getInstance(getActivity()).show();
			ZProgressHUD.getInstance(getActivity()).dismissWithFailure(msg,
					2000);
			return true;
		}

		return false;
	}

	@Override
	public void doException(String type, Exception e, int code) {
		util.print("type= " + type + " 异常  code=" + code);
		if (code == 0) {
			ZProgressHUD.getInstance(getActivity()).show();
			ZProgressHUD.getInstance(getActivity()).dismissWithFailure("网络异常",
					2000);
		} else if (code == 500) {
			ZProgressHUD.getInstance(getActivity()).show();
			ZProgressHUD.getInstance(getActivity()).dismissWithFailure("服务器异常",
					2000);
		} else {
			ZProgressHUD.getInstance(getActivity()).show();
			ZProgressHUD.getInstance(getActivity()).dismissWithFailure(
					"type= " + type + " 异常  code=" + code, 2000);
		}
		if (e != null)
			e.printStackTrace();
	}

	@Override
	public void doTimeOut(String type) {
		util.print("type=" + type + " 超时");
		ZProgressHUD.getInstance(getActivity()).show();
		ZProgressHUD.getInstance(getActivity()).dismissWithFailure(
				"type=" + type + " 超时");
	}

	@Override
	public void onResume() {
		super.onResume();
		// 统计页面
		MobclickAgent.onPageStart(this.getClass().getSimpleName());
	}

	@Override
	public void onPause() {
		super.onPause();
		// 统计页面
		MobclickAgent.onPageStart(this.getClass().getSimpleName());
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		try {
			outState.putSerializable("userVO", app.userVO);
			outState.putSerializable("questionVO", app.questionVO);

			int length = app.favouriteCoach.size();
			Bundle bundle = new Bundle();
			for (int i = 0; i < length; i++) {
				bundle.putSerializable("favouriteCoach" + i,
						app.favouriteCoach.get(i));
			}
			outState.putBundle("favouriteCoach", bundle);

			bundle = new Bundle();
			length = app.favouriteSchool.size();
			for (int i = 0; i < length; i++) {
				bundle.putSerializable("favouriteSchool" + i,
						app.favouriteSchool.get(i));
			}
			outState.putBundle("favouriteSchool", bundle);

			outState.putStringArrayList("subjectTwoContent",
					(ArrayList<String>) app.subjectTwoContent);
			outState.putStringArrayList("subjectThreeContent",
					(ArrayList<String>) app.subjectThreeContent);
			outState.putSerializable("selectEnrollSchool",
					app.selectEnrollSchool);
			outState.putSerializable("selectEnrollCoach", app.selectEnrollCoach);
			outState.putSerializable("selectEnrollCarStyle",
					app.selectEnrollCarStyle);
			outState.putSerializable("selectEnrollClass", app.selectEnrollClass);
			outState.putString("qiniuToken", app.qiniuToken);
		} catch (Exception e) {

		}
		super.onSaveInstanceState(outState);
	}

	public void Toast(String str){
		Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
	}
	
}
