/**
 *
 */
package com.sft.blackcatapp.home.i;

import android.view.KeyEvent;

/**
 * 如果希望Fragment能够实现按下按键时的处理，请实现该方法。
 */
public interface IOnKeyDown {
	/**
	 * Called when a key was pressed down and not handled by any of the views
	 * inside of the activity. So, for example, key presses while the cursor is
	 * inside a TextView will not trigger the event (unless it is a navigation
	 * to another object) because TextView handles its own key presses.
	 * 
	 * <p>
	 * If the focused view didn't want this event, this method is called.
	 * 
	 * <p>
	 * The default implementation takes care of {@link KeyEvent#KEYCODE_BACK} by
	 * calling {@link #onBackPressed()}, though the behavior varies based on the
	 * application compatibility mode: for
	 * {@link android.os.Build.VERSION_CODES#ECLAIR} or later applications, it
	 * will set up the dispatch to call {@link #onKeyUp} where the action will
	 * be performed; for earlier applications, it will perform the action
	 * immediately in on-down, as those versions of the platform behaved.
	 * 
	 * <p>
	 * Other additional default key handling may be performed if configured with
	 * {@link #setDefaultKeyMode}.
	 * 
	 * @see #onKeyUp
	 * @see KeyEvent
	 * 
	 * @return 若返回True，则表示Fragment可以处理，否则交给Activity来处理
	 */
	boolean onKeyDown(int keyCode, KeyEvent event);
}
