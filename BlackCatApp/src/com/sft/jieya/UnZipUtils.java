package com.sft.jieya;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;

import com.sft.util.LogUtil;

/**
 * 文件复制 文件解压
 * 
 * @author aa
 * 
 */
public class UnZipUtils {

	public static String assertName = "ggtkFile.zip";

	public static String targetPath;

	/** 本地文件的解压 */
	public static String localPath;

	public UnZipUtils() {
		String Path = Environment.getExternalStorageDirectory() + "/jzjf/img";
		targetPath = Path + "/a.zip";
	}

	public void createDir() {
		String Path = Environment.getExternalStorageDirectory() + "/jzjf/img";
		File f = new File(Path);
		if (!f.exists()) {
			f.mkdirs();
		}

		File f1 = new File(targetPath);
		if (!f1.exists()) {
			try {
				f1.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// 解压目标 文件
		localPath = Path + "/a";

		File f2 = new File(localPath);
		if (!f2.exists()) {
			f2.mkdirs();
		}
	}

	// 复制文件

	public void CopyFileThread(final Context context, final String assertName,
			final String targetPath, final Handler handler) {
		new Thread() {
			@Override
			public void run() {
				try {
					CopyFile(context, assertName, targetPath);
					handler.sendEmptyMessage(1);
				} catch (IOException e) {
					e.printStackTrace();
					handler.sendEmptyMessage(0);
				}
			}

		}.start();
	}

	private void CopyFile(Context context, String assertName, String targetPath)
			throws IOException {
		// String path11 = "/storage/emulated/0/aa/test.zip";
		LogUtil.print(assertName + "--copy-->" + targetPath);
		File file = new File(targetPath);
		if (!file.exists()) {
			file.createNewFile();
		}
		InputStream is = context.getAssets().open(assertName);

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

	/**
	 * 
	 * @param localPath
	 *            本地压缩包路径 绝对路径
	 * @param targetPath
	 *            目标解压文件路径 绝对路径
	 * @throws IOException
	 */
	public void doZip(Context context, String localPath, String targetPath,
			ZipCall call) throws IOException {

		// Environment.getExternalStorageDirectory()

		File f1 = new File(localPath);
		if (!f1.exists()) {
			f1.mkdir();
		}
		File f2 = new File(targetPath);
		if (!f2.exists()) {
			f2.mkdir();
		}

		LogUtil.print("res-file--zip->>" + localPath);
		LogUtil.print("target-path-zip->>" + targetPath);
		ZipExtractorTask task = new ZipExtractorTask(localPath, targetPath,
				context, true, call);
		// new ZipCall() {
		//
		// @Override
		// public void unzipSuccess() {
		// // 解压成功
		// LogUtil.print("zip->>成功");
		// }
		//
		// @Override
		// public void unzipFailed() {
		// // 解压失败
		// }
		// });

		task.execute();
	}

}
