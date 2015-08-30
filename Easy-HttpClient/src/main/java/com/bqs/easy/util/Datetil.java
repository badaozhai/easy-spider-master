package com.bqs.easy.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Datetil {

	static Log log = LogFactory.getLog(Datetil.class);

	/**
	 * 输入时间，自动转换成日期long
	 * 
	 * @param timeStr
	 * @return
	 */
	public static long convergeTime(String timeStr) {
		String formate = "";
		String dateFormate = "yyyy-MM-dd HH:mm:ss";
		Long longDate = Long.valueOf(0L);

		// 预处理
		timeStr = timeStr.trim();
		// 全角空格替换成空格
		timeStr = timeStr.replaceAll("　", " ");
		timeStr = timeStr.replaceAll("  ", " ");
		// 多个空格或制表符替换成一个
		timeStr = timeStr.replaceAll("[ \t]+", " ");
		timeStr = timeStr.replaceAll("\\s{1,}", " ");

		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(1);
		int month = calendar.get(2) + 1;
		int day = calendar.get(5);

		try {
			// xx秒前
			String regex = "([\\d]{1,2})[\\s]*?秒前";
			String partDate = getStringByRegex(timeStr, regex, 1, 2);

			if (partDate.length() > 0) {
				int timeInt = Integer.parseInt(partDate);
				calendar.add(13, -timeInt);
				longDate = Long.valueOf(calendar.getTimeInMillis());
				return longDate.longValue();
			}

			// xx分钟前/分前
			regex = "([\\d]{1,2})[\\s]*?分[钟]?前";
			partDate = getStringByRegex(timeStr, regex, 1, 2);
			if (partDate.length() > 0) {
				int timeInt = Integer.parseInt(partDate);
				calendar.add(12, -timeInt);
				longDate = Long.valueOf(calendar.getTimeInMillis());
				return longDate.longValue();
			}

			// xx小时前
			regex = "([\\d]{1,2})[\\s]*?小时前";
			partDate = getStringByRegex(timeStr, regex, 1, 2);
			if (partDate.length() > 0) {
				int timeInt = Integer.parseInt(partDate);
				calendar.add(10, -timeInt);
				longDate = Long.valueOf(calendar.getTimeInMillis());
				return longDate.longValue();
			}

			// xx天前
			regex = "([\\d]{1,2})[\\s]*?天前";
			partDate = getStringByRegex(timeStr, regex, 1, 2);
			if (partDate.length() > 0) {
				int timeInt = Integer.parseInt(partDate);
				calendar.add(5, -timeInt);
				longDate = Long.valueOf(calendar.getTimeInMillis());
				return longDate.longValue();
			}

			// xx月前
			regex = "([\\d]{1,2})[\\s]*?月前";
			partDate = getStringByRegex(timeStr, regex, 1, 2);
			if (partDate.length() > 0) {
				int timeInt = Integer.parseInt(partDate);
				calendar.add(Calendar.MONTH, -timeInt);
				longDate = Long.valueOf(calendar.getTimeInMillis());
				return longDate.longValue();
			}

			// xx年前
			regex = "([\\d]{1,2})[\\s]*?年前";
			partDate = getStringByRegex(timeStr, regex, 1, 2);
			if (partDate.length() > 0) {
				int timeInt = Integer.parseInt(partDate);
				calendar.add(Calendar.YEAR, -timeInt);
				longDate = Long.valueOf(calendar.getTimeInMillis());
				return longDate.longValue();
			}

			// 今天xx(时):xx(分) 24小时制
			regex = "今天[\\s]{0,}([\\d]{1,2}:[\\d]{1,2})";
			partDate = getStringByRegex(timeStr, regex, 1, 2);

			if (partDate.length() > 0) {
				formate = year + "-" + month + "-" + day + " " + partDate + ":01";
				longDate = Long.valueOf(string2long(formate, dateFormate));
				return longDate.longValue();
			}

			// 昨天xx(时):xx(分) 24小时制
			regex = "昨天[\\s]{0,}([\\d]{1,2}:[\\d]{1,2})";
			partDate = getStringByRegex(timeStr, regex, 1, 2);

			if (partDate.length() > 0) {
				calendar.add(5, -1);
				formate = calendar.get(1) + "-" + (calendar.get(2) + 1) + "-" + calendar.get(5) + " " + partDate
						+ ":00";
				longDate = Long.valueOf(string2long(formate, dateFormate));
				return longDate.longValue();
			}

			// 2000-2019年xx月xx日 xx:xx
			regex = "20[01][\\d]年[\\d]{1,2}月[\\d]{1,2}日 [\\d]{1,2}:[\\d]{1,2}";
			partDate = getStringByRegex(timeStr, regex, 0, 2);
			if (partDate.length() > 0) {
				partDate = partDate.replaceAll("[年月]", "-");
				partDate = partDate.replaceAll("[日]", "");
				partDate = partDate + ":00";
				longDate = Long.valueOf(string2long(partDate, dateFormate));
				return longDate.longValue();
			}

			// xx(月)/xx(日) xx:xx
			regex = "[\\d]{1,2}/[\\d]{1,2} [\\d]{1,2}:[\\d]{1,2}";
			partDate = getStringByRegex(timeStr, regex, 0, 2);
			if (partDate.length() > 0) {
				partDate = partDate.replaceAll("/", "-");
				partDate = year + "-" + partDate;
				partDate = partDate + ":00";
				longDate = Long.valueOf(string2long(partDate, dateFormate));
				return longDate.longValue();
			}

			// xx(月)-xx(日) xx:xx
			regex = "[\\d]{1,2}-[\\d]{1,2}[\\s][\\d]{1,2}:?[\\d]{1,2}";
			partDate = getStringByRegex(timeStr, regex, 0, 2);
			if (partDate.length() > 0) {
				partDate = year + "-" + partDate;
				partDate = partDate + ":00";
				longDate = Long.valueOf(string2long(partDate, dateFormate));
				return longDate.longValue();
			}
			// xx(月)-xx(日)
			regex = "[\\d]{1,2}-[\\d]{1,2}";
			partDate = getStringByRegex(timeStr, regex, 0, 2);
			if (partDate.length() > 0) {
				partDate = year + "-" + partDate;
				partDate = partDate + " 00:00:10";
				longDate = Long.valueOf(string2long(partDate, dateFormate));
				return longDate.longValue();
			}

			// (10-9999)-xx-xx xx:xx
			regex = "[\\d]{2,4}-[\\d]{1,2}-[\\d]{1,2}[\\s][\\d]{1,2}:?[\\d]{1,2}";
			partDate = getStringByRegex(timeStr, regex, 0, 2);
			if ((partDate != null) && (partDate.length() > 0)) {
				partDate = partDate + ":00";
				longDate = Long.valueOf(string2long(partDate, dateFormate));
				return longDate.longValue();
			}
			// 20150630 14:41
			regex = "20[\\d]{6}[\\s]*[\\d]{1,2}:?[\\d]{1,2}";
			partDate = getStringByRegex(timeStr, regex, 0, 2);
			if ((partDate != null) && (partDate.length() > 0)) {
				partDate = partDate + ":01";
				longDate = Long.valueOf(string2long(partDate, "yyyyMMdd HH:mm:ss"));
				return longDate.longValue();
			}

			// (10-9999)-xx-xx xx:xx:xx(毫秒)
			regex = "[\\d]{2,4}-[\\d]{1,2}-[\\d]{1,2}[\\s]*?[\\d]{1,2}:[\\d]{1,2}:[\\d]{1,2}";
			partDate = getStringByRegex(timeStr, regex, 0, 2);
			if ((partDate != null) && (partDate.length() > 0)) {
				longDate = Long.valueOf(string2long(partDate, dateFormate));
				return longDate.longValue();
			}
			// 2015-07-10 23:59:59.0
			regex = "[\\d]{2,4}-[\\d]{1,2}-[\\d]{1,2}[\\s]*?[\\d]{1,2}:[\\d]{1,2}:[\\d]{1,2}\\.[\\d]{1,}";
			partDate = getStringByRegex(timeStr, regex, 0, 2);
			if ((partDate != null) && (partDate.length() > 0)) {
				longDate = Long.valueOf(string2long(partDate, dateFormate));
				return longDate.longValue();
			}

			// 2014/3/28 10:29
			regex = "[\\d]{2,4}/[\\d]{1,2}/[\\d]{1,2}[\\s][\\d]{1,2}:[\\d]{1,2}";
			partDate = getStringByRegex(timeStr, regex, 0, 2);
			if ((partDate != null) && (partDate.length() > 0)) {
				partDate = partDate.replaceAll("/", "-") + ":01";
				longDate = Long.valueOf(string2long(partDate, dateFormate));
				return longDate.longValue();
			}
			// 2014/3/28 10:29:06
			regex = "[\\d]{2,4}/[\\d]{1,2}/[\\d]{1,2}[\\s][\\d]{1,2}:[\\d]{1,2}:[\\d]{1,2}";
			partDate = getStringByRegex(timeStr, regex, 0, 2);
			if ((partDate != null) && (partDate.length() > 0)) {
				partDate = partDate.replaceAll("/", "-");
				longDate = Long.valueOf(string2long(partDate, dateFormate));
				return longDate.longValue();
			}

			// 2014/3/28
			regex = "[\\d]{2,4}/[\\d]{1,2}/[\\d]{1,2}";
			partDate = getStringByRegex(timeStr, regex, 0, 2);
			if ((partDate != null) && (partDate.length() > 0)) {
				partDate = partDate.replaceAll("/", "-");
				partDate = partDate + " 00:01:00";
				longDate = Long.valueOf(string2long(partDate, dateFormate));
				return longDate.longValue();
			}
			// 2015.05.15
			regex = "[\\d]{2,4}[^\\d][\\d]{1,2}[^\\d][\\d]{1,2}";
			partDate = getStringByRegex(timeStr, regex, 0, 2);
			if ((partDate != null) && (partDate.length() > 0)) {
				partDate = partDate.replaceAll("[^\\d]", "-");
				partDate = partDate + " 00:01:00";
				longDate = Long.valueOf(string2long(partDate, dateFormate));
				return longDate.longValue();
			}

			// 2015.08.04 12:13:57
			regex = "[\\d]{2,4}[^\\d][\\d]{1,2}[^\\d][\\d]{1,2}[\\s][\\d]{1,2}:[\\d]{1,2}:[\\d]{1,2}";
			partDate = getStringByRegex(timeStr, regex, 0, 2);
			if ((partDate != null) && (partDate.length() > 0)) {
				partDate = partDate.replaceAll("[^\\d]", "-");
				String format = "yyyy-MM-dd-HH-mm-ss";
				longDate = Long.valueOf(string2long(partDate, format));
				return longDate.longValue();
			}

			regex = "[\\d]{1,2}月[\\d]{1,2}日[\\s]*?[\\d]{1,2}:[\\d]{1,2}";
			partDate = getStringByRegex(timeStr, regex, 0, 2);
			if ((partDate != null) && (partDate.length() > 0)) {
				partDate = partDate.replaceAll("[月]", "-");
				partDate = partDate.replaceAll("[日]", " ");
				formate = year + "-" + partDate + ":01";
				longDate = Long.valueOf(string2long(formate, dateFormate));
				return longDate.longValue();
			}

			// 2014年08月22日 22:15
			regex = "[\\d]{4}年[\\d]{1,2}月[\\d]{1,2}日[\\s]*?[\\d]{1,2}:[\\d]{1,2}";
			partDate = getStringByRegex(timeStr, regex, 0, 2);
			if ((partDate != null) && (partDate.length() > 0)) {
				partDate = partDate.replaceAll("[年]", "-");
				partDate = partDate.replaceAll("[月]", "-");
				partDate = partDate.replaceAll("[日]", " ");
				longDate = Long.valueOf(string2long(partDate + ":01", dateFormate));
				return longDate.longValue();
			}

			// 2014年08月22日 22:15:01
			regex = "[\\d]{4}年[\\d]{1,2}月[\\d]{1,2}日[\\s]*?[\\d]{1,2}:[\\d]{1,2}:[\\d]{1,2}";
			partDate = getStringByRegex(timeStr, regex, 0, 2);
			if ((partDate != null) && (partDate.length() > 0)) {
				partDate = partDate.replaceAll("[年]", "-");
				partDate = partDate.replaceAll("[月]", "-");
				partDate = partDate.replaceAll("[日]", " ");
				longDate = Long.valueOf(string2long(partDate, "yyyy-MM-dd HH:mm:ss"));
				return longDate.longValue();
			}

			// 2014年1月2日
			regex = "\\d{4}年\\d{1,2}月\\d{1,2}日";
			partDate = getStringByRegex(timeStr, regex, 0, 2);
			if ((partDate != null) && (partDate.length() > 0)) {
				partDate = partDate.replaceAll("[年]", "-");
				partDate = partDate.replaceAll("[月]", "-");
				partDate = partDate.replaceAll("[日]", "") + " 00:01:01";
				longDate = Long.valueOf(string2long(partDate, dateFormate));
				return longDate.longValue();
			}

			// 2014年1月
			regex = "\\d{4}年\\d{1,2}月";
			partDate = getStringByRegex(timeStr, regex, 0, 2);
			if ((partDate != null) && (partDate.length() > 0)) {
				partDate = partDate.replaceAll("[年]", "-");
				partDate = partDate.replaceAll("[月]", "-") + "-01 00:01:01";
				longDate = Long.valueOf(string2long(partDate, dateFormate));
				return longDate.longValue();
			}
			// 2014年
			regex = "\\d{4}年";
			partDate = getStringByRegex(timeStr, regex, 0, 2);
			if ((partDate != null) && (partDate.length() > 0)) {
				partDate = partDate.replaceAll("[年]", "") + "-01-01 00:01:01";
				longDate = Long.valueOf(string2long(partDate, dateFormate));
				return longDate.longValue();
			}

			// 14年
			regex = "\\d{2}年";
			partDate = getStringByRegex(timeStr, regex, 0, 2);
			if ((partDate != null) && (partDate.length() > 0)) {
				int time = Integer.parseInt("20" + partDate.replace("年", ""));
				if (time > year) {
					time = Integer.parseInt("19" + partDate.replace("年", ""));
				}
				partDate = time + "-01-01 00:01:01";
				longDate = Long.valueOf(string2long(partDate, dateFormate));
				return longDate.longValue();
			}
			// 14
			regex = "\\d{2}";
			partDate = getStringByRegex(timeStr, regex, 0, 2);
			if ((partDate != null) && (partDate.length() > 0)) {
				int time = Integer.parseInt("20" + partDate);
				if (time > year) {
					time = Integer.parseInt("19" + partDate);
				}
				partDate = time + "-01-01 00:01:01";
				longDate = Long.valueOf(string2long(partDate, dateFormate));
				return longDate.longValue();
			}

			// 2014
			regex = "\\d{4}";
			partDate = getStringByRegex(timeStr, regex, 0, 2);
			if ((partDate != null) && (partDate.length() > 0)) {
				partDate = partDate + "-01-01 00:01:01";
				longDate = Long.valueOf(string2long(partDate, dateFormate));
				return longDate.longValue();
			}

			// 01月 12
			regex = "\\d{1,2}月[\\s]\\d{1,2}";
			partDate = getStringByRegex(timeStr, regex, 0, 2);
			if ((partDate != null) && (partDate.length() > 0)) {
				partDate = partDate.replaceAll("[月]", "-");
				formate = year + "-" + partDate;
				longDate = Long.valueOf(string2long(formate.replace(" ", "") + " 00:01:01", dateFormate));
				return longDate.longValue();
			}

			// 14-07-01
			regex = "\\d{2}-\\d{1,2}-\\d{1,2}";
			partDate = getStringByRegex(timeStr, regex, 0, 2);
			if ((partDate != null) && (partDate.length() > 0)) {
				partDate = "20" + partDate;
				dateFormate = "yyyy-MM-dd";
				longDate = Long.valueOf(string2long(partDate, dateFormate));
				return longDate.longValue();
			}

			// 3 days ago
			regex = "(\\d{1,2})\\s*?days?\\s*?ago";
			partDate = getStringByRegex(timeStr, regex, 1, 2);
			if (partDate.length() > 0) {
				int timeInt = Integer.parseInt(partDate);
				calendar.add(5, -timeInt);
				longDate = Long.valueOf(calendar.getTimeInMillis());
				return longDate.longValue();
			}

			// 3 months ago
			regex = "(\\d{1,2})\\s*?months?\\s*?ago";
			partDate = getStringByRegex(timeStr, regex, 1, 2);
			if (partDate.length() > 0) {
				int timeInt = Integer.parseInt(partDate);
				calendar.add(Calendar.MONTH, -timeInt);
				longDate = Long.valueOf(calendar.getTimeInMillis());
				return longDate.longValue();
			}

			// 3 years ago
			regex = "(\\d{1,2})\\s*?years?\\s*?ago";
			partDate = getStringByRegex(timeStr, regex, 1, 2);
			if (partDate.length() > 0) {
				int timeInt = Integer.parseInt(partDate);
				calendar.add(Calendar.YEAR, -timeInt);
				longDate = Long.valueOf(calendar.getTimeInMillis());
				return longDate.longValue();
			}

			// Feb/February 13 2011
			regex = "[a-zA-Z]{3,10}[\\s]*?\\d{1,2}[\\s]\\d{4}";
			String timeStrEnglish = timeStr.replaceAll(",", " ");
			partDate = getStringByRegex(timeStrEnglish, regex, 0, 2);
			if ((partDate != null) && (partDate.length() > 0)) {
				dateFormate = "MMM dd yyyy HH:mm";
				longDate = Long.valueOf(string2longEnglish(partDate + " 00:01", dateFormate));
				return longDate.longValue();
			}

			// 13 Feb/February 2011
			regex = "\\d{1,2}[\\s][a-zA-Z]{3,10}[\\s]\\d{4}";
			partDate = getStringByRegex(timeStr, regex, 0, 2);
			if ((partDate != null) && (partDate.length() > 0)) {
				dateFormate = "dd MMM yyyy HH:mm";
				longDate = Long.valueOf(string2longEnglish(partDate + " 00:01", dateFormate));
				return longDate.longValue();
			}

			// Apr 14 2015 8:46:50 AM
			regex = "[a-zA-Z]{3,10}[\\s]\\d{1,2}[\\s]\\d{4}[\\s][\\d]{1,2}:[\\d]{1,2}:[\\d]{1,2} [A|P]M";
			partDate = getStringByRegex(timeStr, regex, 0, 2);
			if ((partDate != null) && (partDate.length() > 0)) {
				dateFormate = "MMM dd yyyy HH:mm:ss aaa";
				longDate = Long.valueOf(string2longEnglish(partDate + " 00:01", dateFormate));
				return longDate.longValue();
			}
			// Apr 14, 2015 8:46:50 AM
			regex = "[a-zA-Z]{3,10}[\\s]\\d{1,2},[\\s]\\d{4}[\\s][\\d]{1,2}:[\\d]{1,2}:[\\d]{1,2} [A|P]M";
			partDate = getStringByRegex(timeStr, regex, 0, 2);
			if ((partDate != null) && (partDate.length() > 0)) {
				partDate = partDate.replace(",", "");
				dateFormate = "MMM dd yyyy HH:mm:ss aaa";
				longDate = Long.valueOf(string2longEnglish(partDate + " 00:01", dateFormate));
				return longDate.longValue();
			}

			// 2011 Feb/February 13
			regex = "\\d{4}[\\s][a-zA-Z]{3,10}[\\s]\\d{1,2}";
			partDate = getStringByRegex(timeStr, regex, 0, 2);
			if ((partDate != null) && (partDate.length() > 0)) {
				dateFormate = "yyyy MMM dd HH:mm";
				longDate = Long.valueOf(string2longEnglish(partDate + " 00:01", dateFormate));
				return longDate.longValue();
			}

			// 08/03/2014, 06:55:PM
			regex = "\\d{1,2}/\\d{1,2}/\\d{4}\\s?,\\s?\\d{1,2}:\\d{1,2}:[A|P]M";
			partDate = getStringByRegex(timeStr, regex, 0, 2);
			if ((partDate != null) && (partDate.length() > 0)) {
				dateFormate = "MM/dd/yyyy, hh:mm:aaa";
				longDate = Long.valueOf(string2longEnglish(partDate, dateFormate));
				return longDate.longValue();
			}

			// 946656060 -------> 2000年 以秒为单位
			regex = "\\d{9,10}";
			partDate = getStringByRegex(timeStr, regex, 0, 2);
			if ((partDate != null) && (partDate.length() > 0)) {
				longDate = Long.valueOf(partDate) * 1000;
				return longDate.longValue();
			}

			// 946656060000---->2000年 以毫秒为单位
			regex = "\\d{12,13}";
			partDate = getStringByRegex(timeStr, regex, 0, 2);
			if ((partDate != null) && (partDate.length() > 0)) {
				longDate = Long.valueOf(partDate);
				return longDate.longValue();
			}

			// 2014-01-01
			regex = "\\d{4}-\\d{1,2}-\\d{1,2}";
			partDate = getStringByRegex(timeStr, regex, 0, 2) + " 00:00:01";
			longDate = Long.valueOf(string2long(partDate, dateFormate));
			return longDate.longValue();

		} catch (Exception e) {
			longDate = Long.valueOf(System.currentTimeMillis());
			log.error(e.getMessage() + "-------" + timeStr);
		}
		return longDate.longValue();
	}

	public static String getStringByRegex(String sourceCode, String regex, int groupNum, int patternCase) {
		String ret = "";
		Pattern pTemp = Pattern.compile(regex, patternCase);
		Matcher mTemp = pTemp.matcher(sourceCode);
		if (mTemp.matches()) {
			ret = mTemp.group(groupNum);
		}

		return ret;
	}

	/**
	 * 默认语言环境下的时间转换
	 * 
	 * @param nowTime
	 * @param dataFormat
	 * @return
	 */
	public static long string2long(String nowTime, String dataFormat) {
		if (StringUtils.isEmpty(nowTime)) {
			return 0L;
		}
		long longTime = 0L;
		DateFormat f = new SimpleDateFormat(dataFormat, Locale.CHINA);
		Date d = null;
		try {
			d = f.parse(nowTime);
		} catch (ParseException e) {
			log.info(e.getMessage());
		}
		longTime = d.getTime();
		return longTime;
	}

	/**
	 * 进行英语的时间处理 (2011 Apr 13) 要在英语语言环境下处理
	 * 
	 * @param nowTime
	 * @param dataFormat
	 * @return
	 */
	public static long string2longEnglish(String nowTime, String dataFormat) {
		if (StringUtils.isEmpty(nowTime)) {
			return 0L;
		}
		long longTime = 0L;
		// 英语语言环境
		DateFormat f = new SimpleDateFormat(dataFormat, Locale.ENGLISH);
		Date d = null;
		try {
			d = f.parse(nowTime);
		} catch (ParseException e) {
			log.info(e.getMessage());
		}
		longTime = d.getTime();
		return longTime;
	}

	public static void main(String[] args) {
		long l = Datetil.convergeTime("Apr 14, 2015 8:46:50 AM");
		System.out.println(l + "------------------->" + System.currentTimeMillis());
		l = Datetil.convergeTime("Apr 14 2015 8:46:50 AM");
		System.out.println(l + "------------------->" + System.currentTimeMillis());
		l = Datetil.convergeTime("10秒前");
		System.out.println(l + "------------------->" + System.currentTimeMillis());
		l = Datetil.convergeTime("10分前");
		System.out.println(l + "------------------->" + System.currentTimeMillis());
		l = Datetil.convergeTime("10分钟前");
		System.out.println(l + "------------------->" + System.currentTimeMillis());
		l = Datetil.convergeTime("10小时前");
		System.out.println(l + "------------------->" + System.currentTimeMillis());
		l = Datetil.convergeTime("10天前");
		System.out.println(l + "------------------->" + System.currentTimeMillis());
		l = Datetil.convergeTime("10月前");
		System.out.println(l + "------------------->" + System.currentTimeMillis());
		l = Datetil.convergeTime("10年前");
		System.out.println(l + "------------------->" + System.currentTimeMillis());
		l = Datetil.convergeTime("今天 11:20");
		System.out.println(l + "------------------->" + System.currentTimeMillis());
		l = Datetil.convergeTime("昨天 11:20");
		System.out.println(l + "------------------->" + System.currentTimeMillis());
		l = Datetil.convergeTime("2019年12月13日 14:22");
		System.out.println(l + "------------------->" + System.currentTimeMillis());
		l = Datetil.convergeTime("12/13 14:22");
		System.out.println(l + "------------------->" + System.currentTimeMillis());
		l = Datetil.convergeTime("12-13 14:22");
		System.out.println(l + "------------------->" + System.currentTimeMillis());
		l = Datetil.convergeTime("12-13");
		System.out.println(l + "------------------->" + System.currentTimeMillis());
		l = Datetil.convergeTime("2019-12-13 14:22");
		System.out.println(l + "------------------->" + System.currentTimeMillis());
		l = Datetil.convergeTime("20150630 14:41");
		System.out.println(l + "------------------->" + System.currentTimeMillis());
		l = Datetil.convergeTime("2019-12-13 14:22:30");
		System.out.println(l + "------------------->" + System.currentTimeMillis());
		l = Datetil.convergeTime("2015-07-10 23:59:59.0");
		System.out.println(l + "------------------->" + System.currentTimeMillis());
		l = Datetil.convergeTime("2014/3/28 10:29");
		System.out.println(l + "------------------->" + System.currentTimeMillis());
		l = Datetil.convergeTime("2014/3/28 10:29:06");
		System.out.println(l + "------------------->" + System.currentTimeMillis());
		l = Datetil.convergeTime("2014/3/28");
		System.out.println(l + "------------------->" + System.currentTimeMillis());
		l = Datetil.convergeTime("2015.05.15");
		System.out.println(l + "------------------->" + System.currentTimeMillis());
		l = Datetil.convergeTime("12月13日 14:22");
		System.out.println(l + "------------------->" + System.currentTimeMillis());
		l = Datetil.convergeTime("2014年08月22日 22:15");
		System.out.println(l + "------------------->" + System.currentTimeMillis());
		l = Datetil.convergeTime("2014年08月22日 22:15:01");
		System.out.println(l + "------------------->" + System.currentTimeMillis());
		l = Datetil.convergeTime("2014年1月2日");
		System.out.println(l + "------------------->" + System.currentTimeMillis());
		l = Datetil.convergeTime("2014年1月");
		System.out.println(l + "------------------->" + System.currentTimeMillis());
		l = Datetil.convergeTime("2014年");
		System.out.println(l + "------------------->" + System.currentTimeMillis());
		l = Datetil.convergeTime("14年");
		System.out.println(l + "------------------->" + System.currentTimeMillis());
		l = Datetil.convergeTime("14");
		System.out.println(l + "------------------->" + System.currentTimeMillis());
		l = Datetil.convergeTime("2014");
		System.out.println(l + "------------------->" + System.currentTimeMillis());
		l = Datetil.convergeTime("01月 12");
		System.out.println(l + "------------------->" + System.currentTimeMillis());
		l = Datetil.convergeTime("14-07-01");
		System.out.println(l + "------------------->" + System.currentTimeMillis());
		l = Datetil.convergeTime("3 days ago");
		System.out.println(l + "------------------->" + System.currentTimeMillis());
		l = Datetil.convergeTime("3 months ago");
		System.out.println(l + "------------------->" + System.currentTimeMillis());
		l = Datetil.convergeTime("February 13 2011");
		System.out.println(l + "------------------->" + System.currentTimeMillis());
		l = Datetil.convergeTime("13 February 2011");
		System.out.println(l + "------------------->" + System.currentTimeMillis());
		l = Datetil.convergeTime("2011 February 13");
		System.out.println(l + "------------------->" + System.currentTimeMillis());
		l = Datetil.convergeTime("08/03/2014, 06:55:PM");
		System.out.println(l + "------------------->" + System.currentTimeMillis());
		l = Datetil.convergeTime("946656060");
		System.out.println(l + "------------------->" + System.currentTimeMillis());
		l = Datetil.convergeTime("946656060000");
		System.out.println(l + "------------------->" + System.currentTimeMillis());
		l = Datetil.convergeTime("2014-01-01");
		System.out.println(l + "------------------->" + System.currentTimeMillis());
		l = Datetil.convergeTime("2014-01-01");
		System.out.println(l + "------------------->" + System.currentTimeMillis());
		l = Datetil.convergeTime("2015.08.04 12:13:57");
		System.out.println(l + "------------------->" + System.currentTimeMillis());
	}
}
