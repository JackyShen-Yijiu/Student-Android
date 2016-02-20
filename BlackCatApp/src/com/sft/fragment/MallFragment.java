package com.sft.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.sft.baseactivity.util.HttpSendUtils;
import cn.sft.infinitescrollviewpager.BitMapURLExcepteionListner;

import com.sft.adapter.MallProductAdapter;
import com.sft.blackcatapp.ProductDetailActivity;
import com.sft.blackcatapp.R;
import com.sft.common.Config;
import com.sft.util.BaseUtils;
import com.sft.util.JSONUtil;
import com.sft.vo.MallVO;
import com.sft.vo.MyCuponVO;
import com.sft.vo.ProductVO;

public class MallFragment extends BaseFragment implements
		BitMapURLExcepteionListner, OnItemClickListener {
	private String producttype = "0";
	private static final String headlineNews = "headlineNews";
	private String cityname;
	private ListView productListView;
	private MyCuponVO myCuponVO;
	private List<ProductVO> adList;
	private Context mContext;
	private int screenWidth;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_apply, container,
				false);
		cityname = app.curCity;

		initViews(rootView);
		obtainHeadLineNews();
		screenWidth = BaseUtils.getScreenWidth(mContext);
		return rootView;
	}

	private void initViews(View rootView) {
		productListView = (ListView) rootView.findViewById(R.id.mall_listview);
		productListView.setOnItemClickListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private void obtainHeadLineNews() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("index", "1");
		paramMap.put("count", "100");
		paramMap.put("producttype", producttype);
		// paramMap.put("cityname", cityname);
		HttpSendUtils.httpGetSend(headlineNews, this, Config.IP
				+ "api/v1/getmailproduct", paramMap);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		ProductVO productVO = (ProductVO) parent.getAdapter().getItem(position);
		Intent intent = new Intent(mContext, ProductDetailActivity.class);
		intent.putExtra("product", productVO);

		if (Config.MoneyType.COIN_CERTIFICATE.getValue().equals(producttype)) {
			intent.putExtra("isCupon", true);
		}
		if (myCuponVO != null) {
			intent.putExtra("myCupon", myCuponVO);
		}
		startActivity(intent);
	}

	@Override
	public void onURlError(Exception e) {

	}

	@Override
	public synchronized boolean doCallBack(String type, Object jsonString) {
		if (super.doCallBack(type, jsonString)) {
			return true;
		}
		try {
			if (type.equals(headlineNews)) {
				if (data != null) {
					MallVO mallVO = JSONUtil.toJavaBean(MallVO.class, data);
					adList = new ArrayList<ProductVO>();
					List<ProductVO> mainList = mallVO.getMainlist();
					if (mainList != null) {
						productListView.setAdapter(new MallProductAdapter(
								mContext, mainList, screenWidth, producttype));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

}
