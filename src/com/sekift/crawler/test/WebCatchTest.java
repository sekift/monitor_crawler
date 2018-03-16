package com.sekift.crawler.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created with IntelliJ IDEA. User: lujianing Date: 13-11-21 Time: 下午3:42 To
 * change this template use File | Settings | File Templates.
 */
public class WebCatchTest {

	private static String tempDir = "d://JQuery//懒人之家//";

	private String catch_url;

	public String getCatch_url() {
		return catch_url;
	}

	public WebCatchTest(String url) {
		catch_url = url;
	}

	public static void main(String[] args) throws IOException {
		// Jquery特效 15页
		for (int i = 4; i <= 4; i++) {
			String str = "http://www.lanrenzhijia.com/jquery/list_5_" + i + ".html";
			WebCatchTest w = new WebCatchTest(str);
			w.run();
		}
	}

	public void run() {
		getListPage(this.getCatch_url());
	}

	public static void getListPage(String url) {
		try {
			Document document = Jsoup.connect(url).userAgent("Mozilla").get(); // 处理首页
			Elements htmls = document.select("div.listbox .newyear a");
			String html = "";
			for (Element element : htmls) {
				html = element.attr("href");
				if (html != null && !html.equals("")) {
					getPage(html);
				}
			}
		} catch (IOException e) {
		}
	}

	public static void getPage(String url) {
		String file_name = "";
		String img_path = "";
		String rar_path = "";
		try {
			Document document = Jsoup.connect(url).userAgent("Mozilla").get(); // 处理首页
			Elements titleLinkElements = document.select("title");
			for (Element element : titleLinkElements) {
				file_name = element.text();
				file_name = file_name.substring(0, file_name.length() - 5);
				System.out.println("资源名:" + file_name);
			}
			Elements imgs = document.select("div.infolist img");
			for (Element element : imgs) {
				img_path = element.attr("src");
				System.out.println("资源图片:" + img_path);
			}
			Elements rarElements = document.select("div.actbox a");
			for (Element element : rarElements) {
				String rar = element.attr("href");
				if (rar.length() > 4) {
					String extt = rar.substring(rar.length() - 3, rar.length());
					if ((extt.equals("rar") || extt.equals("zip"))) {
						System.out.println("资源压缩包：" + rar);
						rar_path = rar;
					}
				}
			}
			
			download(file_name, img_path, "img");
			download(file_name, rar_path, "rar");
		} catch (IOException e) {
		}
	}

	public static void download(String dir, String file, String type) {
		String fileDirPath = tempDir + "/" + dir;
		String filePath = tempDir + "//" + dir + "//" + dir;
		
		if(type.equals("img")){
			filePath = filePath+".jpg"; 
		}else if(type.equals("rar")){
			filePath = filePath+".rar";
		}
		File firdir = new File(fileDirPath);
		if (!firdir.exists()) {
			firdir.mkdirs();
		}

		int bytesum = 0;
		int byteread = 0;
		URL url = null;
		FileOutputStream fs = null;
		try {
			url = new URL(file);
			URLConnection conn = url.openConnection();
			InputStream inStream = conn.getInputStream();
			fs = new FileOutputStream(filePath);

			byte[] buffer = new byte[1024];
			while ((byteread = inStream.read(buffer)) != -1) {
				bytesum += byteread;
				fs.write(buffer, 0, byteread);
			}
			System.out.println(file + "下载完成");
		} catch (Exception e) {
		} finally {
			try {
				fs.flush();
				fs.close();
			} catch (Exception e) {
			}
		}
	}

	public static void downloadImg(String dir, String img) {

		File firdir = new File(tempDir + "/" + dir);
		if (!firdir.exists()) {
			firdir.mkdirs();
		}

		int bytesum = 0;
		int byteread = 0;

		URL url = null;
		try {
			url = new URL(img);
		} catch (Exception e) {

		}

		FileOutputStream fs = null;
		try {
			URLConnection conn = url.openConnection();
			InputStream inStream = conn.getInputStream();
			fs = new FileOutputStream(tempDir + "//" + dir + "//" + dir + ".jpg");

			byte[] buffer = new byte[1024];
			while ((byteread = inStream.read(buffer)) != -1) {
				bytesum += byteread;
				fs.write(buffer, 0, byteread);
			}
			System.out.println(img + "下载完成");
		} catch (Exception e) {
			// e.printStackTrace();
		} finally {

			try {
				fs.flush();
				fs.close();
			} catch (Exception e) {

			}
		}
	}

}
