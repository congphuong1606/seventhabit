package com.discovertodo.phone.android.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.discovertodo.phone.android.R;
import com.discovertodo.phone.android.ebook.BookMark;
import com.discovertodo.phone.android.ebook.EpubWebViewClient;
import com.discovertodo.phone.android.fragment.EbookFragment;

@SuppressLint("SimpleDateFormat")
public class TabBookMarkAdapter extends BaseSwipeAdapter {
	private ArrayList<BookMark> listItem;
	private EbookFragment mContext;
	SimpleDateFormat formatter, format, formatE;
	boolean isOpen = false;
	boolean isShowSwipe = false;
	ArrayList<SwipeLayout> list = new ArrayList<SwipeLayout>();

	public TabBookMarkAdapter(EbookFragment mContext, ArrayList<BookMark> list) {
		this.mContext = mContext;
		this.listItem = list;
		formatter = new SimpleDateFormat("yyyy年 MM月 dd日 ");
		// formatter = new SimpleDateFormat("mm/dd/yyyy");
		format = new SimpleDateFormat("MM/dd/yyyy");
		formatE = new SimpleDateFormat("EEEE");
	}

	@Override
	public int getSwipeLayoutResourceId(int position) {
		return R.id.item;
	}

	@SuppressLint("InflateParams")
	@Override
	public View generateView(int position, ViewGroup parent) {
		View v = LayoutInflater.from(mContext.getActivity()).inflate(
				R.layout.item_bookmark, null);
		return v;
	}

	@Override
	public void fillValues(final int position, View convertView) {
		TextView tv_date = (TextView) convertView.findViewById(R.id.tv_date);
		LinearLayout llClick = (LinearLayout) convertView
				.findViewById(R.id.ll_click);
		final TextView tv_number = (TextView) convertView
				.findViewById(R.id.tv_number);
		final SwipeLayout swipeLayout = (SwipeLayout) convertView
				.findViewById(getSwipeLayoutResourceId(position));
		TextView tv_menu = (TextView) convertView.findViewById(R.id.tv_menu);
		Button btnDel = (Button) convertView
				.findViewById(R.id.btn_delete_bookmark);

		if (format.format(new Date(listItem.get(position).getDate()))
				.toString()
				.equalsIgnoreCase(format.format(new Date()).toString())) {
			tv_date.setText("今日");
		} else
			tv_date.setText(formatter.format(
					new Date(listItem.get(position).getDate())).toString()
					+ textEEEE(formatE.format(new Date(listItem.get(position)
							.getDate()))));
		int height = 0;
		if (EpubWebViewClient.totalHeight > EpubWebViewClient.totalWidth) {
			height = (int) EpubWebViewClient.heightWeb;
		} else
			height = EpubWebViewClient.totalHeight;
		BookMark mbookmark = listItem.get(position);

		int page = (EpubWebViewClient.arrListTagP[mbookmark.getNumber()] / height) + 1;
		// Log.e("DAT", lenghtTabP(mbookmark)
		// + EpubWebViewClient.arrListTagP[mbookmark.getNumber()]
		// + "/..........." + height * page);
		if (lenghtTabP(mbookmark)
				+ EpubWebViewClient.arrListTagP[mbookmark.getNumber()] >= height
				* page) {
			// Log.e("DAT", lenghtTabP(mbookmark) + "==========="
			// + (lenghtTabP(mbookmark) / height));
			page = page + (lenghtTabP(mbookmark) / height) + 1;
		}

		tv_number.setText(page + "");

		tv_menu.setText(menuPage(listItem.get(position).getNumber()));
		if (isShowSwipe) {
			swipeLayout.close();
		} else
			swipeLayout.close(false);
		// Log.e("DAT", swipeLayout.getOpenStatus().toString() + ">>>>>>");
		swipeListener(swipeLayout);

		llClick.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isShowSwipe = true;
				notifyDatasetChanged();
				if (isOpen) {
					isOpen = false;
					swipeLayout.close();
				} else
					mContext.OpenItemBookmark(Integer.parseInt(tv_number
							.getText().toString()) - 1);
			}
		});
		btnDel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				swipeLayout.close();
				isOpen = false;
				mContext.DeleteItemBookmark(listItem.get(position));
			}
		});
		switch (EbookFragment.type_light) {
		case 1:
			tv_number.setTextColor(Color.BLACK);
			tv_date.setTextColor(Color.GRAY);
			tv_menu.setTextColor(Color.BLACK);
			break;
		case 2:
			tv_number.setTextColor(Color.BLACK);
			tv_date.setTextColor(Color.GRAY);
			tv_menu.setTextColor(Color.BLACK);
			break;
		case 3:
			tv_number.setTextColor(Color.GRAY);
			tv_date.setTextColor(Color.GRAY);
			tv_menu.setTextColor(Color.GRAY);
			break;

		default:
			break;
		}
	}

	@Override
	public int getCount() {
		return listItem.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public String textEEEE(String str) {
		if (str.equalsIgnoreCase("Monday"))
			return "月曜日";
		if (str.equalsIgnoreCase("Tuesday"))
			return "火曜日";
		if (str.equalsIgnoreCase("Wednesday"))
			return "水曜日";
		if (str.equalsIgnoreCase("Thursday"))
			return "木曜日";
		if (str.equalsIgnoreCase("Friday"))
			return "金曜日";
		if (str.equalsIgnoreCase("Saturday"))
			return "土曜日";
		if (str.equalsIgnoreCase("Sunday"))
			return "日曜日";
		return "";
	}

	public void setSwipe() {
		isShowSwipe = false;
		isOpen = false;
	}

	private String menuPage(int pos) {
		try {
			String[] list = mContext.getResources()
					.getStringArray(R.array.menu);
			for (int i = EpubWebViewClient.arrMenu.length - 1; i >= 0; i--) {
				if (EpubWebViewClient.arrListTagP[pos]  >= EpubWebViewClient.arrMenu[i] - 5) {
					return list[i].toString();
				}
			}
			return "";
		} catch (Exception e) {
			return "";
		}
	}

	private int lenghtTabP(BookMark mBook) {
		return Math
				.round(((float) (mBook.getPage() + 3) / (float) EbookFragment.list
						.get(mBook.getNumber()))
						* (float) EpubWebViewClient.arrListHeightTagP[mBook
								.getNumber()]);
	}

	private void swipeListener(final SwipeLayout swipeLayout) {
		swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
			@Override
			public void onClose(SwipeLayout layout) {
				//isOpen = false;
			}

			@Override
			public void onUpdate(SwipeLayout layout, int leftOffset,
					int topOffset) {
			}

			@Override
			public void onStartOpen(SwipeLayout layout) {
			}

			@Override
			public void onOpen(SwipeLayout layout) {
				isOpen = true;
			}

			@Override
			public void onStartClose(SwipeLayout layout) {
			}

			@Override
			public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
			}
		});
	}
}
