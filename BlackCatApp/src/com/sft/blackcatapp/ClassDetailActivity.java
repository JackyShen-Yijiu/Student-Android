package com.sft.blackcatapp;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.sft.util.UTC2LOC;
import com.sft.viewutil.MultipleTextViewGroup;
import com.sft.viewutil.MultipleTextViewGroup.MultipleTextViewVO;
import com.sft.vo.ClassVO;

/**
 * 报名班级详情页
 * 
 * @author Administrator
 * 
 */
public class ClassDetailActivity extends BaseActivity {

	private TextView schoolNameTv;
	private TextView schoolAddressTv;

	private TextView styleTv, dataTv, weekTv, brandTv, priceTv, countTv;

	private TextView introductionTv;

	private ClassVO classVO;

	private MultipleTextViewGroup multipleTextViewGroup;

	private int multipleTextViewGroupWidth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_class_detail);
		initView();
	}

	@Override
	protected void onResume() {
		register(getClass().getName());
		super.onResume();
	}

	private void initView() {
		setTitleText(R.string.class_detail);

		schoolNameTv = (TextView) findViewById(R.id.class_detail_school_name_tv);
		schoolAddressTv = (TextView) findViewById(R.id.class_detail_school_address_tv);
		styleTv = (TextView) findViewById(R.id.class_detail_style_tv);
		dataTv = (TextView) findViewById(R.id.class_detail_date_tv);
		weekTv = (TextView) findViewById(R.id.class_detail_week_tv);
		brandTv = (TextView) findViewById(R.id.class_detail_brand_tv);
		priceTv = (TextView) findViewById(R.id.class_detail_price_tv);
		countTv = (TextView) findViewById(R.id.class_detail_count_tv);
		introductionTv = (TextView) findViewById(R.id.class_detail_introduction_content_tv);
		multipleTextViewGroup = (MultipleTextViewGroup) findViewById(R.id.class_detail_multiple_tv);

		final ViewTreeObserver vto = multipleTextViewGroup
				.getViewTreeObserver();
		vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			public boolean onPreDraw() {
				if (multipleTextViewGroupWidth <= 0) {
					multipleTextViewGroupWidth = multipleTextViewGroup
							.getMeasuredWidth();
					initData();
				}
				return true;
			}
		});
	}

	private void initData() {
		classVO = (ClassVO) getIntent().getSerializableExtra("class");

		schoolNameTv.setText(classVO.getSchoolinfo().getName());
		schoolAddressTv.setText(classVO.getSchoolinfo().getAddress());
		styleTv.setText(getString(R.string.license)
				+ classVO.getCarmodel().getCode());
		weekTv.setText(getString(R.string.course_date)
				+ classVO.getClasschedule());
		brandTv.setText(getString(R.string.car_brand) + classVO.getCartype());
		priceTv.setText(classVO.getPrice());
		countTv.setText(getString(R.string.enroll_count)
				+ classVO.getApplycount());

		String beginDate = classVO.getBegindate();
		beginDate = UTC2LOC.instance.getDate(beginDate, "yyyy.MM.dd");
		String endDate = classVO.getBegindate();
		endDate = UTC2LOC.instance.getDate(endDate, "yyyy.MM.dd");
		dataTv.setText(getString(R.string.active_date) + beginDate + "-"
				+ endDate);

		introductionTv.setText(classVO.getClassdesc());

		List<MultipleTextViewVO> multipleList = new ArrayList<MultipleTextViewGroup.MultipleTextViewVO>();
		int length = classVO.getVipserverlist().size();
		for (int i = 0; i < length; i++) {
			MultipleTextViewVO vo = multipleTextViewGroup.new MultipleTextViewVO();
			vo.setText(classVO.getVipserverlist().get(i).getName());

			String color = classVO.getVipserverlist().get(i).getCoclor();
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
	public void onClick(View v) {
		if (!onClickSingleView()) {
			return;
		}
		switch (v.getId()) {
		case R.id.base_left_btn:
			close();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			close();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void close() {
		setResult(RESULT_OK, getIntent());
		finish();
	}
}
