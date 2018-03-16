package com.sekift.crawler.test;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.sekift.crawler.toutiao.ToutiaoCrawler;

public class SchedulerTest {
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	public void beepForAnHour() {
		/*final Runnable beeper = new Runnable() {
			public void run() {
				System.out.println("beep");
			}
		};*/
//		WebCatchImpl wcl = new WebCatchImpl("aaa");
		ToutiaoCrawler tc = new ToutiaoCrawler();
		/*final ScheduledFuture<?> beeperHandle = */scheduler.scheduleAtFixedRate(tc, 0, 60, SECONDS);
		/*scheduler.schedule(new Runnable() {
			public void run() {
				beeperHandle.cancel(true);
			}
		}, 60 * 60, SECONDS);*/
	}
	
	public static void main(String args[]){
		SchedulerTest bc = new SchedulerTest();
		bc.beepForAnHour();
	}
}
