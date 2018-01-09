package com.discovertodo.phone.android.ebook;

public class Search {
	private String title;
	private String result;
	private String number;
	private int intP;
	private int intPos;
	private String text;

	public Search() {

	}

	public Search(String title, String result, String number) {
		this.title = title;
		this.result = result;
		this.number = number;
	}

	public Search(String title, String result, String number, int intP,
			int intPos, String text) {
		this.title = title;
		this.result = result;
		this.number = number;
		this.intP = intP;
		this.intPos = intPos;
		this.text = text;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public int getIntP() {
		return intP;
	}

	public void setIntP(int intP) {
		this.intP = intP;
	}

	public int getIntPos() {
		return intPos;
	}

	public void setIntPos(int intPos) {
		this.intPos = intPos;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
