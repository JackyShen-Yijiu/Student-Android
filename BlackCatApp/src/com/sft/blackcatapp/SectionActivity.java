package com.sft.blackcatapp;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.jzjf.app.R;
import com.sft.adapter.SectionAdapter;
import com.sft.util.LogUtil;
import com.sft.util.Util;
<<<<<<< HEAD
import com.sft.vo.questionbank.TitleVO;
=======
import com.sft.vo.questionbank.Chapter;
import com.sft.fragment.ExciseFragment;
>>>>>>> 972630a2f1de6bece84f03dc60fff75e43b1756b

/**
 * 章节选择
 * 
 * @author Administrator
 * 
 */
public class SectionActivity extends BaseActivity implements
		OnItemClickListener {

	private ListView sectionlist;
	private List<TitleVO> data1;
	private SectionAdapter adapter;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.section_select);
		setTitleText(R.string.section);
		initView();
	}

	private void initView() {
		sectionlist = (ListView) findViewById(R.id.section_list);
		sectionlist.setOnItemClickListener(this);
		new Thread(new Runnable() {

			@Override
			public void run() {
				data1 = Util.getSubjectOneChapter();
				LogUtil.print("web_note--" + data1.size());
				// 耗时的方法
				handler.sendEmptyMessage(1);
				// 执行耗时的方法之后发送消给handler
			}
		}).start();
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// handler接收到消息后就会执行此方法
			switch (msg.what) {
			case 1:
				adapter = new SectionAdapter(SectionActivity.this, data1);
				sectionlist.setAdapter(adapter);
				adapter.setData(data1);
				adapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.base_left_btn:
			finish();
			break;
<<<<<<< HEAD
		// startActivity(new Intent(SectionActivity.this,
		// ExerciseOrderAct.class));
=======
>>>>>>> 972630a2f1de6bece84f03dc60fff75e43b1756b
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		TitleVO titleVO = (TitleVO) parent.getAdapter().getItem(position);
		Intent intent = new Intent(SectionActivity.this, ExerciseOrderAct.class);
		intent.putExtra("id", titleVO);
		startActivity(intent);
	}

}
