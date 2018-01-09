package com.discovertodo.phone.android.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.discovertodo.phone.android.global.Constant;

import java.util.Arrays;

public class BaseActivity extends Activity{
	
	protected BaseActivity activity;
	private final String[] session = {"discover15","discover16","discover17","discover18"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		sendBroadcast(new Intent(Constant.RECEIVER_ALARM_START));
	}
	
	public boolean checkSessionID(String sessionUser){

		if(sessionUser == null){
			SharedPreferences sharedPreferences = getSharedPreferences(Constant.SHARED_PREFERENCE_SETTING, Context.MODE_PRIVATE);
			sessionUser = sharedPreferences.getString(Constant.SETTING_SESSION_USER, "");
		}

		if (Arrays.asList(session).contains(sessionUser))
			return   true;
		else
			return   false;
	}
	
	public String[] getSession(){
		return session;
	}
	
}
