package cn.platform.core.util;

import cn.platform.core.constants.CommonConstants;
import cn.platform.core.exception.PlatformErrorConstants;
import cn.platform.core.exception.PlatformException;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Package: cn.platform.core.util
 * @ClassName: HttpClientUtil
 * @Author: zhangmingyang
 * @CreateDate: 2018/8/16 10:51
 * @Version: 1.0
 */
public class HttpClientUtil {
    private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    /**
     * post 请求
     *
     * @param url
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String doPostRequest(String url, Map<String, String> params) throws IOException {
        CloseableHttpClient httpClient = getHttpClient();
        HttpPost post = new HttpPost(url);

        //设置配置参数
        post.setConfig(setRequestConfig());

        //设置请求业务参数
        if (MapUtils.isNotEmpty(params)) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();
            for (String key : params.keySet()) {
                nameValuePairList.add(new BasicNameValuePair(key, params.get(key)));
            }
            post.setEntity(new UrlEncodedFormEntity(nameValuePairList, CommonConstants.COMMON_ENCODING));
        }

        return EntityUtils.toString(httpClient.execute(post).getEntity());
    }


    /**
     * 获取httpclient
     *
     * @return
     */
    private static CloseableHttpClient getHttpClient() {
        //创建http
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

        //连接池设置
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultMaxPerRoute(20); //每个主机的最大并行链接数
        connectionManager.setMaxTotal(100);          //客户端总并行链接最大数
        httpClientBuilder.setConnectionManager(connectionManager);
        return httpClientBuilder.build();
    }

    /**
     * 设置请求配置参数
     *
     * @return
     */
    private static RequestConfig setRequestConfig() {
        return RequestConfig.custom()
                .setConnectTimeout(20 * 1000)//创建链接时间
                .setConnectionRequestTimeout(20 * 1000)//获取链接时间
                .setSocketTimeout(10 * 1000) // 数据传输的最长时间
                .build();
    }


    /**
     * 获取远程客户端IP
     *
     * @param httpServletRequest
     * @return
     */
    public static String getRemoteClientIP(HttpServletRequest httpServletRequest) {
        String ip = httpServletRequest.getHeader("x-forwarded-for");
        if (checkIPVal(ip)) {
            ip = httpServletRequest.getHeader("Proxy-Client-IP");
        }
        if (checkIPVal(ip)) {
            ip = httpServletRequest.getHeader("WL-Proxy-Client-IP");
        }
        if (checkIPVal(ip)) {
            ip = httpServletRequest.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 判断ip值
     *
     * @param ip
     * @return
     */
    private static boolean checkIPVal(String ip) {
        return StringUtils.isEmpty(ip) || "unknown".equals(ip);
    }

    /**
     * 禁止IP
     *
     * @param httpServletRequest
     */
    public static void banIP(HttpServletRequest httpServletRequest, String banSwitchStr, List<String> ipArrsy) {
        //开关打开进行校验
        if (String.valueOf(Boolean.TRUE).equals(banSwitchStr)) {
            //获取IP
            String requestIp = getRemoteClientIP(httpServletRequest);

            //获取允许访问Ip,未配置所有IP允许访问,配置进行校验
            if (CollectionUtils.isNotEmpty(ipArrsy)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("外部请求IP:{}", requestIp);
                }
                boolean isAllow = false;
                for (String s : ipArrsy) {
                    if (requestIp.equals(s)) {
                        isAllow = true;
                        break;
                    }
                }

                if (!isAllow) {
                    if (logger.isWarnEnabled()) {
                        logger.warn("非法人员:{}访问该系统,已被拦截", requestIp);
                    }
                    throw new PlatformException(PlatformErrorConstants.PLATFORM_CORE_0001, requestIp);
                }
            }
        }
    }

    /**
     * 获取当前机器IP
     *
     * @return
     */
    public static String getSystemLocalAddress() {
        String hostAddress = null;
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            if (logger.isErrorEnabled()) {
                logger.error("Get localHost address faied , cuase by ;", e);
            }
        }
        return hostAddress;
    }
}
