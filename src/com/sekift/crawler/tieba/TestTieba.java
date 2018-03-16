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
		header.put("cookie", "PANWEB=1; bdshare_firstime=1479783649293; Hm_lvt_7a3960b6f067eb0085b7f96ff5e660b0=1503381223,1503381868,1503385111,1503385231; BDUSS=1yOW5GM2tqVjBoUXZvNXNVVnVQTks1WnZMZzV3UVlxN2lKUGxyMy0wWHd5N2haTVFBQUFBJCQAAAAAAAAAAAEAAAAciYZBbGVpcWlhbmdjYQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAPA-kVnwPpFZNi; secu=1; BAIDUID=4266949717B539DEFF9BDB184D733640:FG=1; BIDUPSID=0EA03790EF480BD3D70D08A4C9F5EF2C; PSTM=1499939208; BDCLND=TVcy9w9rmZrsybwWm5HMNcMnVVXyNQyi0ZywRgs8Ct4%3D; STOKEN=754fe7af3dd819ae1e1d31e19c7ff1e1423ffed1dcb30692f7f8a218d1e9b8bb; SCRC=aa1b2d52a8c8a40870b28844dcf5e911; FP_UID=6610ea07e18e91b5b08f092e6d7553ba; locale=zh; uc_login_unique=d67bdf2c1582df5ea900cf5738acbeed; pgv_pvi=9183911936; cflag=15%3A3; PANPSC=15603375461489545167%3anZglRs3jFmj8bZRHrammcyOAoXNFwReX31Cz8uE7zTDA%2fhpuRIzuYyz2PhdQts%2fX8Uyd79c9ZvqbGU3GWyspxyjYY7dSbxGEfkiVl%2ff%2fRfwgvcjop0cMIioOm14RA5g25cfbkec3O1YxBnWFu9npKbjRq%2fXL%2by4yP1nrZfZQmID06E6Dmj%2bMXHLmlnN1N0blrGP3PeT%2fjWZ3MYyx6aD0yQ%3d%3d; H_PS_PSSID=1460_21123_22158; BDRCVFR[eHt_ClL0b_s]=mk3SLVN4HKm; PSINO=6; SFSSID=e7s3a6auc777hn4lrtei5g5vu2; pgv_si=s5885705216; Hm_lpvt_7a3960b6f067eb0085b7f96ff5e660b0=1503385231");
		
		String urlPage1 = "http://pan.baidu.com/mbox/group/getinfooutauth?" + shortStr;
		String result = HttpRequest.getContentByHeader(urlPage1, header);
		
		boolean check = result.contains("{\"errno\":0,");
		System.out.println(result);
		System.out.println(check);
	}

	public static void main(String args[]) {
		TestTieba tt = new TestTieba();
		String url = "http://pan.baidu.com/mbox/homepage?short=o76Ogno";
		tt.getPageContentByHeader(url);
	}

}
