package com.bdlog.demo.utils;

import com.bdlog.demo.domain.EventLogConstants;
import com.bdlog.demo.domain.IPRegionInfo;
import com.bdlog.demo.domain.UserAgentInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class LoggerUtil {
    private static final Logger logger = Logger.getLogger(LoggerUtil.class);
    private static IPSeekerExt ipSeekerExt = new IPSeekerExt();

    /**
     * 处理日志数据，返回处理结果
     *
     * @param logStr log的文件记录
     * @return
     */
    public static Map<String, String> handleLog(String logStr) {
        Map<String, String> clientInfo = new HashMap<>();
        if (StringUtils.isBlank(logStr))
            return null;
        String[] logSplit = logStr.trim().split(EventLogConstants.LOG_SEPARATOR);
        if (logSplit.length != 4)
            return null;//非法的格式，直接去除
        //1.设置ip
        clientInfo.put(EventLogConstants.LOG_COLUMN_NAME_IP, logSplit[0].trim());
        //2.设置服务器时间
        clientInfo.put(EventLogConstants.LOG_COLUMN_NAME_SERVER_TIME, logSplit[1].replace(".", ""));
        //3.获取请求参数，即收集数据
        int index = logSplit[3].indexOf("?");
        if (index <= -1)
            clientInfo.clear();//数据格式异常
        else {
            String requestBody = logSplit[3].substring(index + 1);
            handleRequestBody(requestBody, clientInfo);
            handleUserAgent(clientInfo);
            handleIp(clientInfo);
        }
        return clientInfo;
    }

    /**
     * 处理IP地址，即转换为归属地
     * @param clientInfo
     */
    private static void handleIp(Map<String, String> clientInfo) {
        if(!clientInfo.containsKey(EventLogConstants.LOG_COLUMN_NAME_IP))
            return;
        String ipStr = clientInfo.get(EventLogConstants.LOG_COLUMN_NAME_IP);
        IPRegionInfo ipRegionInfo = ipSeekerExt.analyticIp(ipStr);
        if(ipRegionInfo!=null) {
            clientInfo.put(EventLogConstants.LOG_COLUMN_IP_COUNTRY, ipRegionInfo.getCountry());
            clientInfo.put(EventLogConstants.LOG_COLUMN_IP_PROVINCE, ipRegionInfo.getProvince());
            clientInfo.put(EventLogConstants.LOG_COLUMN_IP_CITY, ipRegionInfo.getCity());
        }
    }

    /**
     * 处理浏览器的userAgent信息
     * @param clientInfo
     */
    private static void handleUserAgent(Map<String, String> clientInfo) {
        if(!clientInfo.containsKey(EventLogConstants.LOG_COLUMN_NAME_USER_AGENT))
            return;
        UserAgentInfo userAgentInfo = UserAgentUtil.analyticUserAgent(clientInfo.get(EventLogConstants.LOG_COLUMN_NAME_USER_AGENT));
        if (userAgentInfo !=null){
            clientInfo.put(EventLogConstants.LOG_COLUMN_NAME_USER_AGENT_OS_NAME,userAgentInfo.getOsName());
            clientInfo.put(EventLogConstants.LOG_COLUMN_NAME_USER_AGENT_OS_VERSION, userAgentInfo.getOsVersion());
            clientInfo.put(EventLogConstants.LOG_COLUMN_NAME_USER_AGENT_BROWSER_NAME, userAgentInfo.getBrowserName());
            clientInfo.put(EventLogConstants.LOG_COLUMN_NAME_USER_AGENT_BROWSER_VERSION, userAgentInfo.getBrowserVersion());
        }
    }

    /**
     * 把请求参数按key/value放到clientInfo中
     * @param requestBody
     * @param clientInfo
     */
    private static void handleRequestBody(String requestBody, Map<String, String> clientInfo) {
        if (StringUtils.isBlank(requestBody))
            return;
        String[] requestParamsArr = requestBody.split("&");
        for (String param : requestParamsArr) {
            if (StringUtils.isBlank(param))
                return;
            int index = param.indexOf("=");
            if (index < 0) {
                logger.warn("没法进行参数解析：" + param + ",请求参数为：" + requestBody);
                continue;
            }
                String key;
                String value;
            try {
                key = param.substring(0, index);
                value = URLDecoder.decode(param.substring(index + 1), "utf-8");
            } catch (UnsupportedEncodingException e) {
                logger.warn("解码操作出现异常："+e);
                continue;
            }
            if (StringUtils.isNotBlank(key)&&StringUtils.isNotBlank(value)){
                clientInfo.put(key,value);
            }
        }
    }
}
