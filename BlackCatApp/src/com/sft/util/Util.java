package com.sft.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import cn.sft.sqlhelper.DBHelper;

import com.sft.common.BlackCatApplication;
import com.sft.common.Config;
import com.sft.vo.CarModelVO;
import com.sft.vo.ClassVO;
import com.sft.vo.CoachVO;
import com.sft.vo.SchoolVO;
import com.sft.vo.questionbank.TitleVO;
import com.sft.vo.questionbank.error_book;
import com.sft.vo.questionbank.web_note;

@SuppressLint("SimpleDateFormat")
public class Util {

	private static BlackCatApplication app;

	public interface EnrollInfoChangedListener {
		public static final int schoolChange = 0;
		public static final int coachChange = 1;
		public static final int classChange = 2;
		public static final int carStyleChange = 3;

		void enrollInfoChange(int type);
	}

	private static String getUserid() {
		if (app == null) {
			app = BlackCatApplication.getInstance();
		}
		if (app.userVO == null)
			return "";
		return app.userVO.getUserid();
	}

	public static File savePic(Bitmap bitmap, String path, String name) {
		File file = new File(Config.PICPATH + File.separator + path
				+ File.separator);
		File f = new File(Config.PICPATH + File.separator + path
				+ File.separator + name);
		try {
			if (!file.exists()) {
				file.mkdirs();
			}
			FileOutputStream out = new FileOutputStream(f);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.flush();
			out.close();
			return f;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String isConfilctEnroll(Object obj) {
		if (app == null) {
			app = BlackCatApplication.getInstance();
		}
		if (obj instanceof SchoolVO) {
			SchoolVO school = (SchoolVO) obj;
			if (app.selectEnrollSchool == null) {
				return "";
			} else {
				if (school.equals(app.selectEnrollSchool)) {
					return null;
				} else if (app.selectEnrollClass == null
						&& app.selectEnrollCoach == null
						&& app.selectEnrollCarStyle == null) {
					return "确定要更换报名的驾校？";
				} else {
					return "更换驾校后，将删除您之前对教练，车型，班级做过的选择，请确认！all";
				}
			}
		} else if (obj instanceof CoachVO) {
			CoachVO coach = (CoachVO) obj;
			if (app.selectEnrollCoach == null) {
				if (app.selectEnrollSchool == null) {
					// 之前没有选择过驾校和教练
					return "refresh";
				} else {
					if (coach.getDriveschoolinfo().getId()
							.equals(app.selectEnrollSchool.getSchoolid())) {
						// 选择了驾校没有选择教练
						return "";
					} else {
						return "当前选择的教练与您选择的驾校冲突，确定要选择此教练并更换驾校？";
					}
				}
			} else {
				if (coach.equals(app.selectEnrollCoach)) {
					// 同一个教练，无需更新
					return null;
				} else if (coach
						.getDriveschoolinfo()
						.getId()
						.equals(app.selectEnrollCoach.getDriveschoolinfo()
								.getId())) {
					// 不同教练，同一个学校
					return "确定要更换报名的教练？";
				}
				// 当前选择的教练与选择的驾校冲突，所有更新
				return "当前选择的教练与您选择的驾校冲突，确定要选择此教练并更换驾校？all";
			}
		}
		return null;
	}

	public static void saveAppointmentCoach(Context context, CoachVO coachVO) {
		// 如果有此教练先删除，在添加，保证顺序（后预约的在最前面）
		DBHelper.getInstance(context).delete(CoachVO.class, "db_userid",
				getUserid(), "db_coachStyle", CoachVO.APPOINTMENT_COACH,
				"coachid", coachVO.getCoachid());

		coachVO.setDb_userid(getUserid());
		coachVO.setDb_coachStyle(CoachVO.APPOINTMENT_COACH);
		DBHelper.getInstance(context).insert(coachVO);
	}

	public static List<CoachVO> getAppointmentCoach(Context context) {
		return DBHelper.getInstance(context).query(CoachVO.class, "db_userid",
				getUserid(), "db_coachStyle", CoachVO.APPOINTMENT_COACH);
	}

	public static CoachVO getEnrollUserSelectedCoach(Context context) {
		List<CoachVO> list = DBHelper.getInstance(context).query(CoachVO.class,
				"db_userid", getUserid(), "db_coachStyle",
				CoachVO.ENROLL_USER_SELECTED);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public static SchoolVO getEnrollUserSelectedSchool(Context context) {
		List<SchoolVO> list = DBHelper.getInstance(context).query(
				SchoolVO.class, "db_userid", getUserid(), "db_schoolStyle",
				SchoolVO.ENROLL_USER_SELECTED);

		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public static CarModelVO getEnrollUserSelectedCarStyle(Context context) {
		List<CarModelVO> list = DBHelper.getInstance(context).query(
				CarModelVO.class, "db_userid", getUserid(), "db_carmodelStyle",
				CarModelVO.ENROLL_USER_SELECTED);

		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public static ClassVO getEnrollUserSelectedClass(Context context) {
		List<ClassVO> list = DBHelper.getInstance(context).query(ClassVO.class,
				"db_userid", getUserid(), "db_classStyle",
				ClassVO.ENROLL_USER_SELECTED);

		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public static void deleteEnrollUserSelectedCoach(Context context) {
		DBHelper.getInstance(context).delete(CoachVO.class, "db_userid",
				getUserid(), "db_coachStyle", CoachVO.ENROLL_USER_SELECTED);
	}

	public static void deleteEnrollUserSelectedSchool(Context context) {
		DBHelper.getInstance(context).delete(SchoolVO.class, "db_userid",
				getUserid(), "db_schoolStyle", SchoolVO.ENROLL_USER_SELECTED);
	}

	public static void deleteEnrollUserSelectedCarStyle(Context context) {
		DBHelper.getInstance(context).delete(CarModelVO.class, "db_userid",
				getUserid(), "db_carmodelStyle",
				CarModelVO.ENROLL_USER_SELECTED);
	}

	public static void deleteEnrollUserSelectedClass(Context context) {
		DBHelper.getInstance(context).delete(ClassVO.class, "db_userid",
				getUserid(), "db_classStyle", ClassVO.ENROLL_USER_SELECTED);
	}

	public static void updateEnrollSchool(Context context, SchoolVO schoolVO,
			boolean isChecked) {
		deleteEnrollUserSelectedSchool(context);
		schoolVO.setDb_schoolStyle(SchoolVO.ENROLL_USER_SELECTED);
		schoolVO.setDb_userid(getUserid());
		DBHelper.getInstance(context).insert(schoolVO);

		int type = -1;
		// 更新了驾校后，判断是否需要更新教练，班级及车型
		CoachVO coach = getEnrollUserSelectedCoach(context);
		if (coach != null
				&& !coach.getDriveschoolinfo().getId()
						.equals(schoolVO.getSchoolid())) {
			// 已选择的教练不在当前的驾校，删除
			deleteEnrollUserSelectedCoach(context);
			type = EnrollInfoChangedListener.coachChange;
		}
		ClassVO classVO = getEnrollUserSelectedClass(context);
		if (classVO != null) {
			// 删除相应学校的班级
			deleteEnrollUserSelectedClass(context);
			type = EnrollInfoChangedListener.classChange;
		}
		CarModelVO carModel = getEnrollUserSelectedCarStyle(context);
		if (carModel != null) {
			// 删除相应学校的车型
			deleteEnrollUserSelectedCarStyle(context);
			type = EnrollInfoChangedListener.carStyleChange;
		}

		// 发送通知
		if (type >= 0 && context instanceof EnrollInfoChangedListener) {
			EnrollInfoChangedListener listener = (EnrollInfoChangedListener) context;
			listener.enrollInfoChange(type);
		}
	}

	public static void updateEnrollSchool(Context context, SchoolVO schoolVO) {
		updateEnrollSchool(context, schoolVO, true);
	}

	public static void updateEnrollCoach(Context context, CoachVO coachVO,
			boolean isChecked) {
		deleteEnrollUserSelectedCoach(context);
		coachVO.setDb_coachStyle(CoachVO.ENROLL_USER_SELECTED);
		coachVO.setDb_userid(getUserid());
		DBHelper.getInstance(context).insert(coachVO);

		if (!isChecked) {
			return;
		}
		// 判断相应的学校信息
		SchoolVO school = getEnrollUserSelectedSchool(context);
		String schoolId = coachVO.getDriveschoolinfo().getId();
		if (school == null || !school.getSchoolid().equals(schoolId)) {
			SchoolVO updateSchool = new SchoolVO();
			updateSchool.setSchoolid(schoolId);
			updateSchool.setName(coachVO.getDriveschoolinfo().getName());
			updateEnrollSchool(context, updateSchool);

			// 发送通知
			if (context instanceof EnrollInfoChangedListener) {
				EnrollInfoChangedListener listener = (EnrollInfoChangedListener) context;
				listener.enrollInfoChange(EnrollInfoChangedListener.schoolChange);
			}
		}
	}

	public static void updateEnrollCoach(Context context, CoachVO coachVO) {
		updateEnrollCoach(context, coachVO, true);
	}

	public static void updateEnrollClass(Context context, ClassVO classVO) {
		deleteEnrollUserSelectedClass(context);
		classVO.setDb_classStyle(ClassVO.ENROLL_USER_SELECTED);
		classVO.setDb_userid(getUserid());
		DBHelper.getInstance(context).insert(classVO);

		// 课程更换只能在当前选择的驾校中操作，此处无需处理
	}

	public static void updateEnrollCarStyle(Context context,
			CarModelVO carModelVO) {
		deleteEnrollUserSelectedCarStyle(context);
		carModelVO.setDb_carmodelStyle(CarModelVO.ENROLL_USER_SELECTED);
		carModelVO.setDb_userid(getUserid());
		DBHelper.getInstance(context).insert(carModelVO);

		// 车型暂时与驾校没有关系，无需处理
	}

	/**
	 * 判断两个日期是否在同一天
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameDate(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);

		boolean isSameYear = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
		boolean isSameMonth = isSameYear
				&& cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
		boolean isSameDate = isSameMonth
				&& cal1.get(Calendar.DAY_OF_MONTH) == cal2
						.get(Calendar.DAY_OF_MONTH);

		return isSameDate;
	}

	/**
	 * ---// 查询科目一所有题
	 * */

	public static List<web_note> getAllSubjectOneBank() {
		SQLiteDatabase db = DataBaseUtil.openDatabase(BlackCatApplication
				.getInstance());
		String sql = "SELECT * FROM web_note where kemu =?   and (strTppe=? or strTppe=? or strTppe=? or strTppe=?) order by id";
		List<web_note> list = DataBaseUtil.getArrays(db, web_note.class, sql,
				new String[] { "1", "01", "02", "03", "04" });
		db.close();
		return list;
	}

	/**
	 * ---// 查询科目四所有题
	 * */

	public static List<web_note> getAllSubjectFourBank() {
		SQLiteDatabase db = DataBaseUtil.openDatabase(BlackCatApplication
				.getInstance());
		String sql = "SELECT * FROM web_note where kemu =?   and (strTppe=? or strTppe=? or strTppe=? or strTppe=? or strTppe=? or strTppe=? or strTppe=?) order by id";
		List<web_note> list = DataBaseUtil.getArrays(db, web_note.class, sql,
				new String[] { "4", "01", "02", "03", "04", "05", "06", "07" });
		db.close();
		return list;
	}

	/**
	 * ---// 查询科目一 章节题
	 * */

	public static List<web_note> getSubjectOneQuestionWithChapter(String chapter) {
		SQLiteDatabase db = DataBaseUtil.openDatabase(BlackCatApplication
				.getInstance());
		String sql = "SELECT * FROM web_note where kemu =?   and strTppe=? order by id";
		List<web_note> list = DataBaseUtil.getArrays(db, web_note.class, sql,
				new String[] { "1", chapter });
		db.close();
		return list;
	}

	/**
	 * ---// 查询科目四 章节题
	 * */

	public static List<web_note> getSubjectFourQuestionWithChapter(
			String chapter) {
		SQLiteDatabase db = DataBaseUtil.openDatabase(BlackCatApplication
				.getInstance());
		String sql = "SELECT * FROM web_note where kemu =?   and strTppe=? order by id";
		List<web_note> list = DataBaseUtil.getArrays(db, web_note.class, sql,
				new String[] { "4", chapter });
		db.close();
		return list;
	}

	/**
	 * ---// 查询科目一 章节
	 * */

	public static List<TitleVO> getSubjectOneChapter() {
		SQLiteDatabase db = DataBaseUtil.openDatabase(BlackCatApplication
				.getInstance());
		String sql = "SELECT"
				+ " c.title as title ,c.id as id, '0'||c.mid as mid, count(c.id) as count"
				+ " FROM Chapter c, web_note w WHERE c.kemu = 1 AND w.kemu=1"
				+ " AND (c.id = 1 OR c.id = 2 OR c.id = 3 OR c.id = 4) AND w.chapterid = c.id"
				+ " AND (w.strTppe = '01' OR w.strTppe = '02' OR w.strTppe = '03' OR w.strTppe = '04')"
				+ " GROUP BY c.title,c.id ORDER BY c.id";
		List<TitleVO> list = DataBaseUtil.getArrays(db, TitleVO.class, sql,
				new String[] {});
		LogUtil.print(list.size() + "sssssssssssss222");
		db.close();
		return list;
	}

	/**
	 * ---// 查询科目四 章节
	 * */

	public static List<TitleVO> getAllSubjectFourChapter() {
		SQLiteDatabase db = DataBaseUtil.openDatabase(BlackCatApplication
				.getInstance());
		String sql = "SELECT c.title as title ,c.id as id, c.mid as mid, count(c.id) as count"
				+ " FROM (select '0'||mid as mid ,title ,id ,kemu FROM Chapter where kemu=4 AND fid=0 and ( mid<>8 )) c,"
				+ " web_note w WHERE c.kemu = 4 AND w.kemu=4" +

				" AND w.strTppe = c.mid GROUP BY c.title,c.id, c.mid ORDER BY c.id";
		List<TitleVO> list = DataBaseUtil.getArrays(db, TitleVO.class, sql,
				new String[] {});
		db.close();
		return list;
	}

	/** 插入错题集合 */
	public static void insertErrorBank(error_book error) {
		SQLiteDatabase db = DataBaseUtil.openDatabase(BlackCatApplication
				.getInstance());
		List<error_book> list = new ArrayList<error_book>();
		list.add(error);
		try {
			DataBaseUtil.updateArray(db, list);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} finally {
			db.close();
		}
		db.close();

	}

	/**
	 * 查询科目一所有的错题
	 * 
	 * @return
	 */
	public static List<web_note> getAllSubjectOneErrorQuestion() {
		SQLiteDatabase db = DataBaseUtil.openDatabase(BlackCatApplication
				.getInstance());
		String sql = "select * from web_note w error_book e where kemu=? and e.webnoteid = w.id";
		List<web_note> list = DataBaseUtil.getArrays(db, web_note.class, sql,
				new String[] { "1" });
		db.close();
		return list;
	}

	/**
	 * 查询科目四所有的错题
	 * 
	 * @return
	 */
	public static List<web_note> getAllSubjectFourErrorQuestion() {
		SQLiteDatabase db = DataBaseUtil.openDatabase(BlackCatApplication
				.getInstance());
		String sql = "select * from web_note w ,error_book e where w.kemu=? and e.kemu=? and e.webnoteid = w.id";
		List<web_note> list = DataBaseUtil.getArrays(db, web_note.class, sql,
				new String[] { "4", "4" });
		db.close();
		return list;
	}
}
