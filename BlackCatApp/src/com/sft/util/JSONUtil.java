package com.sft.util;

import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;

import com.google.gson.Gson;

/**
 * JSON操作工具�?
 * 
 * @author Jet
 * 
 */
@SuppressLint("DefaultLocale")
public class JSONUtil {

	/**
	 * JSONObject到JavaBean
	 * 
	 * @param javabean
	 *            javaBean
	 * @throws ParseException
	 *             json解析异常
	 * @throws JSONException
	 */
	public static <T> T toJavaBean(Class<T> javabean, String jsonString)
			throws Exception {
		return new Gson().fromJson(jsonString, javabean);
	}

	public static <T> T toJavaBean(Class<T> javabean, JSONObject jsonObject)
			throws Exception {
		return toJavaBean(javabean, jsonObject.toString());
	}

	public static String toJsonString(Object object) {
		return new Gson().toJson(object);
	}
}
