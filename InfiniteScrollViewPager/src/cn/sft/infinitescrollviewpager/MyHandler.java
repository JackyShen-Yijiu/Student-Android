package cn.sft.infinitescrollviewpager;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

/**
 * 用于进行定时的操作，共4中构造方法可选择
 * 
 * @author SunFangTao
 * @Date 2014-6-12
 */
@SuppressLint("HandlerLeak")
public class MyHandler {

	private Handler handler;
	private Runnable runnable;
	private int time;
	// 定时任务是否被取消
	private boolean handlerIsCancle;
	// 是否循环执行
	private boolean isLoopRun = false;
	// 循环执行的时间间隔
	private int loopTime = 0;

	/**
	 * 立即执行重写的run方法
	 */
	public MyHandler() {
		this.time = 0;
		init(true);
	}

	/**
	 * @param isRun
	 *            true表示立即执行重写的run方法，false手动调用post执行重写的run方法
	 */
	public MyHandler(boolean isRun) {
		this.time = 0;
		init(isRun);
	}

	/**
	 * 立即循环执行重写的run方法
	 * 
	 * @param isLoopRun
	 *            是否循环执行
	 * @param loopTime
	 *            循环执行的时间间隔，不大于0时不循环
	 * @注 isLoopRun = false;loopTime<=0时需手动调用post执行
	 */
	public MyHandler(boolean isLoopRun, int loopTime) {
		if (loopTime <= 0) {
			this.time = 0;
			init(isLoopRun);
		} else {
			this.loopTime = loopTime;
			this.time = 0;
			this.isLoopRun = isLoopRun;
			init(true);
		}
	}

	/**
	 * 延迟一段时间后自动执行重写的run()方法，如果isLoopRun=true,则循环执行时间默认为延迟的时间
	 * 
	 * @param delayedTime
	 *            自动执行的延迟时间,如果不大于零，则只运行一次，不循环执行
	 */
	public MyHandler(int delayedTime, boolean isLoopRun) {
		if (delayedTime <= 0) {
			this.time = 0;
			init(isLoopRun);
		} else {
			this.time = delayedTime;
			this.loopTime = delayedTime;
			this.isLoopRun = isLoopRun;
			init(true);
		}
	}

	/**
	 * 延迟一段时间后自动执行重写的run()方法
	 * 
	 * @param delayedTime
	 *            自动执行的延迟时间
	 * @param isLoopRun
	 *            是否循环执行
	 * @param loopTime
	 *            自动执行时间间隔
	 */
	public MyHandler(int delayedTime, boolean isLoopRun, int loopTime) {
		if (loopTime <= 0) {
			new MyHandler(delayedTime, isLoopRun);
		} else if (delayedTime <= 0) {
			new MyHandler(isLoopRun, loopTime);
		} else {
			this.time = delayedTime;
			this.loopTime = loopTime;
			init(true);
			this.isLoopRun = isLoopRun;
		}
	}

	public MyHandler(int delayedTime) {
		this.time = delayedTime;
		init(true);
	}

	protected void init(boolean isRun) {

		handlerIsCancle = false;

		this.handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (!handlerIsCancle) {
					run();
				} else {
					isLoopRun = false;
				}
				if (isLoopRun) {
					handler.postDelayed(runnable, loopTime);
				}
			}
		};

		this.runnable = new Runnable() {

			@Override
			public void run() {
				Message msg = handler.obtainMessage();
				msg.sendToTarget();
			}
		};
		if (isRun) {
			this.post();
		}
	}

	/**
	 * 手动执行重写的run()方法
	 */
	public void post() {
		if (time == 0) {
			this.handler.post(this.runnable);
		} else {
			this.handler.postDelayed(this.runnable, time);
		}
	}

	/**
	 * 重写方法，执行自己的操作
	 * 
	 * @author SunFangTao
	 */
	public void run() {

	};

	/**
	 * 取消定时操作
	 */
	public void cancle() {
		handlerIsCancle = true;
		this.handler.removeCallbacks(this.runnable);
	}
}
