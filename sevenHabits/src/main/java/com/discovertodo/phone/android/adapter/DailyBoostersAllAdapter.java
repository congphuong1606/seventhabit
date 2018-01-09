package com.discovertodo.phone.android.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import com.discovertodo.phone.android.R;
import com.discovertodo.phone.android.activity.BaseActivity;
import com.discovertodo.phone.android.model.DailyBoostersAllItem;
import com.discovertodo.phone.android.util.FavoriteUtil;

public class DailyBoostersAllAdapter extends BaseAdapter {
	
	private BaseActivity activity;
	private ArrayList<DailyBoostersAllItem> listItem;
	private ArrayList<DailyBoostersAllItem> listTemp;
	private LayoutInflater layoutInflater;
	private FavoriteUtil favoriteUtil;
	private ValueFilter valueFilter;
	
	public DailyBoostersAllAdapter(BaseActivity activity, ArrayList<DailyBoostersAllItem> listItem){
		this.activity = activity;
		this.listItem = listItem;
		this.listTemp = listItem;
		getFilter();
		this.favoriteUtil = new FavoriteUtil();
		layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
			view = layoutInflater.inflate(R.layout.dailyboosters_all_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.txtTime = (TextView) view.findViewById(R.id.dailyboosters_all_list_item_time_text);
			viewHolder.txtDesc = (TextView) view.findViewById(R.id.dailyboosters_all_list_item_desc_text);
			view.setTag(viewHolder);
		} else {
	        viewHolder = (ViewHolder) view.getTag();
	    }
		
		DailyBoostersAllItem item = listItem.get(position);
		viewHolder.txtTime.setText(item.getTime());
		viewHolder.txtDesc.setText(item.getDesc());
        
        return view;
	}

	class ViewHolder{
		TextView txtTime, txtDesc;
	}

	public void add(DailyBoostersAllItem item){
		listItem.add(item);
		notifyDataSetChanged();

	}
	public void remove(DailyBoostersAllItem item){
		listItem.remove(item);
		notifyDataSetChanged();
	}
	public Filter getFilter(){
		if(valueFilter == null){
			valueFilter = new ValueFilter();
		}
		return valueFilter;
	}

	class ValueFilter extends Filter{

		@Override
		protected FilterResults performFiltering(CharSequence inputText) {
			String constraint = inputText.toString().toLowerCase();
			FilterResults results = new FilterResults();
			if(constraint.length() > 0){
				ArrayList<DailyBoostersAllItem> filterList = new ArrayList<DailyBoostersAllItem>();
				for (int i=0;i<listTemp.size();i++){
					if (listTemp.get(i).getDesc().toLowerCase().contains(constraint)){
						filterList.add(listTemp.get(i));
					}
				}
				results.count = filterList.size();
				results.values = filterList;
			}else{
				results.count = listTemp.size();
				results.values = listTemp;
			}
			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			listItem = (ArrayList<DailyBoostersAllItem>) results.values;
			notifyDataSetChanged();
		}
	}

}
