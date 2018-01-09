package com.discovertodo.phone.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.discovertodo.phone.android.R;
import com.squareup.picasso.Picasso;

public class SplashActivity extends BaseActivity{
	
	private static final int SPLASH_DISPLAY = 1000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_layout);
		ImageView imageView = (ImageView)findViewById(R.id.splash);
		Picasso.with(this).load(R.drawable.splash).fit().into(imageView);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(SplashActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		}, SPLASH_DISPLAY);
	}
}
