package test;

import com.bdlog.demo.utils.IPSeeker;

public class TestIPSeeker {
	public static void main(String[] args) {
		IPSeeker ipSeeker = IPSeeker.getInstance();
		System.out.println(ipSeeker.getCountry("120.197.87.216"));
		System.out.println(ipSeeker.getCountry("192.168.1.14"));
		System.out.println(ipSeeker.getCountry("112.225.35.70"));
		System.out.println(ipSeeker.getCountry("212.225.35.70"));
	}
}
