package com.sekift.crawler.vo;

/**
 * 
 * @author:luyz
 * @time:2017-5-9 下午04:12:07
 * @version:
 */
public class ContentVO {
	private long timestamp;
	private double volumn;
	private double begin;
	private double hight;
	private double low;
	private double end;

	private double currentPrice;

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public double getVolumn() {
		return volumn;
	}

	public void setVolumn(double volumn) {
		this.volumn = volumn;
	}

	public double getBegin() {
		return begin;
	}

	public void setBegin(double begin) {
		this.begin = begin;
	}

	public double getHight() {
		return hight;
	}

	public void setHight(double hight) {
		this.hight = hight;
	}

	public double getLow() {
		return low;
	}

	public void setLow(double low) {
		this.low = low;
	}

	public double getEnd() {
		return end;
	}

	public void setEnd(double end) {
		this.end = end;
	}

	@Override
	public String toString() {
		return "ContentVO [timestamp=" + timestamp + ",volumn=" + volumn + ",begin=" + begin + ",hight=" + hight
				+ ",low=" + low + ",end=" + end + ",currentPrice=" + currentPrice + "]";
	}

	/**
	 * @param currentPrice
	 *            the currentPrice to set
	 */
	public void setCurrentPrice(double currentPrice) {
		this.currentPrice = currentPrice;
	}

	/**
	 * @return the currentPrice
	 */
	public double getCurrentPrice() {
		return currentPrice;
	}
}
