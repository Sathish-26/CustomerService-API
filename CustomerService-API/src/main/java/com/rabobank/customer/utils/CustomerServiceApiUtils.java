package com.rabobank.customer.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomerServiceApiUtils {

	public static boolean isValidDate(Date date) {
		Pattern p = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = sdf.format(date);
		Matcher m = p.matcher(dateStr);
		return m.matches();
	}

	public static Integer getAge(Date dateOfBirth) {
		Integer age = 0;
		Calendar c = Calendar.getInstance();
		c.setTime(dateOfBirth);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int date = c.get(Calendar.DATE);
		LocalDate l1 = LocalDate.of(year, month, date);
		LocalDate now1 = LocalDate.now();
		Period period = Period.between(l1, now1);
		age = period.getYears();
		return age;
	}
}
