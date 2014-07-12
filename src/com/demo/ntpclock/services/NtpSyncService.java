package com.demo.ntpclock.services;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.NtpV3Packet;
import org.apache.commons.net.ntp.TimeInfo;
import org.apache.commons.net.ntp.TimeStamp;

import android.content.Context;

import com.demo.ntpclock.AppConfigs;
import com.demo.ntpclock.R;

public class NtpSyncService {

	public static TimeInfo getCurrentNetworkTimeInfo(String ntpServerHostname) {

		NTPUDPClient client = new NTPUDPClient();
		client.setDefaultTimeout(AppConfigs.REQUEST_TIMEOUT);

		TimeInfo info = null;

		try {
			
			client.open();
			InetAddress hostAddr = InetAddress.getByName(ntpServerHostname);				
			info = client.getTime(hostAddr);		
			
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			
		} finally {
			
			client.close();
		}

		return info;
	}

	public static String parseTimeInfo(TimeInfo info, Context context) {

		StringBuilder htmlContent = new StringBuilder();

		NtpV3Packet message = info.getMessage();

//		TimeStamp refNtpTime = message.getReferenceTimeStamp();
//		htmlContent.append("<div>");
//		htmlContent.append("<b>");
//		htmlContent.append(context.getString(R.string.reference_timestamp));
//		htmlContent.append("</b>");
//		htmlContent.append(refNtpTime.toDateString());
//		htmlContent.append("</div>");
//
//		// Originate Time is time request sent by client
//		TimeStamp origNtpTime = message.getOriginateTimeStamp();
//		htmlContent.append("<div>");
//		htmlContent.append("<b>");
//		htmlContent.append(context.getString(R.string.originate_timestamp));
//		htmlContent.append("</b>");
//		htmlContent.append(origNtpTime.toDateString());
//		htmlContent.append("</div>");
//
//		long destTime = info.getReturnTime();
//		// Receive Time is time request received by server
//		TimeStamp rcvNtpTime = message.getReceiveTimeStamp();
//		htmlContent.append("<div>");
//		htmlContent.append("<b>");
//		htmlContent.append(context.getString(R.string.receive_timestamp));
//		htmlContent.append("</b>");
//		htmlContent.append(rcvNtpTime.toDateString());
//		htmlContent.append("</div>");

		// Transmit time is time reply sent by server
		TimeStamp xmitNtpTime = message.getTransmitTimeStamp();
		htmlContent.append("<div>");
		htmlContent.append("<b>");
		htmlContent.append(context.getString(R.string.server_time_title));
		htmlContent.append("</b>");
		htmlContent.append(xmitNtpTime.toDateString());
		htmlContent.append("</div>");

//		// Destination time is time reply received by client
//		TimeStamp destNtpTime = TimeStamp.getNtpTime(destTime);
//		htmlContent.append("<div>");
//		htmlContent.append("<b>");
//		htmlContent.append(context.getString(R.string.destination_timestamp));
//		htmlContent.append("</b>");
//		htmlContent.append(destNtpTime.toDateString());
//		htmlContent.append("</div>");

		return htmlContent.toString();
	}
}
