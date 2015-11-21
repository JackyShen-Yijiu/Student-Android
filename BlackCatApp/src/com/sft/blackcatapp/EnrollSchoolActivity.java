package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sft.adapter.SchoolListAdapter;
import com.sft.common.Config;
import com.sft.common.Config.EnrollResult;
import com.sft.dialog.EnrollSelectConfilctDialog;
import com.sft.dialog.EnrollSelectConfilctDialog.OnSelectConfirmListener;
import com.sft.util.JSONUtil;
import com.sft.util.Util;
import com.sft.vo.SchoolVO;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.infinitescrollviewpager.MyHandler;
import me.maxwin.view.XListView;

/**
 * 选择驾校界面
 * 
 * @author Administrator
 * 
 */
public class EnrollSchoolActivity extends BaseActivity implements OnItemClickListener, OnSelectConfirmListener {

	private final static String nearBySchool = "nearBySchool";
	// 学校列表
	private XListView schoolList;

	private SchoolVO selectSchool;
	//
	private SchoolListAdapter adapter;
	// 当前选择的学校
	private SchoolVO school;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_enroll_school);
		initView();
		setListener();
		obtainNearBySchool();
	}

	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	};

	private void initView() {
		setTitleText(R.string.select_school);

		schoolList = (XListView) findViewById(R.id.enroll_select_school_listview);
		schoolList.setPullRefreshEnable(false);
		schoolList.setPullLoadEnable(false);

		selectSchool = (SchoolVO) getIntent().getSerializableExtra("school");

		if (app.userVO != null && app.userVO.getApplystate().equals(EnrollResult.SUBJECT_NONE.getValue())) {
			showTitlebarText(BaseActivity.SHOW_RIGHT_TEXT);
			setText(0, R.string.finish);
		}

	}

	private void setData(List<SchoolVO> school, int selectIndex) {
		if (selectIndex >= 0) {
			// 将已选择的驾校放在第一位
			school.add(0, school.get(selectIndex));
			school.remove(selectIndex + 1);
		}
		adapter = new SchoolListAdapter(this, school);
		if (selectIndex >= 0) {
			adapter.setSelected(0);
		}
		schoolList.setAdapter(adapter);
	}

	private void obtainNearBySchool() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("latitude", app.latitude);
		paramMap.put("longitude", app.longtitude);
		paramMap.put("radius", "100000");
		HttpSendUtils.httpGetSend(nearBySchool, this, Config.IP + "api/v1/driveschool/nearbydriveschool", paramMap);
	}

	private void setListener() {
		schoolList.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (!onClickSingleView()) {
			return;
		}
		switch (v.getId()) {
		case R.id.base_left_btn:
			finish();
			break;
		case R.id.base_right_tv:
			if (adapter == null || adapter.getIndex() < 0) {
				finish();
				break;
			}

			school = adapter.getItem(adapter.getIndex());
			String checkResult = Util.isConfilctEnroll(school);
			if (checkResult == null) {
				setResult(v.getId(), new Intent().putExtra("school", school));
				finish();
			} else if (checkResult.length() == 0) {
				app.selectEnrollSchool = school;
				Util.updateEnrollSchool(this, school, false);
				setResult(v.getId(), new Intent().putExtra("school", school));
				finish();
			} else {
				// 提示
				EnrollSelectConfilctDialog dialog = new EnrollSelectConfilctDialog(this, checkResult);
				dialog.show();
			}

			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(this, SchoolDetailActivity.class);
		SchoolVO schoolVO = (SchoolVO) adapter.getItem(position - 1);
		intent.putExtra("school", schoolVO);
		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, final int resultCode, final Intent data) {
		if (data != null) {
			if (resultCode == R.id.base_left_btn) {
				SchoolVO school = (SchoolVO) data.getSerializableExtra("school");
				if (app.userVO != null && app.userVO.getApplystate().equals(EnrollResult.SUBJECT_NONE.getValue())
						&& school != null) {
					int position = adapter.getData().indexOf(school);
					adapter.setSelected(position);
					adapter.notifyDataSetChanged();
				}
				return;
			}
			new MyHandler(200) {
				@Override
				public void run() {
					setResult(resultCode, data);
					finish();
				}
			};
		}
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}

		if (type.equals(nearBySchool)) {
			if (dataArray != null) {
				try {
					int selectIndex = -1;
					int length = dataArray.length();
					List<SchoolVO> schoolList = new ArrayList<SchoolVO>();
					for (int i = 0; i < length; i++) {
						SchoolVO schoolVO;
						schoolVO = (SchoolVO) JSONUtil.toJavaBean(SchoolVO.class, dataArray.getJSONObject(i));
						if (selectSchool != null) {
							if (selectSchool.getSchoolid().equals(schoolVO.getSchoolid())) {
								selectIndex = i;
							}
						}
						schoolList.add(schoolVO);
					}
					setData(schoolList, selectIndex);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	@Override
	public void selectConfirm(boolean isConfirm, boolean isFreshAll) {
		if (isConfirm) {
			app.selectEnrollSchool = school;
			Util.updateEnrollSchool(this, school, isFreshAll);
			if (isFreshAll) {
				app.selectEnrollCoach = Util.getEnrollUserSelectedCoach(this);
				app.selectEnrollCarStyle = Util.getEnrollUserSelectedCarStyle(this);
				app.selectEnrollClass = Util.getEnrollUserSelectedClass(this);
			}
			setResult(R.id.base_right_tv, new Intent().putExtra("school", school));
			finish();
		}
	}

}
