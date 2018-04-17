package com.sekift.crawler.tieba;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.sekift.crawler.core.HttpRequest;

/**
 * 
 * @author:luyz
 * @time:2017-7-10 下午05:18:09
 * @version:
 */
public class TestTieba {

	public void getPageContentByHeader(String url ) {
		String shortStr = null;
		if (null != url) {
			try {
				URL u = new URL(url);
				shortStr = u.getQuery();
			} catch (Exception e) {

			}
		}
		Map<String, String> header = new HashMap<String, String>();
		header.put("Accept","application/json, text/javascript, */*; q=0.01");
		header.put("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
		header.put("Cache-Control", "max-age=0");
		header.put("Connection","keep-alive");
		header.put("Host","pan.baidu.com");
		header.put("Upgrade-Insecure-Requests","1");
		header.put("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:53.0) Gecko/20100101 Firefox/53.0");
		header.put("X-Requested-With","XMLHttpRequest");
		header.put("cookie", "bdshare_firstime=1479783649293; BIDUPSID=0EA03790EF480BD3D70D08A4C9F5EF2C; PSTM=1505100443; __cfduid=d24dc388d73f1e05fe93d09a77dea87821508480798"
				+"; MCITY=-%3A; BDCLND=YH9%2BPueQGF9aoXZeYGktG6W3uA%2Fteh6jg7IAo7CZIuw%3D; H_PS_PSSID=1432_21112_18560_22157; SIGNIN_UC=70a2711cf1d3d9b1a82d2f87d633bd8a02738547733; PSINO=6; SFSSID=k4hokprt3lhsmp1t2rfngmmhf4; BDRCVFR[feWj1Vr5u3D]=mk3SLVN4HKm; BDORZ=B490B5EBF6F3CD402E515D22BCDA1598"
				+"; uc_login_unique=2c00da440a28a92f62ee3c4e5f22336f; uc_recom_mark=cmVjb21tYXJrXzI0MzgwOTEz; "+BaiduCookie.getBaiduCookie("leiqiangca", "leiqiangcaxxxx"));
//		header.put("cookie", 	
//"bdshare_firstime=1479783649293; Hm_lvt_7a3960b6f067eb0085b7f96ff5e660b0=1523594687,1523599957,1523603735"
//+",1523845888; FP_UID=6610ea07e18e91b5b08f092e6d7553ba; BAIDUID=EE2F19336E11C2476045EC93D93F21BA:FG=1;"
//+" BIDUPSID=0EA03790EF480BD3D70D08A4C9F5EF2C; PSTM=1505100443; __cfduid=d24dc388d73f1e05fe93d09a77dea87821508480798"
//+"; PANWEB=1; MCITY=-%3A; BDCLND=YH9%2BPueQGF9aoXZeYGktG6W3uA%2Fteh6jg7IAo7CZIuw%3D; BDUSS=UMzRUY3STFj"
//+"Nm11NWZaN2RGdzROdmhiWDJHV0NJNktCRHVZSHpESVlkaEF0UEphQVFBQUFBJCQAAAAAAAAAAAEAAAAciYZBbGVpcWlhbmdjYQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAEAny1pAJ8tac"
//+"; SCRC=a48710bcc778711700c5aabb62e3e61d; STOKEN=774b14be77c68e7d321da368c9b8c9d044e7a705637e5e36bbc850741cfe2ac6"
//+"; H_PS_PSSID=1432_21112_18560_22157; SIGNIN_UC=70a2711cf1d3d9b1a82d2f87d633bd8a02738547733; PSINO=6;"
//+" SFSSID=k4hokprt3lhsmp1t2rfngmmhf4; BDRCVFR[feWj1Vr5u3D]=mk3SLVN4HKm; BDORZ=B490B5EBF6F3CD402E515D22BCDA1598"
//+"; uc_login_unique=2c00da440a28a92f62ee3c4e5f22336f; uc_recom_mark=cmVjb21tYXJrXzI0MzgwOTEz; PANPSC=12423710153929492696"
//+"%3AbuiwA%2FpMDzv%2FItLn%2F9kOo9wxnZzNwr1w9edlHB%2Bd1obzhZoHIFjSWXgXofrWESI4l3vs551KACT%2FHSZRhh6%2BPUOv"
//+"%2BbFn7vDNgFjHX8xyNyqtUq%2FdzxBw4uWh0XthgcRWkxwTFV4Vd9Unih65clERtryHDAnhPhMz5dNt%2BsRbU69UMSLhYBPCxc5TMpqnKCFM"
//+"; Hm_lpvt_7a3960b6f067eb0085b7f96ff5e660b0=1523846223; cflag=15%3A3");
		String urlPage1 = "http://pan.baidu.com/mbox/group/getinfooutauth?" + shortStr;
		String result = HttpRequest.getContentByHeader(urlPage1, header);
		
		boolean check = result.contains("{\"errno\":0,");
		System.out.println(result);
		System.out.println(check);
	}

	public static void main(String args[]) {
		TestTieba tt = new TestTieba();
		String url = "http://pan.baidu.com/mbox/homepage?short=c3gnMKw";
		tt.getPageContentByHeader(url);
	}

}
