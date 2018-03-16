package com.sekift.crawler.tieba;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sekift.crawler.core.HttpRequest;
import com.sekift.util.JsoupUtil;
import com.sekift.util.SleepUtil;

public class Tieba implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(Tieba.class);
	private static final String HOME_URL = "http://tieba.baidu.com";
	private static final String HOME_PAGE_URL = "https://pan.baidu.com/mbox/homepage";
	private static final String HTTP = "http://";
	private static final int HTTP_INDEX = 48;
	private static final String HTTPS = "https://";
	private static final int HTTPS_INDEX = 49;
	private static final String SHORT_IDX = "?short=";
	private static final int SHORT_INDEX = 14;

	public void getContent() {
		String keyWord = "short";
		String urlPage1 = "http://tieba.baidu.com/f/search/res?isnew=1&kw=&qw=" + keyWord
				+ "&rn=50&un=&only_thread=0&sm=1&sd=&ed=&pn=1";
		String className = "s_post_list";
		try {
			Elements eles = JsoupUtil.getByAttrClass(urlPage1, className);
			Element ele = eles.get(0);
			int length = ele.getElementsByTag("span").size();

			List<String> deleteList = new ArrayList<String>();
			List<String>  resultList = new ArrayList<String>();
			for (int i = 0; i < length; i++) {
				String content = ele.getElementsByClass("p_content").get(i).text();
				if ((!content.contains("失效")) && (!content.contains("复制"))) {
					Element eLink = ele.getElementsByTag("span").get(i);
					String linkAddress = HOME_URL + eLink.select("a").attr("href");
					String linkTitle = eLink.select("a").text();

					Element bLink = ele.getElementsByClass("p_forum").get(i);
					String bAddress = HOME_URL + bLink.select("a").attr("href");
					String bTitle = bLink.select("a").text();

					String dateTime = ele.getElementsByClass("p_date").get(i).text();

					if (content.startsWith(HTTP)) {
						content = content.substring(0, HTTP_INDEX);
					} else if (content.startsWith(HTTPS)) {
						content = content.substring(0, HTTPS_INDEX);
					} else if (content.indexOf(HTTP) > -1) {
						int start = content.indexOf(HTTP);
						content = content.substring(start, start + HTTP_INDEX);
					} else if (content.indexOf(HTTPS) > -1) {
						int start = content.indexOf(HTTPS);
						content = content.substring(start, start + HTTPS_INDEX);
					} else if (content.indexOf(SHORT_IDX) > -1) {
						int start = content.indexOf(SHORT_IDX);
						content = HOME_PAGE_URL + content.substring(start, start + SHORT_INDEX);
					}
					if (content.endsWith(".")) {
						content = content.replace(".", "");
					}
					if (content.contains("panbaiducom")) {
						content = content.replace("panbaiducom", "pan.baidu.com");
					}

					String resultLink = "<a href=\"" + linkAddress + "\" target=\"_blank\">" + linkTitle + "</a>"
							+ " 贴吧：<a href=\"" + bAddress + "\" target=\"_blank\">" + bTitle + "</a>" + " 发布时间："
							+ dateTime;
					String spe = "<br />";
					String resultContent = "<a href=\"" + content + "\" target=\"_blank\">" + content + "</a>";

					if ((!deleteList.contains(content)) && (content.contains("pan.baidu.com"))) {
						deleteList.add(content);
						resultList.add(resultLink + spe + resultContent);
					}
				}

			}

			saveToFile(resultList);
		} catch (Exception e) {
			logger.error("[tieba抓取出错了]", e);
			SleepUtil.sleepBySecond(30, 30);
			getContent();
		}
	}

	public static String getQuery(String url) {
		String shortStr = null;
		if (url != null)
			try {
				URL u = new URL(url);
				shortStr = u.getQuery();
			} catch (Exception localException) {
				logger.error("[tieba]解释URL出错了", localException);
			}
		return shortStr;
	}

	public static boolean check(String url) {
		SleepUtil.sleepBySecond(10, 20);
		String shortStr = getQuery(url);
		Map<String, String> header = new HashMap<String, String>();
		header.put("Accept", "application/json, text/javascript, */*; q=0.01");
		header.put("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
		header.put("Cache-Control", "max-age=0");
		header.put("Connection", "keep-alive");
		header.put("Host", "pan.baidu.com");
		header.put("Upgrade-Insecure-Requests", "1");
		header.put("DNT", "1");
		header.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0");
		header.put("X-Requested-With", "XMLHttpRequest");
		header
				.put(
						"cookie",
						"\tBAIDUID=8A369DA9A9C45DD8BC9FE928438D60BB:FG=1; BIDUPSID=32C3D76E2A16293C792F408EF59926CD; PSTM=1468940621; Hm_lvt_7a3960b6f067eb0085b7f96ff5e660b0=1501303955,1501871110; secu=1; __cfduid=d7661dab55b96415dc8815459d943699f1490805720; BDUSS=NHSi15TUZLemdGSjdUam1FMlhJaW9kdDl3SDhlLWIxOURrR0JJUlhsQlo1VXRaSVFBQUFBJCQAAAAAAAAAAAEAAAAciYZBbGVpcWlhbmdjYQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAFlYJFlZWCRZUH; PANWEB=1; BDCLND=3AaiGAgU9Cxj%2Fxjh3UZVtrKW1%2FaGT8qi; cflag=15%3A3; locale=zh; SCRC=effb58aebc8f6686cbc14c9b3c365044; STOKEN=83534a6e9455f69b52dd57132f8a272b3361e35b224229f54849652e76990aa4; uc_login_unique=ed181ca636958757d68ee8aa321c7515; PANPSC=11081265557532140931%3AbuiwA%2FpMDzv%2FItLn%2F9kOo9wxnZzNwr1w9edlHB%2Bd1obzhZoHIFjSWXgXofrWESI4I6iA8uodFjd7fYrNEXc%2BGUOv%2BbFn7vDNgFjHX8xyNyqtUq%2FdzxBw4uWh0XthgcRWkxwTFV4Vd9Unih65clERtryHDAnhPhMz5dNt%2BsRbU69UMSLhYBPCxc5TMpqnKCFM; SFSSID=ieks1al7c7hggkgo5i1j3shp12; BDRCVFR[gltLrB7qNCt]=mk3SLVN4HKm; PSINO=6; H_PS_PSSID=; SIGNIN_UC=70a2711cf1d3d9b1a82d2f87d633bd8a02537795900; FP_UID=8445b89a7d6d2a8dbe1664d24bc867ea");

		String urlPage1 = "http://pan.baidu.com/mbox/group/getinfooutauth?" + shortStr;
		String result = HttpRequest.getContentByHeader(urlPage1, header);
		boolean check = result.contains("{\"errno\":0,");
		return check;
	}

	private void saveToFile(List<String> content) {
		StringBuilder msg = new StringBuilder();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String proStr = sdf.format(new Date());
			msg.append("<table class=\"table table-condensed\">");
			msg.append("<tbody>");
			msg.append("<tr>");
			msg.append("<td>抓取时间: " + proStr + " ,大概1分钟更新一波</td>");
			msg.append("</tr>");
			msg.append("</tbody></table>");
			for (String str : content) {
				msg.append("<table class=\"table table-condensed\">");
				msg.append("<tbody>");
				msg.append("<tr>");
				msg.append("<td>" + str + "</td>");
				msg.append("</tr>");
				msg.append("</tbody></table>");
			}
			msg.append("\r\n");
			FileUtils.writeStringToFile(new File("/www/client/monitor_crawler/link.txt"), msg.toString(), "utf-8",
					false);
			FileUtils.writeStringToFile(new File("/www/client/monitor_crawler/tieba_link.txt"), msg.toString(),
					"utf-8", true);
			msg.delete(0, msg.length());
		} catch (IOException e1) {
			logger.error("[tieba]写入文件错误了，", e1);
			e1.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Tieba tb = new Tieba();
		tb.getContent();
	}

	public void run() {
		getContent();
	}
}