package com.discovertodo.phone.android.ebook;

import java.util.ArrayList;
import java.util.Collections;

import com.discovertodo.phone.android.util.SortBookMark;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class BookMarkTable {
	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;
	public static final String TABLE_NAME = "tblBookmark";
	public static final String ID_BOOKMARK = "id_bookmark";
	public static final String DATE_BOOKMARK = "date_bookmark";
	public static final String NUMBER_BOOKMARK = "number_bookmark";
	public static final String PAGE_BOOKMARK = "page_bookmark";

	public static final String DATABASE_CREATE_BOOKMARK = "create table "
			+ TABLE_NAME + "(" + ID_BOOKMARK
			+ " integer primary key autoincrement, " + DATE_BOOKMARK + " text,"
			+ NUMBER_BOOKMARK + " integer," + PAGE_BOOKMARK + " integer" + ")";

	public BookMarkTable(Context context) {
		dbHelper = new DatabaseHelper(context);
		database = dbHelper.getWritableDatabase();
		open();
	}

	public void open() {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public boolean addBookmark(BookMark mBookmark) {
		if (!database.isOpen())
			open();
		// if (checkNumber(mBookmark.getNumber())) {
		// return false;
		// } else {
		try {
			ContentValues mValue = new ContentValues();
			mValue.put(DATE_BOOKMARK, mBookmark.getDate() + "");
			mValue.put(NUMBER_BOOKMARK, mBookmark.getNumber());
			mValue.put(PAGE_BOOKMARK, mBookmark.getPage());
			database.insert(TABLE_NAME, null, mValue);
		} catch (Exception e) {
		}
		return true;
		// }
	}

	public ArrayList<BookMark> getAllBookmark() {
		if (!database.isOpen())
			open();
		ArrayList<BookMark> mBookmark = new ArrayList<BookMark>();
		Cursor cursor = database.rawQuery("SELECT * FROM tblBookmark ", null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			mBookmark.add(cursorToBookmark(cursor));
			cursor.moveToNext();
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		if (mBookmark.size() > 0)
			Collections.sort(mBookmark, new SortBookMark());
		return mBookmark;
	}

	public boolean deleteBookmark(BookMark mBookmark) {
		if (!database.isOpen())
			open();
		int id = mBookmark.getId();
		return database.delete(TABLE_NAME, ID_BOOKMARK + " = " + id, null) > 0;
	}

	private BookMark cursorToBookmark(Cursor cursor) {
		BookMark rss = new BookMark();
		rss.setId(cursor.getInt(0));
		rss.setDate(Long.parseLong(cursor.getString(1)));
		rss.setNumber(cursor.getInt(2));
		rss.setPage(cursor.getInt(3));
		return rss;
	}

	public boolean checkNumber(int number) {
		boolean is = false;
		ArrayList<BookMark> mBookmark = new ArrayList<BookMark>();
		Cursor cursor = database.rawQuery("SELECT * FROM tblBookmark ", null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			mBookmark.add(cursorToBookmark(cursor));
			if (cursor.getInt(2) == number) {
				is = true;
			}
			cursor.moveToNext();
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return is;
	}

	public boolean deleteBookmark(int pos) {
		if (!database.isOpen())
			open();
		return database.delete(TABLE_NAME, NUMBER_BOOKMARK + " = " + pos, null) > 0;
	}

	public boolean deleteBookmark(int pos, int page) {
		Log.e("DAT", pos+">>>>>>>>"+page);
		if (!database.isOpen())
			open();
		return database.delete(TABLE_NAME, NUMBER_BOOKMARK + " = " + pos
				+ " and " + PAGE_BOOKMARK + " = " + page, null) > 0;
	}

}
