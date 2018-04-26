package com.sekift.util;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UrlUtil {
	private static final Logger logger = LoggerFactory.getLogger(UrlUtil.class);

	/**
	 * 获取URL具体参数
	 * 
	 * @param url
	 * @return
	 */
	public static Map<String, String> parserUrlParamterToMap(String url) {
		Map<String, String> map = null;
		// 首先获取参数
		url = getUrlParamter(url);
		if (url != null && url.indexOf("=") > -1) {
			map = new HashMap<String, String>();
			//过滤否则出错
			if(url.startsWith("&")){
				url = url.substring(1);
			}
			if(url.endsWith("&")){
				url = url.substring(0, url.length() - 1);
			}
			String[] arrTemp = url.split("&");
			for (String str : arrTemp) {
				String[] qs = str.split("=");
				map.put(qs[0], qs[1]);
			}
		}
		return map;
	}

	public static String getUrlParamterValue(String url, String name) {
		String result = null;
		Map<String, String> map = parserUrlParamterToMap(url);
		if(null != map){
			result = map.get(name);
		}
		return result;
	}
	
	/**
	 * 获取全部url参数
	 * @param url
	 * @return
	 */
	public static String getUrlParamter(String url) {
		String paramters = null;
		if (url != null)
			try {
				URL u = new URL(url);
				paramters = u.getQuery();
			} catch (Exception localException) {
				logger.error("解释URL出错了", localException);
				return null;
			}
		return paramters;
	}

}
