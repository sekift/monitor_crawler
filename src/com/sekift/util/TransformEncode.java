package com.sekift.util;

import java.io.UnsupportedEncodingException;
import java.lang.Character.UnicodeBlock;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 提供转换编码的类
 */
public class TransformEncode {

	/**************判断编码*****************/
	/**
	 * 识别编码的方法
	 * @param str
	 * @return
	 */
	public static String getEncoding(String str) {
		String[] array = { "Shift_JIS", "GB2312", "ISO-8859-1", "UTF-8", "GBK", "ASCII", "Big5", "Unicode" };
		for (int i = 0; i < array.length; i++) {
			try {
				if (str.equals(new String(str.getBytes(array[i]), array[i]))) {
					String s = array[i];
					if (s.equals("Shift_JIS")) {
						return "unknow";
					}
					return s;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "other";
	}
	
	
	public static String getSystemEncoding() {
		String encoding = System.getProperty("file.encoding");
		System.out.println("系统默认编码 : " + encoding);
		return encoding;
	}
	
	/**************字符转换*****************/
	/**
	 * 将Unicode字符串转成汉字,如将 '\\u6d4b\\u8bd5' 转换为 '测试'
	 * 
	 * @param oldValue
	 *            String 要转换的字符串
	 * @return String
	 */
	public static String decodeUnicode(String oldValue) {
		String decodeStr = "";
		String[] hexValue = oldValue.split("\\\\u");
		for (int i = 1; i < hexValue.length; i++) {
			String subStr = hexValue[i];
			char c = (char) Integer.parseInt(subStr, 16);
			decodeStr += c + "";
		}
		return decodeStr;
	}

	/**
	 * 将字符串转换为unicode标量值,如将 '测试' 转换为 '\u6d4b\u8bd5'
	 * 
	 * @param str
	 *            String
	 * @return String
	 */
	public static String getUnicodeEncoding(String str) {
		String unicodeEncoding = "";
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			int intValue = (int) c;
			String hexValue = Integer.toHexString(intValue);
			unicodeEncoding += "\\u" + hexValue;
		}
		return unicodeEncoding;
	}

	public static String encodeUrl(String url) {
		try {
			return URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String decodeUrl(String url) {
		try {
			return URLDecoder.decode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	// Java class文件是UTF-8, 运行时是UTF-16BE
	// UTF-16BE: 双字节
	// US-ASCII: 单字节
	// GBK: 单字节，双字节
	// GB18030: 单字节，双字节，四字节
	// UTF-8: 单字节，双字节, 三字节

	// UTF-16BE --> UCS2
	// UTF-16和UCS2都是双字节， 很相似。
	// 在有些情况下UTF-16比UCS2多了两个字节的字序，当然这两个字节的字序对UTF-16是可选的，不是必须的。
	// 而UCS2没有字序的规定。FEFF表示BE，FFFE表示LE
	/*
	 * UTF-8以字节为编码单元，没有字节序的问题。UTF-16以两个字节为编码单元， 在解释一个UTF-16文本前，首先要弄清楚每个编码单元的字节序。
	 * 例如“奎”的Unicode编码是594E，“乙”的Unicode编码是4E59。 如果我们收到UTF-16字节流“594E”，那么这是“奎”还是
	 * “乙”？ Unicode规范中推荐的标记字节顺序的方法是BOM(Byte Order Mark).
	 * 在UCS编码中有一个叫做"ZERO WIDTH NO-BREAK SPACE"的字符，它的编码是FEFF。
	 * 而FFFE在UCS中是不存在的字符，所以不应该出现在实际传输中。
	 * UCS规范建议我们在传输字节流前，先传输字符"ZERO WIDTH NO-BREAK SPACE"。
	 * 这样如果接收者收到FEFF，就表明这个字节流是Big-Endian的； 如果收到FFFE，就表明这个字节流是Little-Endian的。
	 * 因此字符"ZERO WIDTH NO-BREAK SPACE"又被称作BOM。
	 */
	public static byte[] encodeUCS2(String src) {
		return encodeUTF16BE(src);
	}

	public static String decodeUCS2(byte[] src) {
		return decodeUTF16BE(src);
	}

	public static byte[] encodeUTF16BE(String src) {
		try {
			return src.getBytes("UTF-16BE");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String decodeUTF16BE(byte[] src) {
		try {
			return new String(src, "UTF-16BE");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] encodeUTF8(String src) {
		try {
			return src.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String decodeUTF8(byte[] src) {
		try {
			return new String(src, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	// UTF-16BE --> GBK
	public static byte[] encodeGBK(String src) {
		try {
			return src.getBytes("GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String decodeGBK(byte[] src) {
		try {
			return new String(src, "GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	// UTF-16BE --> ASCII
	public static byte[] encodeASCII(String src) {
		try {
			return src.getBytes("US-ASCII");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String decodeASCII(byte[] src) {
		try {
			return new String(src, "US-ASCII");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**************编码转换*****************/
	public static String toUTF8(String oldValue, String encoding) {
		String newValue = null;
		byte[] data = null;
		try {
			data = oldValue.getBytes(encoding);
			newValue = new String(data, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return newValue;
	}

	public static String toGBK(String value) {
		try {
			return new String(value.getBytes(), "GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * iso2utf8
	 * 
	 * @param str
	 * @return
	 */
	public static String utf82iso(String str) {
		String result = null;
		try {
			result = new String(str.getBytes("utf-8"), "iso-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * iso2utf8
	 * 
	 * @param str
	 * @return
	 */
	public static String iso2utf8(String str) {
		String result = null;
		try {
			result = new String(str.getBytes("iso-8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * gbk2utf8
	 * 
	 * @param gbk
	 * @return
	 */
	public String GBKToUTF8(String gbk) {
		String l_temp = gbk2unicode(gbk);
		l_temp = unicode2utf8(l_temp);
		return l_temp;
	}

	/**
	 * utf82gbk
	 * 
	 * @param utf
	 * @return
	 */
	public String UTF8ToGBK(String utf) {
		String l_temp = utf82unicode(utf);
		l_temp = unicode2gbk(l_temp);

		return l_temp;
	}

	/**
	 * gbk2unicode
	 * 
	 * @param str
	 * @return String
	 */

	public static String gbk2unicode(String str) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			char chr1 = (char) str.charAt(i);
			if (!isNeedConvert(chr1)) {
				result.append(chr1);
				continue;
			}
			result.append("\\u" + Integer.toHexString((int) chr1));
		}
		return result.toString();
	}

	/**
	 * unicode2gbk
	 * 
	 * @param dataStr
	 * @return String
	 */

	public static String unicode2gbk(String dataStr) {
		int index = 0;
		StringBuffer buffer = new StringBuffer();

		int li_len = dataStr.length();
		while (index < li_len) {
			if (index >= li_len - 1 || !"\\u".equals(dataStr.substring(index, index + 2))) {
				buffer.append(dataStr.charAt(index));
				index++;
				continue;
			}
			String charStr = "";
			charStr = dataStr.substring(index + 2, index + 6);
			char letter = (char) Integer.parseInt(charStr, 16);
			buffer.append(letter);
			index += 6;
		}
		return buffer.toString();
	}

	public static boolean isNeedConvert(char para) {
		return ((para & (0x00FF)) != para);
	}

	/**
	 * utf-8 转unicode
	 * 
	 * @param inStr
	 * @return String
	 */
	public static String utf82unicode(String inStr) {
		char[] myBuffer = inStr.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < inStr.length(); i++) {
			UnicodeBlock ub = UnicodeBlock.of(myBuffer[i]);
			if (ub == UnicodeBlock.BASIC_LATIN) {
				sb.append(myBuffer[i]);
			} else if (ub == UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
				int j = (int) myBuffer[i] - 65248;
				sb.append((char) j);
			} else {
				short s = (short) myBuffer[i];
				String hexS = Integer.toHexString(s);
				String unicode = "\\u" + hexS;
				sb.append(unicode.toLowerCase());
			}
		}
		return sb.toString();
	}

	/**
	 * unicode2utf8
	 * 
	 * @param theString
	 * @return String
	 */
	public static String unicode2utf8(String theString) {
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);
		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					// Read the xxxx
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException("Malformed   \\uxxxx   encoding.");
						}
					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't')
						aChar = '\t';
					else if (aChar == 'r')
						aChar = '\r';
					else if (aChar == 'n')
						aChar = '\n';
					else if (aChar == 'f')
						aChar = '\f';
					outBuffer.append(aChar);
				}
			} else
				outBuffer.append(aChar);
		}
		return outBuffer.toString();
	}
}
