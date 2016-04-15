package com.sft.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.jzjf.app.R;
import com.sft.util.CommonUtil;
import com.sft.view.ApplyClassTypeRowLayout.ClassTypeSelectedChangeListener;
import com.sft.vo.ClassVO;

public class ApplyClassTypeLayout extends LinearLayout implements
		ClassTypeSelectedChangeListener {

	private Context context;
	private int column = 2;

	// 用户选择的班型
	private ClassVO selectClass = new ClassVO();
	List<ClassVO> listClassVOs = new ArrayList<ClassVO>();

	public ApplyClassTypeLayout(Context context) {
		super(context);
		init(context);
	}

	public ApplyClassTypeLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public ApplyClassTypeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		this.context = context;
		// setLayoutDirection(LinearLayout.VERTICAL);
	}

	public void setSelectClass(ClassVO selectClass) {
		this.selectClass = selectClass;
	}

	public ClassVO getSelectClass() {
		return selectClass;
	}

	public void clearData() {
		removeAllViews();
		selectClass = new ClassVO();
	}

	private List<ApplyClassTypeRowLayout> rowLayoutList = new ArrayList<ApplyClassTypeRowLayout>();

	public void setData(List<ClassVO> list) {

		if (list == null || list.size() == 0) {
			return;
		}
		listClassVOs.clear();
		listClassVOs.addAll(list);

		// 最后一行的个数
		int lastConut = list.size() % column;
		// 行数
		int row = list.size() / column;
		if (lastConut > 0) {
			row++;
		}

		LinearLayout.LayoutParams typeParams = new LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT, 1);
		typeParams.rightMargin = CommonUtil.dip2px(context, 25);

		// LinearLayout.LayoutParams rowParams = new LayoutParams(
		// LinearLayout.LayoutParams.WRAP_CONTENT,
		// LinearLayout.LayoutParams.WRAP_CONTENT);
		// rowParams.bottomMargin = CommonUtil.dip2px(context, 27);
		// rowParams.setLayoutDirection(LinearLayout.HORIZONTAL);

		for (int i = 0; i < row - 1; i++) {
			LinearLayout innerLayout = (LinearLayout) View.inflate(context,
					R.layout.linearlayout, null);
			LinearLayout.LayoutParams rowParams = (LayoutParams) innerLayout
					.getLayoutParams();
			addView(innerLayout, rowParams);

			for (int j = 0; j < column; j++) {
				ApplyClassTypeRowLayout rowLayout = new ApplyClassTypeRowLayout(
						context);
				rowLayout.setSelectedChangeListener(this);
				// 参数
				rowLayout.setValue(list.get(i * column + j));
				innerLayout.addView(rowLayout, typeParams);
				rowLayoutList.add(rowLayout);
			}

		}

		LinearLayout innerLayout = (LinearLayout) View.inflate(context,
				R.layout.linearlayout, null);
		LinearLayout.LayoutParams rowParams = (LayoutParams) innerLayout
				.getLayoutParams();
		addView(innerLayout, rowParams);

		lastConut = lastConut == 0 ? column : lastConut;
		for (int i = 0; i < lastConut; i++) {
			ApplyClassTypeRowLayout rowLayout = new ApplyClassTypeRowLayout(
					context);
			rowLayout.setSelectedChangeListener(this);
			// 参数
			rowLayout.setValue(list.get((row - 1) * column + i));
			innerLayout.addView(rowLayout, typeParams);
			rowLayoutList.add(rowLayout);
		}

	}

	public interface OnClassTypeSelectedListener {
		void ClassTypeSelectedListener(ClassVO classVO);
	}

	private OnClassTypeSelectedListener classTypeSelectedListener;

	public void setOnClassTypeSelectedListener(
			OnClassTypeSelectedListener classTypeSelectedListener) {

		this.classTypeSelectedListener = classTypeSelectedListener;
	}

	@Override
	public void onClassTypeSelectedChange(ApplyClassTypeRowLayout layout,
			ClassVO classVO, boolean selected) {

		if (classVO == null) {
			return;
		}

		// 更改字体颜色
		int lastConut = listClassVOs.size() % column;
		// 行数
		int row = listClassVOs.size() / column;
		if (lastConut > 0) {
			row++;
		}
		int selectClassId = 0;
		for (int i = 0; i < listClassVOs.size(); i++) {
			if (classVO.getCalssid().equals(listClassVOs.get(i).getCalssid())) {
				selectClassId = i;
			}
		}
		// LogUtil.print("selectClassId=" + selectClassId);
		for (int i = 0; i < row - 1; i++) {
			for (int j = 0; j < column; j++) {
				if (selectClassId == (i * column + j)) {
					rowLayoutList.get(i * column + j).setTextColor(1);
				} else {
					rowLayoutList.get(i * column + j).setTextColor(2);
				}
			}
		}

		lastConut = lastConut == 0 ? column : lastConut;
		for (int i = 0; i < lastConut; i++) {
			if (selectClassId == ((row - 1) * column + i)) {
				rowLayoutList.get((row - 1) * column + i).setTextColor(1);
			} else {
				rowLayoutList.get((row - 1) * column + i).setTextColor(2);
			}
		}

		if (selected) {
			selectClass = classVO;
		} else {
			selectClass = null;
		}
		if (classTypeSelectedListener != null) {
			classTypeSelectedListener.ClassTypeSelectedListener(classVO);
		}
	}
}