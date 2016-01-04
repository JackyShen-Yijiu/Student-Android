package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.List;

import com.sft.adapter.ClasslListAdapter;
import com.sft.blackcatapp.R;
import com.sft.common.Config;
import com.sft.util.JSONUtil;
import com.sft.util.Util;
import com.sft.vo.ClassVO;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import cn.sft.baseactivity.util.HttpSendUtils;
import me.maxwin.view.XListView;

/**
 * 选择班级界面
 * 
 * @author Administrator
 * 
 */
public class EnrollClassActivity extends BaseActivity implements OnItemClickListener {

	// 班级列表
	private XListView classList;
	//
	private ClasslListAdapter adapter;

	private static final String classInfo = "classInfo";

	private ClassVO selectClass;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_enroll_class);
		initView();
		obtainEnrollClass();
	};

	@Override
	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	}

	private void initView() {
		showTitlebarText(BaseActivity.SHOW_RIGHT_TEXT);
		setText(0, R.string.finish);
		setTitleText(R.string.enroll_class);

		classList = (XListView) findViewById(R.id.enroll_class_listview);
		classList.setPullRefreshEnable(false);
		classList.setPullLoadEnable(false);

		selectClass = (ClassVO) getIntent().getSerializableExtra("class");

		classList.setOnItemClickListener(this);
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
			if (adapter != null && adapter.getIndex() >= 0) {
				ClassVO classVO = adapter.getItem(adapter.getIndex());
				if (app.selectEnrollClass == null) {
					app.selectEnrollClass = classVO;
					Util.updateEnrollClass(this, classVO);
				}
				if (!app.selectEnrollClass.getCalssid().equals(classVO.getCalssid())) {
					app.selectEnrollClass = classVO;
					Util.updateEnrollClass(this, classVO);
				}
				Intent intent = new Intent();
				intent.putExtra("class", classVO);
				setResult(RESULT_OK, intent);
			}
			finish();
			break;
		}
	}

	private void obtainEnrollClass() {
		String schoolId = getIntent().getStringExtra("schoolId");
		HttpSendUtils.httpGetSend(classInfo, this, Config.IP + "api/v1/driveschool/schoolclasstype/" + schoolId);
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}

		try {
			if (type.equals(classInfo)) {
				if (dataArray != null) {
					int selectIndex = -1;
					int length = dataArray.length();
					List<ClassVO> list = new ArrayList<ClassVO>();
					for (int i = 0; i < length; i++) {
						ClassVO classVO = (ClassVO) JSONUtil.toJavaBean(ClassVO.class, dataArray.getJSONObject(i));
						if (selectClass != null) {
							if (classVO.getCalssid().equals(selectClass.getCalssid())) {
								selectIndex = i;
							}
						}
						list.add(classVO);
					}
					adapter = new ClasslListAdapter(this, list);
					adapter.setSelected(selectIndex);
					classList.setAdapter(adapter);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(this, ClassDetailActivity.class);
		intent.putExtra("class", adapter.getItem(position - 1));
		intent.putExtra("position", position - 1);
		startActivityForResult(intent, classList.getId());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			int position = data.getIntExtra("position", 0);
			adapter.setSelected(position);
			adapter.notifyDataSetChanged();
		}
	}
}
