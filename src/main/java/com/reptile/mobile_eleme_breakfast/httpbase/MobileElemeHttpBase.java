package com.reptile.mobile_eleme_breakfast.httpbase;



import com.bds.base.http.CustomGetMethod;
import com.bds.base.http.CustomPostMethod;
import com.bds.base.http.Response;
import com.bds.base.util.Constant;
import com.bds.base.util.ProxyIp;
import com.bds.base.util.ProxyPool;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.*;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MobileElemeHttpBase {
    private static Logger logger = Logger.getLogger(MobileElemeHttpBase.class);
    private static final MobileElemeHttpBase instance = new MobileElemeHttpBase();
    private static DesiredCapabilities chrome_capability = DesiredCapabilities.chrome();
    private static DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
    private static MultiThreadedHttpConnectionManager connectionManager;

    private MobileElemeHttpBase() {
    }

    public static MobileElemeHttpBase getInstance() {
        return instance;
    }

    public static Response get(String url, String charset) throws Exception {
        return get(url, charset, (Map) null);
    }

    public static synchronized Response get(String url, String charset, Map<String, String> headers) throws Exception {
        HttpClient client = getClient(charset);
        CustomGetMethod getMethod = new CustomGetMethod(url);
        if (headers != null) {
            Set statusCode = headers.keySet();
            Iterator _map = statusCode.iterator();

            while (_map.hasNext()) {
                String map = (String) _map.next();
                getMethod.setRequestHeader(map, (String) headers.get(map));
            }
        }

        getMethod.setRequestHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        getMethod.setRequestHeader("Accept-Encoding", "gzip, deflate, sdch");
        getMethod.setRequestHeader("Accept-Language", "zh-CN,zh;q=0.8");
        getMethod.setRequestHeader("Upgrade-Insecure-Requests", "1");
        getMethod.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36");
        if (Constant.proxy) {
            getMethod.setRequestHeader("Proxy-Authorization", Constant.ProxySecret);
        }

        int var12 = client.executeMethod(getMethod);
        Header[] var13 = getMethod.getResponseHeaders();
        HashMap var14 = new HashMap();
        Header[] body = var13;
        int r = var13.length;

        for (int var10 = 0; var10 < r; ++var10) {
            Header h = body[var10];
            var14.put(h.getName(), h.getValue());
        }

        String var15;
        Response var16;
        if (var12 == 200) {
            var15 = getMethod.getResponseBodyAsString();
            getMethod.releaseConnection();
            var16 = new Response();
            var16.setResult(var15);
            var16.setHeader(var14);
            var16.setStatus(var12);
            getMethod.abort();
            getMethod.releaseConnection();
            return var16;
        } else if (var12 == 500) {
            var15 = getMethod.getResponseBodyAsString();
            getMethod.releaseConnection();
            if (StringUtils.isNotEmpty(var15)) {
                var16 = new Response();
                var16.setResult(var15);
                var16.setHeader(var14);
                var16.setStatus(var12);
                getMethod.abort();
                getMethod.releaseConnection();
                return var16;
            } else {
                getMethod.abort();
                getMethod.releaseConnection();
                throw new ConnectException("请求错误,status code is[" + var12 + "]");
            }
        } else {
            var15 = getMethod.getResponseBodyAsString();
            var16 = new Response();
            var16.setResult(var15);
            var16.setHeader(var14);
            var16.setStatus(var12);
            getMethod.abort();
            getMethod.releaseConnection();
            return var16;
        }
    }

    public static synchronized Response post(String url, String charset, Map<String, String> header, String data) throws Exception {
        CustomPostMethod postMethod = new CustomPostMethod(url);

        Response var27;
        try {
            HttpClient e = getClient(charset);
            Set statusCode;
            if (header != null && !header.isEmpty()) {
                statusCode = header.keySet();
                Iterator _map = statusCode.iterator();

                while (_map.hasNext()) {
                    String map = (String) _map.next();
                    postMethod.setRequestHeader(map, (String) header.get(map));
                }
            }

            if (Constant.proxy) {
                postMethod.setRequestHeader("Proxy-Authorization", Constant.ProxySecret);
            }

//            if (data != null && !data.isEmpty()) {
//                statusCode = data.keySet();
//                NameValuePair[] var19 = new NameValuePair[statusCode.size()];
//                int var21 = 0;
//
//                String r;
//                for (Iterator result = statusCode.iterator(); result.hasNext(); var19[var21++] = new NameValuePair(r, (String) data.get(r))) {
//                    r = (String) result.next();
//                }
//
//                postMethod.setRequestBody(var19);
//            }
            postMethod.setRequestBody(data);

            int var18 = e.executeMethod(postMethod);
            Header[] var20 = postMethod.getResponseHeaders();
            HashMap var22 = new HashMap();
            Header[] var23 = var20;
            int var25 = var20.length;

            for (int var11 = 0; var11 < var25; ++var11) {
                Header h = var23[var11];
                var22.put(h.getName(), h.getValue());
            }

            String var24;
            Response var26;
            if (var18 != 200) {
                if (var18 == 302) {
                    var24 = postMethod.getResponseBodyAsString();
                    var26 = new Response();
                    var26.setResult(var24);
                    var26.setHeader(var22);
                    var26.setStatus(var18);
                    postMethod.abort();
                    var27 = var26;
                    return var27;
                }

                postMethod.abort();
                throw new ConnectException("请求错误,status code is[" + var18 + "]");
            }

            var24 = postMethod.getResponseBodyAsString();
            var26 = new Response();
            var26.setResult(var24);
            var26.setHeader(var22);
            var26.setStatus(var18);
            postMethod.abort();
            var27 = var26;
        } catch (Exception var16) {
            postMethod.abort();
            throw var16;
        } finally {
            postMethod.releaseConnection();
        }

        return var27;
    }

    public static synchronized String post(String url, String charset, Map<String, String> header, JSONObject object) throws ConnectException {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";

        try {
            URL e = new URL(url);
            URLConnection conn = e.openConnection();
            conn.setRequestProperty("accept", "application/json, text/plain, */*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1");
            if (header != null && !header.isEmpty()) {
                Set p = header.keySet();
                Iterator line = p.iterator();

                while (line.hasNext()) {
                    String key = (String) line.next();
                    conn.setRequestProperty(key, (String) header.get(key));
                }
            }

            StringBuffer p1 = new StringBuffer();
//            if (params != null && !params.isEmpty()) {
//                Set line1 = params.keySet();
//                Iterator key1 = line1.iterator();
//
//                while (key1.hasNext()) {
//                    String k = (String) key1.next();
//                    p1.append(k + "=" + (String) params.get(k) + "&");
//                }
//            }

            if (p1.length() > 0) {
                p1 = p1.deleteCharAt(p1.length() - 1);
            }

            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //out = new PrintWriter(conn.getOutputStream());
            out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), charset));
            out.print(object.toString());
            out.flush();

            String line2;
            for (in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8")); (line2 = in.readLine()) != null; result = result + line2) {
                ;
            }
        } catch (Exception var19) {
            System.out.println("发送 POST 请求出现异常！" + var19);
            throw new ConnectException("请求错误,status code is[" + result + "]");
        } finally {
            try {
                if (out != null) {
                    out.close();
                }

                if (in != null) {
                    in.close();
                }
            } catch (IOException var18) {
                var18.printStackTrace();
            }

        }

        return result;
    }

    private static HttpClient getClient(String charset) {
        HttpClient client = new HttpClient(connectionManager);
        if (Constant.proxy) {
            ProxyIp params = ProxyPool.getRandomProxy();
            client.getHostConfiguration().setProxy(params.getIp(), params.getPort());
            logger.info("########代理ip:" + params.getIp() + ",端口:" + params.getPort() + "注册时间：" + params.getTime() + "########");
        }

        HttpConnectionManagerParams params1 = client.getHttpConnectionManager().getParams();
        params1.setConnectionTimeout(30000);
        params1.setSoTimeout(30000);
        params1.setDefaultMaxConnectionsPerHost(500);
        params1.setMaxTotalConnections(1500);
        params1.setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler());
        HttpClientParams hcp = client.getParams();
        hcp.setParameter("http.connection.timeout", Long.valueOf(20000L));
        hcp.setParameter("http.connection-manager.timeout", Long.valueOf(20000L));
        hcp.setParameter("http.protocol.content-charset", charset);
        hcp.setAuthenticationPreemptive(true);
        return client;
    }

    public static synchronized WebDriver getFirefoxDriver() {
        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        FirefoxProfile ffProfile = new FirefoxProfile();
        ffProfile.setPreference("network.http.use-cache", false);
        ffProfile.setPreference("browser.cache.memory.enable", false);
        ffProfile.setPreference("browser.cache.disk.enable", false);
        ffProfile.setPreference("browser.sessionhistory.max_total_viewers", 3);
        ffProfile.setPreference("network.dns.disableIPv6", true);
        ffProfile.setPreference("Content.notify.interval", 750000);
        ffProfile.setPreference("content.notify.backoffcount", 3);
        ffProfile.setPreference("network.http.pipelining", true);
        ffProfile.setPreference("network.http.proxy.pipelining", true);
        ffProfile.setPreference("network.http.pipelining.maxrequests", 32);
        if (Constant.proxy) {
            System.out.println("start firefox browser...");
            ProxyIp driver = ProxyPool.getRandomProxy();
            Proxy proxy = new Proxy();
            proxy.setHttpProxy(driver.getIp() + ":" + driver.getPort());
            System.out.println("########代理ip:" + driver.getIp() + ",端口:" + driver.getPort() + "注册时间：" + "########");
            capabilities.setCapability("proxy", proxy);
        }

        capabilities.setCapability("firefox_profile", ffProfile);
        FirefoxDriver driver1 = new FirefoxDriver(capabilities);
        return driver1;
    }

    public static synchronized WebDriver getChromeDriver() {
        if (Constant.proxy) {
            ProxyIp driver = ProxyPool.getRandomProxy();
            Proxy proxy = new Proxy();
            proxy.setHttpProxy(driver.getIp() + ":" + driver.getPort());
            logger.info("########代理ip:" + driver.getIp() + ",端口:" + driver.getPort() + "########");
            chrome_capability.setCapability("proxy", proxy);
        }

        ChromeDriver driver1 = new ChromeDriver(chrome_capability);
        return driver1;
    }

    public static synchronized WebDriver getIEDriver() {
        if (Constant.proxy) {
            ProxyIp driver = ProxyPool.getRandomProxy();
            Proxy proxy = new Proxy();
            proxy.setHttpProxy(driver.getIp() + ":" + driver.getPort());
            logger.info("########代理ip:" + driver.getIp() + ",端口:" + driver.getPort() + "########");
            ieCapabilities.setCapability("proxy", proxy);
        }

        InternetExplorerDriver driver1 = new InternetExplorerDriver(ieCapabilities);
        return driver1;
    }

    public static void main(String[] args) {
        Constant.proxy = true;

        try {
            Response e = get("http://blog.csdn.net/tanzhangwen/article/details/16337969", "utf-8");
            System.out.println(e.getResult());
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    static {
        String MAX_TIME_OUT = System.getProperty("user.dir");
        System.setProperty("webdriver.chrome.driver", MAX_TIME_OUT + "/conf/chromedriver.exe");
        String MAX_CONN = System.getProperty("os.arch");
        if (MAX_CONN.indexOf("64") >= 0) {
            System.setProperty("webdriver.ie.driver", MAX_TIME_OUT + "/conf/IEDriverServer64.exe");
            System.setProperty("webdriver.gecko.driver", MAX_TIME_OUT + "/conf/geckodriver64.exe");
        } else {
            System.setProperty("webdriver.ie.driver", MAX_TIME_OUT + "/conf/IEDriverServer32.exe");
            System.setProperty("webdriver.gecko.driver", MAX_TIME_OUT + "/conf/geckodriver32.exe");
        }

        ieCapabilities.setCapability("ignoreProtectedModeSettings", true);
        chrome_capability.setJavascriptEnabled(true);
        ieCapabilities.setJavascriptEnabled(true);
        connectionManager = new MultiThreadedHttpConnectionManager();
        Integer MAX_TIME_OUT1 = Integer.valueOf(5000);
        Integer MAX_CONN1 = Integer.valueOf(5000);
        connectionManager.closeIdleConnections((long) MAX_TIME_OUT1.intValue());
        connectionManager.getParams().setParameter("http.connection-manager.max-total", MAX_CONN1);
    }
}
