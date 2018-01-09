package com.discovertodo.phone.android.fragment;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.discovertodo.phone.android.R;
import com.discovertodo.phone.android.activity.SettingChooseActivity;
import com.discovertodo.phone.android.global.Constant;


public class SettingsFragment extends BaseFragment {
	
	private View btnNotifyFrequency, btnNotifyTime, btnHoneWeek, btnSession;
	private TextView txtTimeNotify, txtFrequencyNotify, txtHoneWeek, txtSession;
	private long timeFlagClick;
	private int hourNotify, minuteNotify, positionFrequency, positionDayHoneWeek;
	private ArrayList<String> arrStrFrequency, arrStrHoneWeek;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(view == null){
			view = inflater.inflate(R.layout.settings_layout, container, false);
			btnNotifyFrequency = view.findViewById(R.id.setting_btn_notification_frequency);
			btnNotifyTime = view.findViewById(R.id.setting_btn_notification_time);
			btnHoneWeek = view.findViewById(R.id.setting_btn_honeweek);
			btnSession = view.findViewById(R.id.setting_btn_session);
			txtFrequencyNotify = (TextView) view.findViewById(R.id.setting_btn_notification_frequency_text);
			txtTimeNotify = (TextView) view.findViewById(R.id.setting_notification_time_text);
			txtHoneWeek = (TextView) view.findViewById(R.id.setting_honeweek_day_text);
			txtSession = (TextView) view.findViewById(R.id.setting_session_text);
			setupLayout();
			String[] arrTemp = new String[]{getString(R.string.setting_notification_freq_off), 
											getString(R.string.setting_notification_freq_daily), 
											getString(R.string.setting_notification_freq_weekly)};
			arrStrFrequency = new ArrayList<String>(Arrays.asList(arrTemp));
			arrTemp = new String[]{getString(R.string.setting_honeweek_day_8), getString(R.string.setting_honeweek_day_2), getString(R.string.setting_honeweek_day_3),
											getString(R.string.setting_honeweek_day_4), getString(R.string.setting_honeweek_day_5),
											getString(R.string.setting_honeweek_day_6), getString(R.string.setting_honeweek_day_7) };
			arrStrHoneWeek = new ArrayList<String>(Arrays.asList(arrTemp));
		}
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		SharedPreferences sharedPreferences = activity.getSharedPreferences(Constant.SHARED_PREFERENCE_SETTING, Context.MODE_PRIVATE);
		hourNotify = sharedPreferences.getInt(Constant.SETTING_NOTIFY_HOUR, 0);
		minuteNotify = sharedPreferences.getInt(Constant.SETTING_NOTIFY_MINUTE, 0);
		positionFrequency = sharedPreferences.getInt(Constant.SETTING_NOTIFY_FREQUENCY, 1);
		positionDayHoneWeek = sharedPreferences.getInt(Constant.SETTING_HONEWEEK_POSITIONDAY, 0);
		setupValue();
	}
	
	private void setupLayout(){
		btnNotifyFrequency.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if((System.currentTimeMillis() - timeFlagClick) < 800){
					return;
				}
				timeFlagClick = System.currentTimeMillis();
				Intent intent = new Intent(activity, SettingChooseActivity.class);
				intent.putExtra("type", Constant.SETTING_TYPE_FREQUENCY_NOTIFY);
				intent.putExtra("list", arrStrFrequency);
				intent.putExtra("positionCheck", positionFrequency);
				startActivity(intent);
			}
		});
		btnNotifyTime.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if((System.currentTimeMillis() - timeFlagClick) < 800){
					return;
				}
				timeFlagClick = System.currentTimeMillis();
				Intent intent = new Intent(activity, SettingChooseActivity.class);
				intent.putExtra("type", Constant.SETTING_TYPE_TIME_NOTIFY);
				intent.putExtra(Constant.SETTING_NOTIFY_HOUR, hourNotify);
				intent.putExtra(Constant.SETTING_NOTIFY_MINUTE, minuteNotify);
				startActivity(intent);
			}
		});
		btnHoneWeek.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if((System.currentTimeMillis() - timeFlagClick) < 800){
					return;
				}
				timeFlagClick = System.currentTimeMillis();
				Intent intent = new Intent(activity, SettingChooseActivity.class);
				intent.putExtra("type", Constant.SETTING_TYPE_HONEWEEK);
				intent.putExtra("list", arrStrHoneWeek);
				intent.putExtra("positionCheck", positionDayHoneWeek);
				startActivity(intent);
			}
		});
		btnSession.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if((System.currentTimeMillis() - timeFlagClick) < 800){
					return;
				}
				timeFlagClick = System.currentTimeMillis();
				Intent intent = new Intent(activity, SettingChooseActivity.class);
				intent.putExtra("type", Constant.SETTING_TYPE_SESSION);
				startActivity(intent);
			}
		});
	}
	
	private void setupValue(){
		txtTimeNotify.setText(String.format("%02d:%02d", hourNotify, minuteNotify));
		txtFrequencyNotify.setText(arrStrFrequency.get(positionFrequency));
		txtHoneWeek.setText(arrStrHoneWeek.get(positionDayHoneWeek));
		if(activity.checkSessionID(null)){
			SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constant.SHARED_PREFERENCE_SETTING, Context.MODE_PRIVATE);
			txtSession.setText(sharedPreferences.getString(Constant.SETTING_SESSION_USER,""));
			txtSession.setTextColor(getResources().getColor(R.color.black));
		}else{
			txtSession.setText(getString(R.string.setting_session_title));
			txtSession.setTextColor(getResources().getColor(R.color.gray));
		}
		activity.notifyDataSetChangedSlide();
	}
}
