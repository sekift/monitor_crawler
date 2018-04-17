package com.sekift.crawler.tieba;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sekift.crawler.core.HttpRequest;
import com.sekift.util.JsoupUtil;
import com.sekift.util.SleepUtil;

public class BaiduTieba implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(BaiduTieba.class);
	private static final String HOME_URL = "http://tieba.baidu.com";
	private static final String HOME_PAGE_URL = "https://pan.baidu.com/mbox/homepage";
	private static final String HTTP = "http://";
	private static final int HTTP_INDEX = 48;
	private static final String HTTPS = "https://";
	private static final int HTTPS_INDEX = 49;
	private static final String SHORT_IDX = "?short=";
	private static final int SHORT_INDEX = 14;
	private static final List<String> denyList = new ArrayList<String>();

	public void getContent() {
		BaiduYun baiduYun = new BaiduYun();
		String keyWord = "short";
		String urlPage1 = "http://tieba.baidu.com/f/search/res?isnew=1&kw=&qw=" + keyWord
				+ "&rn=50&un=&only_thread=0&sm=1&sd=&ed=&pn=1";
		String className = "s_post_list";
		try {
			Elements eles = JsoupUtil.getByAttrClass(urlPage1, className);
			Element ele = eles.get(0);
			int length = ele.getElementsByTag("span").size();

			List<String> deleteList = new ArrayList<String>();
			List<String> resultList = new ArrayList<String>();
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

					try{
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
					}catch(Exception e){
						logger.error("截取出错了，请检查：content="+content+";错误信息为：", e);
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
					String requestQuery = getQuery(content);
					
					if ((!deleteList.contains(content)) && (content.contains("pan.baidu.com"))
							&& (!denyList.contains(requestQuery))) {
						deleteList.add(content);
						//判断失效
						resultList.add(resultLink + spe + resultContent);
						/*if(-1 == check(requestQuery)){
							System.out.println(resultLink + spe + "[失效]:" + resultContent);
							denyList.add(requestQuery);
						} else if (1 == check(requestQuery)){
							System.out.println(resultLink + spe + "[有效]:" + resultContent);
							resultList.add(resultLink + spe + "[有效]:" + resultContent);
						} else {
						    System.out.println(resultLink + spe + "[未知]:" + resultContent);
							resultList.add(resultLink + spe + "[未知]:" + resultContent);
						}*/
					}
				}
			}
			
			// 插入baiduyun.xyz的抓取
			List<String> baiduYunList = baiduYun.getContent();
			resultList.add("<br />以下的时间相对较前，在上面的不够用时可以尝试加群！<br />");
			resultList.addAll(baiduYunList);
			FileOperate.saveToFile(resultList);
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

	/**
	 * "errno": 0  表示能用
	 * "errno": 112 表示失效
	 * 其他都是未知
	 */
	public static int check(String requestQuery) {
		SleepUtil.sleepBySecond(2, 4);
		//String shortStr = getQuery(url);
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
						BaiduCookie.getBaiduCookie("leiqiangca", "leiqiangcaxxxx"));
						//"\tA369DA9A9C45DD8BC9FE928438D60BB:FG=1; BIDUPSID=32C3D76E2A16293C792F408EF59926CD; PSTM=1468940621; Hm_lvt_7a3960b6f067eb0085b7f96ff5e660b0=1501303955,1501871110; secu=1; __cfduid=d7661dab55b96415dc8815459d943699f1490805720; BDUSS=NHSi15TUZLemdGSjdUam1FMlhJaW9kdDl3SDhlLWIxOURrR0JJUlhsQlo1VXRaSVFBQUFBJCQAAAAAAAAAAAEAAAAciYZBbGVpcWlhbmdjYQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAFlYJFlZWCRZUH; PANWEB=1; BDCLND=3AaiGAgU9Cxj%2Fxjh3UZVtrKW1%2FaGT8qi; cflag=15%3A3; locale=zh; SCRC=effb58aebc8f6686cbc14c9b3c365044; STOKEN=83534a6e9455f69b52dd57132f8a272b3361e35b224229f54849652e76990aa4; uc_login_unique=ed181ca636958757d68ee8aa321c7515; PANPSC=11081265557532140931%3AbuiwA%2FpMDzv%2FItLn%2F9kOo9wxnZzNwr1w9edlHB%2Bd1obzhZoHIFjSWXgXofrWESI4I6iA8uodFjd7fYrNEXc%2BGUOv%2BbFn7vDNgFjHX8xyNyqtUq%2FdzxBw4uWh0XthgcRWkxwTFV4Vd9Unih65clERtryHDAnhPhMz5dNt%2BsRbU69UMSLhYBPCxc5TMpqnKCFM; SFSSID=ieks1al7c7hggkgo5i1j3shp12; BDRCVFR[gltLrB7qNCt]=mk3SLVN4HKm; PSINO=6; H_PS_PSSID=; SIGNIN_UC=70a2711cf1d3d9b1a82d2f87d633bd8a02537795900; FP_UID=8445b89a7d6d2a8dbe1664d24bc867ea");

		String urlPage1 = "http://pan.baidu.com/mbox/group/getinfooutauth?" + requestQuery;
		String result = HttpRequest.getContentByHeader(urlPage1, header);
	    System.out.println("result=" + result);
		int check = 0;
		if(result.contains("{\"errno\":0,")){
			check = 1;
		}else if(result.contains("{\"errno\":112,")){
			check = -1;
		}
		return check;
	}

	public void run() {
		getContent();
	}
	
	static {
	    denyList.add("short=1fDVGW");
	    denyList.add("short=A0ISm");
	    denyList.add("short=b2ccVW");
	    denyList.add("short=bpGUkv9");
	    denyList.add("short=boJBfPX");
	    denyList.add("short=bo1uG0n");
	    denyList.add("short=boCA5wb");
	    denyList.add("short=bpCkIUb");
	    denyList.add("short=bp6x1nx");
	    denyList.add("short=bpI9FVT");
	    denyList.add("short=bN1J8i");
	    denyList.add("short=bnwPLpl");
	    denyList.add("short=c0Xt1V6");
	    denyList.add("short=c1F5iJ");
	    denyList.add("short=c1F5iJQ");
	    denyList.add("short=c1JtWmW");
	    denyList.add("short=c18K9rU");
	    denyList.add("short=c2bAWWo");
	    denyList.add("short=c2D4zYC");
	    denyList.add("short=c22FdgW");
	    denyList.add("short=c28KXtY");
	    denyList.add("short=c3rImAW");
	    denyList.add("short=c10gv4G");
	    denyList.add("short=c1El0Gg");
	    denyList.add("short=c5bfbC");
	    denyList.add("short=cgDqpC");
	    denyList.add("short=dFb8OBB");
	    denyList.add("short=b66ME6");
	    denyList.add("short=dDTt3UT");
	    denyList.add("short=dEDSxqh");
	    denyList.add("short=dFLO8hF");
	    denyList.add("short=dELZPsP");
	    denyList.add("short=dE0Aa7B");
	    denyList.add("short=dEXMiYl");
	    denyList.add("short=dF1Xobf");
	    denyList.add("short=dFodC1j");
	    denyList.add("short=dFmygs5");
	    denyList.add("short=dF2WCzJ");
	    denyList.add("short=dE5KRxJ");
	    denyList.add("short=dENv8f3");
	    denyList.add("short=dEXMiY");
	    denyList.add("short=dKTt6UT");
	    denyList.add("short=eSByAhO");
	    denyList.add("short=eSHbRYM");
	    denyList.add("short=eSD0DDC");
	    denyList.add("short=eR6hSBK");
	    denyList.add("short=eR7Zwdk");
	    denyList.add("short=eRNctiU");
	    denyList.add("short=eRQv8Ro");
	    denyList.add("short=eSgq7xo");
	    denyList.add("short=eStNE2e");
	    denyList.add("short=eT7Zwdk");
	    denyList.add("short=eSZwWH8");
	    denyList.add("short=gfMmbE7");
	    denyList.add("short=gfvNzLh");
	    denyList.add("short=gfN7Quf");
	    denyList.add("short=geTxoVT");
	    denyList.add("short=ge7I5ft");
	    denyList.add("short=geZIDhx");
	    denyList.add("short=geZci8V");
	    denyList.add("short=ge4uTX5");
	    denyList.add("short=geE4kOr");
	    denyList.add("short=gfNig7p");
	    denyList.add("short=i43RBEH");
	    denyList.add("short=i5IKdgx");
	    denyList.add("short=i4LRDLr");
	    denyList.add("short=i4AwxuP");
	    denyList.add("short=i4BpQmx");
	    denyList.add("short=i4uKw9v");
	    denyList.add("short=i51PgP3");
	    denyList.add("short=i55081B");
	    denyList.add("short=i519xg5");
	    denyList.add("short=i58AEyx");
	    denyList.add("short=i4Rrx3Z");
	    denyList.add("short=i4ZyGZj");
	    denyList.add("short=jI66UwA");
	    denyList.add("short=jI3OYSe");
	    denyList.add("short=jICREdw");
	    denyList.add("short=jIpXBs6");
	    denyList.add("short=jIpmbLs");
	    denyList.add("short=jIkmbsU");
	    denyList.add("short=jHAu0zc");
	    denyList.add("short=jH7mVR4");
	    denyList.add("short=jHVtaya");
	    denyList.add("short=jHTXm8Q");
	    denyList.add("short=jHOUklg");
	    denyList.add("short=jHRVx90");
	    denyList.add("short=kUAYv47");
	    denyList.add("short=kV1KZif");
	    denyList.add("short=kVRIO3H");
	    denyList.add("short=kV9mHmB");
	    denyList.add("short=kUC9e7t");
	    denyList.add("short=hsgSxms");
	    denyList.add("short=hsk5A4W");
	    denyList.add("short=hsIfW7i");
	    denyList.add("short=hsd4wa4");
	    denyList.add("short=hqB26BM");
	    denyList.add("short=hr9uhBQ");
	    denyList.add("short=hrQSyTe");
	    denyList.add("short=hrPr2xY");
	    denyList.add("short=hsiT0t2");
	    denyList.add("short=miAf2HE");
	    denyList.add("short=micUUl6");
	    denyList.add("short=mhPj2Q0");
	    denyList.add("short=mhWARFy");
	    denyList.add("short=mh78vMC");
	    denyList.add("short=mh831hQ");
	    denyList.add("short=nuVS1ML");
	    denyList.add("short=nuDngoh");
	    denyList.add("short=nvOH7nz");
	    denyList.add("short=pKOxnYj");
	    denyList.add("short=pKHBDX5");
	    denyList.add("short=pKS0Qp1");
	    denyList.add("short=pJQXfz9");
	    denyList.add("short=pLvNFqb");
	    denyList.add("short=pL7csQJ");
	    denyList.add("short=pLG5Z0R");
	    denyList.add("short=pLHlSoF");
	    denyList.add("short=pLPBNwJ");
	    denyList.add("short=o7JTo5W");
	    denyList.add("short=o7VkWem");
	    denyList.add("short=o7RwcOi");
	    denyList.add("short=o7TOXf0");
	    denyList.add("short=o8091sI");
	    denyList.add("short=o8oNJo2");
	    denyList.add("short=o8n0Rii");
	    denyList.add("short=o8TRyp8");
	    denyList.add("short=o8xQwVg");
	    denyList.add("short=o8Hojyy");
	    denyList.add("short=qYdFJZ2");
	    denyList.add("short=qYlUHWC");
	    denyList.add("short=qYKMPZY");
	    denyList.add("short=qYkUC1m");
	    denyList.add("short=qYbfGXQ");
	    denyList.add("short=qYH55Kw");
	    denyList.add("short=qYTvjv2");
	    denyList.add("short=qY0oU1A");
	    denyList.add("short=qXZk0qw");
	    denyList.add("short=skCNVYX");
	    denyList.add("short=sl8u3z3");
	    denyList.add("short=sladgD3");
	    denyList.add("short=slWaWLR");
	    denyList.add("short=slOjcrN");
	    denyList.add("short=skGRCdB");
	    denyList.add("short=skIT9pj");
	    denyList.add("short=skWH2UT");
	    denyList.add("short=sk90gCP");

	    denyList.add("short=?short=");
	    denyList.add("userShort=dFb");
	    denyList.add("userShort=dFb");
	    denyList.add("short=dE5ZP**");

	    denyList.add("?short=jICREdw");
	    denyList.add("?short=gfN7Quf");
	    denyList.add("?short=micUUl6");

	    denyList.add("&short=sl");
	    denyList.add("&short=c1");
	    denyList.add("&short=d1");
	    denyList.add("&short=gf");
	    denyList.add(" short=i5Ielg");
	  }
}