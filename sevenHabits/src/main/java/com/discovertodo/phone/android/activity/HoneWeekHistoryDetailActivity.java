package com.discovertodo.phone.android.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.applidium.headerlistview.HeaderListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.discovertodo.phone.android.R;
import com.discovertodo.phone.android.adapter.HoneWeekHistoryDetailAdapter;
import com.discovertodo.phone.android.model.HoneWeekHistoryItem;
import com.discovertodo.phone.android.model.HoneWeekItem;

public class HoneWeekHistoryDetailActivity extends BaseActivity {
	
	private RelativeLayout layoutParent;
	private HeaderListView listView;
	private TextView txtTitle;
	private HoneWeekHistoryDetailAdapter adapter;
	private HoneWeekHistoryItem itemHistory;
	private ArrayList<HoneWeekItem> listItem;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.honeweek_history_detail_layout);
		
		itemHistory = (HoneWeekHistoryItem) getIntent().getSerializableExtra("history");
		if(itemHistory == null){
			finish();
			return;
		}
		txtTitle = (TextView) findViewById(R.id.honeweek_history_detail_time_text);
		layoutParent = (RelativeLayout) findViewById(R.id.honeweek_history_detail_layout);
		listView = new HeaderListView(activity);
		layoutParent.addView(listView);
		setupValue();
	}

	public void clickBack(View view){
		finish();
	}
	
	@SuppressWarnings("unchecked")
	private void setupValue(){
		Time timeStart = new Time();
		Time timeEnd = new Time();
		timeStart.set(itemHistory.getTimeStart());
		timeEnd.set(itemHistory.getTimeEnd());
		String title = String.format("%04d", timeStart.year) + "年" + String.format("%02d", timeStart.month+1)
				 + "月" + String.format("%02d", timeStart.monthDay) + "日 - " +
				 String.format("%04d", timeEnd.year) + "年" + String.format("%02d", timeEnd.month+1)
				 + "月" + String.format("%02d", timeEnd.monthDay) + "日";
		txtTitle.setText(title);
		try {
			Gson gson = new Gson();
			Type listType = new TypeToken<ArrayList<HoneWeekItem>>(){}.getType();
			listItem = (ArrayList<HoneWeekItem>) gson.fromJson(itemHistory.getContent(), listType);
		} catch (Exception e) {
			listItem = new ArrayList<HoneWeekItem>();
		}
		adapter = new HoneWeekHistoryDetailAdapter(this, listItem);
		listView.setAdapter(adapter);
	}
	
}
