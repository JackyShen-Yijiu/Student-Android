package com.sft.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sft.blackcatapp.R;
import com.sft.blackcatapp.YiBuIntroduceActivity;
import com.sft.util.CommonUtil;
import com.sft.viewutil.ZProgressHUD;

public class IntroducesFragment extends BaseFragment implements OnClickListener {

	private View view;
	private ImageView superiority;
	private ImageView favourableClass;
	private ImageView procedure;
	private Context mContext;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.main_view_one, container, false);

		mContext = getActivity();

		initView();
		initListener();
		return view;
	}

	private void initView() {
		superiority = (ImageView) view.findViewById(R.id.introduce_superiority);
		favourableClass = (ImageView) view
				.findViewById(R.id.introduce_favourable_class);
		procedure = (ImageView) view.findViewById(R.id.introduce_procedure);
	}

	private void initListener() {
		superiority.setOnClickListener(this);
		favourableClass.setOnClickListener(this);
		procedure.setOnClickListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View v) {

		if (!CommonUtil.isNetworkConnected(mContext)) {
			ZProgressHUD.getInstance(mContext).show();
			ZProgressHUD.getInstance(mContext).dismissWithFailure("网络异常");
			return;
		}
		Intent intent = new Intent(mContext, YiBuIntroduceActivity.class);
		switch (v.getId()) {
		case R.id.introduce_superiority:
			intent.putExtra("typeId", R.id.introduce_superiority);
			break;
		case R.id.introduce_favourable_class:
			intent.putExtra("typeId", R.id.introduce_favourable_class);

			break;
		case R.id.introduce_procedure:
			intent.putExtra("typeId", R.id.introduce_procedure);

			break;

		default:
			break;
		}

		mContext.startActivity(intent);
	}

}
