package com.sekift.crawler.btc38;

/**
 * 
 * @author:luyz
 * @time:2017-8-7 上午10:54:28
 * @version:
 */
public enum Btc {
	BTC("比特币", 10), 
	LTC("莱特币", 250), 
	DOGE("狗狗币", 50000000),
	XRP("瑞波币", 600000),
	BTS("比特股", 1200000),
	XLM("恒星币", 2500000), 
	NXT("未来币", 1000000), 
	BLK("黑币", 20000),
	XEM("新经币", 120000),
	EMC("崛起币", 7500),
	XZC("零币", 2700), 
	VASH("微币", 200000), 
	MGC("合众币", 150000), 
	ZCC("招财币", 250000),
	XPM("质数币", 30000), 
	HLB("活力币", 500000),
	NCS("资产股", 200000), 
	MEC("美卡币", 30000),
	RIC("黎曼币", 30000),
	TAG("悬赏币", 10000),
	ETH("以太币", 100), 
	INF("讯链币", 5000000),
	BCC("BCC",250),
	//DASH("达世币", 125), 
	//PPC("点点币", 4000),
	//YBC("元宝币", 5000),
	//BOST("增长币", 20000),
	//DGC("数码币", 130000), 
	//BEC("比奥币",6000), 
	//SRC("安全币", 20000),
	XCN("氪石币", 2000000);
	
	private final String name;
	private final int middle;

	Btc(String name, int middle) {
		this.name = name;
		this.middle = middle;
	}

	public String getName() {
		return name;
	}

	public int getMiddle() {
		return middle;
	}
}
