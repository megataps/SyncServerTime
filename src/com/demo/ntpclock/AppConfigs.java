package com.demo.ntpclock;

public class AppConfigs {
	
	public static final int REQUEST_WIFI_SETTING_CODE = 1;
	
	public static final String NTP_SERVER_URL = "0.ubuntu.pool.ntp.org";
	
	public static final int REQUEST_TIMEOUT = 30*1000; // time in millisecond
	
	public static final String DIGITAL_TIMER_FORMAT = "%02d:%02d:%02d";	
	public static final String COUNTDONW_TIMER_FORMAT = "%02d:%02d";
	public static final String CURRENT_DATE_FORMAT = "MM-dd-yyyy";
	
	public static final long START_COUNDDOWN_TIME = 10*60*1000;
	public static final long COUNTDOWN_ITNTERVAL = 1*1000;
	
}
