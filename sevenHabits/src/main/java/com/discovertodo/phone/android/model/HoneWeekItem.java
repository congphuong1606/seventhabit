package com.discovertodo.phone.android.model;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class HoneWeekItem implements Serializable{
	
	private String title;
	private ArrayList<HoneWeekChildItem> listItem = new ArrayList<HoneWeekChildItem>();

	public String getTitle(){
		return title;
	}
	public void setTitle(String title){
		this.title = title;
	}
	public ArrayList<HoneWeekChildItem> getListItem(){
		return listItem;
	}
}
