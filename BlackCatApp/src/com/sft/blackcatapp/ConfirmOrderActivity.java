package com.sft.blackcatapp;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.sft.alipay.PayDemoActivity;
import com.sft.alipay.PayResult;
import com.sft.alipay.PayUtils;
import com.sft.util.LogUtil;
import com.sft.vo.ClassVO;

public class ConfirmOrderActivity extends BaseActivity implements
		OnClickListener {

	private TextView tv_coupon_show, tvGoodName, tvGoodPrice, tvYcode;

	private TextView tvShoudPay, tvReallyPay;

	private ClassVO classe;

	/**
	 * 填写的 phone 号码
	 */
	private String phone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addView(R.layout.activity_confirm_order);
		setBg(getResources().getColor(R.color.white));
		
		initView();
		Listenner();
	}
	
	String schoolName;

	private void initView() {
		classe = (ClassVO) getIntent().getSerializableExtra("class");
		schoolName = getIntent().getStringExtra("schoolName");
		phone = getIntent().getStringExtra("phone");
//		phone = "13120064118";
		tv_coupon_show = (TextView) findViewById(R.id.tv_coupon_show);
		tvGoodName = (TextView) findViewById(R.id.tv_goods_show);
		tvGoodPrice = (TextView) findViewById(R.id.tv_goods_money_show);
		// tvDiscode = (TextView) findViewById(R.id.tv_goods_show);
		tvYcode = (TextView) findViewById(R.id.tv_discount_show);
		tvShoudPay = (TextView) findViewById(R.id.textView_money_should);
		tvReallyPay = (TextView) findViewById(R.id.textView_money_cale);

		tvReallyPay.setText(classe.getOnsaleprice() + "元");
		tvShoudPay.setText(classe.getPrice() + "元(" + schoolName + ")");

	}

	private void Listenner() {
		tv_coupon_show.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_coupon_show:
			Intent intent = new Intent(ConfirmOrderActivity.this,
					CheckDiscodeAct.class);
			intent.putExtra("phone", phone);
			startActivityForResult(intent, 3);
			break;
		case R.id.base_left_btn:
			finish();
			break;
		case R.id.act_pay_now://立即支付
			PayUtils pay = new PayUtils();
			pay.setTradeNo("123456");
//			classe.getClassname()
			new PayUtils().pay(v,this,mHandler,payName,payDetail,money);
			break;
		default:
			break;
		}
	}
	
	private static final int SDK_PAY_FLAG = 1;
	private static final int SDK_CHECK_FLAG = 2;

	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			LogUtil.print("msg;::::>>>"+msg.obj);
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);
				/**
				 * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
				 * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
				 * docType=1) 建议商户依赖异步通知
				 */
				String resultInfo = payResult.getResult();// 同步返回需要验证的信息

				String resultStatus = payResult.getResultStatus();
				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(ConfirmOrderActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
				} else {
					// 判断resultStatus 为非"9000"则代表可能支付失败
					// "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(ConfirmOrderActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(ConfirmOrderActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				Toast.makeText(ConfirmOrderActivity.this, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
				break;
			}
			default:
				break;
			}
		};
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 3 && data != null) {
			data.getStringExtra("name");
//			data.getStringExtra("money");
			codeId = data.getStringExtra("id");
			tv_coupon_show.setText(data.getStringExtra("name"));
			tvYcode.setText("￥-"+data.getStringExtra("money"));
			String m = data.getStringExtra("money");
			tvShoudPay.setText(getPrice(classe.getPrice(),m) + "元(" + schoolName + ")");
		}

	}
	
	private int getPrice(String a1,String a2){
		int a = Integer.parseInt(a1)-Integer.parseInt(a2);
		//支付
		money = a+"";
		return a;
	}
	
	private String codeId = "";
	private String money = "";
	private String payName;
	private String payDetail;

}
