package test;

import com.bdlog.demo.AnalyticsEngineSDK;

public class TestLog {
    public static void main(String[] args){
        AnalyticsEngineSDK.onChargeSuccess("orderid123", "zhougong123");
        AnalyticsEngineSDK.onChargeRefund("orderid456", "zhougong456");
    }
}
