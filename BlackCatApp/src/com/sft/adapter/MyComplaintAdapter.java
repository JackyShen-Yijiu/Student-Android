package com.sft.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sft.infinitescrollviewpager.BitmapManager;

import com.jzjf.app.R;
import com.sft.util.UTC2LOC;
import com.sft.vo.ComplaintListVO;

/**
 * 我的投诉
 * 
 * @author sun 2016-3-1 下午9:57:48
 * 
 */
public class MyComplaintAdapter extends BaseAdapter {

	private List<ComplaintListVO> list = new ArrayList<ComplaintListVO>();

	private Context context;

	public MyComplaintAdapter(Context context, List<ComplaintListVO> list) {
		this.context = context;
		this.list = list;

	}

	public void setData(List<ComplaintListVO> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.item_my_complains,
					null);
			holder.date = (TextView) convertView
					.findViewById(R.id.item_my_complain_time);
			holder.name = (TextView) convertView
					.findViewById(R.id.item_my_complain_name);
			holder.content = (TextView) convertView
					.findViewById(R.id.item_my_complain_content);

			holder.type = (TextView) convertView
					.findViewById(R.id.complain_type);

			holder.iv_one = (ImageView) convertView.findViewById(R.id.iv_one);
			holder.iv_two = (ImageView) convertView.findViewById(R.id.iv_two);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(list.get(position).becomplainedname);
		holder.content.setText(list.get(position).feedbackmessage);

		holder.date.setText(""
				+ UTC2LOC.instance.getDate(list.get(position).createtime,
						"MM/dd HH:ss"));
		if (list.get(position).feedbacktype == 1) {
			holder.type.setText("投诉教练:");
		} else {
			holder.type.setText("投诉驾校:");
		}

		RelativeLayout.LayoutParams headParam = (RelativeLayout.LayoutParams) holder.iv_one
				.getLayoutParams();
		RelativeLayout.LayoutParams headParams = (RelativeLayout.LayoutParams) holder.iv_two
				.getLayoutParams();
		String[] url = list.get(position).getPiclist();
		switch (url.length) {
		case 1:
			// 第一个显示
			if (TextUtils.isEmpty(url[0])) {
			} else {
				holder.iv_one.setVisibility(View.VISIBLE);
				BitmapManager.INSTANCE.loadBitmap2(url[0], holder.iv_one,
						headParam.width, headParam.height);
				break;
			}
			break;
		case 2:
			// 第二个显示
			if (TextUtils.isEmpty(url[1])) {
			} else {
				holder.iv_one.setVisibility(View.VISIBLE);
				BitmapManager.INSTANCE.loadBitmap2(url[0], holder.iv_one,
						headParam.width, headParam.height);

				holder.iv_two.setVisibility(View.VISIBLE);
				BitmapManager.INSTANCE.loadBitmap2(url[1], holder.iv_two,
						headParams.width, headParams.height);
				break;
			}

		default:
			break;
		}

		// holder.title.setText(mData.get(position).getType());
		// holder.date.setText(UTC2LOC.instance.getDate(mData.get(position)
		// .getCreatetime(), "yyyy/MM/dd"));
		// holder.income.setText(mData.get(position).getIncome());
		return convertView;
	}

	private class ViewHolder {
		public TextView date;
		public TextView name;
		public TextView content;
		public TextView type;
		public ImageView iv_one;
		public ImageView iv_two;
	}
}
