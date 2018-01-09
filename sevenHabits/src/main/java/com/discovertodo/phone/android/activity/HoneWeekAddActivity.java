package com.discovertodo.phone.android.activity;

import com.discovertodo.phone.android.R;
import com.discovertodo.phone.android.model.HoneWeekChildItem;
import com.discovertodo.phone.android.model.HoneWeekItem;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

public class HoneWeekAddActivity extends BaseActivity {
	
	public static HoneWeekItem item;
	private EditText editText;
	private View btnDone;
	private String str;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.honeweek_add_layout);
		
		if(item == null){
			finish();
			return;
		}
		
		btnDone = findViewById(R.id.honeweek_add_btn_done);
		editText = (EditText) findViewById(R.id.honeweek_add_edittext);
		editText.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(s.toString().length() == 0){
					btnDone.setEnabled(false);
				}else{
					btnDone.setEnabled(true);
				}
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
			public void afterTextChanged(Editable s) { }
		});
	}
	
	public static void setHoneWeekItem(HoneWeekItem item){
		HoneWeekAddActivity.item = item;
	}
	
	public void clickBack(View view){
		finish();
	}
	
	public void clickDone(View view){
		str = editText.getText().toString().trim();
		if(!str.equals("")){
			HoneWeekChildItem itemChild = new HoneWeekChildItem();
			itemChild.setTitle(str);
			item.getListItem().add(itemChild);
			finish();
		}
	}
	
	@Override
	protected void onPause() {
		item = null;
		super.onPause();
	}

}
