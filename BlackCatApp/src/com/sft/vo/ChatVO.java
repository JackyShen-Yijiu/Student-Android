package com.sft.vo;

import android.graphics.Bitmap;
import cn.sft.sqlhelper.DBVO;

public class ChatVO extends DBVO {

	private static final long serialVersionUID = 1L;
	// 类型
	private String style; // 0 other,1 my, 其他为时间
	// 头像
	private Bitmap bitmap;
	// 内容
	private String content;

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
