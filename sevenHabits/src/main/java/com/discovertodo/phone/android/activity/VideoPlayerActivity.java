package com.discovertodo.phone.android.activity;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;

import com.discovertodo.phone.android.R;
import com.discovertodo.phone.android.util.CustomVideoViewUtil;

public class VideoPlayerActivity extends Activity {
	private String TAG = "VideoPlayerActivity";
	private CustomVideoViewUtil mVideoPlayer;
	private Button mBtnFinishVideoPlayer, mBtnPlayVideo;
	private ProgressBar progressBar;
	private String videoUrl;
	AssetFileDescriptor afd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			videoUrl = extras.getString("listVideo");
		}
		setContentView(R.layout.video_player_activity);
		mVideoPlayer = (CustomVideoViewUtil) findViewById(R.id.vdoViewPlayer);
		mBtnFinishVideoPlayer = (Button) findViewById(R.id.btnFinishVideoPlayer);
		mBtnPlayVideo = (Button) findViewById(R.id.btnPlayVideo);
		progressBar = (ProgressBar) findViewById(R.id.loadingVideoPlayer);
		progressBar.setVisibility(View.VISIBLE);
		mBtnPlayVideo.setVisibility(View.GONE);
		mBtnPlayVideo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mBtnPlayVideo.setVisibility(View.GONE);
				init();
			}
		});
		mBtnFinishVideoPlayer.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mVideoPlayer.isPlaying()) {
					mVideoPlayer.stopPlayback();
				}
				finish();
			}
		});
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		MediaController mediacontroller;
		try {
			// Start the MediaController
			mediacontroller = new MediaController(VideoPlayerActivity.this);
			mediacontroller.setAnchorView(mVideoPlayer);
			// Get the URL from String VideoURL
			mVideoPlayer.setMediaController(mediacontroller);
			// mVideoPlayer.setVideoPath(videoUrl);
			Log.v(TAG, "videoUrl = " + videoUrl);

			mVideoPlayer.setVideoURI(Uri.parse(videoUrl));
		} catch (Exception e) {
			Log.e("Error", "error = " + e);
			e.printStackTrace();
		}

		// mVideoPlayer.requestFocus();
		mVideoPlayer.setOnPreparedListener(new OnPreparedListener() {
			// Close the progress bar and play the video
			public void onPrepared(MediaPlayer mp) {
				mp.setScreenOnWhilePlaying(false);
				mVideoPlayer.start();
				mBtnPlayVideo.setVisibility(View.GONE);
				progressBar.setVisibility(View.GONE);
				mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
					@Override
					public boolean onInfo(MediaPlayer mp, int what, int extra) {

						if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
							return true;
						}
						return false;

					}
				});
			}
		});
		mVideoPlayer.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				mBtnPlayVideo.setVisibility(View.VISIBLE);
				finish();
			}
		});
	}

}
