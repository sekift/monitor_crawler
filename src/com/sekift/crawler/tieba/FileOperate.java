package com.sekift.crawler.tieba;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sekift.Constants;

/**
 * 保存到文件
 * @author:luyz
 * @time:2018-3-26 上午10:56:23
 * @version:
 */
public class FileOperate {
	private static final Logger logger = LoggerFactory.getLogger(FileOperate.class);
	
	public static void saveToFile(List<String> content) {
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
			FileUtils.writeStringToFile(new File(Constants.TIEBA_SINGLE), msg.toString(), "utf-8", false);
			logger.info(Constants.TIEBA_SINGLE);
			FileUtils.writeStringToFile(new File(Constants.TIEBA_ALL), msg.toString(), "utf-8", true);
			msg.delete(0, msg.length());
		} catch (IOException e1) {
			logger.error("[tieba]写入文件错误了，", e1);
			e1.printStackTrace();
		}
	}

}
