package com.sft.blackcatapp;

<<<<<<< HEAD
import java.util.List;

import android.content.Context;
=======
import android.content.Intent;
>>>>>>> beef03890c833c138f0c77409395a081484e9b89
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;

import com.jzjf.app.R;
<<<<<<< HEAD
import com.sft.adapter.SectionAdapter;
import com.sft.util.LogUtil;
import com.sft.util.Util;
import com.sft.vo.questionbank.Chapter;
=======
import com.sft.fragment.ExciseFragment;
>>>>>>> beef03890c833c138f0c77409395a081484e9b89

/**
 * 章节选择
 * 
 * @author Administrator
 * 
 */
public class SectionActivity extends BaseActivity {

	private ListView sectionlist;
	private List<Chapter> data1;
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
				adapter = new SectionAdapter(context, data1);
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
		case R.id.section_test:
		case R.id.section_test1:
		case R.id.section_test2:
		case R.id.section_test4:
			startActivity(new Intent(SectionActivity.this,ExerciseOrderAct.class));
			break;
		}
	}

}
