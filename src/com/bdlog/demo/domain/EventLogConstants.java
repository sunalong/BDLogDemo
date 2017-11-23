package com.bdlog.demo.domain;

public class EventLogConstants {
    /**
     * 日志分隔符
     */
    public static final String LOG_SEPARATOR = "\\^A";
    /**
     * 用户IP地址
     */
    public static final String LOG_COLUMN_NAME_IP = "ip";
    /**
     * 服务器时间
     */
    public static final String LOG_COLUMN_NAME_SERVER_TIME = "s_time";
    /**
     * 事件名称
     */
    public static final String LOG_COLUMN_NAME_EVENT_NAME = "en";
    /**
     * 表名称
     */
    public static final String HBASE_NAME_EVENT_LOGS = "event_logs";
    /**
     * 定义的运行时间变量名
     */
    public static final String RUNNING_DATE_PARAMES = "RUNNING_DATE";
    /**
     * 浏览器user agent参数
     */
    public static String LOG_COLUMN_NAME_USER_AGENT = "b_iev";
    /**
     * 操作系统名称
     */
    public static String LOG_COLUMN_NAME_USER_AGENT_OS_NAME = "os";
    /**
     * 操作系统版本号
     */
    public static String LOG_COLUMN_NAME_USER_AGENT_OS_VERSION = "os_v";
    /**
     * 浏览器名称
     */
    public static String LOG_COLUMN_NAME_USER_AGENT_BROWSER_NAME = "browser";
    /**
     * 浏览器版本号
     */
    public static String LOG_COLUMN_NAME_USER_AGENT_BROWSER_VERSION = "browser_v";
    /**
     * IP归属地：国家
     */
    public static final String LOG_COLUMN_IP_COUNTRY = "country";
    /**
     * IP归属地：省份
     */
    public static final String LOG_COLUMN_IP_PROVINCE = "province";
    /**
     * IP归属地：城市
     */
    public static final String LOG_COLUMN_IP_CITY = "city";
    /**
     * 用户唯一标识符
     */
    public static final String LOG_column_Name_UUID = "u_ud";
    /**
     * 会话id
     */
    public static final String LOG_COLUMN_NAME_SESSION_ID = "u_sd";
    /**
     * 会员唯一标识符
     */
    public static final String LOG_COLUMN_NAME_MEMBER_ID = "u_mid";
    /**
     * 服务器时间
     */
    public static final String LOG_COLUMN_NAME_SERVER_ID = "s_time";
    /**
     * event_logs表的列簇名称
     */
    public static final String EVENT_LOGS_FAMILY_NAME = "info";

    /**
     * 事件枚举类，指定事件的名称
     */
    public static enum EventEnum {
        Launch(1, "launch event", "e_1"),//launch事件，表示第一次访问
        PAGE_VIEW(2, "page view event", "e_pv"),//页面浏览事件
        CHARGE_REQUEST(3, "charge request event", "e_crt"),//订单生产事件
        CHARGE_SUCCESS(4, "charge success event", "e_cs"),//订单成功事件
        CHARGE_REFUND(5, "charge refund event", "e_cr"),//订单退款事件
        EVENT(6, "event duration event", "e_e")//事件
        ;
        public final int id;//唯一标识
        public final String name;//名称
        public final String alias;//别名，用于数据收集的简写

        private EventEnum(int id, String name, String alias) {
            this.id = id;
            this.name = name;
            this.alias = alias;
        }

        /**
         * 获取匹配别名的event枚举对象
         *
         * @param alias
         * @return
         */
        public static EventEnum valueofAlias(String alias) {
            for (EventEnum eventEnum : values()) {
                if (eventEnum.alias.equals(alias))
                    return eventEnum;
            }
            return null;
        }
    }

}
