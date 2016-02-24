package com.sft.city;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.sft.blackcatapp.R;
import com.sft.city.MyLetterListView.OnTouchingLetterChangedListener;

public class Act_City extends Activity implements OnScrollListener {
	private BaseAdapter adapter;
	private ResultListAdapter resultListAdapter;
	private ListView personList;
	private ListView resultList;
	private TextView overlay; // �Ի�������ĸtextview
	private MyLetterListView letterListView; // A-Z listview
	private HashMap<String, Integer> alphaIndexer;// ��Ŵ��ڵĺ���ƴ������ĸ����֮��Ӧ���б�λ��
	private String[] sections;// ��Ŵ��ڵĺ���ƴ������ĸ
	private Handler handler;
	private OverlayThread overlayThread; // ��ʾ����ĸ�Ի���
	private ArrayList<City> allCity_lists; // ���г����б�
	private ArrayList<City> city_lists;// �����б�
	private ArrayList<City> city_hot;
	private ArrayList<City> city_result;
	private ArrayList<String> city_history;
//	private EditText sh;
	private TextView tv_noresult;

	private LocationClient mLocationClient;
	private MyLocationListener mMyLocationListener;

	private String currentCity; // ���ڱ��涨λ���ĳ���
	private int locateProcess = 1; // ��¼��ǰ��λ��״̬ ���ڶ�λ-��λ�ɹ�-��λʧ��
	private boolean isNeedFresh;

//	private DatabaseHelper helper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_city);
		letterListView = (MyLetterListView) findViewById(R.id.MyLetterListView01);
		letterListView.setVisibility(View.GONE);
		personList = (ListView) findViewById(R.id.list_view);
		allCity_lists = new ArrayList<City>();
		city_lists = new ArrayList<City>();
		city_hot = new ArrayList<City>();
		city_result = new ArrayList<City>();
		city_history = new ArrayList<String>();
		resultList = (ListView) findViewById(R.id.search_result);
//		sh = (EditText) findViewById(R.id.sh);
		tv_noresult = (TextView) findViewById(R.id.tv_noresult);
//		helper = new DatabaseHelper(this);
//		sh.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before,
//					int count) {
//				if (s.toString() == null || "".equals(s.toString())) {
//					// letterListView.setVisibility(View.VISIBLE);
//					personList.setVisibility(View.VISIBLE);
//					resultList.setVisibility(View.GONE);
//					tv_noresult.setVisibility(View.GONE);
//				} else {
//					city_result.clear();
//					letterListView.setVisibility(View.GONE);
//					personList.setVisibility(View.GONE);
//					getResultCityList(s.toString());
//					if (city_result.size() <= 0) {
//						tv_noresult.setVisibility(View.VISIBLE);
//						resultList.setVisibility(View.GONE);
//					} else {
//						tv_noresult.setVisibility(View.GONE);
//						resultList.setVisibility(View.VISIBLE);
//						resultListAdapter.notifyDataSetChanged();
//					}
//				}
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after) {
//			}
//
//			@Override
//			public void afterTextChanged(Editable s) {
//
//			}
//		});
		letterListView
				.setOnTouchingLetterChangedListener(new LetterListViewListener());

		alphaIndexer = new HashMap<String, Integer>();
		handler = new Handler();
		overlayThread = new OverlayThread();
		isNeedFresh = true;
		personList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position >= 4) {

					Toast.makeText(getApplicationContext(),
							allCity_lists.get(position).getName(),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		locateProcess = 1;
		personList.setAdapter(adapter);
		personList.setOnScrollListener(this);
		resultListAdapter = new ResultListAdapter(this, city_result);
		resultList.setAdapter(resultListAdapter);
		resultList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(getApplicationContext(),
						city_result.get(position).getName(), Toast.LENGTH_SHORT)
						.show();
			}
		});
		// initOverlay();
		cityInit();
		// allCityInit();
		hotCityInit();
		// hisCityInit();
		setAdapter(allCity_lists, city_hot, city_history);

		mLocationClient = new LocationClient(this.getApplicationContext());
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		InitLocation();
		mLocationClient.start();
	}

