package com.bdlog.demo.utils;

import com.bdlog.demo.domain.UserAgentInfo;
import cz.mallat.uasparser.OnlineUpdater;
import cz.mallat.uasparser.UASparser;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;

public class UserAgentUtil {
    static UASparser uaSparser = null;
    static {
        try {
            uaSparser = new UASparser(OnlineUpdater.getVendoredInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static UserAgentInfo analyticUserAgent(String userAgentStr){
        cz.mallat.uasparser.UserAgentInfo czInfo = null;
        UserAgentInfo userAgentInfoRet = null;
        if(StringUtils.isBlank(userAgentStr))
            return null;
        try {
           czInfo =  uaSparser.parse(userAgentStr);
           userAgentInfoRet = new UserAgentInfo();
           userAgentInfoRet.setBrowserName(czInfo.getUaFamily());
           userAgentInfoRet.setBrowserVersion(czInfo.getBrowserVersionInfo());
           userAgentInfoRet.setOsName(czInfo.getOsName());
           userAgentInfoRet.setOsVersion(czInfo.getOsFamily());
        } catch (IOException e) {
            e.printStackTrace();
            userAgentInfoRet = null;
        }
        return userAgentInfoRet;
    }
}
