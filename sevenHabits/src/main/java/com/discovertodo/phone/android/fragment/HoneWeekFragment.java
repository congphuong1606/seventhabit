package com.discovertodo.phone.android.fragment;

import java.util.ArrayList;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.applidium.headerlistview.HeaderListView;
import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;
import com.discovertodo.phone.android.R;
import com.discovertodo.phone.android.activity.HoneWeekHistoryActivity;
import com.discovertodo.phone.android.adapter.HoneWeekAdapter;
import com.discovertodo.phone.android.global.Constant;
import com.discovertodo.phone.android.model.HoneWeekChildItem;
import com.discovertodo.phone.android.model.HoneWeekDragItem;
import com.discovertodo.phone.android.model.HoneWeekItem;
import com.discovertodo.phone.android.util.HoneWeekUtil;

public class HoneWeekFragment extends BaseFragment {
	
	private HomeFragment parentFragment;
	private TextView txtTimeTitle, txtBtnEdit;
	private View btnShowAll;
	private RelativeLayout layoutParentList;
	private HeaderListView listView;
	private HoneWeekAdapter adapter;
	private DragSortController dragSortController;
	private ArrayList<HoneWeekItem> listItem;
    private ArrayList<HoneWeekDragItem> listDrag;
    private HoneWeekChildItem itemChildChoose;
    private Time timeStart, timeEnd;
    private long timeStartEventEditting;

//	public HoneWeekFragment(HomeFragment fragment){
//		parentFragment = fragment;
//	}
	public static HoneWeekFragment getInstance(HomeFragment fragment){
		HoneWeekFragment honeWeekFragment = new HoneWeekFragment();
		honeWeekFragment.parentFragment = fragment;
		return honeWeekFragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(view == null){
			view = inflater.inflate(R.layout.honeweek_layout, container, false);
			btnShowAll = view.findViewById(R.id.honeweek_btn_show_all);
			txtBtnEdit = (TextView) parentFragment.btnHoneWeekEdit;
			txtTimeTitle = (TextView) view.findViewById(R.id.honeweek_time_text);
			layoutParentList = (RelativeLayout) view.findViewById(R.id.honeweek_layout);
			listView = new HeaderListView(activity);
			layoutParentList.addView(listView);
			btnShowAll.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(activity, HoneWeekHistoryActivity.class);
					startActivity(intent);
					activity.overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_stood);
				}
			});
			listDrag = new ArrayList<HoneWeekDragItem>();
			setupValue();
			setupControllerListView();
		}
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		setTextTitle();
		txtBtnEdit.setVisibility(View.VISIBLE);
		adapter.notifyDataSetChanged();
	}
	
	@Override
	public void onPause() {
		HoneWeekUtil.saveItemWeekCurrent(activity, listItem, timeStart, timeEnd);
		super.onPause();
	}
	
	@Override
	public void onStop() {
		txtBtnEdit.setVisibility(View.GONE);
		super.onStop();
	}
	
	private void setupValue(){
		timeStart = new Time();
		timeEnd = new Time();
		listItem = HoneWeekUtil.getCurrent(activity);
		adapter = new HoneWeekAdapter(this, listItem);
		listView.setAdapter(adapter);
		
		txtBtnEdit.setText("編集");
		txtBtnEdit.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if(txtBtnEdit.getText().toString().equals("編集")){
					txtBtnEdit.setText("完了");
					adapter.notifyDataSetChanged();
				}else{
					txtBtnEdit.setText("編集");
					adapter.notifyDataSetChanged();
				}
			}
		});
	}
	
	private void setTextTitle(){
		SharedPreferences sharedPreferences = activity.getSharedPreferences(Constant.SHARED_PREFERENCE_SETTING, Context.MODE_PRIVATE);
		int positionDaySave = sharedPreferences.getInt(Constant.SETTING_HONEWEEK_POSITIONDAY, 0);
		Time time = new Time();
		time.setToNow();
		int positionDayNow = time.weekDay;
		time.set(0, 0, 0, time.monthDay, time.month, time.year);
		if(positionDayNow >= positionDaySave){
			timeStart.set(time.toMillis(true) - (positionDayNow - positionDaySave) * 86400000);
			timeEnd.set(time.toMillis(true) + (6 - (positionDayNow - positionDaySave)) * 86400000);
		}else{
			timeEnd.set(time.toMillis(true) + (positionDaySave - positionDayNow - 1) * 86400000);
			timeStart.set(time.toMillis(true) - (6 - (positionDaySave - positionDayNow - 1)) * 86400000);
		}
		txtTimeTitle.setText(String.format("%04d", timeStart.year) + "年" + String.format("%02d", timeStart.month+1)
				 + "月" + String.format("%02d", timeStart.monthDay) + "日 - " +
				 String.format("%04d", timeEnd.year) + "年" + String.format("%02d", timeEnd.month+1)
				 + "月" + String.format("%02d", timeEnd.monthDay) + "日");
	}
	
	public boolean isEditList(){
		if(txtBtnEdit.getText().toString().equals("編集")){
			return false;
		}else{
			return true;
		}
	}
	
	private void setupControllerListView(){
		dragSortController = buildController(listView.getListView());
		listView.getListView().setDropListener(onDrop);
		listView.getListView().setFloatViewManager(dragSortController);
		listView.getListView().setOnTouchListener(dragSortController);
		listView.getListView().setDragEnabled(false);
	}
	
	public void canMoveItemListView(boolean isCanMove){
		listView.getListView().setDragEnabled(isCanMove);
	}
            
    private DragSortListView.DropListener onDrop =
            new DragSortListView.DropListener() {
                @Override
                public void drop(int from, int to) {
                	calculateListDrag();
                	try{
	                    if (from != to) {
	                    	HoneWeekDragItem itemFrom = listDrag.get(from);
	                    	HoneWeekDragItem itemTo = listDrag.get(to);
	                    	swapItem(itemFrom, itemTo);
	                    }
                	}catch(Exception e){
                		e.printStackTrace();
                	}
                	adapter.notifyDataSetChanged();
                }
            };
	
    public DragSortController buildController(DragSortListView dslv) {
        DragSortController controller = new DragSortController(dslv);
        controller.setDragHandleId(R.id.honeweek_item_row_content_img_move);
        controller.setSortEnabled(true);
        controller.setDragInitMode(DragSortController.ON_DOWN);
        return controller;
    }
    
    private void swapItem(HoneWeekDragItem itemFrom, HoneWeekDragItem itemTo) throws Exception{
    	HoneWeekChildItem oldHoneWeekChild = listItem
				.get(itemFrom.getSection()).getListItem()
				.get(itemFrom.getRow());
		HoneWeekChildItem newHoneWeekChild = oldHoneWeekChild.clone();
		int positionParent, positionChild;
		if(itemFrom.getSection() < itemTo.getSection() || 
				(itemFrom.getSection() == itemTo.getSection() && itemFrom.getRow() < itemTo.getRow())){
			if(itemTo.isHeader()){
				positionChild = 0;
			}else if(itemTo.isAdd()){
				positionChild = itemTo.getRow();
			}else{
				positionChild = itemTo.getRow() + 1;
			}
			positionParent = itemTo.getSection();
		}else if(itemFrom.getSection() > itemTo.getSection() || 
				(itemFrom.getSection() == itemTo.getSection() && itemFrom.getRow() > itemTo.getRow())){
			if(itemTo.isHeader()){
				if(itemTo.getSection() == 0){
					positionParent = 0;
					positionChild = 0;
				}else{
					positionParent = itemTo.getSection() - 1;
					positionChild = listItem.get(positionParent).getListItem().size();
				}
			}else if(itemTo.isAdd()){
				positionParent = itemTo.getSection();
				positionChild = itemTo.getRow();
			}else{
				positionParent = itemTo.getSection();
				positionChild = itemTo.getRow();
			}
		}else{
			if(itemTo.isHeader()){
				if(itemTo.getSection() == 0){
					positionParent = 0;
					positionChild = 0;
				}else{
					positionParent = itemTo.getSection() - 1;
					positionChild = listItem.get(positionParent).getListItem().size();
				}
			}else{
				Log.e("XXX-YYY", itemFrom.getSection() + "  " + itemFrom.getRow() + "    " +
						itemTo.getSection() + "   "  + itemTo.getRow());
				return;
			}
		}
		listItem.get(positionParent).getListItem().add(positionChild, newHoneWeekChild);
		listItem.get(itemFrom.getSection()).getListItem().remove(oldHoneWeekChild);
    }
    
    private void calculateListDrag(){
    	listDrag.clear();
    	for (int i = 0; i < listItem.size(); i++) {
    		HoneWeekDragItem item1 = new HoneWeekDragItem();
    		item1.setHeader(true);
    		item1.setAdd(false);
			item1.setSection(i);
    		item1.setRow(0);
    		listDrag.add(item1);
			for (int j = 0; j < listItem.get(i).getListItem().size(); j++) {
				HoneWeekDragItem item2 = new HoneWeekDragItem();
	    		item2.setHeader(false);
	    		item2.setAdd(false);
	    		item2.setSection(i);
	    		item2.setRow(j);
	    		listDrag.add(item2);
			}
			HoneWeekDragItem item3 = new HoneWeekDragItem();
    		item3.setHeader(false);
    		item3.setAdd(true);
    		item3.setSection(i);
    		item3.setRow(listItem.get(i).getListItem().size());
    		listDrag.add(item3);
		}
    }
    
	public void addCalendar(HoneWeekChildItem honeWeekChildItem){
		timeStartEventEditting = System.currentTimeMillis();
		itemChildChoose = honeWeekChildItem;
		Intent intent = new Intent(Intent.ACTION_INSERT);
		intent.setData(Events.CONTENT_URI);
		intent.putExtra(Events.TITLE, itemChildChoose.getTitle());
		startActivityForResult(intent, Constant.REQUEST_CODE_CALENDAR);
	}
	
	public void viewCalendar(HoneWeekChildItem honeWeekChildItem){
		Uri uri = ContentUris.withAppendedId(Events.CONTENT_URI, honeWeekChildItem.getId());
		long[] time = getEventTime(honeWeekChildItem.getId());
		Intent intent = new Intent(Intent.ACTION_VIEW).setData(uri)
				.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, time[0])
				.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, time[1]);
		startActivity(intent);
	}
	
	private long[] getEventTime(long id) {
		long[] result = new long[2];
		Cursor cursor = activity.getContentResolver().query(Events.CONTENT_URI,
				new String[] { "dtstart", "dtend" }, "_id=?",
				new String[] { id + "" }, null);
		if (cursor.moveToFirst()) {
			int beginTimeIndex = cursor.getColumnIndex("dtstart");
			int endTimeIndex = cursor.getColumnIndex("dtend");
			result[0] = cursor.getLong(beginTimeIndex);
			result[1] = cursor.getLong(endTimeIndex);
		}
		return result;
	}
    
}
