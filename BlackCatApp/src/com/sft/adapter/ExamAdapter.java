package com.sft.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jzjf.app.R;
import com.sft.vo.ExerciseAnswerVO;
import com.sft.vo.OpenCityVO;

public class ExamAdapter extends BaseAdapter {

	private List<ExerciseAnswerVO> data;
	private Context mContext;

	public ExamAdapter(Context context, List<ExerciseAnswerVO> data) {
		this.data = data;
		this.mContext = context;
	}
	
	public List<ExerciseAnswerVO> getData(){
		return data;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			
			convertView = View.inflate(mContext, R.layout.exam_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ExerciseAnswerVO vo = data.get(position);
		holder.tv.setText(vo.getContent());
		if(vo.getChecked() == 0){//未选中
			holder.check.setChecked(false);
		}else{//已经选中
			holder.check.setChecked(true);
		}
		switch(position){
		case 0:
			
			holder.check.setBackgroundResource(R.drawable.selector_choose_a);
			break;
		case 1:
			holder.check.setBackgroundResource(R.drawable.selector_choose_b);
			break;
		case 2:
			holder.check.setBackgroundResource(R.drawable.selector_choose_c);
			break;
		case 3:
			holder.check.setBackgroundResource(R.drawable.selector_choose_d);
			break;
		default:
			break;
		}
		
		return convertView;
	}

	private class ViewHolder {
		private TextView tv;
		private CheckBox check;
		
		public ViewHolder(View view){
			
			tv = (TextView) view.findViewById(R.id.tv_content);
			check = (CheckBox) view.findViewById(R.id.cb_exam);
			
		}
	}

}
