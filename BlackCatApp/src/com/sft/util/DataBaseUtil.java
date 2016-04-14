package com.sft.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataBaseUtil {

	/**
	 * 从assert中获取数据库到内存中
	 * 
	 * @param context
	 * @return
	 */
	public static SQLiteDatabase openDatabase(Context context) {

		// 数据库存储路径 getPackageName().replace(".", "")-- 数据库名字
		String filePath = "data/data/" + context.getPackageName()
				+ "/databases/" + context.getPackageName().replace(".", "");
		// 数据库存放的文件夹 data/data/com.main.jh 下面
		String pathStr = "data/data/" + context.getPackageName() + "/databases";

		SQLiteDatabase database;
		File jhPath = new File(filePath);
		// 查看数据库文件是否存在
		if (jhPath.exists()) {
			// 存在则直接返回打开的数据库
			return SQLiteDatabase.openOrCreateDatabase(jhPath, null);
		} else {
			// 不存在先创建文件夹
			File path = new File(pathStr);
			if (path.mkdir()) {
				Log.i("test", "创建成功" + jhPath);
			} else {
				Log.i("test", "创建失败" + jhPath);
			}
			try {
				// 得到资源
				AssetManager am = context.getAssets();
				// 得到数据库的输入流
				InputStream is = am.open("test.db");
				// 用输出流写到SDcard上面
				FileOutputStream fos = new FileOutputStream(jhPath);
				// 创建byte数组 用于1KB写一次
				byte[] buffer = new byte[1024];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				// 最后关闭就可以了
				fos.flush();
				fos.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			// 如果没有这个数据库 我们已经把他写到SD卡上了，然后在执行一次这个方法 就可以返回数据库了
			return openDatabase(context);
		}
	}

	public static List getArrays(SQLiteDatabase db, Class cls, String sql,
			String[] paramsvalues) {
		Field[] fields = cls.getDeclaredFields();
		String[] names = new String[fields.length];
		for (int i = 0; i < names.length; i++) {
			Field f = fields[i];
			f.setAccessible(true);
			fields[i].setAccessible(true);
			names[i] = f.getName();
		}

		String tableName = cls.getSimpleName();
		List entities = new ArrayList();
		LogUtil.print("sql-->" + sql);
		Cursor cursor = db.rawQuery(sql, paramsvalues);

		try {

			while (cursor.moveToNext()) {
				Object entity = cls.newInstance();
				for (Field f : fields) {
					f.setAccessible(true);
					// /用反射来调用相应的方法
					// /先构造出方法的名字
					String typeName = f.getType().getSimpleName();
					// /int --> Int,doble--->Double
					typeName = typeName.substring(0, 1).toUpperCase()
							+ typeName.substring(1);
					// /cuosor 的方法的名字
					String methodName = "get" + typeName;
					// /得到方法
					Method method = cursor.getClass().getMethod(methodName,
							int.class);
					Object retValue = method.invoke(cursor,
							cursor.getColumnIndex(f.getName()));
					f.set(entity, retValue);
					// f.set(entity, cursor.)
				}
				entities.add(entity);
			}

		} catch (NullPointerException ex) {
			ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				db.close();
			}
		}

		return entities;
	}

	public static void updateArray(SQLiteDatabase db, List list)
			throws IllegalArgumentException, IllegalAccessException {
		LogUtil.print("error---"+list.size());
		if (list == null || list.size() == 0) {
			return;
		}

		
		Object obj = list.get(0);
		// /根据对象的类型来确定数据表
		String table = obj.getClass().getSimpleName();
		// /根据类的成员变量来确定要查询那些列
		Field[] fields = obj.getClass().getDeclaredFields();
		String[] columns = new String[fields.length];
		// /初始化列名
		for (int i = 0; i < columns.length; i++) {
			fields[i].setAccessible(true);
			columns[i] = fields[i].getName();
		}

		for (int i = 0; i < list.size(); i++) {
			Object o = list.get(i);
			ContentValues val = new ContentValues();
			for (int j = 0; j < columns.length; j++) {
				Class<?> type = fields[j].getType();
				if (type == double.class) {
					double value = fields[j].getDouble(o);
					val.put(columns[j], value);
				} else if (type == int.class) {
					int value = fields[j].getInt(o);
					val.put(columns[j], value);
				} else {
					String value = String.valueOf(fields[j].get(o));
					val.put(columns[j], value);
				}
			}

			LogUtil.print(val.toString()+"error---"+table);
			db.insert(table, null, val);
		}
	}
}
