package com.discovertodo.phone.android.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.Time;

import com.discovertodo.phone.android.global.Constant;

public class DailyBoostersUtil {

	private static final String fileName = "dailybooster.txt";
	private static Date dateInstall;
	private static JSONArray arrBootster;
	
	public static int getDaysBetweenNow(Context context){
		if(dateInstall == null){
			dateInstall = getDateInstall(context);
		}
		Calendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(System.currentTimeMillis());
		int days = calculateDays(dateInstall, calendar.getTime());
		if(days < 0){
			SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.SHARED_PREFERENCE_DATE_INSTALL, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.remove("year").remove("month").remove("day");
			editor.commit();
			dateInstall = getDateInstall(context);
			calendar = new GregorianCalendar();
			calendar.setTimeInMillis(System.currentTimeMillis());
			days = calculateDays(dateInstall, calendar.getTime());
		}
		return days;
	}
	
	public static int getDaysBetweenCustom(Context context, int year, int month0to11, int day){
		if(dateInstall == null){
			dateInstall = getDateInstall(context);
		}
		Calendar calendar = new GregorianCalendar();
		calendar.set(year, month0to11, day);
		int days = calculateDays(dateInstall, calendar.getTime());
		if(days < 0){
			SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.SHARED_PREFERENCE_DATE_INSTALL, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.remove("year").remove("month").remove("day");
			editor.commit();
			dateInstall = getDateInstall(context);
			calendar = new GregorianCalendar();
			calendar.set(year, month0to11, day);
			days = calculateDays(dateInstall, calendar.getTime());
		}
		return days;
	}
	
	public static Date getDateInstall(Context context){
		Time now = new Time();
		now.setToNow();
		SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.SHARED_PREFERENCE_DATE_INSTALL, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		int year = sharedPreferences.getInt("year", now.year);
		int month = sharedPreferences.getInt("month", now.month);
		int day = sharedPreferences.getInt("day", now.monthDay);
		editor.putString("format", "0-11");
		editor.putInt("year", year);
		editor.putInt("month", month);
		editor.putInt("day", day);
		editor.commit();
		Calendar calendar = new GregorianCalendar();
		calendar.set(year, month, day, 0, 0, 0);
		dateInstall = calendar.getTime();
		return dateInstall;
	}
	
	private static int calculateDays(Date date1, Date date2) {
		return (int) ((date2.getTime() - date1.getTime()) / 86400000);
	}

	@SuppressWarnings("deprecation")
	private static String loadText(Context context) {
		String text = "";
		InputStream fIn = null;
		InputStreamReader isr = null;
		BufferedReader input = null;
		try {
			fIn = context.getResources().getAssets().open(fileName, Context.MODE_WORLD_READABLE);
			isr = new InputStreamReader(fIn);
			input = new BufferedReader(isr);
			String line = "";
			while (((line = input.readLine()).trim()) != null) {
				text += line;
			}
		} catch (Exception e) {
			e.getMessage();
		} finally {
			try {
				if (isr != null)
					isr.close();
				if (fIn != null)
					fIn.close();
				if (input != null)
					input.close();
			} catch (Exception e2) {
				e2.getMessage();
			}
		}
		return text;
	}
	
	public static JSONArray loadArray(Context context){
		try {
			arrBootster = new JSONArray(loadText(context));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return arrBootster;
	}
	
}
