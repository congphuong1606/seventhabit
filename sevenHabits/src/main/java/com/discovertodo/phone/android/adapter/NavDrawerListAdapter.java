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
import com.discovertodo.phone.android.model.NavDrawerItem;

public class NavDrawerListAdapter extends BaseAdapter {
	
	private BaseActivity activity;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private LayoutInflater layoutInflater;
	private int[] images = {R.drawable.ic_detail_item,R.drawable.ic_detail_item,R.drawable.ic_detail_item,R.drawable.ic_detail_item,R.drawable.ic_detail_item,
			R.drawable.ic_detail_item, 0,R.drawable.ic_detail_item,R.drawable.ic_detail_item,R.drawable.ic_detail_item,R.drawable.ic_detail_item};

	public NavDrawerListAdapter(BaseActivity activity, ArrayList<NavDrawerItem> navDrawerItems){
		this.activity = activity;
		this.navDrawerItems = navDrawerItems;
		layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		if (!activity.checkSessionID(null)){
			return navDrawerItems.size() -4;
		}else{
			return navDrawerItems.size();
		}

	}

	@Override
	public Object getItem(int position) {		
		return navDrawerItems.get(position);
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
			view = layoutInflater.inflate(R.layout.slidermenu_item_list, null);
			viewHolder = new ViewHolder();
			viewHolder.layout = view.findViewById(R.id.drawer_item_layout);
			viewHolder.textView = (TextView) view.findViewById(R.id.drawer_item_title);
			viewHolder.imageView = (ImageView)view.findViewById(R.id.drawer_item_icon);
			view.setTag(viewHolder);
		} else {
	        viewHolder = (ViewHolder) view.getTag();
	    }
		if (!activity.checkSessionID(null)){
			switch (position){
				case 0: position= 0;
					break;
				case 1: position = 4;
					break;
				case 2: position = 6;
					viewHolder.layout.setVisibility(View.GONE);
					break;
				case 3: position = 7;
					break;
				case 4: position = 8;
					break;
				case 5: position = 9;
			}
		}else{
			if (position==6)viewHolder.layout.setVisibility(View.GONE);

			if(position==2)viewHolder.layout.setVisibility(View.VISIBLE);
		}
		viewHolder.imageView.setImageResource(images[position]);
		viewHolder.textView.setText(navDrawerItems.get(position).getTitle());
        return view;
	}

	class ViewHolder{
		View layout;
		TextView textView;
		ImageView imageView;
	}
	
}
