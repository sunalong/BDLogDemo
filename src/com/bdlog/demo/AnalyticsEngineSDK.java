package com.bdlog.demo;

import com.bdlog.demo.utils.SendDataMonitor;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.net.URLEncoder.*;

/**
 * 分析引擎sdk， java服务器端数据收集
 */
public class AnalyticsEngineSDK {
    private static String platformName = "java_server";
    private static String sdkName = "chargeSDK";
    private static final String accessUrl = "http://172.16.250.129/log.gif";

    /**
     * 当支付成功后调用这个方法
     *
     * @param orderId  订单支付id
     * @param memberId 订单支付会员id
     * @return 如果发送数据成功(加入到发送队列中)返回true, 否则返回false
     */
    public static boolean onChargeSuccess(String orderId, String memberId) {
        try {
            if (isEmpty(orderId) || isEmpty(memberId)) {
                Logger.getGlobal().log(Level.WARNING, "订单id和会员id不能为空");
                return false;
            }
            Map<String, String> data = new HashMap<>();
            data.put("u_mid", memberId);
            data.put("oid", orderId);
            data.put("c_time", String.valueOf(System.currentTimeMillis()));
            data.put("ver", "1");
            data.put("en", "e_cs");
            data.put("pl", platformName);
            data.put("sdk", sdkName);
            String url = buildUrl(data);
            SendDataMonitor.addSendUrl(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 触发订单退款事件，发送退款数据到服务器
     *
     * @param orderId
     *            退款订单id
     * @param memberId
     *            退款会员id
     * @return 如果发送数据成功，返回true。否则返回false。
     */
    public static boolean onChargeRefund(String orderId, String memberId) {
        try {
            if (isEmpty(orderId) || isEmpty(memberId)) {
                // 订单id或者memberid为空
                Logger.getGlobal().log(Level.WARNING, "订单id和会员id不能为空");
                return false;
            }
            // 代码执行到这儿，表示订单id和会员id都不为空。
            Map<String, String> data = new HashMap<String, String>();
            data.put("u_mid", memberId);
            data.put("oid", orderId);
            data.put("c_time", String.valueOf(System.currentTimeMillis()));
            data.put("ver", "1");
            data.put("en", "e_cr");
            data.put("pl", platformName);
            data.put("sdk", sdkName);
            // 构建url
            String url = buildUrl(data);
            // 发送url&将url添加到队列中
            SendDataMonitor.addSendUrl(url);
            return true;
        } catch (Throwable e) {
            Logger.getGlobal().log(Level.WARNING, "发送数据异常", e);
        }
        return false;
    }
    /**
     * 根据传入的参数构建url
     *
     * @param data
     * @return
     */
    private static String buildUrl(Map<String, String> data) throws UnsupportedEncodingException {
        StringBuffer sb = new StringBuffer();
        sb.append(accessUrl).append("?");
        for (Map.Entry<String, String> entry : data.entrySet()) {
            if (!isEmpty(entry.getKey()) && !isEmpty(entry.getValue())) {
                sb.append(entry.getKey().trim())
                        .append("=")
                        .append(encode(entry.getValue().trim(), "UTF-8"))
                        .append("&");
            }
        }

        return sb.substring(0, sb.length() - 1);
    }

    private static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}
