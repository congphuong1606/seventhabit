package com.discovertodo.phone.android.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.discovertodo.phone.android.R;
import com.discovertodo.phone.android.adapter.HoneWeekHistoryAdapter;
import com.discovertodo.phone.android.model.HoneWeekHistoryItem;
import com.discovertodo.phone.android.util.HoneWeekUtil;

public class HoneWeekHistoryActivity extends BaseActivity implements OnItemClickListener{
	
	private ListView listView;
	private HoneWeekHistoryAdapter adapter;
	private ArrayList<HoneWeekHistoryItem> listHistory;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.honeweek_history_layout);
		listView = (ListView) findViewById(R.id.honeweek_history_all_listview);
		listHistory = HoneWeekUtil.getHistory(this, false);
		adapter = new HoneWeekHistoryAdapter(this, listHistory);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		Intent intent = new Intent(this, HoneWeekHistoryDetailActivity.class);
		intent.putExtra("history", listHistory.get(position));
		startActivity(intent);
	}
	
	@Override
	public void onBackPressed() {
		clickBack(null);
	}
	
	public void clickBack(View view){
		finish();
		overridePendingTransition(R.anim.activity_stood, R.anim.activity_fade_out);
	}
	
}
