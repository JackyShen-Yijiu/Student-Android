package com.sft.jieya;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;

import com.jzjf.app.R;
import com.sft.util.LogUtil;

public class JieYaMainActivity extends Activity implements ZipCall {

	private final String TAG = "MainActivity";
	private String assertName = "ggtkFile.zip";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
		// zipCall =
		// Log.d(TAG,
		// "Environment.getExternalStorageDirectory()="

		// fileThread();
		// showUnzipDialog();
		// doZipExtractorWork();
		// doDownLoadWork();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void showUnzipDialog() {
		new AlertDialog.Builder(this).setTitle("确认").setMessage("是否解压？")
				.setPositiveButton("是", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Log.d(TAG, "onClick 1 = " + which);
						try {
							String localPathString = "";
							String targetPath = "";
							doZipExtractorWork(localPathString, localPathString);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}).setNegativeButton("否", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Log.d(TAG, "onClick 2 = " + which);
					}
				}).show();
	}

	File f = new File("/storage/emulated/0");

	/**
	 * 
	 * @param localPath
	 *            本地压缩包路径 绝对路径
	 * @param targetPath
	 *            目标解压文件路径 绝对路径
	 * @throws IOException
	 */
	public void doZipExtractorWork(String localPath, String targetPath)
			throws IOException {

		// Environment.getExternalStorageDirectory()

		File f1 = new File(localPath);
		if (!f1.exists()) {
			f1.mkdir();
		}
		File f2 = new File(targetPath);
		if (!f2.exists()) {
			f2.mkdir();
		}

		LogUtil.print("file--sasss->>" + localPath);
		ZipExtractorTask task = new ZipExtractorTask(localPath, targetPath,
				this, true, new ZipCall() {

					@Override
					public void unzipSuccess() {
						// 解压成功

					}

					@Override
					public void unzipFailed() {
						// 解压失败
					}
				});

		task.execute();
	}

	private void fileThread(final String assertName, final String targetPath,
			final Handler handler) {
		new Thread() {
			@Override
			public void run() {
				try {
					CopyFile(assertName, targetPath);
					handler.sendEmptyMessage(1);
				} catch (IOException e) {
					e.printStackTrace();
					handler.sendEmptyMessage(0);
				}
			}

		}.start();
	}

	private void CopyFile(String assertName, String targetPath)
			throws IOException {
		// String path11 = "/storage/emulated/0/aa/test.zip";
		File file = new File(targetPath);
		if (!file.exists()) {
			file.createNewFile();
		}
		InputStream is = getAssets().open(assertName);

		FileOutputStream fos = new FileOutputStream(new File(targetPath));
		byte[] buffer = new byte[1024];
		int count = 0;
		while (true) {
			count++;
			int len = is.read(buffer);
			LogUtil.print("copy-->" + count);
			if (len == -1) {
				break;
			}
			fos.write(buffer, 0, len);
		}
		is.close();
		fos.close();
		LogUtil.print("copy--end");
	}

	@Override
	public void unzipSuccess() {
		// TODO Auto-generated method stub

	}

	@Override
	public void unzipFailed() {
		// TODO Auto-generated method stub

	}
}
