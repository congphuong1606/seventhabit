package com.discovertodo.phone.android.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.discovertodo.phone.android.R;

public class SwitchView extends LinearLayout {
	private Context cnt;
	private LinearLayout llSwitch,llBg;
	private Button btnOn, btnOff;
	private boolean isOn = true;
	CallClick call;
	private int type_color = 1;

	public SwitchView(Context context) {
		super(context);
		this.cnt = context;
		initView();
	}

	public SwitchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.cnt = context;
		initView();
	}

	@SuppressLint("InflateParams") public void initView() {
		LayoutInflater mInflater = (LayoutInflater) cnt
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		llSwitch = (LinearLayout) mInflater.inflate(R.layout.switch_layout,
				null);
		llBg = (LinearLayout)llSwitch.findViewById(R.id.ll_bg);
		btnOn = (Button) llSwitch.findViewById(R.id.btn_on);
		btnOff = (Button) llSwitch.findViewById(R.id.btn_off);
		btnOn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isOn = !isOn;
				showSwith();
				call.clickLeft();
			}
		});
		btnOff.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isOn = !isOn;
				showSwith();
				call.clickRight();
			}
		});
		showSwith();
		addView(llSwitch, new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	}

	private void showSwith() {
		if (!isOn) {
			btnOff.setBackgroundColor(Color.TRANSPARENT);
			btnOn.setEnabled(true);
			btnOff.setEnabled(false);
			switch (type_color) {
			case 1:
				llBg.setBackgroundResource(R.drawable.bg_switch);
				btnOn.setBackgroundResource(R.drawable.bg_btn_right);
				btnOn.setTextColor(cnt.getResources().getColor(R.color.bluex));
				btnOff.setTextColor(cnt.getResources().getColor(
						R.color.white_color));
				break;
			case 2:
				llBg.setBackgroundResource(R.drawable.bg_switch1);
				btnOn.setBackgroundResource(R.drawable.bg_btn_right_1);
				btnOn.setTextColor(cnt.getResources().getColor(R.color.brown));
				btnOff.setTextColor(cnt.getResources().getColor(
						R.color.sepia));
				break;
			case 3:
				llBg.setBackgroundResource(R.drawable.bg_switch2);
				btnOn.setBackgroundResource(R.drawable.bg_btn_right_2);
				btnOn.setTextColor(Color.DKGRAY);
				btnOff.setTextColor(cnt.getResources().getColor(
						R.color.white));
				break;

			default:
				break;
			}

		} else {
			btnOn.setBackgroundColor(Color.TRANSPARENT);
			btnOn.setEnabled(false);
			btnOff.setEnabled(true);
			switch (type_color) {
			case 1:
				llBg.setBackgroundResource(R.drawable.bg_switch);
				btnOff.setBackgroundResource(R.drawable.bg_btn);
				btnOff.setTextColor(cnt.getResources().getColor(R.color.bluex));
				btnOn.setTextColor(cnt.getResources().getColor(
						R.color.white_color));
				break;
			case 2:
				llBg.setBackgroundResource(R.drawable.bg_switch1);
				btnOff.setBackgroundResource(R.drawable.bg_btn1);
				btnOff.setTextColor(cnt.getResources().getColor(R.color.brown));
				btnOn.setTextColor(cnt.getResources().getColor(
						R.color.sepia));
				break;
			case 3:
				llBg.setBackgroundResource(R.drawable.bg_switch2);
				btnOff.setBackgroundResource(R.drawable.bg_btn2);
				btnOff.setTextColor(Color.DKGRAY);
				btnOn.setTextColor(cnt.getResources().getColor(
						R.color.white_color));
				break;

			default:
				break;
			}

		}
	}

	public boolean getIsOn() {
		return isOn;
	}

	public void setIsOn(boolean is, CallClick call) {
		this.call = call;
		isOn = is;
		showSwith();
	}

	public interface CallClick {
		public void clickLeft();

		public void clickRight();
	};

	public void setColor(int id) {
		type_color = id;
		showSwith();
	}

}
