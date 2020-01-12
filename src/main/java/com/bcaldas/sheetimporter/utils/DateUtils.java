package com.bcaldas.sheetimporter.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public final class DateUtils {

	private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	private static final DateTimeFormatter dateFormatterHyphen = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

	public static LocalDate parseStringToLocalDate(String date) {
		if (date.matches("\\d\\d-[a-z][a-z][a-z]-\\d\\d\\d\\d")) {
			return LocalDate.parse(date, dateFormatterHyphen);
		}
		return LocalDate.parse(date, dateFormatter);
	}
	
	public static LocalDateTime calendarToLocalDateTime(Calendar calendar) {
	      if (calendar == null) {
	          return null;
	      }
	      TimeZone tz = calendar.getTimeZone();
	      ZoneId zid = tz == null ? ZoneId.systemDefault() : tz.toZoneId();
	      return LocalDateTime.ofInstant(calendar.toInstant(), zid);
	  }

	public static LocalDateTime parseStringToLocalDateTime(String date) {
		return LocalDateTime.parse(date, dateTimeFormatter);
	}

	public static String formatLocalDate(LocalDate date) {
		return date.format(dateFormatter);
	}

	public static String formatLocalDate(LocalDate date, String pattern) {
		DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern(pattern);
		return date.format(customFormatter);
	}

	public static String formatLocalDatetime(LocalDateTime date) {
		return date.format(dateTimeFormatter);
	}

	public static String formatLocalDatetime(LocalDateTime date, String pattern) {
		DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern(pattern);
		return date.format(customFormatter);
	}

	public static Date parseLocalDateToDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}

	public static Date parseLocalDateTimeToDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	public static LocalDate parseDateToLocalDate(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static LocalDateTime parseDateToLocalDateTime(Date date) {
		return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	public static LocalDate parseToLocalDateViaInstant(Date dateToConvert) {
		return dateToConvert.toInstant()
				.atZone(ZoneId.systemDefault())
				.toLocalDate();
	}
}
