package com.sekift.crawler.btc38;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sekift.crawler.core.HttpRequest;
import com.sekift.crawler.tieba.TiebaScheduler;
import com.sekift.crawler.vo.ContentVO;
import com.sekift.util.DateUtil;
import com.sekift.util.JsonUtil;
import com.sekift.util.MailUtil;
import com.sekift.util.SleepUtil;

/**
 * 
 * @author:sekift
 * @time:2015-2-6 上午11:29:58
 * @version:
 */
public class Btc38 implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(Btc38.class);

	private static List<String> siteList = new ArrayList<String>();
	private static List<String> currentSiteList = new ArrayList<String>();
	private static List<String> hourSiteList = new ArrayList<String>();
	private static Map<String, String> nameMap = new HashMap<String, String>();
	private static Map<String, Integer> volumnMap = new HashMap<String, Integer>();
	private static Map<String, String> webSiteMap = new HashMap<String, String>();

	private static List<List<Object>> result = new ArrayList<List<Object>>();

	static {
		getHourSiteList();
		getSiteList();
		getNameMap();
		getVolumnMap();
		getWebSiteMap();
		getCurrentSiteList();
	}
	
	public static void main(String[] args) {
		//Thread t = new Thread(new Btc38());
		//t.start();

		//ToutiaoScheduler ts = new ToutiaoScheduler();
		//ts.joke();
		
		//TiebaScheduler tb = new TiebaScheduler();
		//tb.link();
	}

	// 1小时线数据
	private static final String getTradeTimelinePre = "http://www.btc38.com/trade/getTradeTimeLine.php?coinname=";
	private static final String getTradePro = "&mk_type=CNY&n=";

	private static void getHourSiteList() {
		for (Btc type : Btc.values()) {
			hourSiteList.add(getTradeTimelinePre + type + getTradePro);
		}
	}

	// 5 分钟线数据
	private final static String getTrade5minLinePre = "http://www.btc38.com/trade/getTrade5minLine.php?coinname=";

	private static void getSiteList() {
		for (Btc type : Btc.values()) {
			siteList.add(getTrade5minLinePre + type + getTradePro);
		}
	}

	// 当前价格数据
	private final static String getTradeList30Pre = "http://www.btc38.com/trade/getTradeList30.php?coinname=";

	private static void getCurrentSiteList() {
		for (Btc type : Btc.values()) {
			currentSiteList.add(getTradeList30Pre + type + getTradePro);
		}
	}

	private static final String webSiteRootPre = "https://www.btc123.com/market/btc38?symbol=btc38";
	private static final String webSiteRootPro = "cny";

	// 币的btc123网址map 27种
	private static void getWebSiteMap() {
		for (Btc type : Btc.values()) {
			if (Btc.XCN.equals(type)) {
				webSiteMap.put(type + "", webSiteRootPre + webSiteRootPro + type.toString().toLowerCase());
			}else{
			    webSiteMap.put(type + "", webSiteRootPre + type.toString().toLowerCase() + webSiteRootPro);
			}
		}
	}

	// 缩略和中文Map 27种
	private static void getNameMap() {
		for (Btc type : Btc.values()) {
			nameMap.put(type + "", type.getName());
		}
	}

	// 中位线Map 27种
	private static void getVolumnMap() {
		for (Btc type : Btc.values()) {
			volumnMap.put(type + "", type.getMiddle());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		Date date = null;
		Random rand = new Random();
		try {
			boolean valumnSum = false;
			while (true) {
				date = new Date(System.currentTimeMillis());
				/*
				 * if (date.getHours() == 4) { SleepUtil.sleepByHour(7, 7); }
				 */
				for (int i = 0; i < siteList.size(); i++) {
					String site = siteList.get(i);
					String html = HttpRequest.getContent(site + rand.nextDouble());
					// logger.info("html" + html);
					ArrayList<ArrayList<Object>> htmlList = (ArrayList<ArrayList<Object>>) JsonUtil.toBean(html,
							List.class);
					ArrayList<Object> proList = (ArrayList<Object>) htmlList.get(htmlList.size() - 1);
					ArrayList<Object> preList = (ArrayList<Object>) htmlList.get(htmlList.size() - 2);

					// 转bean
					ContentVO vo1 = new ContentVO();
					ContentVO vo2 = new ContentVO();
					vo1.setTimestamp((Long) preList.get(0));
					if (preList.get(1) instanceof Integer) {
						vo1.setVolumn((Integer) preList.get(1));
					} else if (preList.get(1) instanceof Double) {
						vo1.setVolumn((Double) preList.get(1));
					}
					if (preList.get(2) instanceof Integer) {
						vo1.setBegin((Integer) preList.get(2));
					} else if (preList.get(2) instanceof Double) {
						vo1.setBegin((Double) preList.get(2));
					}
					if (preList.get(3) instanceof Integer) {
						vo1.setHight((Integer) preList.get(3));
					} else if (preList.get(3) instanceof Double) {
						vo1.setHight((Double) preList.get(3));
					}
					if (preList.get(4) instanceof Integer) {
						vo1.setLow((Integer) preList.get(4));
					} else if (preList.get(4) instanceof Double) {
						vo1.setLow((Double) preList.get(4));
					}
					if (preList.get(5) instanceof Integer) {
						vo1.setEnd((Integer) preList.get(5));
					} else if (preList.get(5) instanceof Double) {
						vo1.setEnd((Double) preList.get(5));
					}

					vo2.setTimestamp((Long) proList.get(0));
					if (proList.get(1) instanceof Integer) {
						vo2.setVolumn((Integer) proList.get(1));
					} else if (proList.get(1) instanceof Double) {
						vo2.setVolumn((Double) proList.get(1));
					}
					if (proList.get(2) instanceof Integer) {
						vo2.setBegin((Integer) proList.get(2));
					} else if (preList.get(2) instanceof Double) {
						vo2.setBegin((Double) proList.get(2));
					}
					if (proList.get(3) instanceof Integer) {
						vo2.setHight((Integer) proList.get(3));
					} else if (proList.get(3) instanceof Double) {
						vo2.setHight((Double) proList.get(3));
					}
					if (proList.get(4) instanceof Integer) {
						vo2.setLow((Integer) proList.get(4));
					} else if (preList.get(4) instanceof Double) {
						vo2.setLow((Double) proList.get(4));
					}
					if (proList.get(5) instanceof Integer) {
						vo2.setEnd((Integer) proList.get(5));
					} else if (proList.get(5) instanceof Double) {
						vo2.setEnd((Double) proList.get(5));
					}

					// 1小时
					String hourSite = hourSiteList.get(i);
					String hourHtml = HttpRequest.getContent(hourSite + rand.nextDouble());
					ArrayList<ArrayList<Object>> hourHtmlList = (ArrayList<ArrayList<Object>>) JsonUtil.toBean(
							hourHtml, List.class);
					ArrayList<Object> hourProList = (ArrayList<Object>) hourHtmlList.get(hourHtmlList.size() - 1);
					ArrayList<Object> hourPreList = (ArrayList<Object>) hourHtmlList.get(hourHtmlList.size() - 2);

					// 转bean
					ContentVO vo3 = new ContentVO();
					ContentVO vo4 = new ContentVO();
					vo3.setTimestamp((Long) hourPreList.get(0));
					if (hourPreList.get(1) instanceof Integer) {
						vo3.setVolumn((Integer) hourPreList.get(1));
					} else if (hourPreList.get(1) instanceof Double) {
						vo3.setVolumn((Double) hourPreList.get(1));
					}
					if (hourPreList.get(2) instanceof Integer) {
						vo3.setBegin((Integer) hourPreList.get(2));
					} else if (hourPreList.get(2) instanceof Double) {
						vo3.setBegin((Double) hourPreList.get(2));
					}
					if (hourPreList.get(3) instanceof Integer) {
						vo3.setHight((Integer) hourPreList.get(3));
					} else if (hourPreList.get(3) instanceof Double) {
						vo3.setHight((Double) hourPreList.get(3));
					}
					if (hourPreList.get(4) instanceof Integer) {
						vo3.setLow((Integer) hourPreList.get(4));
					} else if (hourPreList.get(4) instanceof Double) {
						vo3.setLow((Double) hourPreList.get(4));
					}
					if (hourPreList.get(5) instanceof Integer) {
						vo3.setEnd((Integer) hourPreList.get(5));
					} else if (hourPreList.get(5) instanceof Double) {
						vo3.setEnd((Double) hourPreList.get(5));
					}

					vo4.setTimestamp((Long) hourProList.get(0));
					if (hourProList.get(1) instanceof Integer) {
						vo4.setVolumn((Integer) hourProList.get(1));
					} else if (hourProList.get(1) instanceof Double) {
						vo4.setVolumn((Double) hourProList.get(1));
					}
					if (hourProList.get(2) instanceof Integer) {
						vo4.setBegin((Integer) hourProList.get(2));
					} else if (preList.get(2) instanceof Double) {
						vo4.setBegin((Double) hourProList.get(2));
					}
					if (hourProList.get(3) instanceof Integer) {
						vo4.setHight((Integer) hourProList.get(3));
					} else if (hourProList.get(3) instanceof Double) {
						vo4.setHight((Double) hourProList.get(3));
					}
					if (hourProList.get(4) instanceof Integer) {
						vo4.setLow((Integer) hourProList.get(4));
					} else if (preList.get(4) instanceof Double) {
						vo4.setLow((Double) hourProList.get(4));
					}
					if (hourProList.get(5) instanceof Integer) {
						vo4.setEnd((Integer) hourProList.get(5));
					} else if (hourProList.get(5) instanceof Double) {
						vo4.setEnd((Double) hourProList.get(5));
					}

					// System.out.println(vo1);
					// System.out.println(vo2);
					// 时间戳，交易量，开盘，最高，最低，收盘
					// 获取名称
					// System.out.println(nameMap.get(getCodeFromSite(site)));

					NumberFormat nf = NumberFormat.getInstance();
					// 成交量与中位线比较
					double middle = ((vo2.getVolumn() / volumnMap.get(getCodeFromSite(site)) * 100.0));
					// System.out.println(nf.format(vo2.getVolumn()) + "(" +
					// nf.format(valumnUp) + "%,"
					// + nf.format(middle) + "%)");

					// 5分钟成交量涨幅
					double valumnUp = ((vo2.getVolumn() - vo1.getVolumn()) / (vo1.getVolumn()) * 100.0);

					// 5分钟价格涨幅
					double priceUp = ((vo2.getEnd() - vo1.getEnd()) / (vo1.getEnd()) * 100.0);
					// System.out.println(nf.format(vo2.getEnd()) + "(" +
					// nf.format(priceUp) + "%)");

					// 1小时成交量涨幅
					double hourValumnUp = ((vo4.getVolumn() - vo3.getVolumn()) / (vo3.getVolumn()) * 100.0);

					// 1小时价格涨幅
					double hourPriceUp = ((vo4.getEnd() - vo3.getEnd()) / (vo3.getEnd()) * 100.0);
					// System.out.println(nf.format(hourValumnUp)+":"+
					// nf.format(vo4.getEnd()) + "(" +
					// nf.format(hourPriceUp) + "%)");

					// 当前价格
					String currentSite = currentSiteList.get(i);
					String currentHtml = HttpRequest.getContent(currentSite + rand.nextDouble());
					// System.out.println(currentHtml);
					// logger.info("currentHtml" + currentHtml);
					Map<String, List<List<Object>>> response = JsonUtil.toBean(currentHtml, Map.class);
					// 当前价格
					Object buyPrice = response.get("tradeStr").get(0).get(1);
					if (buyPrice instanceof Integer) {
						vo2.setCurrentPrice((Integer) buyPrice);
					} else if (buyPrice instanceof Double) {
						vo2.setCurrentPrice((Double) buyPrice);
					}

					// 当前涨幅
					double currentPriceUp = ((vo2.getCurrentPrice() - vo2.getEnd()) / (vo2.getEnd()) * 100.0);
					// System.out.println(vo2.getCurrentPrice() + "(" +
					// nf.format(currentPriceUp) + "%)");

					// 时间-最后一个时间即可
					SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
					Date proDate = new Date(vo2.getTimestamp());
					String proStr = sdf.format(proDate);
					// System.out.println(proStr);

					int sortType = 0;
					// 流量异动
					boolean valumn = false;
					if ((valumnUp > 500 && middle > 100) || (middle > 200) || (valumnUp > 800 && middle > 30)) {
						valumn = true;
						valumnSum = true;
					}

					// 建议
					String recommend = "RE";
					if ((valumnUp >= 500 && priceUp > 2 && currentPriceUp > 0) || (priceUp > 3 && middle > 100)) {
						recommend = "闪电买入";
						sortType = 7;
					} else if (middle > 100 && valumnUp > 100 && priceUp > 1 && currentPriceUp > 0) {
						recommend = "强烈买入";
						sortType = 6;
					} else if (middle > 100 && valumnUp > 100 && priceUp > 0) {
						recommend = "迅速买入";
						sortType = 5;
					} else if (middle > 60 && valumnUp > 60 && priceUp > 0 && currentPriceUp > 0) {
						recommend = "推荐买入";
						sortType = 4;
					} else if ((valumnUp > 100 && priceUp >= 1 && currentPriceUp >= 1)
							|| (valumnUp > 100 && currentPriceUp >= 2)) {
						recommend = "适当买入";
						sortType = 3;
					} else if (priceUp >= 1 && currentPriceUp >= 1) {
						recommend = "可以建仓";
						sortType = 2;
					} else if (middle <= 60 && middle >= 0 && valumnUp <= 60 && valumnUp >= 0 && priceUp >= 0) {
						recommend = "可以关注";
						sortType = 1;
					} else if (middle < 40 && middle > 0 && priceUp < 0 && currentPriceUp > 0) {
						recommend = "推荐卖出";
						sortType = -1;
					} else if (middle < 40 && middle > 0 && priceUp < 0 && currentPriceUp > -1) {
						recommend = "迅速卖出";
						sortType = -2;
					} else if ((middle > 20 && valumnUp > 50 && priceUp < -0.5 && currentPriceUp < -0.5)
							|| (priceUp <= -1.5 && currentPriceUp < 0.5) || (priceUp <= -1 && currentPriceUp <= -1)) {
						recommend = "强烈卖出";
						sortType = -3;
					} else if (priceUp < 0 && currentPriceUp < 0 && priceUp > currentPriceUp) {
						recommend = "闪电卖出";
						sortType = -4;
					} else if (currentPriceUp < -4) {
						recommend = "崩盘卖出";
						sortType = -5;
					} else {
						recommend = "静待观察";
						sortType = 0;
					}
					// System.out.println(recommend);
					/**
					 * 输出顺序：名称，是否异动/建议，当前价格（
					 * 与5分钟价格涨幅），5分钟价格（5分钟价格涨幅），5分钟量（5分钟量涨幅，与中位线比较），5分时间
					 * */
					List<Object> listObject = new ArrayList<Object>();
					listObject.add(sortType);
					// 带网址
					String name = nameMap.get(getCodeFromSite(site)) + (valumn ? "[量]" : "");
					listObject.add("<a href=\"" + webSiteMap.get(getCodeFromSite(site)) + "\" target=\"_blank\">"
							+ name + "</a>");
					listObject.add(recommend);
					listObject.add(vo2.getCurrentPrice() + "(" + nf.format(currentPriceUp) + "%)");
					listObject.add(nf.format(vo2.getEnd()) + "(" + nf.format(priceUp) + "%)");
					listObject.add(nf.format(vo2.getVolumn()) + "(" + nf.format(valumnUp) + "%/" + nf.format(middle)
							+ "%)");
					listObject.add(nf.format(hourPriceUp) + "%(" + nf.format(hourValumnUp) + "%)");
					listObject.add(proStr);

					result.add(listObject);
					// 抓一个休息1-2秒钟
					SleepUtil.sleepBySecond(1, 2);
				}

				// 发邮件判断
				if (!result.isEmpty()) {
					// 邮件通知
					sendMessageByEmail("一览表", result, date, valumnSum);
					// 发送短信
					// sendMessageByMobile(time,content,siteList.get(i));

					// j++;
					// 防止出了问题疯狂地发邮件
					/*
					 * if (j > 1000) { break; }
					 */
				}

				// 置空 恢复
				result.clear();
				valumnSum = false;

				// System.out.println(DateUtil.convertDateToStr(date,
				// "yyyy-MM-dd HH:mm:ss") + " =====================");
				logger.info(DateUtil.convertDateToStr(date, "yyyy-MM-dd HH:mm:ss") + " =====================");
				SleepUtil.sleepByMinute(2, 3); // 3-4分钟随机时间
			}
		} catch (Exception e) {
			logger.error("抓取出错了: ", e);
			e.printStackTrace();
			// 1-2分钟随机时间
			SleepUtil.sleepByMinute(1, 2);

			// 出错后重新开始
			Thread t = new Thread(new Btc38());
			t.start();
			System.out.println("thread run again");
		} finally {
			result.clear();
		}
	}

	// 邮箱通知 内容过滤
	@SuppressWarnings("deprecation")
	private static void sendMessageByEmail(String title, List<List<Object>> content, Date date, boolean valumnSum) {
		String text = "排位,名称[量异动],建议,当前价（涨幅）,5分价(涨幅),5分量(涨幅/中位数),1小时价(量)涨幅,5分时间";
		// 排序
		if (content.size() > 0) {
			Collections.sort(content, new Comparator<List<Object>>() {
				@Override
				public int compare(List<Object> vo1, List<Object> vo2) {
					if ((Integer) vo1.get(0) > (Integer) vo2.get(0)) {
						return -1;
					} else if ((Integer) vo1.get(0) < (Integer) vo2.get(0)) {
						return 1;
					}
					return 0;
				}
			});
		}

		StringBuilder msg = new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String proStr = sdf.format(date);
		msg.append("抓取的时间是：" + proStr + "，数据如下：");
		msg.append("<table class=\"table table-condensed\">");
		msg.append("<thead><tr>");
		String[] textArray = text.split(",");
		for (String str : textArray) {
			msg.append("<th>" + str + "</th>");
		}
		msg.append("</tr></thead>");
		msg.append("<tbody>");
		for (List<Object> list : content) {
			msg.append("<tr>");
			for (Object obj : list) {
				String contentStr = String.valueOf(obj);
				if (contentStr.contains("比特币")) {
					msg.append("<td style=\"font-weight:bold;\">" + obj + "</td>");
				} else {
					msg.append("<td>" + obj + "</td>");
				}
			}
			msg.append("</tr>");
		}

		msg.append("</tbody></table>");
		try {
			FileUtils
					.writeStringToFile(new File("/www/client/monitor_crawler/btc.txt"), msg.toString(), "UTF-8", false);
		} catch (IOException e1) {
			logger.error("[btc38]写入文件错误了，", e1);
			e1.printStackTrace();
		}
		try {
			if (valumnSum) {
				// 不同时间发送的邮箱不一样
				// TODO 添加接收邮件的邮件
				String preEmail = "<html><head></head><body><div id=\"msgFrompPush\" style=\"font-family: '微软雅黑';\">";
				String proEmail = "</div></body></html>";
				String result = preEmail + msg.toString() + proEmail;
				if (date.getHours() >= 9 && date.getHours() <= 18) {
					MailUtil.sendHTML("sekift@163.com", title, result);
				} else if (date.getHours() <= 24 || date.getHours() > 18) {
					MailUtil.sendHTML("574919797@qq.com", title, result);
				}
			}
		} catch (Exception e) {
			System.out.println("marby mail's jar error，please check……");
			e.printStackTrace();
		}
	}

	// 获取网址币种
	private static String getCodeFromSite(String str) {
		String result = "";
		try {
			URL url = new URL(str);
			String query = url.getQuery();
			String type = "coinname=";
			String[] queryArray = query.split("\\&");
			for (String s : queryArray) {
				if (s.contains(type)) {
					result = s.replace(type, "");
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}