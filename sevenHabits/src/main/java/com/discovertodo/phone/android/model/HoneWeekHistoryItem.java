package com.discovertodo.phone.android.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class HoneWeekHistoryItem implements Serializable{
	private String content;
	private long timeStart, timeEnd;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getTimeStart() {
		return timeStart;
	}
	public void setTimeStart(long timeStart) {
		this.timeStart = timeStart;
	}
	public long getTimeEnd() {
		return timeEnd;
	}
	public void setTimeEnd(long timeEnd) {
		this.timeEnd = timeEnd;
	}
}
