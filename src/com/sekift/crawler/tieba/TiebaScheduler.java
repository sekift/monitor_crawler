package com.sekift.crawler.tieba;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author 作者:sekift 
 * @author E-mail:sekiftlyz@gmail.com
 * @version 创建时间：2017-7-6 下午09:41:49
 * 类说明:[]
 */
public class TiebaScheduler {
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	
	public void link() {
		BaiduTieba tb = new BaiduTieba();
		scheduler.scheduleAtFixedRate(tb, 0, 3 * 60, SECONDS);
	}
	
	public static void main(String[] args) {
		TiebaScheduler ts= new TiebaScheduler();
		ts.link();
	}
}
