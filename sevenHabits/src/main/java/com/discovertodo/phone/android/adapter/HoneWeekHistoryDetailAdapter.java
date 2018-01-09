package com.discovertodo.phone.android.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.applidium.headerlistview.SectionAdapter;
import com.discovertodo.phone.android.R;
import com.discovertodo.phone.android.activity.BaseActivity;
import com.discovertodo.phone.android.model.HoneWeekChildItem;
import com.discovertodo.phone.android.model.HoneWeekItem;

public class HoneWeekHistoryDetailAdapter extends SectionAdapter {
	
	private BaseActivity activity;
	private ArrayList<HoneWeekItem> listItem;
	private LayoutInflater layoutInflater;
	
	public HoneWeekHistoryDetailAdapter (BaseActivity activity, ArrayList<HoneWeekItem> listItem){
		this.activity = activity;
		this.listItem = listItem;
		layoutInflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int numberOfSections() {
		return listItem.size();
	}

	@Override
	public int numberOfRows(int section) {
		if(section >=0)
			return listItem.get(section).getListItem().size();
		else
			return 0;
	}

	@Override
	public HoneWeekChildItem getRowItem(int section, int row) {
		return listItem.get(section).getListItem().get(row);
	}

    @Override
    public boolean hasSectionHeaderView(int section) {
        return true;
    }

	@Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolderHeader viewHolder = null;
		if (convertView == null) {
			view = layoutInflater.inflate(R.layout.honeweek_item_header, null);
			viewHolder = new ViewHolderHeader();
			viewHolder.textView = (TextView) view.findViewById(R.id.honeweek_item_header_text);
			view.setTag(viewHolder);
		} else {
	        viewHolder = (ViewHolderHeader) view.getTag();
	    }
		viewHolder.textView.setText(listItem.get(section).getTitle());
        
        return view;
    }

	class ViewHolderHeader{
		TextView textView;
	}

	@Override
	public View getRowView(final int section, int row, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolderRow viewHolder = null;
		if (convertView == null) {
			view = layoutInflater.inflate(R.layout.honeweek_item_row_content, null);
			viewHolder = new ViewHolderRow();
			viewHolder.layoutContent = view.findViewById(R.id.honeweek_item_row_content_layout);
			viewHolder.btnCheck = view.findViewById(R.id.honeweek_item_row_content_btn_check);
			viewHolder.btnCalendar = view.findViewById(R.id.honeweek_item_row_content_btn_calendar);
			viewHolder.txtTitle = (TextView) view.findViewById(R.id.honeweek_item_row_content_text_title);
			view.setTag(viewHolder);
		} else {
	        viewHolder = (ViewHolderRow) view.getTag();
	    }
		ArrayList<HoneWeekChildItem> listRow = listItem.get(section).getListItem();
		HoneWeekChildItem honeWeekChildItem = listRow.get(row);
		viewHolder.txtTitle.setText(honeWeekChildItem.getTitle());
		viewHolder.btnCheck.setSelected(honeWeekChildItem.isCheck());
        
        return view;
	}

	class ViewHolderRow{
		View btnAdd, btnCheck, btnCalendar, btnMove, btnDelete, layoutContent;
		TextView txtTitle, txtDelete;
	}

}
