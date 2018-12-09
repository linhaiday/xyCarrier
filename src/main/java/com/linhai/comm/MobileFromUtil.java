package com.linhai.comm;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 通过手机号码,获得该号码的归属地
 *
 * @author Administrator
 *
 */
public final class MobileFromUtil {

    /**
     * 获得手机号码归属地
     */
    public static String getMobileFrom(String mobileNumber) throws Exception {
        String result = null;
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();

            HttpGet httpget = new HttpGet("http://www.ip138.com:8080/search.asp?action=mobile&mobile="+mobileNumber);
            httpget.addHeader("content-type", "text/html; charset=gb2312");
            httpget.addHeader("mobile-agent", "format=html5; url=http://m.ip138.com/mobile.html");
            StringBuilder resMes = new StringBuilder();

            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(600000)
                    .setConnectTimeout(15000).build();
            httpget.setConfig(requestConfig);
            CloseableHttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            BufferedReader in = new BufferedReader(new InputStreamReader(entity.getContent(), "gb2312"));
            String line;
            while ((line = in.readLine()) != null) {
                resMes.append(line);
            }
            Document doc = Jsoup.parse(resMes.toString());
            Elements elts = doc.getElementsByTag("TR");
            Element elt = elts.get(3).getElementsByTag("td").get(1);
            result=elt.text().replace(" ", "省");
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getMobileCity(String mobileNumber) throws Exception {
        String result = null;
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();

            HttpGet httpget = new HttpGet("http://www.ip138.com:8080/search.asp?action=mobile&mobile="+mobileNumber);
            httpget.addHeader("content-type", "text/html; charset=gb2312");
            httpget.addHeader("mobile-agent", "format=html5; url=http://m.ip138.com/mobile.html");
            StringBuilder resMes = new StringBuilder();

            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(600000)
                    .setConnectTimeout(15000).build();
            httpget.setConfig(requestConfig);
            CloseableHttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            BufferedReader in = new BufferedReader(new InputStreamReader(entity.getContent(), "gb2312"));
            String line;
            while ((line = in.readLine()) != null) {
                resMes.append(line);
            }
            Document doc = Jsoup.parse(resMes.toString());
            Elements elts = doc.getElementsByTag("TR");
            Element elt = elts.get(3).getElementsByTag("td").get(1);
            result=elt.text().split(" ")[1];
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return result;
    }
}