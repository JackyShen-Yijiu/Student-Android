package com.sft.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.content.res.Resources.NotFoundException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioGroup;

public class DatePickerUtil {
	private static int mwidth;

	public static void resizePicker(FrameLayout tp, int width) {
		mwidth = width;
		List<NumberPicker> npList = findNumberPicker(tp);
		for (NumberPicker np : npList) {
			resizeNumberPicker(np);
		}
	}

	private static List<NumberPicker> findNumberPicker(ViewGroup viewGroup) {
		List<NumberPicker> npList = new ArrayList<NumberPicker>();
		View child = null;
		if (null != viewGroup) {
			for (int i = 0; i < viewGroup.getChildCount(); i++) {
				child = viewGroup.getChildAt(i);
				if (child instanceof NumberPicker) {
					npList.add((NumberPicker) child);
				} else if (child instanceof LinearLayout) {
					List<NumberPicker> result = findNumberPicker((ViewGroup) child);
					if (result.size() > 0) {
						return result;
					}
				}
			}
		}
		return npList;
	}

	/**
	 * 
	 * 设置时间选择器的分割线颜色
	 * 
	 * @param datePicker
	 */
	public static void setDatePickerDividerColor(DatePicker datePicker) {
		// Divider changing:

		// 获取 mSpinners
		LinearLayout llFirst = (LinearLayout) datePicker.getChildAt(0);

		// 获取 NumberPicker
		LinearLayout mSpinners = (LinearLayout) llFirst.getChildAt(0);
		for (int i = 0; i < mSpinners.getChildCount(); i++) {
			NumberPicker picker = (NumberPicker) mSpinners.getChildAt(i);

			Field[] pickerFields = NumberPicker.class.getDeclaredFields();
			for (Field pf : pickerFields) {
				if (pf.getName().equals("mSelectionDivider")) {
					pf.setAccessible(true);
					try {
						pf.set(picker,
								new ColorDrawable(Color.parseColor("#ff0000")));
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (NotFoundException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
					break;
				}
			}
		}
	}

	private static void resizeNumberPicker(NumberPicker np) {
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				mwidth / 9, RadioGroup.LayoutParams.WRAP_CONTENT);
		params.setMargins(5, 0, 5, 0);
		np.setLayoutParams(params);
	}

	public static void setTestSize(DatePicker datePicker) {
		Field[] fields = DatePicker.class.getDeclaredFields();
		View v_month3 = null;
		EditText v_edit3 = null;
		// 获取DatePicker中的属性
		for (Field field : fields) {
			field.setAccessible(true);
			if (field.getType().getSimpleName().equals("NumberPicker")) {
				try {
					v_month3 = (View) field.get(datePicker);
				} catch (Exception e) {
					// Log.e(TAG, e.getMessage());
					// }
				}
			}
		}

		// 获取NumberPicker中的属性
		if (v_month3 != null) {
			fields = v_month3.getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				if (field.getType().getName().equals(EditText.class.getName())) {
					try {
						v_edit3 = (EditText) field.get(v_month3);
					} catch (Exception e) {
						// Log.e(TAG, e.getMessage());
					}
				}
			}
		}

		// 改变Month的字体大小
		if (v_edit3 != null) {
			v_edit3.setTextSize(10);
		}
	}
}
