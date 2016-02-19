package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.List;

import me.maxwin.view.XListView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.sft.adapter.SubjectListAdapter;
import com.sft.vo.SubjectVO;

/**
 * 选择科目界面
 * 
 * @author Administrator
 * 
 */
public class EnrollSubjectActivity extends BaseActivity implements
		OnItemClickListener {

	// 科目列表
	private XListView subjectList;
	//
	private SubjectListAdapter adapter;

	private String[] subjects = new String[] { "科目一", "科目二", "科目三", "科目四" };

	private SubjectVO selectSubject;
	private List<SubjectVO> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_enroll_class);
		initView();
		// obtainEnrollClass();
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

		list = new ArrayList<SubjectVO>();
		for (int i = 0; i < subjects.length; i++) {
			SubjectVO subjectVO = new SubjectVO();
			subjectVO.setSubjectId(i + 1);
			subjectVO.setName(subjects[i]);
			list.add(subjectVO);
		}
		subjectList = (XListView) findViewById(R.id.enroll_class_listview);
		subjectList.setPullRefreshEnable(false);
		subjectList.setPullLoadEnable(false);
		adapter = new SubjectListAdapter(this, list);
		subjectList.setAdapter(adapter);
		subjectList.setOnItemClickListener(this);
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

			Intent intent = new Intent();
			intent.putExtra("subject", selectSubject);
			setResult(RESULT_OK, intent);
			finish();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		selectSubject = list.get(position - 1);
		adapter.setSelected(position - 1);
		adapter.notifyDataSetChanged();

	}

}
