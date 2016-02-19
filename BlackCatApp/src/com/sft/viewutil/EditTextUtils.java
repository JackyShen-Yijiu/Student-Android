package com.sft.viewutil;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.widget.EditText;

public class EditTextUtils {

	public static void setEditTextHint(EditText et,String str,int size){
		// 新建一个可以添加属性的文本对象
		SpannableString ss = new SpannableString(str);
		// 新建一个属性对象,设置文字的大小
		AbsoluteSizeSpan ass = new AbsoluteSizeSpan(size,true);
		// 附加属性到文本
		ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		// 设置hint
		et.setHint(new SpannedString(ss)); // 一定要进行转换,否则属性会消失
	}
	
}
