package com.discovertodo.phone.android.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.discovertodo.phone.android.R;
import com.discovertodo.phone.android.activity.BaseActivity;
import com.discovertodo.phone.android.ebook.Search;
import com.discovertodo.phone.android.fragment.EbookFragment;

public class ResultSearchAdapter extends BaseAdapter {

	private BaseActivity activity;
	private ArrayList<Search> listItem;
	private LayoutInflater layoutInflater;

	public ResultSearchAdapter(BaseActivity activity, ArrayList<Search> listItem) {
		this.activity = activity;
		this.listItem = listItem;
		layoutInflater = (LayoutInflater) this.activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return listItem.size() + 1;
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
			view = layoutInflater.inflate(R.layout.item_lv_result_search, null);
			viewHolder = new ViewHolder();
			viewHolder.tvName = (TextView) view.findViewById(R.id.tv_name_menu);
			viewHolder.tvNumber = (TextView) view
					.findViewById(R.id.tv_number_search);
			viewHolder.tvResult = (TextView) view
					.findViewById(R.id.tv_result_search);
			viewHolder.tvItem = (TextView) view.findViewById(R.id.tv_item);
			viewHolder.llItem = (LinearLayout) view.findViewById(R.id.ll_item);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		if (position >= listItem.size()) {
			viewHolder.llItem.setVisibility(View.GONE);
			if (!EbookFragment.edtSearch.getText().toString()
					.equalsIgnoreCase(""))
				viewHolder.tvItem.setVisibility(View.VISIBLE);
			else
				viewHolder.tvItem.setVisibility(View.GONE);
			if (listItem.size() > 0)
				viewHolder.tvItem.setText("検索が完了しました\n検索結果　" + listItem.size()
						+ " 件");
			else
				viewHolder.tvItem.setText("検索が完了しました\n一致するものが見つかりませんでした");
		} else {
			viewHolder.llItem.setVisibility(View.VISIBLE);
			viewHolder.tvItem.setVisibility(View.GONE);
			Search item = listItem.get(position);
			viewHolder.tvName.setText(item.getTitle());
			try {
				if (Integer.parseInt(item.getNumber()) > EbookFragment.numberMax) {
					viewHolder.tvNumber.setText(EbookFragment.numberMax + "");
				} else
					viewHolder.tvNumber.setText(item.getNumber());
			} catch (Exception e) {
				viewHolder.tvNumber.setText(item.getNumber());
			}
			if (!item.getResult().equalsIgnoreCase("")) {
				viewHolder.tvResult.setText(Html.fromHtml(item.getResult()));
			}

			switch (EbookFragment.type_light) {
			case 1:
				viewHolder.tvNumber.setTextColor(Color.BLACK);
				viewHolder.tvName.setTextColor(Color.BLACK);
				viewHolder.tvResult.setTextColor(Color.BLACK);
				break;
			case 2:
				viewHolder.tvNumber.setTextColor(Color.BLACK);
				viewHolder.tvName.setTextColor(Color.BLACK);
				viewHolder.tvResult.setTextColor(Color.BLACK);
				break;
			case 3:
				viewHolder.tvNumber.setTextColor(Color.GRAY);
				viewHolder.tvName.setTextColor(Color.GRAY);
				viewHolder.tvResult.setTextColor(Color.GRAY);
				break;
			default:
				break;
			}
		}
		return view;
	}

	class ViewHolder {
		TextView tvName, tvNumber, tvResult, tvItem;
		LinearLayout llItem;
	}
}