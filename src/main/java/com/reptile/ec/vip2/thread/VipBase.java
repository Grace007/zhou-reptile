package com.reptile.ec.vip2.thread;

import com.bds.base.http.CustomGetMethod;
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
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class VipBase {

    private static Logger logger = Logger.getLogger(VipBase.class);

    private static MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();

    public synchronized static Response get(String url, String charset, Map<String, String> headers) throws Exception {
        HttpClient client = getClient(charset);
        CustomGetMethod getMethod = new CustomGetMethod(url);// 创建一个get方法，类似在浏览器地址栏中输入一个地址
        if (headers != null) {
            Set<String> keys = headers.keySet();
            for (String k : keys) {
                getMethod.setRequestHeader(k, headers.get(k));
            }
        }
        getMethod.setRequestHeader("Accept",
                "text/html, application/xhtml+xml, image/jxr, */*");
        getMethod.setRequestHeader("Accept-Encoding", "gzip, deflate");
        getMethod.setRequestHeader("Accept-Language", "zh-Hans-CN, zh-Hans; q=0.7, ja; q=0.3");
        getMethod.setRequestHeader("Connection", "Keep-Alive");
        //getMethod.setRequestHeader("Upgrade-Insecure-Requests", "1");
        String random_version = (44 + (int) (Math.random() * 10)) + ".0." + (2500 + (int) (Math.random() * 300)) + "."
                + (int) (Math.random() * 100);
        getMethod.setRequestHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko");

        int statusCode = client.executeMethod(getMethod);// 回车——出拳！

        Header[] _map = getMethod.getResponseHeaders();
        Map<String, String> map = new HashMap<String, String>();
        for (Header h : _map) {
            map.put(h.getName(), h.getValue());
        }
        if (statusCode == 200) {
            String body = getMethod.getResponseBodyAsString();
            getMethod.releaseConnection();
            Response r = new Response();
            r.setResult(body);
            r.setHeader(map);
            r.setStatus(statusCode);
            getMethod.abort();
            getMethod.releaseConnection();// 释放，记得收拳哦
            return r;
        }
        if (statusCode == 500) {
            String body = getMethod.getResponseBodyAsString();
            getMethod.releaseConnection();
            if (StringUtils.isNotEmpty(body)) {
                Response r = new Response();
                r.setResult(body);
                r.setHeader(map);
                r.setStatus(statusCode);
                getMethod.abort();
                getMethod.releaseConnection();// 释放，记得收拳哦
                return r;
            }
            getMethod.abort();
            getMethod.releaseConnection();// 释放，记得收拳哦
            throw new java.net.ConnectException("请求错误,status code is[" + statusCode + "]");
        }
        String body = getMethod.getResponseBodyAsString();
        Response r = new Response();
        r.setResult(body);
        r.setHeader(map);
        r.setStatus(statusCode);
        getMethod.abort();
        getMethod.releaseConnection();// 释放，记得收拳哦
        return r;
    }

    private static HttpClient getClient(String charset) {
        HttpClient client = new HttpClient(connectionManager);
        if (Constant.proxy) {
            // ProxyIp ip = new ProxyIp("120.24.184.166", 16816,
            // "20160323174944");
            ProxyIp ip = ProxyPool.getRandomProxy();
            client.getHostConfiguration().setProxy(ip.getIp(), ip.getPort());
            logger.info("########代理ip:" + ip.getIp() + ",端口:" + ip.getPort() + "注册时间：" + ip.getTime() + "########");
        }
        HttpConnectionManagerParams params = client.getHttpConnectionManager().getParams();
        params.setConnectionTimeout(30000);
        params.setSoTimeout(30000);
        params.setDefaultMaxConnectionsPerHost(500);
        params.setMaxTotalConnections(1500);
        params.setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        HttpClientParams hcp = client.getParams();
        hcp.setParameter("http.connection.timeout", 20 * 1000L);
        hcp.setParameter("http.connection-manager.timeout", 20 * 1000L);
        hcp.setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, charset);
        hcp.setAuthenticationPreemptive(true);
        return client;
    }
}
