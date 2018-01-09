package com.discovertodo.phone.android.adapter;

import com.discovertodo.phone.android.R;
import com.discovertodo.phone.android.fragment.ContentVideoListFragment;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ContentVideoListAdapter extends BaseAdapter {
	private String TAG = "ContentVideoListAdapter";
	private LayoutInflater mInflater;
	private Context mContext;
	private String[] mListVideo;
	private ContentVideoListFragment contentvideofragment;

	private int[] listVideoName = { R.string.content_video_name03,
			R.string.content_video_name04, R.string.content_video_name05,
			R.string.content_video_name02, R.string.content_video_name01,
			R.string.content_video_name06 };
	private int[] listVideoThumb = { R.drawable.thumb_video3,
			R.drawable.thumb_video4, R.drawable.thumb_video5,
			R.drawable.thumb_video2, R.drawable.thumb_video1,
			R.drawable.thumb_video6 };
	private int[] listDescription = {R.string.description_video1,R.string.description_video2,R.string.description_video3,
			R.string.description_video4,R.string.description_video5,R.string.description_video6};

	private String[] listVideoDuration = { "(4分58秒)", "(8分36秒)", "(4分03秒)", "(14分26秒)",
			"(2分27秒)", "(3分12秒)" };

	public ContentVideoListAdapter(Context context, String[] listVideo) {
		// TODO Auto-generated constructor stub
		this.mListVideo = listVideo;
		this.mContext = context;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mListVideo.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mListVideo[position];
	}

	@SuppressLint("SimpleDateFormat")
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolderItem viewHolder;
		if (convertView == null) {
			mInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.content_video_itemlist,
					null);
			// create view holder
			viewHolder = new ViewHolderItem();
			viewHolder.imgVideoContent = (ImageView) convertView
					.findViewById(R.id.imgVideoContent);
			viewHolder.txtVideoName = (TextView) convertView
					.findViewById(R.id.txtVideoName);
			viewHolder.txtVideoSizeTime = (TextView) convertView
					.findViewById(R.id.txtVideoSizeTime);
			viewHolder.txtDescription = (TextView)convertView.findViewById(R.id.txtDescription);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolderItem) convertView.getTag();
		}
		viewHolder.txtVideoName.setText(listVideoName[position]);
		viewHolder.imgVideoContent.setImageResource(listVideoThumb[position]);
		viewHolder.txtVideoSizeTime.setText(listVideoDuration[position]);
		viewHolder.txtDescription.setText(listDescription[position]);
		return convertView;
	}

	public static class ViewHolderItem {
		public ImageView imgVideoContent;
		public TextView txtVideoName, txtVideoSizeTime,txtDescription;
	}
}