//	public void InsertCity(String name) {
//		SQLiteDatabase db = helper.getReadableDatabase();
//		Cursor cursor = db.rawQuery("select * from recentcity where name = '"
//				+ name + "'", null);
//		if (cursor.getCount() > 0) { //
//			db.delete("recentcity", "name = ?", new String[] { name });
//		}
//		db.execSQL("insert into recentcity(name, date) values('" + name + "', "
//				+ System.currentTimeMillis() + ")");
//		db.close();
//	}

	private void InitLocation() {
		// ���ö�λ����
		LocationClientOption option = new LocationClientOption();
		option.setCoorType("bd09ll"); // ������������
		option.setScanSpan(10000); // 10����ɨ��1��
		// ��Ҫ��ַ��Ϣ������Ϊ�����κ�ֵ��string���ͣ��Ҳ���Ϊnull��ʱ������ʾ�޵�ַ��Ϣ��
		option.setAddrType("all");
		// �����Ƿ񷵻�POI�ĵ绰�͵�ַ����ϸ��Ϣ��Ĭ��ֵΪfalse����������POI�ĵ绰�͵�ַ��Ϣ��
//		option.setPoiExtraInfo(true);
		// ���ò�Ʒ�����ơ�ǿ�ҽ�����ʹ���Զ���Ĳ�Ʒ�����ƣ����������Ժ�Ϊ���ṩ����Ч׼ȷ�Ķ�λ����
		option.setProdName("ͨ��GPS��λ�ҵ�ǰ��λ��");
		// �������û��涨λ����
		option.disableCache(true);
		// �������ɷ��ص�POI������Ĭ��ֵΪ3������POI��ѯ�ȽϺķ�������������෵�ص�POI�������Ա��ʡ������
//		option.setPoiNumber(3);
		// ���ö�λ��ʽ�����ȼ���
		// ��gps���ã����һ�ȡ�˶�λ���ʱ�����ٷ�����������ֱ�ӷ��ظ��û����ꡣ���ѡ���ʺ�ϣ���õ�׼ȷ����λ�õ��û������gps�����ã��ٷ����������󣬽��ж�λ��
		option.setPriority(LocationClientOption.GpsFirst);
		mLocationClient.setLocOption(option);
	}

	private void cityInit() {
		City city = new City("��λ", "0"); // ��ǰ��λ����
		allCity_lists.add(city);
		city = new City("���", "1"); // ������ʵĳ���
		allCity_lists.add(city);
		city = new City("����", "2"); // ���ų���
		allCity_lists.add(city);
		city = new City("ȫ��", "3"); // ȫ������
		allCity_lists.add(city);
		// ���ݿ�
		city_lists = getCityList();
		allCity_lists.addAll(city_lists);
	}

	/**
	 * ���ų���
	 */
	public void hotCityInit() {
		City city = new City("�Ϻ�", "2");
		city_hot.add(city);
		city = new City("����", "2");
		city_hot.add(city);
		city = new City("����", "2");
		city_hot.add(city);
		city = new City("����", "2");
		city_hot.add(city);
		city = new City("�人", "2");
		city_hot.add(city);
		city = new City("���", "2");
		city_hot.add(city);
		city = new City("����", "2");
		city_hot.add(city);
		city = new City("�Ͼ�", "2");
		city_hot.add(city);
		city = new City("����", "2");
		city_hot.add(city);
		city = new City("�ɶ�", "2");
		city_hot.add(city);
		city = new City("����", "2");
		city_hot.add(city);

	}

	public void allCityInit() {
		City city = new City("�Ϻ�", "3");
		city = new City("����", "3");
		allCity_lists.add(city);
	}

	private void hisCityInit() {
//		SQLiteDatabase db = helper.getReadableDatabase();
//		Cursor cursor = db.rawQuery(
//				"select * from recentcity order by date desc limit 0, 3", null);
//		while (cursor.moveToNext()) {
//			city_history.add(cursor.getString(1));
//		}
//		cursor.close();
//		db.close();
	}

	@SuppressWarnings("unchecked")
	private ArrayList<City> getCityList() {
		DBHelper dbHelper = new DBHelper(this);
		ArrayList<City> list = new ArrayList<City>();
		try {
			dbHelper.createDataBase();
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			Cursor cursor = db.rawQuery("select * from city", null);
			City city;
			while (cursor.moveToNext()) {
				city = new City(cursor.getString(1), cursor.getString(2));
				list.add(city);
			}
			cursor.close();
			db.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Collections.sort(list, comparator);
		return list;
	}

	@SuppressWarnings("unchecked")
	private void getResultCityList(String keyword) {
		DBHelper dbHelper = new DBHelper(this);
		try {
			dbHelper.createDataBase();
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			Cursor cursor = db.rawQuery(
					"select * from city where name like \"%" + keyword
							+ "%\" or pinyin like \"%" + keyword + "%\"", null);
			City city;
			Log.e("info", "length = " + cursor.getCount());
			while (cursor.moveToNext()) {
				city = new City(cursor.getString(1), cursor.getString(2));
				city_result.add(city);
			}
			cursor.close();
			db.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Collections.sort(city_result, comparator);
	}

	/**
	 * a-z����
	 */
	@SuppressWarnings("rawtypes")
	Comparator comparator = new Comparator<City>() {
		@Override
		public int compare(City lhs, City rhs) {
			String a = lhs.getPinyi().substring(0, 1);
			String b = rhs.getPinyi().substring(0, 1);
			int flag = a.compareTo(b);
			if (flag == 0) {
				return a.compareTo(b);
			} else {
				return flag;
			}
		}
	};

	private void setAdapter(List<City> list, List<City> hotList,
			List<String> hisCity) {
		adapter = new ListAdapter(this, list, hotList, hisCity);
		personList.setAdapter(adapter);
	}

	/**
	 * ʵ��ʵλ�ص�����
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation arg0) {
			Log.e("info", "city = " + arg0.getCity());
			if (!isNeedFresh) {
				return;
			}
			isNeedFresh = false;
			if (arg0.getCity() == null) {
				locateProcess = 3; // ��λʧ��
				personList.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				return;
			}
			currentCity = arg0.getCity().substring(0,
					arg0.getCity().length() - 1);
			locateProcess = 2; // ��λ�ɹ�
			personList.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}

//		@Override
//		public void onReceivePoi(BDLocation arg0) {
//
//		}
	}

	private class ResultListAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private ArrayList<City> results = new ArrayList<City>();

		public ResultListAdapter(Context context, ArrayList<City> results) {
			inflater = LayoutInflater.from(context);
			this.results = results;
		}

		@Override
		public int getCount() {
			return results.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_city_list, null);
				viewHolder = new ViewHolder();
				viewHolder.name = (TextView) convertView
						.findViewById(R.id.name);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.name.setText(results.get(position).getName());
			return convertView;
		}

		class ViewHolder {
			TextView name;
		}
	}

	public class ListAdapter extends BaseAdapter {
		private Context context;
		private LayoutInflater inflater;
		private List<City> list;
		private List<City> hotList;
		private List<String> hisCity;
		final int VIEW_TYPE = 5;

		public ListAdapter(Context context, List<City> list,
				List<City> hotList, List<String> hisCity) {
			this.inflater = LayoutInflater.from(context);
			this.list = list;
			this.context = context;
			this.hotList = hotList;
			this.hisCity = hisCity;
			alphaIndexer = new HashMap<String, Integer>();
			sections = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				// ��ǰ����ƴ������ĸ
				String currentStr = getAlpha(list.get(i).getPinyi());
				// ��һ������ƴ������ĸ�����������Ϊ" "
				String previewStr = (i - 1) >= 0 ? getAlpha(list.get(i - 1)
						.getPinyi()) : " ";
				if (!previewStr.equals(currentStr)) {
					String name = getAlpha(list.get(i).getPinyi());
					alphaIndexer.put(name, i);
					sections[i] = name;
				}
			}
		}

		@Override
		public int getViewTypeCount() {
			return VIEW_TYPE;
		}

		@Override
		public int getItemViewType(int position) {
			return position < 4 ? position : 4;
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

		ViewHolder holder;

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final TextView city;
			int viewType = getItemViewType(position);
			if (viewType == 0) { // ��λ
				convertView = inflater.inflate(R.layout.item_city_first, null);
				TextView locateHint = (TextView) convertView
						.findViewById(R.id.locateHint);
				city = (TextView) convertView.findViewById(R.id.lng_city);
				city.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (locateProcess == 2) {

							Toast.makeText(getApplicationContext(),
									city.getText().toString(),
									Toast.LENGTH_SHORT).show();
						} else if (locateProcess == 3) {
							locateProcess = 1;
							personList.setAdapter(adapter);
							adapter.notifyDataSetChanged();
							mLocationClient.stop();
							isNeedFresh = true;
							InitLocation();
							currentCity = "";
							mLocationClient.start();
						}
					}
				});
				ProgressBar pbLocate = (ProgressBar) convertView
						.findViewById(R.id.pbLocate);
				if (locateProcess == 1) { // ���ڶ�λ
					locateHint.setText("���ڶ�λ");
					city.setVisibility(View.GONE);
					pbLocate.setVisibility(View.VISIBLE);
				} else if (locateProcess == 2) { // ��λ�ɹ�
					locateHint.setText("��ǰ��λ����");
					city.setVisibility(View.VISIBLE);
					city.setText(currentCity);
					mLocationClient.stop();
					pbLocate.setVisibility(View.GONE);
				} else if (locateProcess == 3) {
					locateHint.setText("δ��λ������,��ѡ��");
					city.setVisibility(View.VISIBLE);
					city.setText("����ѡ��");
					pbLocate.setVisibility(View.GONE);
				}
			} else if (viewType == 1) { // ������ʳ���
				convertView = inflater.inflate(R.layout.item_city_recenty, null);
				LinearLayout recent_citys = (LinearLayout) convertView
						.findViewById(R.id.recent_citys);
				GridView rencentCity = (GridView) convertView
						.findViewById(R.id.recent_city);
				rencentCity
						.setAdapter(new HitCityAdapter(context, this.hisCity));
				rencentCity.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						Toast.makeText(getApplicationContext(),
								city_history.get(position), Toast.LENGTH_SHORT)
								.show();

					}
				});
				// TextView recentHint = (TextView) convertView
				// .findViewById(R.id.recentHint);
				// recentHint.setText("������ʵĳ���");
				recent_citys.setVisibility(View.GONE);
			} else if (viewType == 2) {
				convertView = inflater.inflate(R.layout.item_city_recenty, null);
				GridView hotCity = (GridView) convertView
						.findViewById(R.id.recent_city);
				hotCity.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						Toast.makeText(getApplicationContext(),
								city_hot.get(position).getName(),
								Toast.LENGTH_SHORT).show();

					}
				});
				hotCity.setAdapter(new HotCityAdapter(context, this.hotList));
				TextView hotHint = (TextView) convertView
						.findViewById(R.id.recentHint);
				hotHint.setText("���ų���");
			} else if (viewType == 3) {
				convertView = inflater.inflate(R.layout.item_city_total, null);
			} else {
				if (convertView == null) {
					convertView = inflater.inflate(R.layout.item_city_list, null);
					holder = new ViewHolder();
					holder.alpha = (TextView) convertView
							.findViewById(R.id.alpha);
					holder.name = (TextView) convertView
							.findViewById(R.id.name);
					convertView.setTag(holder);
				} else {
					holder = (ViewHolder) convertView.getTag();
				}
				if (position >= 1) {
					holder.name.setText(list.get(position).getName());
					String currentStr = getAlpha(list.get(position).getPinyi());
					String previewStr = (position - 1) >= 0 ? getAlpha(list
							.get(position - 1).getPinyi()) : " ";
					if (!previewStr.equals(currentStr)) {
						holder.alpha.setVisibility(View.VISIBLE);
						holder.alpha.setText(currentStr);
					} else {
						holder.alpha.setVisibility(View.GONE);
					}
				}
			}
			return convertView;
		}

		private class ViewHolder {
			TextView alpha; // ����ĸ����
			TextView name; // ��������
		}
	}

	@Override
	protected void onStop() {
		mLocationClient.stop();
		super.onStop();
	}

	class HotCityAdapter extends BaseAdapter {
		private Context context;
		private LayoutInflater inflater;
		private List<City> hotCitys;

		public HotCityAdapter(Context context, List<City> hotCitys) {
			this.context = context;
			inflater = LayoutInflater.from(this.context);
			this.hotCitys = hotCitys;
		}

		@Override
		public int getCount() {
			return hotCitys.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("ViewHolder") @Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = inflater.inflate(R.layout.item_city_list, null);
			TextView city = (TextView) convertView.findViewById(R.id.city);

			city.setText(hotCitys.get(position).getName());
			city.setTextColor(Color.WHITE);
//			city.setBackgroundResource(R.drawable.text_selector);
			return convertView;
		}
	}

	class HitCityAdapter extends BaseAdapter {
		private Context context;
		private LayoutInflater inflater;
		private List<String> hotCitys;

		public HitCityAdapter(Context context, List<String> hotCitys) {
			this.context = context;
			inflater = LayoutInflater.from(this.context);
			this.hotCitys = hotCitys;
		}

		@Override
		public int getCount() {
			return hotCitys.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = inflater.inflate(R.layout.item_city_list, null);
			TextView city = (TextView) convertView.findViewById(R.id.city);
			city.setText(hotCitys.get(position));
			return convertView;
		}
	}

	private boolean mReady;

	// ��ʼ������ƴ������ĸ������ʾ��
	private void initOverlay() {
		mReady = true;
		LayoutInflater inflater = LayoutInflater.from(this);
		overlay = (TextView) inflater.inflate(R.layout.city_overlay, null);
		overlay.setVisibility(View.INVISIBLE);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
				PixelFormat.TRANSLUCENT);
		WindowManager windowManager = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		windowManager.addView(overlay, lp);
	}

	private boolean isScroll = false;

	private class LetterListViewListener implements
			OnTouchingLetterChangedListener {

		@Override
		public void onTouchingLetterChanged(final String s) {
			isScroll = false;
			if (alphaIndexer.get(s) != null) {
				int position = alphaIndexer.get(s);
				personList.setSelection(position);
				overlay.setText(s);
				overlay.setVisibility(View.VISIBLE);
				handler.removeCallbacks(overlayThread);
				// �ӳ�һ���ִ�У���overlayΪ���ɼ�
				handler.postDelayed(overlayThread, 1000);
			}
		}
	}

	// ����overlay���ɼ�
	private class OverlayThread implements Runnable {
		@Override
		public void run() {
			overlay.setVisibility(View.GONE);
		}
	}

	// ��ú���ƴ������ĸ
	private String getAlpha(String str) {
		if (str == null) {
			return "#";
		}
		if (str.trim().length() == 0) {
			return "#";
		}
		char c = str.trim().substring(0, 1).charAt(0);
		// ������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase();
		} else if (str.equals("0")) {
			return "��λ";
		} else if (str.equals("1")) {
			return "���";
		} else if (str.equals("2")) {
			return "����";
		} else if (str.equals("3")) {
			return "ȫ��";
		} else {
			return "#";
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_TOUCH_SCROLL
				|| scrollState == SCROLL_STATE_FLING) {
			isScroll = true;
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (!isScroll) {
			return;
		}

		if (mReady) {
			String text;
			String name = allCity_lists.get(firstVisibleItem).getName();
			String pinyin = allCity_lists.get(firstVisibleItem).getPinyi();
			if (firstVisibleItem < 4) {
				text = name;
			} else {
				text = PingYinUtil.converterToFirstSpell(pinyin)
						.substring(0, 1).toUpperCase();
			}
			overlay.setText(text);
			overlay.setVisibility(View.VISIBLE);
			handler.removeCallbacks(overlayThread);
			// �ӳ�һ���ִ�У���overlayΪ���ɼ�
			handler.postDelayed(overlayThread, 1000);
		}
	}
}