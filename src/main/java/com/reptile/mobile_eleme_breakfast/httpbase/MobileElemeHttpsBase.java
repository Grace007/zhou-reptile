package com.reptile.mobile_eleme_breakfast.httpbase;

import com.bds.base.http.CustomGetMethod;
import com.bds.base.http.CustomPostMethod;
import com.bds.base.http.Response;
import com.bds.base.http.SecureProtocolSocketFactory;
import com.bds.base.util.Constant;
import com.bds.base.util.ProxyIp;
import com.bds.base.util.ProxyPool;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MobileElemeHttpsBase {
    private static Logger logger = Logger.getLogger(MobileElemeHttpsBase.class);
    private static final MobileElemeHttpsBase instance = new MobileElemeHttpsBase();
    private static MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();

    private MobileElemeHttpsBase() {
    }

    public static MobileElemeHttpsBase getInstance() {
        return instance;
    }

    public static Response get(String url, String charset) throws Exception {
        return get(url, charset, (Map) null);
    }

    public static synchronized Response get(String url, String charset, Map<String, String> headers) throws Exception {
        SecureProtocolSocketFactory fcty = new SecureProtocolSocketFactory();
        Protocol.registerProtocol("https", new Protocol("https", fcty, 443));
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

        getMethod.setRequestHeader("accept", "application/json, text/plain, */*");
        getMethod.setRequestHeader("Accept-Encoding", "gzip, deflate, br");
        getMethod.setRequestHeader("Accept-Language", "zh-CN,zh;q=0.8");
        //getMethod.setRequestHeader("Upgrade-Insecure-Requests", "1");
        getMethod.setRequestHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1");
        int var13 = client.executeMethod(getMethod);
        Header[] var14 = getMethod.getResponseHeaders();
        HashMap var15 = new HashMap();
        Header[] body = var14;
        int r = var14.length;

        for (int var11 = 0; var11 < r; ++var11) {
            Header h = body[var11];
            var15.put(h.getName(), h.getValue());
        }

        String var16;
        Response var17;
        if (var13 == 200) {
            var16 = getMethod.getResponseBodyAsString();
            getMethod.releaseConnection();
            var17 = new Response();
            var17.setResult(var16);
            var17.setHeader(var15);
            var17.setStatus(var13);
            getMethod.abort();
            getMethod.releaseConnection();
            return var17;
        } else if (var13 == 500) {
            var16 = getMethod.getResponseBodyAsString();
            getMethod.releaseConnection();
            if (StringUtils.isNotEmpty(var16)) {
                var17 = new Response();
                var17.setResult(var16);
                var17.setHeader(var15);
                var17.setStatus(var13);
                getMethod.abort();
                getMethod.releaseConnection();
                return var17;
            } else {
                getMethod.abort();
                getMethod.releaseConnection();
                throw new ConnectException("请求错误,status code is[" + var13 + "]");
            }
        } else {
            var16 = getMethod.getResponseBodyAsString();
            var17 = new Response();
            var17.setResult(var16);
            var17.setHeader(var15);
            var17.setStatus(var13);
            getMethod.abort();
            getMethod.releaseConnection();
            return var17;
        }
    }

    public static synchronized Response post(String url, String charset, Map<String, String> header, Map<String, String> data) throws Exception {
        CustomPostMethod postMethod = new CustomPostMethod(url);

        Response var29;
        try {
            SecureProtocolSocketFactory e = new SecureProtocolSocketFactory();
            Protocol.registerProtocol("https", new Protocol("https", e, 443));
            HttpClient client = getClient(charset);
            Set headers = header.keySet();
            Iterator keys = headers.iterator();

            while (keys.hasNext()) {
                String ps = (String) keys.next();
                postMethod.setRequestHeader(ps, (String) header.get(ps));
            }

            Set var23 = data.keySet();
            NameValuePair[] var24 = new NameValuePair[var23.size()];
            int index = 0;

            String _map;
            for (Iterator statusCode = var23.iterator(); statusCode.hasNext(); var24[index++] = new NameValuePair(_map, (String) data.get(_map))) {
                _map = (String) statusCode.next();
            }

            postMethod.setRequestBody(var24);
            int var25 = client.executeMethod(postMethod);
            Header[] var26 = postMethod.getResponseHeaders();
            HashMap map = new HashMap();
            Header[] result = var26;
            int r = var26.length;

            for (int var16 = 0; var16 < r; ++var16) {
                Header h = result[var16];
                map.put(h.getName(), h.getValue());
            }

            if (var25 != 200) {
                postMethod.abort();
                throw new ConnectException("请求错误,status code is[" + var25 + "]");
            }

            String var27 = postMethod.getResponseBodyAsString();
            Response var28 = new Response();
            var28.setResult(var27);
            var28.setHeader(map);
            var28.setStatus(var25);
            postMethod.abort();
            var29 = var28;
        } catch (Exception var21) {
            postMethod.abort();
            throw var21;
        } finally {
            postMethod.releaseConnection();
        }

        return var29;
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

    public static void main(String[] args) {
        String url = "https://www.zhanqi.tv/api/static/live.hots/30-2.json";

        try {
            HashMap e = new HashMap();
            e.put("Host", "www.zhanqi.tv");
            String html = get(url, "utf-8", e).getResult();
            System.out.println(html);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    static {
        Integer MAX_TIME_OUT = Integer.valueOf(5000);
        Integer MAX_CONN = Integer.valueOf(5000);
        connectionManager.closeIdleConnections((long) MAX_TIME_OUT.intValue());
        connectionManager.getParams().setParameter("http.connection-manager.max-total", MAX_CONN);
    }
}
