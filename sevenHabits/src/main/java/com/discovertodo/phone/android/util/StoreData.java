package com.discovertodo.phone.android.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

import android.content.Context;
import android.content.SharedPreferences;

public class StoreData {

	SharedPreferences prefs = null;
	SharedPreferences.Editor editor = null;
	Context context;

	public StoreData(Context context) {
		this.context = context;
		prefs = this.context.getSharedPreferences("sevenhabits.data",
				Context.MODE_PRIVATE);
	}

	public String getStringValue(String key) {
		return prefs.getString(key, "");
	}

	public void setStringValue(String key, String value) {
		if (editor == null) {
			editor = prefs.edit();
		}
		editor.putString(key, value);
		editor.commit();
	}

	public int getIntValue(String key) {
		return prefs.getInt(key, 0);
	}

	public void setIntValue(String key, int value) {
		if (editor == null) {
			editor = prefs.edit();
		}
		editor.putInt(key, value);
		editor.commit();
	}

	public void removeKey(String key) {
		if (editor == null) {
			editor = prefs.edit();
		}
		editor.remove(key);
		editor.commit();
	}

	public void setBooleanValue(String key, boolean value) {
		if (editor == null) {
			editor = prefs.edit();
		}
		editor.putBoolean(key, value);
		editor.commit();
	}

	public boolean getBooleanValue(String key) {
		return prefs.getBoolean(key, false);
	}
	public static BigDecimal bigRound(float number, int digit){
		try {
			BigDecimal bd = BigDecimal.valueOf(number);
			bd = bd.setScale(digit, RoundingMode.HALF_UP);
			return bd;
		} catch (Exception e) {
			return null;
		}

	}
	public boolean isExists(String key){
		return prefs.contains(key);
	}
	public void clearValue(String key){
		if (prefs.contains(key)){
			if (editor == null) {
				editor = prefs.edit();
			}
			editor.remove(key);
			editor.commit();
		}
	}
}
