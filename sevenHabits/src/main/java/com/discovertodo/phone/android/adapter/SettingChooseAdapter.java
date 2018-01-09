package com.discovertodo.phone.android.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.discovertodo.phone.android.R;
import com.discovertodo.phone.android.activity.BaseActivity;

public class SettingChooseAdapter extends BaseAdapter {
	
	private BaseActivity activity;
	private ArrayList<String> listItem;
	private int positionCheck;
	private LayoutInflater layoutInflater;
	
	public SettingChooseAdapter(BaseActivity activity, ArrayList<String> listItem, int positionCheck){
		this.activity = activity;
		this.listItem = listItem;
		this.positionCheck = positionCheck;
		layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void setPositionCheck(int positionCheck){
		this.positionCheck = positionCheck;
	}
	
	public int getPositionCheck(){
		return positionCheck;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder viewHolder = null;
		if (convertView == null) {
			view = layoutInflater.inflate(R.layout.setting_choose_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.textView = (TextView) view.findViewById(R.id.setting_choose_list_item_text);
			viewHolder.imageView = (ImageView) view.findViewById(R.id.setting_choose_list_item_check);
			view.setTag(viewHolder);
		} else {
	        viewHolder = (ViewHolder) view.getTag();
	    }
		
		viewHolder.textView.setText(listItem.get(position));
		if(position == positionCheck)
			viewHolder.imageView.setVisibility(View.VISIBLE);
		else
			viewHolder.imageView.setVisibility(View.GONE);
        
        return view;
	}

	class ViewHolder{
		TextView textView;
		ImageView imageView;
	}
	
}
