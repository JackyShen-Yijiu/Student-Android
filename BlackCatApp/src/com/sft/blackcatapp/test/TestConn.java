package com.sft.blackcatapp.test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TestConn {

	public void test(){
		
	}
//	http://www.8264.com/member.php?mod=logging&action=login
//	member.php?mod=logging&action=login&loginsubmit=yes&loginhash=LC1r7
	
//	
	
	/**
	 * http://account.lvye.cn/accounts/ajax_login
	 * 必要参数:
	 * 1.username
	 * 2.password
	 * 3.cap_code
	 * 
	 * 3.csrfmiddlewaretoken
	 * 4.next
	 * 5.client_id  1
	 * 6.state  None
	 * 7.cap_key
	 * 8.login_code
	 * 
	 * cookies:
	Ryow_c470_saltkey=WKTNkcfT
	Ryow_c470_lastvisit=1458192789
	Ryow_c470_sid=JK8TbA
	Ryow_c470_sendmail=1
	Ryow_c470_lastact=1458196389%09getstatus.php%09
	Hm_lvt_d115ff5d8612d97fba465e8c30ea73b7=1457150245,1457526637,1457575280,1458196412
	Hm_lpvt_d115ff5d8612d97fba465e8c30ea73b7=1458196420
	_gat=1
	csrftoken=VAsWYS8Ngcpwt8YWWYnO0aIg5ZRL11OY
	sessionid=uz1gc621wyrj9tkylniqo0syzhj7m6iu
	_zg=%7B%22uuid%22%3A%20%2215344c1ebb928b-0d69041be-3396b70-384000-15344c1ebba565%22%2C%22sid%22%3A%201458196411.82%2C%22updated%22%3A%201458196580.004%2C%22info%22%3A%201458196411826%2C%22cuid%22%3A%20%22%22%7D
	_ga=GA1.2.701831736.1457147473
	 */
	
	/**
	 * post： body:
	 * username=sunqimin09%40163.com&password=sqm0417.&cap_code=&auto=on&csrfmiddlewaretoken=VAsWYS8Ngcpwt8YWWYnO0aIg5ZRL11OY&next=http%3A%2F%2Fwww.lvye.cn%2F&client_id=1&state=None&cap_key=&login_code=
	 */
	private Map<String, String> postInit(String name,String pwd,String token){
		Map<String, String> map = new HashMap<String, String>();
		map.put("username", "");
		map.put("password", "");
		map.put("cap_code", "");
		map.put("auto", "on");
		map.put("csrfmiddlewaretoken", token);
		map.put("next", "http://www.lvye.cn/");
		map.put("cliend_id", "1");
		map.put("state", "None");
		map.put("cap_key", "");
		map.put("login_code", "");
		return map;
	}
	
	private void Login(String url,String name,String pwd){
		Map<String, String> map = postInit(name, pwd, "");
		System.out.print("post-->"+HttpPost(url, map));
	}
	
	private static final String charsetName = "UTF-8";
	private static final String UA = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.152 Safari/537.36";
	private static final String BOUNDARY = UUID.randomUUID().toString(); // 边界标识
																			// 随机生成
	private static final String LINE_END = "\r\n";
	
	/**	 * 使用POST提交，Map<String, String> map参数	 * @param urlString	 * @param map	 * @return	 */
	public static String HttpPost(String urlString, Map<String, String> map) {
		String result = "";
		try {
			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();

			connection.setRequestMethod("POST");
			connection.setReadTimeout(60 * 1000 * 2);
			connection.setConnectTimeout(60 * 1000 * 2);
			connection.setUseCaches(false);
			connection.setDoOutput(true);
			connection.setDoInput(true);

			String data = getParamData(map);

			connection.setRequestProperty("Connection", "keep-alive");
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Length",
					String.valueOf(data.getBytes().length));
			connection.setRequestProperty("User-Agent", UA);

			OutputStream os = connection.getOutputStream();
			os.write(data.getBytes());
			os.flush();
			os.close();
			if (connection.getResponseCode() == 200) {
				InputStream is = connection.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader bf = new BufferedReader(isr);
				String line = "";
				StringBuffer stringBuffer = new StringBuffer();
				while ((line = bf.readLine()) != null) {
					stringBuffer.append(line);
				}
				result = stringBuffer.toString();
				is.close();
				isr.close();
				bf.close();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**	 * Map<String, String> map转换成 a=b&c=d&e=f...	 * @param map	 * @return	 */
	public static String getParamData(Map<String, String> map) {

		String data = "";
		int i = 1;

		try {
			for (String key : map.keySet()) {
				System.out.println(key + ":" + map.get(key));
				if (i == map.size()) {
					data += key + "="
							+ URLEncoder.encode(map.get(key), charsetName);
				} else {
					data += key + "="
							+ URLEncoder.encode(map.get(key), charsetName)
							+ "&";
					i++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	/**	 * 获取网页内容	 * @param urlString	 * @return	 */
	public static String getHtmlAsGET(String urlString) {
		String result = "";
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		StringBuffer stringBuffer = new StringBuffer();
		String line = null;
		try {
			URL url = new URL(urlString);
			URLConnection connection = url.openConnection();
			HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
			httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
			httpURLConnection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			
//			httpURLConnection.getres
			inputStream = httpURLConnection.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(inputStreamReader);

			while ((line = bufferedReader.readLine()) != null) {
				stringBuffer.append(line);
			}
			result = stringBuffer.toString();
			inputStream.close();
			inputStreamReader.close();
			bufferedReader.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static InputStream getInputStream(String urlString) {
		InputStream inputStream = null;
		try {
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			return conn.getInputStream();
		} catch (MalformedURLException e1) {

		} catch (IOException e) {

		}
		return inputStream;
	}
	
	/**	 * 参数一是String URL，参数二是Map<String, String>	 * @param urlString	 * @param param	 * @return	 */
	public static String upFile(String urlString,Map<String, String> param) {
		return upFile(urlString, null, null, null, param);
	}
	/**	 * 可上传一张图片	 * @param urlString	 * @param filePath	 * @param key	 * @return	 */
	public static String upFile(String urlString, String filePath, String key) {
		return upFile(urlString, filePath, key, null, null);
	}
	/**	 * 可上一张图片String filePath，多个POST参数Map<String, String>	 * @param urlString	 * @param filePath	 * @param key	 * @param param	 * @return	 */
	public static String upFile(String urlString, String filePath, String key, Map<String, String> param) {
		return upFile(urlString, filePath, key, null, param);
	}
	/**	 * 可上传多张图片List<String> imgList	 * @param urlString	 * @param imgList	 * @param key	 * @return	 */
	public static String upFile(String urlString, List<String> imgList, String key) {
		return upFile(urlString, null, key, imgList, null);
	}
	/**	 * 可上传多张图片List<String> imgList，多个参数Map<String, String>	 * @param urlString	 * @param imgList	 * @param key	 * @param param	 * @return	 */
	public static String upFile(String urlString, List<String> imgList, String key, Map<String, String> param) {
		return upFile(urlString, null, key, imgList, param);
	}
	/**	 * 	 * @param urlString 提交的URL	 * @param filePath 图片路径	 * @param key 上传图片的name，多张图片是为name1,name2,name3...，单张图片时,name就是name不变	 * @param imgList 图片路径数组	 * @param param POST参数 Map<String, String> param	 * @return 提交后返回的结果	 */
	public static String upFile(String urlString, String filePath, String key,
			List<String> imgList, Map<String, String> param) {
		String result = "";
		try {
			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestProperty("Charest", "UTF-8");
			connection.setRequestProperty("connection", "keep-alive");
			connection.setRequestProperty("User-Agent", UA);
			connection.setRequestProperty("Content-Type", "multipart/form-data"
					+ ";boundary=" + BOUNDARY);

			DataOutputStream dos = new DataOutputStream(
					connection.getOutputStream());

			if (param != null) {
				for (String keys : param.keySet()) {
					String line = "--" + BOUNDARY + LINE_END;
					
					line += "Content-Disposition: form-data; name=\"" + keys + "\"" + LINE_END + LINE_END;
					line += param.get(keys)+LINE_END;
					dos.write(line.getBytes());
				}
			}
			
			if (imgList != null) {
				doDataOutputStream(dos, imgList, key);
			} else if(filePath != null){
				File file = new File(filePath);
				String line = "--" + BOUNDARY + LINE_END;
				line += "Content-Disposition: form-data; name=\"" + key
						+ "\"; filename=\"" + file.getName() + "\"" + LINE_END;
				line += "Content-Type:image/pjpeg" + LINE_END;
				line += LINE_END;

				dos.write(line.getBytes());

				byte[] bytes = new byte[1024];
				int len = 0;

				InputStream fileIs = new FileInputStream(file);
				while ((len = fileIs.read(bytes)) != -1) {
					dos.write(bytes, 0, len);
				}
				fileIs.close();

				dos.write(LINE_END.getBytes());
				byte[] endByte = ("--" + BOUNDARY + "--" + LINE_END).getBytes();
				dos.write(endByte);
				dos.flush();
				dos.close();
			}

			if (connection.getResponseCode() == 200) {
				result = getInputStreamReaderToString(connection
						.getInputStream());
				System.out.println("Http.UploadFile:" + result);
			}else {
				System.out.println("Http.UploadFile:ERROR:");
			}
		} catch (MalformedURLException e) {
			System.out.println(e.toString());
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return result;
	}

	
	/**	 * DataOutputStream，图片路径数组，图片name	 * @param dos	 * @param imgList	 * @param key Content-Disposition: form-data; name=\"" + key+i	 */
	public static void doDataOutputStream(DataOutputStream dos,
			List<String> imgList, String key) {

		try {
			for (int i = 0; i < imgList.size(); i++) {
				File file = new File(imgList.get(i));

				String line = "--" + BOUNDARY + LINE_END;
				line += "Content-Disposition: form-data; name=\"" + key+i
						+ "\"; filename=\"" + file.getName() + "\"" + LINE_END;
				line += "Content-Type:image/pjpeg" + LINE_END;
				line += LINE_END;
				dos.write(line.getBytes());

				byte[] bytes = new byte[1024];
				int len = 0;

				InputStream fileIs = new FileInputStream(file);
				while ((len = fileIs.read(bytes)) != -1) {
					dos.write(bytes, 0, len);
				}
				fileIs.close();

				dos.write(LINE_END.getBytes());
			}

			byte[] endByte = ("--" + BOUNDARY + "--" + LINE_END).getBytes();
			dos.write(endByte);
			dos.flush();
			dos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**	 * InputStream 转 字符串	 * @param is	 * @return	 */
	public static String getInputStreamReaderToString(InputStream is) {
		String result = "";
		try {
			StringBuffer sb = new StringBuffer();
			String l = null;
			BufferedReader bfr = new BufferedReader(new InputStreamReader(is));
			while ((l = bfr.readLine()) != null) {
				sb.append(l);
			}
			result = sb.toString();
			is.close();
			bfr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
