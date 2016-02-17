/**
 *
 */
package com.sft.blackcatapp.home.i;

import android.content.Context;

/**
 * 支持主界面下方的Tab的“小红点”的显示。当MainScreen在刷新时，会遍历的调一遍实现该接口的类
 */
public interface IIndicateMainTabNotification {

	/**
	 * 当需要主界面下方的小红点显示时，返回True，否则返回False。
	 */
	boolean isNeedIndicateMainScreenTabNotification(Context context);

	/**
	 * 当前tab被选中时，是否隐藏红点，返回True，否则返回False。
	 */
	boolean removeRedPointhOnSelected(Context context);

}
