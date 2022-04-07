package com.models.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.apache.commons.lang3.Range;

public class DateUtils {

	public static boolean isWeekDay(LocalDate date) {
		int dow = DayOfWeek.from(date).getValue();
		return (Range.between(0, 5).contains(dow));
	}

	public static int getWorkDayDiff(LocalDate startDate, LocalDate endDate) {
		int retValue = 0;

		if (startDate.isAfter(endDate)) {
			return retValue;
		}
		while (startDate.isBefore(endDate) || startDate.equals(endDate)) {
			if (isWeekDay(startDate))
				retValue++;
			startDate = startDate.plusDays(1);
		}

		return retValue;
	}

	public static long getMinsToExpire(String in_start_date, String in_end_date) {

		LocalDateTime startTime = LocalDateTime.parse(in_start_date, dtFormatter);
		LocalDate startDate = LocalDate.parse(in_start_date.subSequence(0, 8), dFormatter);

		LocalDate endDate = LocalDate.parse(in_end_date.subSequence(0, 8), dFormatter);

		long todayMinsLeft = getMinsTo4pm(startTime);

		if (startDate.equals(endDate)) {

			return todayMinsLeft;

		} else {
			int dateDiff = DateUtils.getWorkDayDiff(startDate, endDate);
			return todayMinsLeft + (dateDiff - 1) * 390;

		}
	}
	

	public static DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
	public static DateTimeFormatter dFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

	private static long getMinsTo4pm(LocalDateTime endDate) {
		long retValue = 0;
		if (endDate.getHour() <= 16) {
			LocalDateTime qDateObj4PM = endDate.toLocalDate().atTime(16, 0);
			retValue = (long) endDate.until(qDateObj4PM, ChronoUnit.MINUTES);
		}
		return retValue;
	}

	public static long getMinsTo4pm(String qDate) {
		return getMinsTo4pm(LocalDateTime.parse(qDate, dtFormatter));

	}

}
