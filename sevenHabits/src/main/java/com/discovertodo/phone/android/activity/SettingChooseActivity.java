package com.discovertodo.phone.android.activity;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.Time;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.discovertodo.phone.android.R;
import com.discovertodo.phone.android.adapter.SettingChooseAdapter;
import com.discovertodo.phone.android.global.Constant;

public class SettingChooseActivity extends BaseActivity {

	private SharedPreferences sharedPreferences;
	private Editor editor;
	private TextView txtTitle;
	private NumberPicker pickerHour, pickerMinute;
	private ListView listView;
	private EditText editText;
	private SettingChooseAdapter adapter;
	private ArrayList<String> listItem;
	private int type, positionCheck;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_choose_layout);
		sharedPreferences = activity.getSharedPreferences(Constant.SHARED_PREFERENCE_SETTING, Context.MODE_PRIVATE);
		editor = sharedPreferences.edit(); 
		
		txtTitle = (TextView) findViewById(R.id.setting_choose_text_title);
		type = getIntent().getIntExtra("type", 0);
		if(type == 0){
			finish();
			return;
		}
		switch (type) {
		case Constant.SETTING_TYPE_FREQUENCY_NOTIFY:
			txtTitle.setText(getString(R.string.setting_freq_notify_title));
			setupList();
			break;
		case Constant.SETTING_TYPE_TIME_NOTIFY:
			txtTitle.setText(getString(R.string.setting_time_notify_title));
			setupPicker();
			break;
		case Constant.SETTING_TYPE_HONEWEEK:
			txtTitle.setText(getString(R.string.setting_honeweek_day_title));
			setupList();
			break;
		case Constant.SETTING_TYPE_SESSION:
			txtTitle.setText(getString(R.string.setting_session_title));
			setupEditText();
			break;
		}
	}
	
	private void setupPicker(){
		int hourNotify, minuteNotify;
		hourNotify = getIntent().getIntExtra(Constant.SETTING_NOTIFY_HOUR, 0);
		minuteNotify = getIntent().getIntExtra(Constant.SETTING_NOTIFY_MINUTE, 0);
		pickerHour = (NumberPicker) findViewById(R.id.setting_choose_hour);
		pickerMinute = (NumberPicker) findViewById(R.id.setting_choose_minute);
		((View) pickerHour.getParent()).setVisibility(View.VISIBLE);
		pickerHour.setMaxValue(23);
		pickerHour.setMinValue(0);
		pickerMinute.setMaxValue(59);
		pickerMinute.setMinValue(0);
		pickerHour.setValue(hourNotify);
		pickerMinute.setValue(minuteNotify);
		pickerHour.setFormatter(new NumberPicker.Formatter() {
		    @Override
		    public String format(int value) {
		    	if(value < 10){
		    		return "0" + value;
		    	}
		        return "" + value;
		    }
		});
		pickerMinute.setFormatter(new NumberPicker.Formatter() {
		    @Override
		    public String format(int value) {
		    	if(value < 10){
		    		return "0" + value;
		    	}
		        return "" + value;
		    }
		});
		pickerMinute.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		pickerHour.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
	}
	
	private void setupList(){
		listView = (ListView) findViewById(R.id.setting_choose_listview);
		listView.setVisibility(View.VISIBLE);
		listItem = getIntent().getStringArrayListExtra("list");
		positionCheck = getIntent().getIntExtra("positionCheck", 0);
		adapter = new SettingChooseAdapter(activity, listItem, positionCheck);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				positionCheck = position;
				adapter.setPositionCheck(positionCheck);
				adapter.notifyDataSetChanged();
			}
		});
	}
	
	private void setupEditText(){
		View btnCheckSession = findViewById(R.id.setting_choose_btn_check_session);
		View btnShowHelp = findViewById(R.id.setting_choose_session_btn_help);
		editText = (EditText) findViewById(R.id.setting_choose_session_text);
		((View)editText.getParent().getParent()).setVisibility(View.VISIBLE);
		if(checkSessionID(null)){
			editText.setText(sharedPreferences.getString(Constant.SETTING_SESSION_USER,""));
		}
		btnCheckSession.setVisibility(View.VISIBLE);
		btnCheckSession.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				String str = editText.getText().toString();
				boolean result = checkSessionID(str);
				if(!result){
					showPopup((View)editText.getParent().getParent(), getString(R.string.setting_session_error_input), true);
				}else{
					SharedPreferences sharedPreferences = getSharedPreferences(Constant.SHARED_PREFERENCE_SETTING, Context.MODE_PRIVATE);
					Editor editor = sharedPreferences.edit();
					editor.putString(Constant.SETTING_SESSION_USER, str);
					editor.commit();
					clickBack(null);
				}
			}
		});
		btnShowHelp.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showPopup((View)editText.getParent().getParent(), getString(R.string.setting_session_help_input), false);
			}
		});
	}
	
	private void showPopup(View anchorView, String text, boolean isCenter){
		Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setContentView(R.layout.popup_settings_session);
		dialog.getWindow().setLayout(anchorView.getWidth(), LayoutParams.WRAP_CONTENT);
		dialog.setCanceledOnTouchOutside(true);
		
		View viewCenter = dialog.findViewById(R.id.popup_setting_choose_session_mtpopup_center);
		View viewRight = dialog.findViewById(R.id.popup_setting_choose_session_mtpopup_right);
		if(isCenter){
			viewCenter.setVisibility(View.VISIBLE);
			viewRight.setVisibility(View.GONE);
		}else{
			viewCenter.setVisibility(View.GONE);
			viewRight.setVisibility(View.VISIBLE);
		}
		TextView textView = (TextView) dialog.findViewById(R.id.popup_setting_choose_session_notice_text);
	    textView.setText(text);

		WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
		wmlp.gravity = Gravity.TOP | Gravity.LEFT;
		wmlp.x = anchorView.getLeft();
		wmlp.y = anchorView.getTop() + anchorView.getHeight();

		dialog.show();
	}
	
	@Override
	public void onBackPressed() {
		clickBack(null);
	}
	
	public void clickBack(View view){
		switch (type) {
		case Constant.SETTING_TYPE_FREQUENCY_NOTIFY:
			saveFreqNotify();
			break;
		case Constant.SETTING_TYPE_TIME_NOTIFY:
			saveTimeNotify();
			break;
		case Constant.SETTING_TYPE_HONEWEEK:
			saveHoneWeek();
			break;
		case Constant.SETTING_TYPE_SESSION:
			
			break;
		}
		sendBroadcast(new Intent(Constant.RECEIVER_ALARM_START));
		finish();
	}
	
	private void saveFreqNotify(){
		if(adapter.getPositionCheck() == 0){
			editor.putBoolean(Constant.SETTING_NOTIFY_STATUS, false);
		}else if(adapter.getPositionCheck() == 1){
			// SETTING_NOTIFY_WEEK_DAY = 99 có nghĩa là ngày nào cũng bắn
			editor.putBoolean(Constant.SETTING_NOTIFY_STATUS, true);
			editor.putInt(Constant.SETTING_NOTIFY_WEEK_DAY, 99);
		}else if(adapter.getPositionCheck() == 2){
			Time time = new Time();
			time.setToNow();
			editor.putBoolean(Constant.SETTING_NOTIFY_STATUS, true);
			editor.putInt(Constant.SETTING_NOTIFY_WEEK_DAY, time.weekDay);
		}
		editor.putInt(Constant.SETTING_NOTIFY_FREQUENCY, adapter.getPositionCheck());
		editor.commit();
	}
	
	private void saveTimeNotify(){
		int hourNotify = pickerHour.getValue();
		int minuteNotify = pickerMinute.getValue();
		editor.putInt(Constant.SETTING_NOTIFY_HOUR, hourNotify);
		editor.putInt(Constant.SETTING_NOTIFY_MINUTE, minuteNotify);
		editor.commit();
	}
	
	private void saveHoneWeek(){
		editor.putInt(Constant.SETTING_HONEWEEK_POSITIONDAY, positionCheck);
		editor.commit();
	}

}
