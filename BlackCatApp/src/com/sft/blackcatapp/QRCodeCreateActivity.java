package com.sft.blackcatapp;

import java.util.Date;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.jzjf.app.R;
import com.sft.qrcode.EncodingHandler;
import com.sft.util.JSONUtil;
import com.sft.util.LogUtil;
import com.sft.vo.MyAppointmentVO;
import com.sft.vo.QRCodeCreateVO;

public class QRCodeCreateActivity extends BaseActivity {

	private ImageView qRCodeIv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.activity_qr_code);
		initView();
		initData();
	}

	private void initView() {
		setTitleText(R.string.sign_in);

		qRCodeIv = (ImageView) findViewById(R.id.qr_code_iv);

		findViewById(R.id.qr_code_finish_but).setOnClickListener(this);
	}

	private void initData() {

		MyAppointmentVO myAppointmentVO = (MyAppointmentVO) getIntent()
				.getSerializableExtra("myappointment");
		QRCodeCreateVO codeCreateVO = new QRCodeCreateVO();
		if (myAppointmentVO != null) {

			if (app != null) {

				codeCreateVO.setStudentId(app.userVO.getUserid());
				codeCreateVO.setStudentName(app.userVO.getName());
				codeCreateVO.setReservationId(myAppointmentVO.get_id());
				codeCreateVO.setCoachName(app.userVO.getApplycoachinfo()
						.getName());
				codeCreateVO.setCourseProcessDesc(app.userVO.getSubject()
						.getName());
				codeCreateVO.setCreateTime((new Date().getTime() / 1000) + "");
				codeCreateVO.setLatitude(app.latitude);
				codeCreateVO.setLocationAddress(app.userVO.getAddress());
				codeCreateVO.setLongitude(app.longtitude);
			}
		}
		// 生成二维码
		try {
			String contentString = JSONUtil.toJsonString(codeCreateVO);
			LogUtil.print("contentString---" + contentString);
			if (contentString != null && contentString.trim().length() > 0) {
				// 根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（350*350）
				Bitmap qrCodeBitmap = EncodingHandler.createQRCode(
						contentString, 500);
				qRCodeIv.setImageBitmap(qrCodeBitmap);
			} else {
				toast.setText("生成二维码失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
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
		case R.id.qr_code_finish_but:
			finish();
			break;
		}
	}
}
