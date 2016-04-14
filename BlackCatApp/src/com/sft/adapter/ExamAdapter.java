package com.sft.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jzjf.app.R;
import com.sft.util.LogUtil;
import com.sft.vo.ExerciseAnswerVO;
import com.sft.vo.OpenCityVO;

public class ExamAdapter extends BaseAdapter {

	private List<ExerciseAnswerVO> data;
	private Context mContext;
	/**正确答案： 用来显示已经提交答案后*/
	private String true_answer = null;
	int[] right = null;
	/**是否提交*/
	private int submit = 0;

	public ExamAdapter(Context context, List<ExerciseAnswerVO> data) {
		this.data = data;
		this.mContext = context;
	}
	
	public void setRightAnswer(String true_answer){
		this.true_answer = true_answer;
		right = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		getRightAnswer(true_answer);
	}
	
	public void setSubmit(int submit){
		this.submit = submit;
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
		
		//默认状态
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
		// 选择后的状态，，显示 正确还是错误
		if(vo.getChecked() == 0){//未选中
			holder.check.setChecked(false);
		}else {//已经选中,
			holder.check.setChecked(true);
		}
		LogUtil.print("excise-->>"+submit+"position--checked-->"+vo.getChecked());
		
		//已经提交
		if(submit == 1){//
			if(vo.getChecked() ==1){//选择了这个选择后,判断是否是正确答案
				//先设置 默认 错误
				holder.check.setBackgroundResource(R.drawable.choose_wrong);
				for(int i=0;i<right.length;i++){//遍历正确答案
					LogUtil.print("answer--->"+right[i]+"position-->"+position);
					if(right[i] == position +1){// 正确的
						holder.check.setBackgroundResource(R.drawable.choose_right);
						break;
					}
				}
			}
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
	
	public int[] getRightAnswer(String t) {
		if(right!=null)
			return right;
		for (int i = 0; i < t.length(); i++) {
			right[i] = Integer.parseInt(String.valueOf(t.charAt(i)));
		}
		
		return right;
	}

}
