package com.bdlog.demo.domain;

/**
 * IP归属地的javaBean
 */
public class IPRegionInfo {
    public static final String DEFAULT_VALUE = "unknown"; // 默认值
    private String country = DEFAULT_VALUE; // 国家
    private String province = DEFAULT_VALUE; // 省份
    private String city = DEFAULT_VALUE; // 城市

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "IPRegionInfo [country=" + country + ", province=" + province + ", city=" + city + "]";
    }
}