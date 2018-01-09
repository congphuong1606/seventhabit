package com.discovertodo.phone.android.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.preference.PreferenceActivity.Header;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.discovertodo.phone.android.R;
import com.discovertodo.phone.android.activity.BaseActivity;
import com.discovertodo.phone.android.ebook.EpubWebViewClient;
import com.discovertodo.phone.android.fragment.EbookFragment;

public class TabMenuAdapter extends BaseAdapter {

	private BaseActivity activity;
	private ArrayList<String> listItem;
	private LayoutInflater layoutInflater;

	public TabMenuAdapter(BaseActivity activity, ArrayList<String> listItem) {
		this.activity = activity;
		this.listItem = listItem;
		layoutInflater = (LayoutInflater) this.activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return listItem.size();
	}

	@Override
	public Object getItem(int position) {
		return listItem.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder viewHolder = null;
		if (convertView == null) {
			view = layoutInflater.inflate(R.layout.item_lv_tab_menu, null);
			viewHolder = new ViewHolder();
			viewHolder.tvName = (TextView) view.findViewById(R.id.tv_name_menu);
			viewHolder.tvNumber = (TextView) view
					.findViewById(R.id.tv_number_menu);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		String item = listItem.get(position);
		viewHolder.tvName.setText(item);
		try {
			int height = 0;

			if (EpubWebViewClient.totalHeight > EpubWebViewClient.totalWidth) {
				height = (int) EpubWebViewClient.heightWeb;
			} else {
				height = EpubWebViewClient.totalHeight;
			}
			// if ( EpubWebViewClient.arrMenu[position] % height == 0) {
			// viewHolder.tvNumber
			// .setText((EpubWebViewClient.arrMenu[position] / height)
			// + "");
			// } else
			viewHolder.tvNumber
					.setText((EpubWebViewClient.arrMenu[position] / height) + 1
							+ "");
//			Log.e("DAT",position+"/"+ EpubWebViewClient.arrMenu[position] + "/" + height
//					+ "/" + (EpubWebViewClient.arrMenu[position] / height + 1));
		} catch (Exception e) {
			// TODO: handle exception
		}

		switch (EbookFragment.type_light) {
		case 1:
			viewHolder.tvNumber.setTextColor(Color.BLACK);
			viewHolder.tvName.setTextColor(Color.BLACK);
			break;
		case 2:
			viewHolder.tvNumber.setTextColor(Color.BLACK);
			viewHolder.tvName.setTextColor(Color.BLACK);
			break;
		case 3:
			viewHolder.tvNumber.setTextColor(Color.GRAY);
			viewHolder.tvName.setTextColor(Color.GRAY);
			break;

		default:
			break;
		}

		return view;
	}

	class ViewHolder {
		TextView tvName, tvNumber;
	}

}