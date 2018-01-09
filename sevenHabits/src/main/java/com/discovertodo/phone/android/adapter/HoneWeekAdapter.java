package com.discovertodo.phone.android.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Handler;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.applidium.headerlistview.SectionAdapter;
import com.discovertodo.phone.android.R;
import com.discovertodo.phone.android.activity.HoneWeekAddActivity;
import com.discovertodo.phone.android.fragment.HoneWeekFragment;
import com.discovertodo.phone.android.model.HoneWeekChildItem;
import com.discovertodo.phone.android.model.HoneWeekItem;

public class HoneWeekAdapter extends SectionAdapter {
	
	private HoneWeekFragment fragment;
	private ArrayList<HoneWeekItem> listItem;
	private LayoutInflater layoutInflater;
	private int widthScreen;
	private long flagTimeCalendar;
	
	public HoneWeekAdapter (HoneWeekFragment fragment, ArrayList<HoneWeekItem> listItem){
		this.fragment = fragment;
		this.listItem = listItem;
		layoutInflater = (LayoutInflater) fragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Display display = fragment.getActivity().getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		widthScreen = size.x;
	}

	@Override
	public int numberOfSections() {
		return listItem.size();
	}

	@Override
	public int numberOfRows(int section) {
		if(section >=0)
			return (listItem.get(section).getListItem().size() + 1);
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
		final ArrayList<HoneWeekChildItem> listRow = listItem.get(section).getListItem();
		if (convertView == null) {
			view = layoutInflater.inflate(R.layout.honeweek_item_row_content, null);
			viewHolder = new ViewHolderRow();
			viewHolder.btnAdd = view.findViewById(R.id.honeweek_item_row_add_layout);
			viewHolder.layoutContent = view.findViewById(R.id.honeweek_item_row_content_layout);
			viewHolder.btnDelete = view.findViewById(R.id.honeweek_item_row_content_img_delete);
			viewHolder.txtDelete = (TextView) view.findViewById(R.id.honeweek_item_row_content_text_delete);
			viewHolder.btnMove = view.findViewById(R.id.honeweek_item_row_content_img_move);
			viewHolder.btnCheck = view.findViewById(R.id.honeweek_item_row_content_btn_check);
			viewHolder.btnCalendar = view.findViewById(R.id.honeweek_item_row_content_btn_calendar);
			viewHolder.txtTitle = (TextView) view.findViewById(R.id.honeweek_item_row_content_text_title);
			view.setTag(viewHolder);
		} else {
	        viewHolder = (ViewHolderRow) view.getTag();
	    }
		if(row < listRow.size()){
			final View txtDelete = viewHolder.txtDelete;
			final HoneWeekChildItem honeWeekChildItem = listRow.get(row);
			final Animation animRightIn = AnimationUtils.loadAnimation(fragment.getActivity(),
	                R.anim.view_right_in);  
			Animation animLeftIn = AnimationUtils.loadAnimation(fragment.getActivity(),
	                R.anim.view_left_in);  
			final Animation animRightOut = AnimationUtils.loadAnimation(fragment.getActivity(),
	                R.anim.view_right_out);  
			Animation animLeftOut = AnimationUtils.loadAnimation(fragment.getActivity(),
	                R.anim.view_left_out);  
			final Handler handler = new Handler();
			viewHolder.btnAdd.setVisibility(View.GONE);
			viewHolder.txtTitle.setText(honeWeekChildItem.getTitle());
			viewHolder.layoutContent.getLayoutParams().width = widthScreen;
			viewHolder.txtTitle.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if(txtDelete.getVisibility() == View.VISIBLE){
						txtDelete.startAnimation(animRightOut);
					}
					handler.postDelayed(new Runnable() {
						public void run() {
							txtDelete.setVisibility(View.GONE);
						}
					}, 120);
				}
			});
			final ViewHolderRow tempViewHolder = viewHolder;
			if(fragment.isEditList()){
				if(viewHolder.btnDelete.getVisibility() == View.GONE){
					viewHolder.btnDelete.startAnimation(animLeftIn);
					viewHolder.btnMove.startAnimation(animRightIn);
					tempViewHolder.btnDelete.setVisibility(View.VISIBLE);
					tempViewHolder.btnMove.setVisibility(View.VISIBLE);
				}
				fragment.canMoveItemListView(true);
				txtDelete.setVisibility(View.GONE);
			}else{
				if(viewHolder.btnDelete.getVisibility() == View.VISIBLE){
					viewHolder.btnDelete.startAnimation(animLeftOut);
					viewHolder.btnMove.startAnimation(animRightOut);
				}
				handler.postDelayed(new Runnable() {
					public void run() {
						tempViewHolder.btnDelete.setVisibility(View.GONE);
						tempViewHolder.btnMove.setVisibility(View.GONE);
					}
				}, 120);
				fragment.canMoveItemListView(false);
				txtDelete.setVisibility(View.GONE);
			} 
			viewHolder.txtDelete.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					listRow.remove(honeWeekChildItem);
					notifyDataSetChanged();
				}
			});
			viewHolder.btnCheck.setSelected(honeWeekChildItem.isCheck());
			viewHolder.btnCheck.setOnClickListener(new OnClickListener() {
				public void onClick(View view) {
					honeWeekChildItem.setCheck(!honeWeekChildItem.isCheck());
					view.setSelected(honeWeekChildItem.isCheck());
				}
			});
			viewHolder.btnCalendar.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					long temp = System.currentTimeMillis();
					if((temp - flagTimeCalendar) > 800){
						flagTimeCalendar = temp;
						if(honeWeekChildItem.getId() < 0){
							fragment.addCalendar(honeWeekChildItem);
						}else{
							fragment.viewCalendar(honeWeekChildItem);
						}
					}
				}
			});
			viewHolder.btnDelete.setOnClickListener(new OnClickListener() {
				public void onClick(View view) {
					if(txtDelete.getVisibility() == View.GONE){
						txtDelete.startAnimation(animRightIn);
					}
					handler.postDelayed(new Runnable() {
						public void run() {
							txtDelete.setVisibility(View.VISIBLE);
						}
					}, 100);
				}
			});
		}else{
			viewHolder.btnAdd.setVisibility(View.VISIBLE);
			viewHolder.btnAdd.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(fragment.getActivity(), HoneWeekAddActivity.class);
					HoneWeekAddActivity.item = listItem.get(section);
					fragment.getActivity().startActivity(intent);
				}
			});
		}
        
        return view;
	}

	class ViewHolderRow{
		View btnAdd, btnCheck, btnCalendar, btnMove, btnDelete, layoutContent;
		TextView txtTitle, txtDelete;
	}

}
