package com.sft.blackcatapp;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import cn.sft.infinitescrollviewpager.BitmapManager;

import com.jzjf.app.R;
import com.sft.common.Config;
import com.sft.event.ProductExchangeSuccessEvent;
import com.sft.qrcode.EncodingHandler;
import com.sft.util.LogUtil;
import com.sft.vo.ExchangeOrderItemVO;
import com.sft.vo.ProductVO;

import de.greenrobot.event.EventBus;

/**
 * 购买成功
 * 
 */
public class ProductOrderSuccessActivity extends BaseActivity {

	private Button returnBtn;

	private WebView webview;

	private ProgressBar progress;

	private WebSettings settings;

	private ProductVO productVO;

	private ImageView productPic;

	private TextView productNameTv;

	private TextView productPriceTv;

	private TextView timeTv;

	private ImageView productQr;

	private ImageView successPic;

	private TextView statusTv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_order_success);
		initView();
		initData();
		// setListener();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initView() {
		setTitleText("兑换优惠券");

		showTitlebarBtn(1);

		productPic = (ImageView) findViewById(R.id.product_order_pic);
		successPic = (ImageView) findViewById(R.id.product_order_success_pic);
		productNameTv = (TextView) findViewById(R.id.product_order_name_tv);
		productPriceTv = (TextView) findViewById(R.id.product_order_price_tv);
		timeTv = (TextView) findViewById(R.id.product_order_time_tv);

		productQr = (ImageView) findViewById(R.id.product_order_qr_iv);
		statusTv = (TextView) findViewById(R.id.product_order_status_tv);

	}

	private void initData() {
		ExchangeOrderItemVO exchangeOrderItemVO = (ExchangeOrderItemVO) getIntent()
				.getSerializableExtra("exchangeOrderItemVO");
		productVO = (ProductVO) getIntent().getSerializableExtra("productVO");

		LinearLayout.LayoutParams headParam = (LinearLayout.LayoutParams) productPic
				.getLayoutParams();
		if (exchangeOrderItemVO != null) {
			//
			successPic.setVisibility(View.GONE);
			productNameTv.setText(exchangeOrderItemVO.productname);
			LogUtil.print(exchangeOrderItemVO.productprice*exchangeOrderItemVO.count+"---=="+exchangeOrderItemVO.count+"--"+exchangeOrderItemVO.productprice);
			productPriceTv.setText(exchangeOrderItemVO.productprice*exchangeOrderItemVO.count+ "");
			timeTv.setText("兑换时间：" + exchangeOrderItemVO.createtime);
			if (TextUtils.isEmpty(exchangeOrderItemVO.productimg)) {
				productPic.setBackgroundResource(R.drawable.defaultimage);
			} else {

				BitmapManager.INSTANCE.loadBitmap2(
						exchangeOrderItemVO.productimg, productPic,
						headParam.width, headParam.height);
			}

			createQr(exchangeOrderItemVO.orderscanaduiturl, productQr);
			if ("5".equals(exchangeOrderItemVO.orderstate)) {
				statusTv.setText("已领取");
			} else {
				statusTv.setText("未领取");
			}
		} else {
			if (productVO != null) {
				int price = (int) getIntent().getDoubleExtra("money",
						Double.parseDouble(productVO.getProductprice()));
				productNameTv.setText(productVO.getProductname());
				productPriceTv.setText(price+"");
				SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
				timeTv.setText("兑换时间：" + format.format(new Date()));

				// exchangeAddrTv.setText(productVO.getAddress());
				if (TextUtils.isEmpty(productVO.getProductimg())) {
					productPic.setBackgroundResource(R.drawable.shop_pic);
				} else {

					BitmapManager.INSTANCE.loadBitmap2(
							productVO.getProductimg(), productPic,
							headParam.width, headParam.height);
				}

				createQr(getIntent().getStringExtra("orderscanaduiturl"),
						productQr);
				statusTv.setText("未领取");
			}
		}
	}

	private void setListener() {
		// returnBtn.setOnClickListener(this);
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
			// case R.id.order_success_btn:
			// finish();
			// break;
			Intent intent1 = new Intent(this, MallActivity.class);
			intent1.putExtra("moneytype",
					Config.MoneyType.COIN_CERTIFICATE.getValue());
			startActivity(intent1);
			break;
		}
	}

	private void createQr(String contentString, ImageView img) {

		// 生成二维码
		try {
			LogUtil.print("contentString---" + contentString);
			if (contentString != null && contentString.trim().length() > 0) {
				// 根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（350*350）
				Bitmap qrCodeBitmap = EncodingHandler.createQRCode(
						contentString, 500);
				img.setImageBitmap(qrCodeBitmap);
			} else {
				Toast.makeText(this, "签到二维码生成失败", Toast.LENGTH_SHORT).show();
				// toast.setText("生成二维码失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void finish() {
		super.finish();
		ProductExchangeSuccessEvent event = new ProductExchangeSuccessEvent();
		event.money = getIntent().getDoubleExtra("money", 0.0);
		EventBus.getDefault().post(event);
	}
	// @Override
	// public void finish() {
	// sendBroadcast(new Intent(ProductOrderActivity.class.getName())
	// .putExtra("finish", true));
	// sendBroadcast(new Intent(ProductDetailActivity.class.getName())
	// .putExtra("finish", true));
	// super.finish();
	// }
}
