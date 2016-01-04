package com.sft.util;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.List;

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

	/**
	 * 把json字符串变成集合 params: new TypeToken<List<yourbean>>(){}.getType(),
	 * 
	 * @param json
	 * @param type
	 *            new TypeToken<List<yourbean>>(){}.getType()
	 * @return
	 */
	public static List<?> parseJsonToList(String json, Type type) {
		Gson gson = new Gson();
		List<?> list = gson.fromJson(json, type);
		return list;
	}
}
