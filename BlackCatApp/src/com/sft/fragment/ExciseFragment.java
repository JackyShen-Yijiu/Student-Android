package com.sft.fragment;

import java.util.ArrayList;
import java.util.List;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.VideoView;

import com.jzjf.app.R;
import com.sft.adapter.ExamAdapter;
import com.sft.blackcatapp.ExerciseOrderAct;
import com.sft.util.BaseUtils;
import com.sft.util.LogUtil;
import com.sft.vo.ExerciseAnswerVO;
import com.sft.vo.ExerciseVO;

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

	private ExerciseVO param1;

	private final String test = "         ";

	private TextView tvTitle;
	private ImageView imgType;
	private ImageView img;
	private Button btnSubmit;
	private ListView lv;
	private VideoView videoView;
	
	private LinearLayout llAnswer;
	/**解析答案*/
	private TextView tvAnswer;
	
	private ExamAdapter adapter;

	private String video_url = "";
	/**本地文件的路径*/
	private String localPath = "";
	

	public static ExciseFragment newInstance(ExerciseVO param1, String param2) {
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
			param1 = (ExerciseVO) getArguments().getSerializable(PARAM1);
			param2 = getArguments().getString(PARAM2);
		}
		localPath = Environment.getExternalStorageDirectory().toString();
		video_url = localPath+"/test.mp4";
		
		LogUtil.print("instantiateItem---onCreate-->Excise"+video_url);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LogUtil.print("instantiateItem---onCreateView-->Excise");
		View view = View
				.inflate(getActivity(), R.layout.frag_excise_item, null);
		initData();
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
//		LogUtil.print("excise--111>>"+param1.getId()+"position---checked-->"+param1.getAnswers());
		
		return view;
	}

	private void initData() {
		
		
	}

	private void initView(View view) {
		tvTitle = (TextView) view.findViewById(R.id.frag_excise_item_title);
		imgType = (ImageView) view.findViewById(R.id.frag_excise_item_type);
		btnSubmit = (Button) view.findViewById(R.id.frag_excise_btn);
		lv = (ListView) view.findViewById(R.id.frag_excise_item_lv);
		img = (ImageView) view.findViewById(R.id.frag_excise_img);
		videoView = (VideoView) view.findViewById(R.id.frag_excise_videov);
		llAnswer = (LinearLayout) view.findViewById(R.id.frag_excise_item_answer_ll);
		tvAnswer = (TextView) view.findViewById(R.id.frag_excise_item_answer_content);
		
		btnSubmit.setOnClickListener(this);
		
		lv.setOnItemClickListener(this);
		
		tvTitle.setText(test + param1.getWebnote().getQuestion());
		LogUtil.print("int---21465>" + param1.getWebnote().getType());
		
		
		switch (param1.getWebnote().getType()) {
		case 0:
		case 1:// 判断
			imgType.setImageResource(R.drawable.ic_study_judge);
			if(param1.getAnswers().size() == 0){
				List<ExerciseAnswerVO> answers = new ArrayList<ExerciseAnswerVO>();
				answers.add(new ExerciseAnswerVO("正确"));
				answers.add(new ExerciseAnswerVO("错误"));
				param1.setAnswers(answers);
			}
			LogUtil.print("answeer--->"+param1.getAnswers());
			break;
		case 2:// 单选
			imgType.setImageResource(R.drawable.ic_study_single_select);
			break;
		case 3:// 多选
			imgType.setImageResource(R.drawable.ic_study_mulity_select);
			btnSubmit.setVisibility(View.VISIBLE);
			break;

		}
		
		adapter = new ExamAdapter(getActivity(), param1.getAnswers());
		adapter.setRightAnswer(param1.getWebnote().getAnswer_true());
		adapter.setSubmit(param1.submit);
		lv.setAdapter(adapter);
		BaseUtils.setListViewHeightBasedOnChildren(lv);
		//显示视频
//		doImage();
	}
	
	private void doImage(){
		Uri uri = Uri.parse(localPath+"/abc.jpg");
		img.setImageURI(uri);
		img.setVisibility(View.VISIBLE);
	}
	
	private void doVideo(){
		videoView.setVisibility(View.VISIBLE);
		videoView.setVideoPath(video_url);
		videoView.start();
		videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {  
            @Override  
            public void onCompletion(MediaPlayer mp) {  
                if(null!=videoView){
                	try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
                	videoView.seekTo(0);
                	videoView.start();
                }
            }  
        }); 
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
		switch (param1.getWebnote().getType()) {
		case 0:
		case 1:// 判断
		case 2:
			// 单选
			//已经选择了
			param1.submit = 1;
			for (int i = 0; i < data.size(); i++) {
				data.get(i).setChecked(0);
			}
			data.get(arg2).setChecked(1);
			adapter.setSubmit(param1.submit);
			adapter.notifyDataSetChanged();
			if(checkAnswerSingle(arg2)){//正确
//				try {
//					Thread.sleep(100);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
				((ExerciseOrderAct)getActivity()).addRight();
				((ExerciseOrderAct)getActivity()).next();
				
			}else{//错误
				showAnalysy();
				((ExerciseOrderAct)getActivity()).addWrong();
			}
			
			
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
		switch (arg0.getId()) {
		case R.id.frag_excise_btn:// 提交
			param1.submit = 1;
			adapter.setSubmit(param1.submit);
			adapter.notifyDataSetChanged();
			break;
		}
	}

	private int[] getRightAnswer(int type) {
		int[] result = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		String t = String.valueOf(type).toString();
		for (int i = 0; i < t.length(); i++) {
			result[i] = t.charAt(i);
		}
		return result;
	}
	
	/**
	 * 判断 单选  答案是否正确
	 * @param pos
	 * @return
	 */
	private boolean checkAnswerSingle(int pos){
		int trueAn = Integer.parseInt(param1.getWebnote().getAnswer_true());
		//与数据库 单位 一致
		pos++;
		if(trueAn == pos){//正确
			return true;
		}else{//错误
			return false;
		}
	}
	
	private boolean checkMulity(){
		
//		int[] right = adapter.getRightAnswer(param1.getWebnote().getAnswer_true());
//		for(ExerciseAnswerVO vo :param1.getAnswers()){
//			if(vo.getChecked() ==1){//选择了这个选择后,判断是否是正确答案
//				//先设置 默认 错误
//				holder.check.setBackgroundResource(R.drawable.choose_wrong);
//				for(int i=0;i<right.length;i++){//遍历正确答案
//					LogUtil.print("answer--->"+right[i]+"position-->"+position);
//					if(right[i] == position +1){// 正确的
//						holder.check.setBackgroundResource(R.drawable.choose_right);
//						break;
//					}
//				}
//			}
//		}
		
//		param1.getAnswer_true()
//		List<ExerciseAnswerVO> answers = param1.getAnswers();
//		for(int i=0;i<answers.size();i++){
//			if(answers.get(i).getChecked() == 1){//选择了
//				
//			}
//		}
//		
		
		return false;
	}
	
	/**
	 * 显示解析
	 */
	private void showAnalysy(){
		llAnswer.setVisibility(View.VISIBLE);
		tvAnswer.setText(param1.getWebnote().getExplain());
	}

}
