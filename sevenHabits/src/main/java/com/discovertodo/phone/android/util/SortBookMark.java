package com.discovertodo.phone.android.util;

import java.util.Comparator;

import com.discovertodo.phone.android.ebook.BookMark;

public class SortBookMark implements Comparator<BookMark> {

	@Override
	public int compare(BookMark arg0, BookMark arg1) {
		long age0 = arg0.getDate();
		long age1 = arg1.getDate();
		if (age0 < age1) {
			return 1;
		} else if (age0 == age1) {
			return 0;
		} else {
			return -1;
		}
	}
}
