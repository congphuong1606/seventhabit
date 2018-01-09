package com.discovertodo.phone.android.adapter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.discovertodo.phone.android.R;
import com.discovertodo.phone.android.activity.BaseActivity;
import com.discovertodo.phone.android.fragment.EbookFragment;
import com.discovertodo.phone.android.util.StoreData;

@SuppressLint("InflateParams")
public class ChangeFontAdapter extends BaseAdapter {

	private BaseActivity activity;
	private String[] listItem;
	private LayoutInflater layoutInflater;
	TTFAnalyzer analyzer;
	StoreData data;
	String[] listNameFont;
	String[] listFont;

	public ChangeFontAdapter(BaseActivity activity, String[] listItem) {
		this.activity = activity;
		this.listItem = listItem;
		layoutInflater = (LayoutInflater) this.activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		data = new StoreData(activity);
		listNameFont = activity.getResources().getStringArray(R.array.fontname);
		listFont = activity.getResources().getStringArray(R.array.font);
	}

	@Override
	public int getCount() {
		return listItem.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder viewHolder = null;
		if (convertView == null) {
			view = layoutInflater.inflate(R.layout.item_listview_dialog_font,
					null);
			viewHolder = new ViewHolder();
			viewHolder.tvName = (TextView) view.findViewById(R.id.tv_name);
			viewHolder.imgCheck = (ImageView) view.findViewById(R.id.img_check);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		String item = listItem[position];
		//Typeface type = Typeface.createFromFile(item);
//		Typeface type = Typeface.createFromFile(listFont[position].toString());
//		viewHolder.tvName.setTypeface(type);
		viewHolder.tvName.setText(listNameFont[position]);

		if (data.getStringValue("font").equalsIgnoreCase("")) {
			if (item.equalsIgnoreCase(listItem[0])) {
				viewHolder.imgCheck.setVisibility(View.VISIBLE);
			} else
				viewHolder.imgCheck.setVisibility(View.INVISIBLE);
		} else {
			if (item.equalsIgnoreCase(data.getStringValue("font"))) {
				viewHolder.imgCheck.setVisibility(View.VISIBLE);
			} else
				viewHolder.imgCheck.setVisibility(View.INVISIBLE);
		}

		// try {
		// viewHolder.tvName.setText(item.replace("/system/fonts/", "")
		// .replace(".ttf", ""));
		// } catch (Exception e) {
		// viewHolder.tvName.setText(item);
		// }
		// if (data.getStringValue("font").equalsIgnoreCase("")) {
		// if (item.equalsIgnoreCase(listItem[0])) {
		// viewHolder.imgCheck.setVisibility(View.VISIBLE);
		// } else
		// viewHolder.imgCheck.setVisibility(View.INVISIBLE);
		// } else if (item.equalsIgnoreCase(data.getStringValue("font"))) {
		// viewHolder.imgCheck.setVisibility(View.VISIBLE);
		// } else
		// viewHolder.imgCheck.setVisibility(View.INVISIBLE);

		switch (EbookFragment.type_light) {
		case 1:
			viewHolder.imgCheck.setBackgroundResource(R.drawable.check);
			viewHolder.tvName.setTextColor(Color.BLACK);
			break;
		case 2:
			viewHolder.imgCheck.setBackgroundResource(R.drawable.check1);
			viewHolder.tvName.setTextColor(Color.BLACK);
			break;
		case 3:
			viewHolder.imgCheck.setBackgroundResource(R.drawable.check2);
			viewHolder.tvName.setTextColor(Color.GRAY);
			break;

		default:
			break;
		}

		return view;
	}

	class ViewHolder {
		TextView tvName;
		ImageView imgCheck;
	}

	class TTFAnalyzer {
		public String getTtfFontName(String fontFilename) {
			try {
				m_file = new RandomAccessFile(fontFilename, "r");
				int version = readDword();
				if (version != 0x74727565 && version != 0x00010000)
					return null;
				int numTables = readWord();
				readWord(); // skip searchRange
				readWord(); // skip entrySelector
				readWord(); // skip rangeShift
				for (int i = 0; i < numTables; i++) {
					int tag = readDword();
					readDword(); // skip checksum
					int offset = readDword();
					int length = readDword();
					if (tag == 0x6E616D65) {
						byte[] table = new byte[length];
						m_file.seek(offset);
						read(table);
						int count = getWord(table, 2);
						int string_offset = getWord(table, 4);
						for (int record = 0; record < count; record++) {
							int nameid_offset = record * 12 + 6;
							int platformID = getWord(table, nameid_offset);
							int nameid_value = getWord(table, nameid_offset + 6);
							if (nameid_value == 4 && platformID == 1) {
								int name_length = getWord(table,
										nameid_offset + 8);
								int name_offset = getWord(table,
										nameid_offset + 10);
								name_offset = name_offset + string_offset;
								if (name_offset >= 0
										&& name_offset + name_length < table.length)
									return new String(table, name_offset,
											name_length);
							}
						}
					}
				}

				return null;
			} catch (FileNotFoundException e) {
				return null;
			} catch (IOException e) {
				return null;
			}
		}

		private RandomAccessFile m_file = null;

		private int readByte() throws IOException {
			return m_file.read() & 0xFF;
		}

		private int readWord() throws IOException {
			int b1 = readByte();
			int b2 = readByte();
			return b1 << 8 | b2;
		}

		private int readDword() throws IOException {
			int b1 = readByte();
			int b2 = readByte();
			int b3 = readByte();
			int b4 = readByte();
			return b1 << 24 | b2 << 16 | b3 << 8 | b4;
		}

		private void read(byte[] array) throws IOException {
			if (m_file.read(array) != array.length)
				throw new IOException();
		}

		private int getWord(byte[] array, int offset) {
			int b1 = array[offset] & 0xFF;
			int b2 = array[offset + 1] & 0xFF;
			return b1 << 8 | b2;
		}
	}

}
