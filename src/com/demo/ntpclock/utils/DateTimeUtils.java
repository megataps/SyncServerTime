package com.demo.ntpclock.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtils {

	public static Date getDateFromString(String dateInput, String formatDate) {
		if (dateInput.length() <= 0)
			return null;

		SimpleDateFormat df = new SimpleDateFormat(formatDate);
		try {
			Date date = df.parse(dateInput);

			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getStringFromDate(Date dateInput, String formatDate) {

		SimpleDateFormat format = new SimpleDateFormat(formatDate);
		String dateStr = format.format(dateInput);

		return dateStr;
	}

}
