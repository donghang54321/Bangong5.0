package com.rentian.newoa.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


/**
 * Date Utility Class This is used to convert Strings to Dates and Timestamps
 * 
 * <p>
 * <a href="DateUtil.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a> Modified by
 *         <a href="mailto:dan@getrolling.com">Dan Kibler </a> to correct time
 *         pattern. Minutes should be mm not MM (MM is month).
 * @version $Revision: 1.1 $ $Date: 2011/02/23 04:10:34 $
 */
public final class DateUtil {
  // ~ Static fields/initializers =============================================
  /**
   * Log of the class
   */

  /**
   * 缺省的日期匹配模式
   */
  private static String defaultDatePattern = null;

  /**
   * 时间匹配模式
   */
  private static String timePattern = "HH:mm:ss";

  // ~ Methods ================================================================

  /**
   * Return default datePattern (MM/dd/yyyy)
   * 
   * @return a string representing the date pattern on the UI
   */
  public static synchronized String getDatePattern() {
    defaultDatePattern = "yyyy-MM-dd";
    return defaultDatePattern;
  }

  /**
   * This method attempts to convert an Oracle-formatted date in the form
   * dd-MMM-yyyy to mm/dd/yyyy.
   * 
   * @param aDate date from database as a string
   * @return formatted string for the ui
   */
  public static final String getDate(Date aDate) {
    SimpleDateFormat df = null;
    String returnValue = "";

    if (aDate != null) {
      df = new SimpleDateFormat(getDatePattern());
      returnValue = df.format(aDate);
    }

    return (returnValue);
  }

  /**
   * This method generates a string representation of a date/time in the format
   * you specify on input
   * 
   * @param aMask the date pattern the string is in
   * @param strDate a string representation of a date
   * @return a converted Date object
   * @see SimpleDateFormat
   * @throws ParseException
   */
  public static final Date convertStringToDate(String aMask, String strDate)
      throws ParseException {
    if (aMask == null || aMask.length() == 0) {
      aMask = getDatePattern();
    }
    if (strDate == null || strDate.length() == 0) {
      return new Date();
    }
    SimpleDateFormat df = null;
    Date date = null;
    df = new SimpleDateFormat(aMask);


    try {
      date = df.parse(strDate);
    } catch (ParseException pe) {
      // log.error("ParseException: " + pe);
      throw new ParseException(pe.getMessage(), pe.getErrorOffset());
    }

    return (date);
  }

  /**
   * This method returns the current date time in the format: MM/dd/yyyy HH:MM a
   * 
   * @param theTime the current time
   * @return the current date/time
   */
  public static String getTimeNow(Date theTime) {
    return getDateTime(timePattern, theTime);
  }

  /**
   * This method returns the current date in the format: MM/dd/yyyy
   * 
   * @return the current date
   * @throws ParseException
   */
  public static Calendar getToday() throws ParseException {
    Date today = new Date();
    SimpleDateFormat df = new SimpleDateFormat(getDatePattern());

    // This seems like quite a hack (date -> string -> date),
    // but it works ;-)
    String todayAsString = df.format(today);
    Calendar cal = new GregorianCalendar();
    cal.setTime(convertStringToDate(todayAsString));

    return cal;
  }

  /**
   * This method generates a string representation of a date's date/time in the
   * format you specify on input
   * 
   * @param aMask the date pattern the string is in
   * @param aDate a date object
   * @return a formatted string representation of the date
   * 
   * @see SimpleDateFormat
   */
  public static final String getDateTime(String aMask, Date aDate) {
    SimpleDateFormat df = null;
    String returnValue = null;

    if (aDate != null) {
      df = new SimpleDateFormat(aMask);
      returnValue = df.format(aDate);
    }

    return (returnValue);
  }

  /**
   * This method generates a string representation of a date based on the System
   * Property 'dateFormat' in the format you specify on input
   * 
   * @param aDate A date to convert
   * @return a string representation of the date
   */
  public static final String convertDateToString(Date aDate) {
    return getDateTime(getDatePattern(), aDate);
  }

