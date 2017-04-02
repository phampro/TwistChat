package com.hoangsong.zumechat.untils;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@SuppressLint("SimpleDateFormat")
public class MyDateTimeISO {


	public static String dateFormatGetBooking = "yyyy-MM-dd'T'HH:mm:ss";

	/**
	 * time format dd/mm/yyyy
	 * 
	 * @param d
	 * @return
	 */
	public static String getDateFormat(Date d) {
		SimpleDateFormat dft = new SimpleDateFormat("dd/MM/yyyy",
				Locale.getDefault());
		return dft.format(d);
	}


	public static String getCurrentStrDate(String outFormat)
	{
		Calendar cal=Calendar.getInstance(Locale.US);
		SimpleDateFormat dft=new SimpleDateFormat(outFormat, Locale.US);
		return dft.format(cal.getTime());
	}
	
	public static int getYear(String strDate)
	{
		String s="";
		if(strDate.length()==10)
		{
			s = strDate.substring(6, 10);
		}
		if(strDate.length()==8)
		{
			s = strDate.substring(4, 8);
		}
		if(strDate.length()==4)
		{
			s = strDate.substring(0, 4);
		}
		if(strDate.length()==9)
		{
			s = strDate.substring(5, 9);
		}
		return (Integer.parseInt(s)+1900);
	}
	public static int getMonth(String strDate)
	{
		int kq=-1;
		String s="";
		if(strDate.length()==10)
		{
			s = strDate.substring(3, 5);
			kq=Integer.parseInt(s);
		}
		if(strDate.length()==8)
		{
			s = strDate.substring(2, 3);
			kq=Integer.parseInt(s);
		}
		if(strDate.length()==9)
		{
			try
			{
				s = strDate.substring(2, 4);
				kq=Integer.parseInt(s);
			}
			catch (Exception e)
			{
				s = strDate.substring(3, 4);
				kq=Integer.parseInt(s);
			}
		}
		return kq-1;
	}
	public static int getDay(String strDate)
	{
		int kq=-1;
		String s="";
		if(strDate.length()==10)
		{
			s = strDate.substring(0, 2);
			kq=Integer.parseInt(s);
		}
		if(strDate.length()==8)
		{
			s = strDate.substring(0, 1);
			kq=Integer.parseInt(s);
		}
		if(strDate.length()==9)
		{
			try
			{
				s = strDate.substring(0, 2);
				kq=Integer.parseInt(s);
			}
			catch (Exception e)
			{
				s = strDate.substring(0, 1);
				kq=Integer.parseInt(s);
			}
		}
		return kq;
	}
	// count the number of days between 2 milestones
	/**
	 * count the number of days between 2 milestones
	 * returns the number of days
	 * */
	public static long daysBetween2Dates(Date date1, Date date2) {
        // Time Format
     //   SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.setTime(date1);
        c2.setTime(date2);
        
        // The formula for calculating the number of days between two timelines:
        long noDay = (c2.getTime().getTime() - c1.getTime().getTime())
                / (24 * 3600 * 1000);
        return noDay;
    }

	/**
	 * add date: public input on needs and wants additional dates, the result returned is on form
	 * dd/mm/yyyy
	 * */
	public static String plushDay(String strDay, int numDay)
	{
		Calendar c1 = Calendar.getInstance();
		//SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		ISO8601DateFormat format = new ISO8601DateFormat();
        Date d1 = null;
        try {
            d1 = format.parse(strDay);
        } catch (ParseException e) {
        }
        c1.setTime(d1);
        c1.add(Calendar.DATE, numDay);
        return format.format(c1.getTime());
		
	}
	/**
	 * calculate the distance between 2 days time format (yyyy-MM-dd HH:mm:ss) day hour minute second
	 * option: h= hour, m = minute, s= second
	 * */
	public static long between2StringDate(String strDateStart, String srrDateEnd, String option)
	{
		 //SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		ISO8601DateFormat format = new ISO8601DateFormat();
	        Date d1 = null;
	        Date d2 = null;
	        try {
	            d1 = format.parse(strDateStart);
	            d2 = format.parse(srrDateEnd);
	        } catch (ParseException e) {
	        }
	        // Get msec from each, and subtract.
	        long diff = d2.getTime() - d1.getTime();
	        long diffSeconds = diff / 1000;
	        long diffMinutes = diff / (60 * 1000);
	        long diffHours = diff / (60 * 60 * 1000);
	        if(option.equals("h"))
	        	return diffHours;
	        else if(option.equals("m"))
	        	return diffMinutes;
	        else
	        	return diffSeconds;
	}
	/**
	 * Get the current date of the system:
	 * return date dd/mm/yyyy hh:mm
	 * */
	public static String getCurrentDateLong()
	{
		Calendar cal=Calendar.getInstance();
		SimpleDateFormat dft=null;
		dft=new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
		String strDateHT=dft.format(cal.getTime());
		dft=new SimpleDateFormat("HH:mm",Locale.getDefault());
		String strGioHt = dft.format(cal.getTime());
		return strDateHT+" "+strGioHt+":00";
	}


	public static String currentDateFormat()
	{
		//SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss", Locale.US);
		ISO8601DateFormat dateFormat = new ISO8601DateFormat();
		String  currentTimeStamp = dateFormat.format(new Date());
		return currentTimeStamp;
	}

