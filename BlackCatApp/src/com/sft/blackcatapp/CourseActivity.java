package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.sft.baseactivity.util.HttpSendUtils;

import com.sft.adapter.CourseListAdapter;
import com.jzjf.app.R;
import com.sft.common.Config;
import com.sft.util.JSONUtil;
import com.sft.vo.VideoVO;

/**
 * 课件
 * 
 * @author Administrator
 * 
 */
public class CourseActivity extends BaseActivity implements OnItemClickListener {

	private static final String course = "obtainCourse";
	// 课件列表
	private ListView courseListView;

	private List<VideoVO> courseList = new ArrayList<VideoVO>();

	private CourseListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_course);
		initView();
		obtainCourse();
	}

	@Override
	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	}

	private void obtainCourse() {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("subjectid", getIntent().getStringExtra("subjectid"));
		paramsMap.put("seqindex", courseList.size() + "");
		paramsMap.put("count", "10");

		HttpSendUtils.httpGetSend(course, this, Config.IP
				+ "api/v1/getcourseware", paramsMap);
	}

	private void initView() {
		courseListView = (ListView) findViewById(R.id.course_listview);
		setTitleText(getIntent().getStringExtra("title"));

		courseListView.setOnItemClickListener(this);
	}

	private void refreshData(List<VideoVO> list) {
		if (adapter == null) {
			adapter = new CourseListAdapter(this, list, screenWidth);
			courseListView.setAdapter(adapter);
		} else {
			adapter.setData(list);
		}
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
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(this, AudioPlayHtmlActivity.class);
		intent.putExtra("video", courseList.get(position));
		startActivity(intent);
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		try {
			if (type.equals(course)) {
				if (dataArray != null) {
					if (courseList == null) {
						courseList = new ArrayList<VideoVO>();
					}
					int length = dataArray.length();
					for (int i = 0; i < length; i++) {
						VideoVO videoVO = (VideoVO) JSONUtil.toJavaBean(
								VideoVO.class, dataArray.getJSONObject(i));
						courseList.add(videoVO);
					}
					refreshData(courseList);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
}
