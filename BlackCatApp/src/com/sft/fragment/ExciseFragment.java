package com.sft.fragment;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jzjf.app.R;
import com.sft.adapter.ExamAdapter;
import com.sft.util.BaseUtils;
import com.sft.util.LogUtil;
import com.sft.vo.ExerciseAnswerVO;
import com.sft.vo.questionbank.web_note;

/**
 * 考试每一项
 * 
 * @author pengdonghua
 * 
 */
public class ExciseFragment extends Fragment implements OnItemClickListener,
		OnClickListener {

	private final static String PARAM1 = "p1";
	private final static String PARAM2 = "p2";

	private String param2;

	private web_note param1;

	private final String test = "         ";

	private TextView tvTitle;
	private ImageView imgType;
	private ImageView img;
	private Button btnSubmit;
	private ListView lv;
	private ExamAdapter adapter;

	public static ExciseFragment newInstance(web_note param1, String param2) {
		ExciseFragment fragment = new ExciseFragment();
		Bundle b = new Bundle();
		b.putSerializable(PARAM1, param1);
		b.putString(PARAM2, param2);
		LogUtil.print("instantiateItem----params2>>" + param2);
		fragment.setArguments(b);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			param1 = (web_note) getArguments().getSerializable(PARAM1);
			param2 = getArguments().getString(PARAM2);
		}
		LogUtil.print("instantiateItem---onCreate-->Excise");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LogUtil.print("instantiateItem---onCreateView-->Excise");
		View view = View
				.inflate(getActivity(), R.layout.frag_excise_item, null);
		initView(view);

		if (view == null) {
			view = inflater.inflate(R.layout.frag_excise_item, null);
		}
		// 缓存的view需要判断是否已经被加过parent，
		// 如果有parent需要从parent删除，要不然会发生这个view已经有parent的错误。
		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
			parent.removeView(view);
		}

		return view;
	}

	private void initView(View view) {
		tvTitle = (TextView) view.findViewById(R.id.frag_excise_item_title);
		imgType = (ImageView) view.findViewById(R.id.frag_excise_item_type);
		btnSubmit = (Button) view.findViewById(R.id.frag_excise_btn);
		lv = (ListView) view.findViewById(R.id.frag_excise_item_lv);
		img = (ImageView) view.findViewById(R.id.frag_excise_img);

		btnSubmit.setOnClickListener(this);
		adapter = new ExamAdapter(getActivity(), param1.getAnswers());

		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);
		BaseUtils.setListViewHeightBasedOnChildren(lv);
		tvTitle.setText(test + param1.getQuestion());
		LogUtil.print("int---21465>" + param1.getType());
		switch (param1.getType()) {
		case 0:
		case 1:// 判断
			imgType.setImageResource(R.drawable.ic_study_judge);
			break;
		case 2:// 单选
			imgType.setImageResource(R.drawable.ic_study_single_select);
			break;
		case 3:// 多选
			imgType.setImageResource(R.drawable.ic_study_mulity_select);
			btnSubmit.setVisibility(View.VISIBLE);
			break;

		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// LogUtil.print("onItem--click"+arg2);
		// Toast.makeText(getActivity(), "onItemClick"+arg2,
		// Toast.LENGTH_SHORT).show();
		List<ExerciseAnswerVO> data = adapter.getData();
		switch (param1.getType()) {
		case 0:
		case 1:// 判断
		case 2:
			// 单选

			for (int i = 0; i < data.size(); i++) {
				data.get(i).setChecked(0);
			}
			data.get(arg2).setChecked(1);
			adapter.notifyDataSetChanged();
			break;
		case 3:
			// 多选
			data.get(arg2).setChecked(data.get(arg2).getChecked() == 0 ? 1 : 0);
			adapter.notifyDataSetChanged();
			break;
		}

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.frag_excise_btn:// 提交

			break;
		}
	}

	public int[] getRightAnswer(int type) {
		int[] result = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		String t = String.valueOf(type).toString();
		for (int i = 0; i < t.length(); i++) {
			result[i] = t.charAt(i);
		}
		return result;
	}

}
