package com.sekift.util;

import java.util.Random;
import java.util.UUID;

import com.sekift.Constants;

/**
 * 随机数生成类
 * 
 * @author:sekift
 * @time:2014-8-9 下午06:10:40
 * @version 1.0.0
 */
public class RandomTool {

	/**
	 * //产生一个a到b的随机数,double
	 */
	public static double random(double a, double b) {
		return (b - a) * Math.random() + a;
	}

	/**
	 * //产生一个a到b的随机数,int
	 */
	public static int randomInt(double a, double b) {
		return new Double((b - a) * Math.random() + a).intValue();
	}

	/**
	 * //产生一个a到b的随机数,string
	 */
	public static String randomString() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * 返回一个定长的随机字符串(只包含大小写字母、数字)
	 * 
	 * @param length
	 *            随机字符串长度
	 * @return 随机字符串
	 */
	public static String generateString(int length) {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(Constants.string_ID.ALLCHAR.charAt(random.nextInt(Constants.string_ID.ALLCHAR.length())));
		}
		return sb.toString();
	}

	/**
	 * 返回一个定长的随机纯字母字符串(只包含大小写字母)
	 * 
	 * @param length
	 *            随机字符串长度
	 * @return 随机字符串
	 */
	public static String generateMixString(int length) {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(Constants.string_ID.ALLCHAR.charAt(random.nextInt(Constants.string_ID.CHAR.length())));
		}
		return sb.toString();
	}

	/**
	 * 返回一个定长的随机纯大写字母字符串(只包含大小写字母)
	 * 
	 * @param length
	 *            随机字符串长度
	 * @return 随机字符串
	 */
	public static String generateLowerString(int length) {
		return generateMixString(length).toLowerCase();
	}

	/**
	 * 返回一个定长的随机纯小写字母字符串(只包含大小写字母)
	 * 
	 * @param length
	 *            随机字符串长度
	 * @return 随机字符串
	 */
	public static String generateUpperString(int length) {
		return generateMixString(length).toUpperCase();
	}

	/**
	 * 生成一个定长的纯0字符串
	 * 
	 * @param length
	 *            字符串长度
	 * @return 纯0字符串
	 */
	public static String generateZeroString(int length) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			sb.append('0');
		}
		return sb.toString();
	}

	/**
	 * 根据数字生成一个定长的字符串，长度不够前面补0
	 * 
	 * @param num
	 *            数字
	 * @param fixdlenth
	 *            字符串长度
	 * @return 定长的字符串
	 */
	public static String toFixdLengthString(long num, int fixdlenth) {
		StringBuffer sb = new StringBuffer();
		String strNum = String.valueOf(num);
		if (fixdlenth - strNum.length() >= 0) {
			sb.append(generateZeroString(fixdlenth - strNum.length()));
		} else {
			throw new RuntimeException("将数字" + num + "转化为长度为" + fixdlenth + "的字符串发生异常！");
		}
		sb.append(strNum);
		return sb.toString();
	}

	/**
	 * 根据数字生成一个定长的字符串，长度不够前面补0
	 * 
	 * @param num
	 *            数字
	 * @param fixdlenth
	 *            字符串长度
	 * @return 定长的字符串
	 */
	public static String toFixdLengthString(int num, int fixdlenth) {
		StringBuffer sb = new StringBuffer();
		String strNum = String.valueOf(num);
		if (fixdlenth - strNum.length() >= 0) {
			sb.append(generateZeroString(fixdlenth - strNum.length()));
		} else {
			throw new RuntimeException("将数字" + num + "转化为长度为" + fixdlenth + "的字符串发生异常！");
		}
		sb.append(strNum);
		return sb.toString();
	}
	
	/**
	 * 随机生成数字和字母混合的 
	 *   编码混淆 去掉了1 0 和l o等几个
	 * 
	 * @param length
	 * @return
	 */
	public static String getRandomWithNumberAndChar(int length) {
		char[] chr = { '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
				'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
				'H', 'I', 'J', 'K', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

		Random random = new Random();
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < length; i++) {
			buffer.append(chr[random.nextInt(56)]);
		}
		return buffer.toString();
	}
	
	public static void main(String args[]){
		System.out.println(getRandomWithNumberAndChar(8));
	}
}
