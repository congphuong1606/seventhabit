package com.discovertodo.phone.android.util;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.format.Time;

import com.discovertodo.phone.android.global.Constant;
import com.discovertodo.phone.android.model.HoneWeekHistoryItem;
import com.discovertodo.phone.android.model.HoneWeekItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

public class HoneWeekUtil {
	
	public static void saveItemWeekCurrent(Context context, ArrayList<HoneWeekItem> listItem, Time timeStart, Time timeEnd){
		Gson gson = new GsonBuilder().create();
		JsonArray myCustomArray = gson.toJsonTree(listItem).getAsJsonArray();
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				Constant.SHARED_PREFERENCE_HONEWEEK_CURRENT,
				Context.MODE_PRIVATE);

		timeStart.hour = 0;
		timeStart.minute = 0;
		timeStart.second = 0;
		timeEnd.hour = 23;
		timeEnd.minute = 59;
		timeEnd.second = 59;
		Editor editor = sharedPreferences.edit();
		editor.putString(Constant.SHARED_PREFERENCE_HONEWEEK_CONTENT, myCustomArray.toString());
		editor.putLong(Constant.SHARED_PREFERENCE_HONEWEEK_TIME_START, timeStart.toMillis(true));
		editor.putLong(Constant.SHARED_PREFERENCE_HONEWEEK_TIME_END, timeEnd.toMillis(true));
		editor.commit();
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<HoneWeekHistoryItem> getHistory(Context context, boolean isNow){
		SharedPreferences sharedPreferencesHistory = context.getSharedPreferences(
				Constant.SHARED_PREFERENCE_HONEWEEK_HISTORY,
				Context.MODE_PRIVATE);
		String strJsonHistory = sharedPreferencesHistory.getString(Constant.SHARED_PREFERENCE_HONEWEEK_CONTENT, "[]");
		ArrayList<HoneWeekHistoryItem> listHistory;
		try {
			Gson gson = new Gson();
			Type listType = new TypeToken<ArrayList<HoneWeekHistoryItem>>(){}.getType();
			listHistory = (ArrayList<HoneWeekHistoryItem>) gson.fromJson(strJsonHistory, listType);
		} catch (Exception e) {
			listHistory = new ArrayList<HoneWeekHistoryItem>();
		}
		Collections.sort(listHistory, new Comparator<HoneWeekHistoryItem>() {
	        @Override
	        public int compare(HoneWeekHistoryItem item1, HoneWeekHistoryItem item2) {
	        	if(item1.getTimeStart() > item2.getTimeStart()){
	        		return -1;
	        	}else if(item1.getTimeStart() == item2.getTimeStart()){
	        		return 0;
	        	}else{
	        		return 1;
	        	}
	        }
	    });
		for (int i = 0; i < listHistory.size(); i++) {
			HoneWeekHistoryItem item = listHistory.get(i);
			long tempTime;
			if(isNow){
				tempTime = 0;  // full
			}else{
				tempTime = item.getTimeEnd();
			}
			if(System.currentTimeMillis() > tempTime){
				continue;
			}else{
				listHistory.remove(item);
				--i;
			}
		}
		return listHistory;
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<HoneWeekItem> getCurrent(Context context){
		ArrayList<HoneWeekItem> listItem;
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				Constant.SHARED_PREFERENCE_HONEWEEK_CURRENT,
				Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		String strJson = sharedPreferences.getString(Constant.SHARED_PREFERENCE_HONEWEEK_CONTENT, "[]");
		long tempTimeStart = sharedPreferences.getLong(Constant.SHARED_PREFERENCE_HONEWEEK_TIME_START, 0);
		long tempTimeEnd = sharedPreferences.getLong(Constant.SHARED_PREFERENCE_HONEWEEK_TIME_END, 0);
		Time timeNow = new Time();
		timeNow.setToNow();
		if(tempTimeStart <= timeNow.toMillis(true) && timeNow.toMillis(true) <= tempTimeEnd){
			try {
				Gson gson = new Gson();
				Type listType = new TypeToken<ArrayList<HoneWeekItem>>(){}.getType();
				listItem = (ArrayList<HoneWeekItem>) gson.fromJson(strJson, listType);
			} catch (Exception e) {
				e.printStackTrace();
				listItem = new ArrayList<HoneWeekItem>();
			}
		}else{
			if(strJson.length() > 10){
				SharedPreferences sharedPreferencesHistory = context.getSharedPreferences(
						Constant.SHARED_PREFERENCE_HONEWEEK_HISTORY,
						Context.MODE_PRIVATE);
				Editor editorHistory = sharedPreferencesHistory.edit();
				String strJsonHistory = sharedPreferencesHistory.getString(Constant.SHARED_PREFERENCE_HONEWEEK_CONTENT, "[]");
				HoneWeekHistoryItem itemHistory = new HoneWeekHistoryItem();
				itemHistory.setContent(strJson);
				itemHistory.setTimeStart(tempTimeStart);
				itemHistory.setTimeEnd(tempTimeEnd);
				ArrayList<HoneWeekHistoryItem> listHistory;
				try {
					Gson gson = new Gson();
					Type listType = new TypeToken<ArrayList<HoneWeekHistoryItem>>(){}.getType();
					listHistory = (ArrayList<HoneWeekHistoryItem>) gson.fromJson(strJsonHistory, listType);
				} catch (Exception e) {
					listHistory = new ArrayList<HoneWeekHistoryItem>();
				}
				listHistory.add(itemHistory);
				Gson gson = new GsonBuilder().create();
				JsonArray myCustomArray = gson.toJsonTree(listHistory).getAsJsonArray();
				editorHistory.putString(Constant.SHARED_PREFERENCE_HONEWEEK_CONTENT, myCustomArray.toString());
				editorHistory.commit();
			}
			listItem = new ArrayList<HoneWeekItem>();
		}
		if(listItem.size() < 6){
			listItem.clear();
			editor.clear();
			editor.commit();

			HoneWeekItem item1 = new HoneWeekItem();
			item1.setTitle("信頼：能力");
			HoneWeekItem item2 = new HoneWeekItem();
			item2.setTitle("信頼：人格");
			HoneWeekItem item3 = new HoneWeekItem();
			item3.setTitle("自分を磨く：身体");
			HoneWeekItem item4 = new HoneWeekItem();
			item4.setTitle("自分を磨く：知性");
			HoneWeekItem item5 = new HoneWeekItem();
			item5.setTitle("自分を磨く：精神");
			HoneWeekItem item6 = new HoneWeekItem();
			item6.setTitle("自分を磨く：人間関係");
			listItem.add(item1);
			listItem.add(item2);
			listItem.add(item3);
			listItem.add(item4);
			listItem.add(item5);
			listItem.add(item6);
		}
		int total = 0;
		for (int i = 0; i < listItem.size(); i++) {
			HoneWeekItem itemWeek = listItem.get(i);
			total += itemWeek.getListItem().size();
		}
		if(total == 0){
			ArrayList<HoneWeekHistoryItem> listHistory = getHistory(context, true);
			for (int i = 0; i < listHistory.size(); i++) {
				HoneWeekHistoryItem item = listHistory.get(i);
				long currentTime = System.currentTimeMillis();
				if(currentTime >= item.getTimeStart() && currentTime <= item.getTimeEnd()){
					listHistory.remove(item);
					SharedPreferences sharedPreferencesHistory = context.getSharedPreferences(
							Constant.SHARED_PREFERENCE_HONEWEEK_HISTORY,
							Context.MODE_PRIVATE);
					Editor editorHistory = sharedPreferencesHistory.edit();
					Gson gson = new GsonBuilder().create();
					JsonArray myCustomArray = gson.toJsonTree(listHistory).getAsJsonArray();
					editorHistory.putString(Constant.SHARED_PREFERENCE_HONEWEEK_CONTENT, myCustomArray.toString());
					editorHistory.commit();
					strJson = item.getContent();
					try {
						gson = new Gson();
						Type listType = new TypeToken<ArrayList<HoneWeekItem>>(){}.getType();
						listItem = (ArrayList<HoneWeekItem>) gson.fromJson(strJson, listType);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return listItem;
	}
}
