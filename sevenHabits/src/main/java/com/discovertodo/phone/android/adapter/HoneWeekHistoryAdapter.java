package com.discovertodo.phone.android.adapter;

import java.lang.reflect.Type;
import java.util.ArrayList;

import android.content.Context;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.discovertodo.phone.android.R;
import com.discovertodo.phone.android.activity.BaseActivity;
import com.discovertodo.phone.android.model.HoneWeekChildItem;
import com.discovertodo.phone.android.model.HoneWeekHistoryItem;
import com.discovertodo.phone.android.model.HoneWeekItem;

public class HoneWeekHistoryAdapter extends BaseAdapter {
	
	private BaseActivity activity;
	private ArrayList<HoneWeekHistoryItem> listItem;
	private LayoutInflater layoutInflater;
	
	public HoneWeekHistoryAdapter(BaseActivity activity, ArrayList<HoneWeekHistoryItem> listItem){
		this.activity = activity;
		this.listItem = listItem;
		layoutInflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return listItem.size();
	}

	@Override
	public Object getItem(int position) {		
		return listItem.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder viewHolder = null;
		if (convertView == null) {
			view = layoutInflater.inflate(R.layout.honeweek_history_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.txtTitle = (TextView) view.findViewById(R.id.honeweek_history_list_item_text);
			viewHolder.txtDesc = (TextView) view.findViewById(R.id.honeweek_history_list_item_desc);
			view.setTag(viewHolder);
		} else {
	        viewHolder = (ViewHolder) view.getTag();
	    }
		
		HoneWeekHistoryItem item = listItem.get(position);
		Time timeStart = new Time();
		Time timeEnd = new Time();
		timeStart.set(item.getTimeStart());
		timeEnd.set(item.getTimeEnd());
		String title = String.format("%04d", timeStart.year) + "年" + String.format("%02d", timeStart.month+1)
				 + "月" + String.format("%02d", timeStart.monthDay) + "日 - " +
				 String.format("%04d", timeEnd.year) + "年" + String.format("%02d", timeEnd.month+1)
				 + "月" + String.format("%02d", timeEnd.monthDay) + "日";
		viewHolder.txtTitle.setText(title);
		
		ArrayList<HoneWeekItem> listItemWeek;
		String strJson = item.getContent();
		try {
			Gson gson = new Gson();
			Type listType = new TypeToken<ArrayList<HoneWeekItem>>(){}.getType();
			listItemWeek = (ArrayList<HoneWeekItem>) gson.fromJson(strJson, listType);
		} catch (Exception e) {
			listItemWeek = new ArrayList<HoneWeekItem>();
		}
		int count = 0, total = 0;
		for (int i = 0; i < listItemWeek.size(); i++) {
			HoneWeekItem itemWeek = listItemWeek.get(i);
			total += itemWeek.getListItem().size();
			for (int j = 0; j < itemWeek.getListItem().size(); j++) {
				HoneWeekChildItem itemChild = itemWeek.getListItem().get(j);
				if(itemChild.isCheck()){
					++count;
				}
			}
		}
		viewHolder.txtDesc.setText(count + "/" + total + "  達成");
        
        return view;
	}

	class ViewHolder{
		TextView txtTitle, txtDesc;
	}
	
}
