package com.discovertodo.phone.android.model;

public class HoneWeekChildItem implements Cloneable {
	private long id = -1;
	private String title;
	private boolean isCheck = false;

	public HoneWeekChildItem clone() throws CloneNotSupportedException {
		return (HoneWeekChildItem) super.clone();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}
}
