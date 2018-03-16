package com.sekift.crawler.toutiao;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sekift.crawler.core.HttpRequest;
import com.sekift.util.EncryptMD5;
import com.sekift.util.JsonUtil;
import com.sekift.util.SleepUtil;
import com.sekift.util.TransformEncode;

/**
 * @author 作者:sekift
 * @author E-mail:sekiftlyz@gmail.com
 * @version 创建时间：2017-7-6 下午09:16:52 类说明:[]
 */
public class Toutiao implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(Toutiao.class);

	@SuppressWarnings("unchecked")
	public void getContentByHeader() {
		Map<String, String> header = new HashMap<String, String>();
		header.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
		// 加这个头之后会压缩，获取后还需要unzip，这里直接不用。
		// header.put("Accept-Encoding", "gzip, deflate");
		header.put("Accept-Language", "zh-CN,zh;q=0.8");
		header.put("Cache-Control", "max-age=0");
		header.put("Connection", "keep-alive");
		header.put("Referer", "http://www.toutiao.com/ch/essay_joke/");
		header.put("X-Requested-With", "XMLHttpRequest");
		header.put("Content-type", "application/x-www-form-urlencoded");
		header
				.put(
						"Cookie",
						"_ga=GA1.2.539073602.1488273378; tt_webid=56316756917; uuid=\"w:fcbab1b5648445ca971e1e4bb60152a4\"; csrftoken"
								+ "=5664f96af12ae5480e6094a084e7e330; CNZZDATA1259612802=1307665971-1488269760-https%253A%252F%252Fwww.bing"
								+ ".com%252F%7C1505266169; _ba=BA0.2-20170228-51d9e-QjCP3rlM9eGIy7biLVkv; __utma=24953151.539073602.1488273378"
								+ ".1499655613.1499661093.4; __utmz=24953151.1499661093.4.4.utmcsr=toutiao|utmccn=(not%20set)|utmcmd=(not"
								+ "%20set); sid_guard=\"e606af9f697941090cd3f17a7057bb46|1504167569|2591999|Sat\054 30-Sep-2017 08:19:28"
								+ " GMT\"; WEATHER_CITY=%E5%8C%97%E4%BA%AC; sso_login_status=1; login_flag=e0a492ec823750aa61acbb0b4c7b6851"
								+ "; uid_tt=b7871459a3f697f2eef9fd1531bce45f; sid_tt=e606af9f697941090cd3f17a7057bb46; sessionid=e606af9f697941090cd3f17a7057bb46"
								+ "; tt_webid=56316756917; UM_distinctid=15e79169bdd88-0846bbbca1345a8-46524130-13c680-15e79169bdeac; __tasessionId"
								+ "=j5ulm9x0f1505270078959");
		header.put("Host", "www.toutiao.com");
		header.put("Upgrade-Insecure-Requests", "1");
		header
				.put("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");

		int t = (int) Math.floor(new Date().getTime() / 1000);
		String e = Integer.toHexString(t).toUpperCase();
		String i = EncryptMD5.encryptMD5(String.valueOf(t), "utf-8").toUpperCase();
		String s = "";
		String n = i.substring(0, 5);
		String a = i.substring(i.length() - 5, i.length());
		int o = 0;
		for (o = 0; 5 > o; o++) {
			s += (n.toCharArray()[o] + "" + e.toCharArray()[o]);
		}
		String r = "";
		int c = 0;
		for (c = 0; 5 > c; c++) {
			r += (e.toCharArray()[c + 3] + "" + a.toCharArray()[c]);
		}
		String as = "A1" + s + e.substring(e.length() - 3, e.length());
		String cp = e.substring(0, 3) + r + "E1";

		String url = "http://www.toutiao.com/api/article/feed/?category=essay_joke"
				+ "&utm_source=toutiao&widen=1&max_behot_time=0&max_behot_time_tmp=0&tadrequire=true" + "&as=" + as
				+ "&cp=" + cp;// 479BB4B7254C150&cp=7E0AC8874BB0985";
		// String url ="http://www.bubbt.com/btc/joke";
		// ="http://s3a.pstatp.com/toutiao/resource/ntoutiao_web/page/home/whome/home_e76c6fb.js";
		// "http://www.toutiao.com/api/pc/feed/?category=news_hot&utm_source=toutiao&widen=1&max_behot_time=0&max_behot_time_tmp=0&tadrequire=true&as=A19539159D19AD9&cp=595D497AAD994E1";
		String result = HttpRequest.getContentByHeader(url, header);
		System.out.println(result);
		try {
			FileUtils.writeStringToFile(new File("/www/client/monitor_crawler/result.txt"), result, "utf-8", false);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String trans = TransformEncode.unicode2utf8(result);
		System.out.println(trans);
		// logger.info(result);
		// logger.info(trans);
		Map<String, List<Map<String, Map<String, Object>>>> map = new HashMap<String, List<Map<String, Map<String, Object>>>>();

		try {
			map = JsonUtil.toBean(trans, Map.class);
		} catch (Exception e1) {
			logger.error("抓取转换出错了，", e1);

			// 出错就重新抓取。
			SleepUtil.sleepBySecond(10, 10);
			getContentByHeader();
		}
		System.out.println(map);
		// logger.info(map.toString());
		List<Map<String, Map<String, Object>>> list = (List<Map<String, Map<String, Object>>>) map.get("data");
		if (null == list || list.size() == 0) {
			return;
		}

		List<String> resultList = new ArrayList<String>();

		for (Map<String, Map<String, Object>> content : list) {
			int repin_count = (Integer) content.get("group").get("repin_count");
			int digg_count = (Integer) content.get("group").get("digg_count");
			int bury_count = (Integer) content.get("group").get("bury_count");
			int share_count = (Integer) content.get("group").get("share_count");
			int favorite_count = (Integer) content.get("group").get("favorite_count");
			int go_detail_count = (Integer) content.get("group").get("go_detail_count");
			int comment_count = (Integer) content.get("group").get("comment_count");
			int status = digg_count - bury_count + repin_count + share_count + favorite_count + go_detail_count / 5
					+ comment_count;
			// System.out.println("内涵指数：" + status + " : "
			// + content.get("group").get("content"));
			// 保存到文件
			resultList.add("内涵指数[" + status + "]<br />" + content.get("group").get("content"));
		}
		saveToFile(resultList);
	}

	private void saveToFile(List<String> content) {
		StringBuilder msg = new StringBuilder();
		try {
			for (String str : content) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String proStr = sdf.format(new Date());
				msg.append("<table class=\"table table-condensed\">");
				msg.append("<tbody>");
				msg.append("<tr>");
				msg.append("<td>" + proStr + "-" + str + "</td>"); //
				msg.append("</tr>");
				msg.append("</tbody></table>");
				msg.append("\r\n");
				FileUtils.writeStringToFile(new File("/www/client/monitor_crawler/joke.txt"), msg.toString(), "utf-8",
						false);
				FileUtils.writeStringToFile(new File("/www/client/monitor_crawler/toutiao_joke.txt"), msg.toString(),
						"utf-8", true);
				SleepUtil.sleepBySecond(10, 10);
				msg.delete(0, msg.length());
			}
		} catch (IOException e1) {
			logger.error("[btc38]写入文件错误了，", e1);
			e1.printStackTrace();
		}
	}

	@Override
	public void run() {
		getContentByHeader();
	}

	public static void main(String[] args) {
		ToutiaoScheduler ts = new ToutiaoScheduler();
		ts.joke();
	}
}
