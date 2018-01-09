package com.discovertodo.phone.android.ebook;

public class BookMark {
	private int id;
	private int page;
	private long date;
	private int number;

	public BookMark() {
	}

	public BookMark(int id, long date, int number) {
		this.id = id;
		this.date = date;
		this.number = number;
	}

	public BookMark(int id, long date, int number, int page) {
		this.id = id;
		this.date = date;
		this.number = number;
		this.page = page;
	}

	public BookMark(long date, int number) {
		this.date = date;
		this.number = number;
	}

	public BookMark(long date, int number, int page) {
		this.date = date;
		this.number = number;
		this.page = page;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

}
