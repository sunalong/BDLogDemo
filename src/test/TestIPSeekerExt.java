package test;


import com.bdlog.demo.domain.IPRegionInfo;
import com.bdlog.demo.utils.IPSeekerExt;

public class TestIPSeekerExt {
    public static void main(String[] args) {
        IPSeekerExt ipSeekerExt = new IPSeekerExt();
        IPRegionInfo info1 = ipSeekerExt.analyticIp("120.197.87.216");
        System.out.println(info1);
        IPRegionInfo info = ipSeekerExt.analyticIp("112.225.35.70");
        System.out.println(info);

//		List<String> ips = ipSeekerExt.getAllIp();
//		for (String ip : ips) {
//			System.out.println(ipSeekerExt.analyticIp(ip));
//		}
    }
}
