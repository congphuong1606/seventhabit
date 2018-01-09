package com.discovertodo.phone.android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.discovertodo.phone.android.R;
import com.discovertodo.phone.android.activity.MainActivity;
import com.discovertodo.phone.android.activity.VideoPlayerActivity;
import com.discovertodo.phone.android.adapter.ContentVideoListAdapter;

public class ContentVideoListFragment extends BaseFragment {
	private ListView mLstContentVideoList;
	private MainActivity activity;
	private ContentVideoListAdapter mContentVideoListAdapter;
	private String[] listVideo = {
			"http://7habit.ominext.com/videos/video3.mp4",
			"http://7habit.ominext.com/videos/video4.mp4",
			"http://7habit.ominext.com/videos/video5.mp4",
			"http://7habit.ominext.com/videos/video2.mp4",
			"http://7habit.ominext.com/videos/video1.mp4",
			"http://7habit.ominext.com/videos/video6.mp4" };

	public static ContentVideoListFragment getInstance(){
		return new ContentVideoListFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.content_video_list, container,
					false);
			activity = (MainActivity) getActivity();
			mLstContentVideoList = (ListView) view
					.findViewById(R.id.lstContentVideoList);
			if (listVideo != null) {

				mContentVideoListAdapter = new ContentVideoListAdapter(
						activity, listVideo);
				mLstContentVideoList.setAdapter(mContentVideoListAdapter);
				mContentVideoListAdapter.notifyDataSetChanged();
			}

			mLstContentVideoList
					.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(getActivity(),
									VideoPlayerActivity.class);
							intent.putExtra("listVideo", listVideo[position]);
							startActivity(intent);

						}
					});
		}
		return super.onCreateView(inflater, container, savedInstanceState);
	}
}
