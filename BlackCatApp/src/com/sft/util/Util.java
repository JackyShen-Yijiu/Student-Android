package com.sft.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.sft.common.BlackCatApplication;
import com.sft.common.Config;
import com.sft.vo.CarModelVO;
import com.sft.vo.ClassVO;
import com.sft.vo.CoachVO;
import com.sft.vo.SchoolVO;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import cn.sft.sqlhelper.DBHelper;

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
		File file = new File(Config.PICPATH + File.separator + path + File.separator);
		File f = new File(Config.PICPATH + File.separator + path + File.separator + name);
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
		if (obj instanceof SchoolVO) {
			SchoolVO school = (SchoolVO) obj;
			if (app.selectEnrollSchool == null) {
				return "";
			} else {
				if (school.equals(app.selectEnrollSchool)) {
					return null;
				} else if (app.selectEnrollClass == null && app.selectEnrollCoach == null
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
					if (coach.getDriveschoolinfo().getId().equals(app.selectEnrollSchool.getSchoolid())) {
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
				} else if (coach.getDriveschoolinfo().getId()
						.equals(app.selectEnrollCoach.getDriveschoolinfo().getId())) {
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
		DBHelper.getInstance(context).delete(CoachVO.class, "db_userid", getUserid(), "db_coachStyle",
				CoachVO.APPOINTMENT_COACH, "coachid", coachVO.getCoachid());

		coachVO.setDb_userid(getUserid());
		coachVO.setDb_coachStyle(CoachVO.APPOINTMENT_COACH);
		DBHelper.getInstance(context).insert(coachVO);
	}

	public static List<CoachVO> getAppointmentCoach(Context context) {
		return DBHelper.getInstance(context).query(CoachVO.class, "db_userid", getUserid(), "db_coachStyle",
				CoachVO.APPOINTMENT_COACH);
	}

	public static CoachVO getEnrollUserSelectedCoach(Context context) {
		List<CoachVO> list = DBHelper.getInstance(context).query(CoachVO.class, "db_userid", getUserid(),
				"db_coachStyle", CoachVO.ENROLL_USER_SELECTED);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public static SchoolVO getEnrollUserSelectedSchool(Context context) {
		List<SchoolVO> list = DBHelper.getInstance(context).query(SchoolVO.class, "db_userid", getUserid(),
				"db_schoolStyle", SchoolVO.ENROLL_USER_SELECTED);

		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public static CarModelVO getEnrollUserSelectedCarStyle(Context context) {
		List<CarModelVO> list = DBHelper.getInstance(context).query(CarModelVO.class, "db_userid", getUserid(),
				"db_carmodelStyle", CarModelVO.ENROLL_USER_SELECTED);

		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public static ClassVO getEnrollUserSelectedClass(Context context) {
		List<ClassVO> list = DBHelper.getInstance(context).query(ClassVO.class, "db_userid", getUserid(),
				"db_classStyle", ClassVO.ENROLL_USER_SELECTED);

		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public static void deleteEnrollUserSelectedCoach(Context context) {
		DBHelper.getInstance(context).delete(CoachVO.class, "db_userid", getUserid(), "db_coachStyle",
				CoachVO.ENROLL_USER_SELECTED);
	}

	public static void deleteEnrollUserSelectedSchool(Context context) {
		DBHelper.getInstance(context).delete(SchoolVO.class, "db_userid", getUserid(), "db_schoolStyle",
				SchoolVO.ENROLL_USER_SELECTED);
	}

	public static void deleteEnrollUserSelectedCarStyle(Context context) {
		DBHelper.getInstance(context).delete(CarModelVO.class, "db_userid", getUserid(), "db_carmodelStyle",
				CarModelVO.ENROLL_USER_SELECTED);
	}

	public static void deleteEnrollUserSelectedClass(Context context) {
		DBHelper.getInstance(context).delete(ClassVO.class, "db_userid", getUserid(), "db_classStyle",
				ClassVO.ENROLL_USER_SELECTED);
	}

	public static void updateEnrollSchool(Context context, SchoolVO schoolVO, boolean isChecked) {
		deleteEnrollUserSelectedSchool(context);
		schoolVO.setDb_schoolStyle(SchoolVO.ENROLL_USER_SELECTED);
		schoolVO.setDb_userid(getUserid());
		DBHelper.getInstance(context).insert(schoolVO);

		int type = -1;
		// 更新了驾校后，判断是否需要更新教练，班级及车型
		CoachVO coach = getEnrollUserSelectedCoach(context);
		if (coach != null && !coach.getDriveschoolinfo().getId().equals(schoolVO.getSchoolid())) {
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

	public static void updateEnrollCoach(Context context, CoachVO coachVO, boolean isChecked) {
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

	public static void updateEnrollCarStyle(Context context, CarModelVO carModelVO) {
		deleteEnrollUserSelectedCarStyle(context);
		carModelVO.setDb_carmodelStyle(CarModelVO.ENROLL_USER_SELECTED);
		carModelVO.setDb_userid(getUserid());
		DBHelper.getInstance(context).insert(carModelVO);

		// 车型暂时与驾校没有关系，无需处理
	}
}
