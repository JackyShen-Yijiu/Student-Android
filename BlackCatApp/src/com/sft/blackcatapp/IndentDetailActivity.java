package com.sft.blackcatapp;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import cn.sft.baseactivity.util.HttpSendUtils;

import com.sft.common.Config;
import com.sft.vo.PayOrderVO;

public class IndentDetailActivity extends BaseActivity implements
		OnClickListener {
	private static final String notPay = "notPay";
	private TextView intent_code;
	private TextView indent_school;
	private TextView indent_class;
	private TextView indent_time;
	private TextView gross_money;
	private TextView favorable_money;
	private TextView actual_money;
	private TextView last_money;
	private Button button_commit;
	private PayOrderVO payOrderVO;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_indent);
		initView();
		initData();
		obtainEnrollClass();
	}

	private void initData() {

		intent_code.setText(app.userVO.getPayOrderVO()._id);

		indent_school.setText(app.userVO.getPayOrderVO().applyschoolinfo
				.getName());
		indent_class
				.setText(app.userVO.getPayOrderVO().applyclasstypeinfo.name);
		indent_time.setText(app.userVO.getPayOrderVO().creattime);

		gross_money.setText("￥"
				+ app.userVO.getPayOrderVO().applyclasstypeinfo.price);
		favorable_money
				.setText("-￥" + app.userVO.getPayOrderVO().discountmoney);
		actual_money.setText("￥" + app.userVO.getPayOrderVO().paymoney);
		last_money.setText("需支付  " + app.userVO.getPayOrderVO().paymoney + "元");
	}

	private void obtainEnrollClass() {
		HttpSendUtils.httpGetSend(notPay, this, Config.IP
				+ "api/v1/userinfo/getmypayorder/");
	}

	private void initView() {
		setTitleText(R.string.indent_detail);

		intent_code = (TextView) findViewById(R.id.tv_intent_code);
		indent_school = (TextView) findViewById(R.id.indent_school);
		indent_class = (TextView) findViewById(R.id.indent_class);
		indent_time = (TextView) findViewById(R.id.indent_time);

		gross_money = (TextView) findViewById(R.id.gross_money);
		favorable_money = (TextView) findViewById(R.id.favorable_money);
		actual_money = (TextView) findViewById(R.id.actual_money);
		last_money = (TextView) findViewById(R.id.last_money);

		button_commit = (Button) findViewById(R.id.button_commit);
		button_commit.setOnClickListener(this);
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
		case R.id.button_commit:
			break;
		}
	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		// TODO Auto-generated method stub
		if (type.equals(notPay)) {
			if (data != null) {
				// try {
				// payOrderVO = JSONUtil.toJavaBean(PayOrderVO.class, data);
				// } catch (Exception e) {
				// e.printStackTrace();
				// }
				obtainEnrollClass();
				initData();
			}
		}
		return true;
	}
}
