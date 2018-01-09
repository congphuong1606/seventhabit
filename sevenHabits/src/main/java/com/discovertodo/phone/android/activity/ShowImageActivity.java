package com.discovertodo.phone.android.activity;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.discovertodo.phone.android.R;
import com.discovertodo.phone.android.model.TouchImageView;

public class ShowImageActivity extends BaseActivity implements View.OnTouchListener{
	
	private TouchImageView imageView;
	private String strImage;
	long lasTimeClick = 0;
	long firstTimeClick = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_image_layout);
		
		imageView = (TouchImageView) findViewById(R.id.showimage_image);
		imageView.setMaxZoom(2.0f);
		imageView.setMinZoom(0.5f);
		int path = getIntent().getIntExtra("path",0);
		imageView.setImageResource(path);

		strImage = getIntent().getStringExtra("image");
		if (strImage!=null) {
			if (strImage.startsWith("file://")) {
				strImage = strImage.replaceFirst("file://", "");
			}
			File imgFile = new File(strImage);
			if (imgFile.exists()) {
				Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
				imageView.setImageBitmap(myBitmap);
			}
		}
		imageView.setOnTouchListener(this);

	}
	
	public void closeImage(View view){
		finish();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		switch (event.getAction()){
			case MotionEvent.ACTION_DOWN: firstTimeClick = System.currentTimeMillis();break;
			case MotionEvent.ACTION_UP:
				if (firstTimeClick-lasTimeClick< 300){
					finish();
				}
				lasTimeClick = firstTimeClick;
				break;
		}
		return true;
	}
}