	/**
	 * Get the current date of the system:
	 * return date dd/mm/yyyy
	 * */
	public static String getCurrentDateShort()
	{
		Calendar cal=Calendar.getInstance();
		SimpleDateFormat dft=null;
		dft=new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
		String strDateHT=dft.format(cal.getTime());
//		dft=new SimpleDateFormat("HH:mm",Locale.getDefault());
//		String strGioHt = dft.format(cal.getTime());
		return strDateHT;
	}
	/**
	 * Get hour from chain dd/mm/yyyy
	 * return hour type int
	 * */
	public static int getHour(String time)
	{
		int gio=-1;
		String s="";
		if(time.length()==5)
		{
			s=time.substring(0,2);
			gio=Integer.parseInt(s);
		}
		if(time.length()==3)
		{
			s=time.substring(0,1);
			gio=Integer.parseInt(s);
		}
		if(time.length()==4)
		{
			try
			{
				s=time.substring(0,2);
				gio=Integer.parseInt(s);
			}
			catch(Exception e)
			{
				s=time.substring(0,1);
				gio=Integer.parseInt(s);
			}
			
		}
		if(0<=gio && gio<=23)
			return gio;
		else
			return -1;
		
	}
	/**
	 * Get minutes from chain dd/mm/yyyy
	 * return minute type int
	 * */
	public static int getMinute(String time)
	{
		int phut=-1;
		String s="";
		if(time.length()==5)
		{
			s=time.substring(3,5);
			phut=Integer.parseInt(s);
		}
		if(time.length()==3)
		{
				s=time.substring(2,3);
				phut=Integer.parseInt(s);
			
		}
		if(time.length()==4)
		{
			try
			{
				s=time.substring(2,4);
				phut=Integer.parseInt(s);
			}
			catch(Exception e)
			{
				s=time.substring(3,4);
				phut=Integer.parseInt(s);
			}
			
		}
		if(0<=phut && phut<=59)
			return phut;
		else
			return -1;
		
	}
	

	/**
	 * get date with time format hh:mm a
	 * 
	 * @param d
	 * @return
	 */
	public String getHourFormat(Date d) {
		SimpleDateFormat dft = new SimpleDateFormat("hh:mm a",
				Locale.getDefault());
		return dft.format(d);
	}
	
	public static String getFormatDateRegStr(String text) {
        Calendar c = Calendar.getInstance();
        String dateFormat = null;
		//SimpleDateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
		//DATEFORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
        ISO8601DateFormat df = new ISO8601DateFormat();
        try {
            //Date parsed = DATEFORMAT.parse(text);
        	Date parsed = df.parse(text);
            c.setTime(parsed);
            dateFormat  = new SimpleDateFormat("EEEE, MMMM dd, yyyy").format(c.getTime());
        } catch (ParseException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return dateFormat;
    }
	
	public static String getFormatHourRegStr(String text) {
        Calendar c = Calendar.getInstance();
        String dateFormat = null;
        //SimpleDateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
		//DATEFORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
        ISO8601DateFormat df = new ISO8601DateFormat();
        try {
            //Date parsed = DATEFORMAT.parse(text);
        	Date parsed = df.parse(text);
            c.setTime(parsed);
            dateFormat  = new SimpleDateFormat("HH:mm").format(c.getTime());
        } catch (ParseException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return dateFormat;
    }
	
	public static String getFormatDateISOsystem() {
        Calendar c = Calendar.getInstance();
        String dateFormat = null;
		dateFormat  = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(c.getTime());
        return dateFormat;
    }
	
	public static Date getFormatDateReg(String text) {
        Calendar c = Calendar.getInstance();
		//SimpleDateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
		//DATEFORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
        ISO8601DateFormat df = new ISO8601DateFormat();
        try {
            //Date parsed = DATEFORMAT.parse(text);
        	Date parsed = df.parse(text);
            c.setTime(parsed);
        } catch (ParseException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return c.getTime();
    }
	
	public static int getDateOfYear(Date date){
		Calendar ca1 = Calendar.getInstance();
		ca1.setTime(date);
		return ca1.get(Calendar.DAY_OF_YEAR);
	}

	/**
	 * calculate the distance between 2 days
	 * option: h= hour, m = minute, s= second
	 * */
	public static long between2StringDate(Date dateStart, String srrDateEnd, String option)
	{
		//SimpleDateFormat format = new SimpleDateFormat(inPutFormat, Locale.US);
		ISO8601DateFormat format = new ISO8601DateFormat();
		Date d1 = dateStart;
		Date d2 = null;
		try {
			d2 = format.parse(srrDateEnd);

			// Get msec from each, and subtract.
			long diff = d2.getTime() - d1.getTime();
			long diffSeconds = diff / 1000;
			long diffMinutes = diff / (60 * 1000);
			long diffHours = diff / (60 * 60 * 1000);
			if(option.equals("h"))
				return diffHours;
			else if(option.equals("m"))
				return diffMinutes;
			else
				return diffSeconds;
		} catch (ParseException e) {
			e.printStackTrace();
			return -1;
		}
	}


	public static long daysBetween2Dates(String strDateStart, String strDateEnd) {
		// Time Format
		//SimpleDateFormat dateFormat = new SimpleDateFormat(inputFormat, Locale.US);
		ISO8601DateFormat dateFormat = new ISO8601DateFormat();
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();

		Date date1 = new Date();
		Date date2 = new Date();
		try {
			date1 = dateFormat.parse(strDateStart);
			date2 = dateFormat.parse(strDateEnd);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c1.setTime(date1);
		c2.setTime(date2);

		// The formula for calculating the number of days between two timelines:
		long noDay = (c2.getTime().getTime() - c1.getTime().getTime())
				/ (24 * 3600 * 1000);
		return noDay;
	}

}
