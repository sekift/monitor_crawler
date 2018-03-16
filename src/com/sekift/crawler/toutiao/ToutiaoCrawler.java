package com.sekift.crawler.toutiao;

import org.jsoup.nodes.Document;

import com.sekift.util.JsoupUtil;
import com.sekift.util.TransformEncode;

/**
 * @author 作者:sekift 
 * @author E-mail:sekiftlyz@gmail.com
 * @version 创建时间：2017-6-17 下午11:19:11
 * 类说明:[]
 */
public class ToutiaoCrawler implements Runnable{

	@Override
	public void run() {
		getContentFromToutiao();
	}
	
//	private static final String URL_TOUTIAO = "https://www.toutiao.com/api/pc/feed/" +
//			"?category=__all__&utm_source=toutiao&widen=1&max_behot_time=1497713817" +
//			"&max_behot_time_tmp=1497713817&tadrequire=false" +
//			"&as=A1051994C5F4D3B&cp=594524BD33DBBE1";
	private static final String URL_TOUTIAO = "http://www.toutiao.com/api/pc/focus/";
	
	public void getContentFromToutiao(){
		Document doc = JsoupUtil.getDocByConnectIgnoreContent(URL_TOUTIAO);
		String body = doc.getElementsByTag("body").text();
		System.out.println(body);
//	    body = body.replaceAll("\\", "\\\\");
//		System.out.println(TransformEncode.unicode2utf8(body));
		body = "\u65f6\u653f";
	    System.out.println(TransformEncode.unicode2utf8(body));
	}

}
