package com.sekift.crawler.tieba;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.sekift.util.SleepUtil;

public class BaiduCookie {
	public static String BAIDU_COOKIE = null;
	
	public static String getBaiduCookie(String username, String password){
		if(null == BAIDU_COOKIE){
			try {
				BAIDU_COOKIE = getBaiduCookieByUsername(username, password);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return BAIDU_COOKIE;
	}

	/**
	 * 获取百度cookie
	 */
	public static String getBaiduCookieByUsername(String username, String password)
			throws Exception {
		StringBuilder sb = new StringBuilder();
		 //HtmlUnitDriver driver = new HtmlUnitDriver();
		 //driver.setJavascriptEnabled(true);

		//设置必要参数
		DesiredCapabilities dcaps = new DesiredCapabilities();
		//浏览器
		dcaps.setCapability("browserName", "htmlunit");
		//浏览器版本，htmlunit留空
		dcaps.setCapability("version", "");
		//平台系统
		dcaps.setCapability("platform", "LINUX");
		//处理弹出框
		dcaps.setCapability("handlesAlerts", true);
		//ssl证书支持
		dcaps.setCapability("acceptSslCerts", true);
		//截屏支持
		dcaps.setCapability("takesScreenshot", true);
		//css搜索支持
		dcaps.setCapability("cssSelectorsEnabled", true);
		//js支持
		dcaps.setJavascriptEnabled(true);
		//驱动支持
		dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
				"D:\\selenium\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");
		
		//创建无界面浏览器对象
		PhantomJSDriver driver = new PhantomJSDriver(dcaps);

		driver.get("https://passport.baidu.com/v2/?login&u=");

		driver.findElementById("TANGRAM__PSP_3__footerULoginBtn").click();
		SleepUtil.sleepBySecond(2, 2);
		WebElement userName = driver.findElementByCssSelector("input[id=TANGRAM__PSP_3__userName]");
		userName.sendKeys(username);
		SleepUtil.sleepBySecond(2, 2);
		WebElement pass = driver.findElementByCssSelector("input[id=TANGRAM__PSP_3__password]");
		pass.sendKeys(password);
		SleepUtil.sleepBySecond(2, 2);
		WebElement submit2 = driver.findElementByCssSelector("input[id=TANGRAM__PSP_3__submit]");
		submit2.click();
		SleepUtil.sleepBySecond(2, 2);

		Set<Cookie> cookieSet = driver.manage().getCookies();
		driver.close();
		for (Cookie cookie : cookieSet) {
			sb.append(cookie.getName() + "=" + cookie.getValue() + ";");
			//sb.append(cookie);
		}
		String cookie = sb.toString();
		System.out.println(cookie);
		return cookie;
	}

	/**
	 * 看看能不能访问登录之后跳转的主页
	 * 
	 * 这边只是做个测试代码，懒得使用HttpClient,HttpURLConnection去抓了，为什么不使用上面那个HtmlUnitDriver类，
	 * 因为我随便百度到WebClient设置cookie，所以懒得再去百度下HtmlUnitDriver如何设置cookie。
	 * 
	 * 大家可以根据自己需要去选择相应的jar包来抓取网页，只要设置好cookie之类的头参数就行了。
	 */
	public static void test(String cookie) {
		URL link = null;
		try {
			link = new URL("https://passport.baidu.com/center?_t=1523600132");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		WebClient wc = new WebClient();

		WebRequest request = new WebRequest(link);
		// 重要的步骤
		request.setAdditionalHeader("Cookie", cookie);
		request.setCharset("UTF-8");

		// request.setProxyHost("120.120.120.x");
		// request.setProxyPort(8080);
		// 设置请求报文头里的refer字段
		// request.setAdditionalHeader("Referer", refer);

		// wc.addRequestHeader和request.setAdditionalHeader功能应该是一样的。选择一个即可。
		// 其他报文头字段可以根据需要添加
		request.setAdditionalHeader("Accept","application/json, text/javascript, */*; q=0.01");
		request.setAdditionalHeader("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
		request.setAdditionalHeader("Cache-Control", "max-age=0");
		request.setAdditionalHeader("Connection","keep-alive");
		request.setAdditionalHeader("Host","passport.baidu.com");
		request.setAdditionalHeader("Referer","https://passport.baidu.com/v2/?login&u=");
		request.setAdditionalHeader("Upgrade-Insecure-Requests","1");
		request.setAdditionalHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:53.0) Gecko/20100101 Firefox/53.0");
		request.setAdditionalHeader("X-Requested-With","XMLHttpRequest");

		// 开启cookie管理
		wc.getCookieManager().setCookiesEnabled(true);
		// 开启js解析。对于变态网页，这个是必须的
		wc.getOptions().setJavaScriptEnabled(true);
		// 开启css解析。对于变态网页，这个是必须的
		wc.getOptions().setCssEnabled(true);
		wc.getOptions().setThrowExceptionOnFailingStatusCode(false);
		wc.getOptions().setThrowExceptionOnScriptError(false);
		wc.getOptions().setTimeout(10000);
		// 打印html内容
		try {
			System.out.println(wc.getPage(request).getWebResponse().getResponseHeaders());
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		// 百度的账号和密码
		String cookie = getBaiduCookie("leiqiangca", "leiqiangcaxxxx");
		test(cookie);
	}
}
