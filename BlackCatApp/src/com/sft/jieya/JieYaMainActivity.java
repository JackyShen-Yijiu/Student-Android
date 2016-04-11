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
import android.util.Log;
import android.view.Menu;

import com.jzjf.app.R;
import com.sft.util.LogUtil;

public class JieYaMainActivity extends Activity {

	private final String TAG = "MainActivity";
	private String assertName = "ggtkFile.zip";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);

		// Log.d(TAG,
		// "Environment.getExternalStorageDirectory()="

		fileThread();
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
							doZipExtractorWork();
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

	public void doZipExtractorWork() throws IOException {

		// String[] temp = getAssets().getLocales();
		String[] temp = getAssets().list("");
		for (int i = 0; i < temp.length; i++) {
			LogUtil.print("123546-->" + temp[i]);
		}

		// Environment.getExternalStorageDirectory()

		// ZipExtractorTask task = new
		// ZipExtractorTask("/storage/usb3/system.zip",
		// "/storage/emulated/legacy/", this, true);
		String path = "/storage/emulated/0/aa/152.zip";
		File f = new File("/storage/emulated/0");
		File f1 = new File("/storage/emulated/0/aa");
		if (!f1.exists()) {
			f1.mkdir();
		}
		LogUtil.print("file--sasss->>" + path);
		ZipExtractorTask task = new ZipExtractorTask(path,
				"/storage/emulated/0/aa/", this, true);

		task.execute();
	}

	private void fileThread() {
		new Thread() {
			@Override
			public void run() {
				try {
					doFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}.start();
	}

	private void doFile() throws IOException {
		String path11 = "/storage/emulated/0/aa/test.zip";
		File file = new File(path11);
		if (!file.exists()) {
			file.createNewFile();
		}
		InputStream is = getAssets().open(assertName);

		FileOutputStream fos = new FileOutputStream(new File(path11));
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

}
