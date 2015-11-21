package com.sft.viewutil;

import java.lang.reflect.Field;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

public class MyHorizontalScrollView extends HorizontalScrollView {

	public MyHorizontalScrollView(Context context) {
		super(context);
		init();
	}

	public MyHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MyHorizontalScrollView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		try {
			Class<?> c = (Class<?>) Class.forName(HorizontalScrollView.class.getName());
			Field egtLeft = c.getDeclaredField("mEdgeGlowLeft");
			Field egbRight = c.getDeclaredField("mEdgeGlowRight");
			egtLeft.setAccessible(true);
			egbRight.setAccessible(true);
			Object eglObject = egtLeft.get(this); 
			Object egrObject = egbRight.get(this);

			Class<?> cc = (Class<?>) Class.forName(eglObject.getClass()
					.getName());
			Field mGlow = cc.getDeclaredField("mGlow");
			mGlow.setAccessible(true);
			mGlow.set(eglObject, new ColorDrawable(Color.TRANSPARENT));
			mGlow.set(egrObject, new ColorDrawable(Color.TRANSPARENT));

			Field mEdge = cc.getDeclaredField("mEdge");
			mEdge.setAccessible(true);
			mEdge.set(eglObject, new ColorDrawable(Color.TRANSPARENT));
			mEdge.set(egrObject, new ColorDrawable(Color.TRANSPARENT));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
