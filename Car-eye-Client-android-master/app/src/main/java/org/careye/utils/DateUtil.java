package org.careye.utils;

import android.annotation.SuppressLint;
import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间帮助类
 * 
 * @author zx
 * 
 */
@SuppressLint("SimpleDateFormat")
public class DateUtil {

	private static final String TAG = "DateUtil.";

	@SuppressLint("SimpleDateFormat")
	public static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat dfC = new SimpleDateFormat("yyyy年MM月dd日");
	public static SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMddHHmm");
	static SimpleDateFormat df3 = new SimpleDateFormat("MM月dd日");
	static SimpleDateFormat df4 = new SimpleDateFormat("yyyy年MM月");
	static SimpleDateFormat df5 = new SimpleDateFormat("HH:mm");
	public static SimpleDateFormat df6 = new SimpleDateFormat("yyyyMMddHHmmss");
	static SimpleDateFormat df7 = new SimpleDateFormat("MM月dd日 HH:mm");
	public static SimpleDateFormat df8 = new SimpleDateFormat("yyMMddHHmmss");
	public static SimpleDateFormat df9 = new SimpleDateFormat("yyyy-MM");
	static SimpleDateFormat df10 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	/**
	 * 格式化日期
	 * @param format
	 * @return
	 */
	public static String dateFormatMMD(String format) {
		if (format ==null || format =="") {
			return "";
		}
		
		Date date = null;
		String str = null;

		try {
			date = new SimpleDateFormat("yyyy-M-dd").parse(format);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			str = sdf.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return str;
	}
	
	/**
	 * 格式化日期
	 * @param format
	 * @return
	 */
	public static String dateFormatyyMMddHHmmss(String format) {
		if (format ==null || format =="") {
			return "";
		}
		
		Date date = null;
		String str = null;

		try {
			date = new SimpleDateFormat("yyyy-M-dd HH:mm:ss").parse(format);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			str = sdf.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 格式化日期 日期
	 * @param format
	 * @return
	 */
	public static String dateFormatYearM(String format) {
		Date date = null;
		String str = null;

		try {
			date = new SimpleDateFormat("yyyy-MM-d").parse(format);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			str = sdf.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return str;
	}

	/**
	 * 格式化日期 时分秒
	 * @param format
	 * @return
	 */
	public static String dateFormatM(String format) {
		Date date = null;
		String str = null;

		try {
			date = new SimpleDateFormat("H:m").parse(format);
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			str = sdf.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return str;
	}

	/**
	 * 通过时间获取毫秒值
	 * @param timeStr	yyyy-MM-dd HH:mm:ss
	 * @return	millis	135465556545
	 */
	public static long getTimeMillis(String timeStr) {
		long millis = 0;

		try {
			Date date = df1.parse(timeStr);
			millis = date.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return millis;
	}

	public static String getTime(String time) {
		try {
			Date date = df8.parse(time);
			time = df1.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return time;
	}

	/**
	 * 获取按要求的当天日期时间
	 * 
	 */
	public static String getTodayDate(SimpleDateFormat dateFormat) {
		return dateFormat.format(new Date());
	}

	/**
	 * 获取当天日期
	 * 
	 * yyyy-MM-dd
	 */
	public static String getTodayDate() {
		return df.format(new Date());
	}

	/**
	 * 获取当前时间
	 * 
	 * yyyy-MM-dd HH:mm:ss
	 */
	public static String getNowDate() {
		return df1.format(new Date());
	}

	/**
	 * 获取当前时间，计价器所需刷卡时间
	 * 
	 * yyyyMMddHHmmss
	 */
	public static String getNowDateToMeter() {
		return df6.format(new Date());
	}

	/**
	 * 获取前第七天
	 */
	public static String getLastSevenDay() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -15);

		return df.format(cal.getTime());
	}

	/**
	 * 获取当前日期的上一天
	 */
	public static String getMinusDate(String date) {
		try {
			Date tempDate = df.parse(date);

			Calendar cal = Calendar.getInstance();
			cal.setTime(tempDate);
			cal.add(Calendar.DATE, -1);

			return df.format(cal.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	/**
	 * 获取当前日期的上一月
	 */
	public static String getMonthDate(String date) {
		try {
			Date tempDate = df9.parse(date);

			Calendar cal = Calendar.getInstance();
			cal.setTime(tempDate);
			cal.add(Calendar.MONTH, -1);

			return df9.format(cal.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 获取当前日期的上n天
	 */
	public static String getMinusDateN(String date,int n) {
		try {
			Date tempDate = df.parse(date);

			Calendar cal = Calendar.getInstance();
			cal.setTime(tempDate);
			cal.add(Calendar.DATE, -n);

			return df.format(cal.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 获取当前日期的下一天
	 */
	public static String getPlusDate(String date) {
		try {
			Date tempDate = df.parse(date);

			Calendar cal = Calendar.getInstance();
			cal.setTime(tempDate);
			cal.add(Calendar.DATE, +1);

			return df1.format(cal.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 获取当前日期的下n天
	 */
	public static String getPlusDateN(String date ,int n) {
		try {
			Date tempDate = df.parse(date);

			Calendar cal = Calendar.getInstance();
			cal.setTime(tempDate);
			cal.add(Calendar.DATE, +n);

			return df.format(cal.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 格式化日期
	 * @param format
	 * @return
	 */
	public static String dateFormatCarLife(String format) {
		Date date = null;
		String str = null;

		try {
			date = new SimpleDateFormat("H:m:s").parse(format);
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			str = sdf.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return str;
	}

	public static String dateFromTimeStamp(String time) {
		String str = null;

		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(Long.parseLong(time));

			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			str = sf.format(calendar.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return str;
	}

	/**
	 * 格式化日期
	 * @param format
	 * @return
	 */
	public static String dateFormat(String format) {
		Date date = null;
		String str = null;

		try {
			date = new SimpleDateFormat("yyyy-MM-d H:m:s").parse(format);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			str = sdf.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return str;
	}

	/**
	 * 格式化日期yyyy-MM-dd ---- yyyy年MM月dd日
	 * @param format
	 * @return
	 */
	public static String dateFormatC(String format) {
		Date date = null;
		String str = null;

		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(format);
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			str = dfC.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return str;
	}

	/**
	 * 格式化日期
	 * @param format
	 * @return
	 */
	public static String dateFormatClearyear(String format) {
		Date date = null;
		String str = null;

		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(format);
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
			str = sdf.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return str;
	}

	/**
	 * 格式化日期
	 * @param format
	 * @return
	 */
	public static String dateFormatMD(String format) {
		if (format ==null || format =="") {
			return "";
		}
		
		Date date = null;
		String str = null;

		try {
			date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(format);
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			str = sdf.format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return str;
	}

	/**
	 * 日期比较
	 * @param DATE1
	 * @param DATE2
	 * @return DATE1 > DATE2 返回1 
	 */
	public static int compare_date(String DATE1, String DATE2) {
		try {
			Date dt1 = df1.parse(DATE1);
			Date dt2 = df1.parse(DATE2);

			if (dt1.getTime() > dt2.getTime()) {
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	/**
	 * 判断date是否处于date1和date2之间
	 * @param date1
	 * @param date2
	 * @param date
	 * @return
	 */
	public static Boolean isBetweenDate1AndDate2(String date1,String date2,Date date){
		try {
			Date dt1 = df1.parse(date1);
			Date dt2 = df1.parse(date2);

			//当date1小于date2的时候
			if (dt1.getTime()<dt2.getTime()) {
				if (date.getTime()>dt1.getTime()&&date.getTime()<dt2.getTime()) {
					return true;
				} else {
					return false;
				}
				//当date1大于date2的时候
			} else if(dt1.getTime()>dt2.getTime()) {
				if (date.getTime()>dt2.getTime()&&date.getTime()<dt1.getTime()) {
					return true;
				} else {
					return false;
				}
			} else {
				//当date1等于date2的时候
				if (date.getTime() == dt1.getTime()) {
					return true;
				} else {
					return false;
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 截取时间
	 * @param  0.5秒
	 * @return	500  即:(0.5*1000)
	 */
	public static long getLongTime(String timeStr){
		long time = 0;
		timeStr = timeStr.substring(0, timeStr.indexOf("秒"));
		time = (long) (1000*Double.parseDouble(timeStr));
		return time;
	}

	public static String dateSwitchDateString(final Date date) {
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String format = "";

		if (date != null) {
			format = simpleDateFormat.format(date);
		}

		return format;
	}

	public static String dateSwitchTimeString(final Date date) {
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
		String format = "";

		if (date != null) {
			format = simpleDateFormat.format(date);
		}

		return format;
	}

	public static String dateSwitchDateAndTimeString(final Date date) {
		String format = "";

		if (date != null) {
			format = df1.format(date);
		}

		return format;
	}
}