  /**
   * This method generates a string representation of a date based on the System
   * Property 'dateFormat' in the format you specify on input
   * @param aDate A date to convert
   * @param defaultString defaultString.
   * @return
   */
  public static final String convertDateToString(Date aDate,
      String defaultString) {
    String s = getDateTime(getDatePattern(), aDate);

    return s;
  }

  /**
   * This method converts a String to a date using the datePattern
   * 
   * @param strDate the date to convert (in format MM/dd/yyyy)
   * @return a date object
   * 
   * @throws ParseException
   */
  public static Date convertStringToDate(String strDate) throws ParseException {
    Date aDate = null;


      aDate = convertStringToDate(getDatePattern(), strDate);

    return aDate;
  }

  /** 一天最后一个小时 */
  public static final int LAST_HOUR_OF_DATE = 23;

  /** 一小时最后一分钟 */
  public static final int LAST_MINUTE_OF_HOUR = 59;

  /** 一分钟最后一秒 */
  public static final int LAST_SECOND_OF_MIN = 59;

  /**
   * 到某一天的最后一秒
   * @param date 给定的时间
   * @return 定位到当天的最后一秒
   */
  public static Date lastSecondOfDate(Date date) {
    if (date == null) {
      date = new Date();
    }

    Calendar c = Calendar.getInstance();
    c.setTime(date);

    c.set(Calendar.HOUR_OF_DAY, LAST_HOUR_OF_DATE);
    c.set(Calendar.MINUTE, LAST_MINUTE_OF_HOUR);
    c.set(Calendar.SECOND, LAST_SECOND_OF_MIN);

    return c.getTime();
  }

  /**
   * 到某一天的第一秒
   * @param date 给定的时间
   * @return 定位到当天的第一秒
   */
  public static Date firstSecondOfDate(Date date) {
    if (date == null) {
      date = new Date();
    }

    Calendar c = Calendar.getInstance();
    c.setTime(date);
    c.set(Calendar.HOUR_OF_DAY, 0);
    c.set(Calendar.MINUTE, 0);
    c.set(Calendar.SECOND, 0);

    return c.getTime();
  }

  /**
   * @see {@link Calendar#add(int, int)}
   */
  public static Date add(Date date, int field, int amount) {
    if (amount == 0) {
      return date;
    }

    Calendar c = Calendar.getInstance();
    c.setTime(date);
    c.add(field, amount);

    return c.getTime();
  }

  /**
   * 得到当前的日期，时间从00:00:00开始
   * @return yyyy-MM-dd hh:mm:ss
   */
  public static Date getCurrentDate() {
    String pattern = "yyyy-MM-dd hh:mm:ss";
    Date date = Calendar.getInstance().getTime();
    
    SimpleDateFormat format = 
          new SimpleDateFormat(pattern, Locale.getDefault());
    format.format(date);

    return date;
  }

  /**
   * 返回自今日00:00:00以后若干天日期（如果返回自今日以前的若干天日期，请用"-days"）
   * @param date
   * @param days
   * @return
   */
  public static Date getDate(Date date, int days) {
    if (date == null) {
      return null;
    }
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(Calendar.DATE, days);
    calendar.set(Calendar.HOUR, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);

    return calendar.getTime();
  }

  /**
   * @param date
   * @param months
   * @return
   */
  public static Date getMonth(Date date, int months) {
    if (date == null) {
      return null;
    }
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(Calendar.MONTH, months);
    calendar.set(Calendar.HOUR, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);

    return calendar.getTime();
  }
  public static String formatTimeInMillis(long timeInMillis) {
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(timeInMillis);
    Date date = cal.getTime();
    SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");
    String fmt = dateFormat.format(date);

    return fmt;
  }

  public static String formatTimeInMillis(long timeInMillis, String farmat) {
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(timeInMillis);
    Date date = cal.getTime();
    SimpleDateFormat dateFormat = new SimpleDateFormat(farmat);
    String fmt = dateFormat.format(date);

    return fmt;
  }
  /**
   * prevent from initializing
   * 
   */

  private DateUtil() {
  }
}
