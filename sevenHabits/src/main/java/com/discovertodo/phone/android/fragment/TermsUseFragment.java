package com.discovertodo.phone.android.fragment;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.discovertodo.phone.android.R;
import com.discovertodo.phone.android.model.JustifyTextView;


public class TermsUseFragment extends BaseFragment {
	
	private final String fileName = "termsure.txt";
	private JustifyTextView textView;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(view == null){
			view = inflater.inflate(R.layout.termsuse_layout, container, false);
			textView = (JustifyTextView) view.findViewById(R.id.termsuse_text);
			new Thread(){
				public void run() {
					try { Thread.sleep(300); } catch (InterruptedException e) { }
					final String text = loadText();
					activity.runOnUiThread(new Runnable() {
						public void run() {
							textView.setText(text);
						}
					});
				};
			}.start();
			textView.setText(loadText());
		}
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@SuppressWarnings("deprecation")
	private String loadText() {
		String text = "\n";
		InputStream fIn = null;
		InputStreamReader isr = null;
		BufferedReader input = null;
		try {
			fIn = activity.getResources().getAssets()
					.open(fileName, Context.MODE_WORLD_READABLE);
			isr = new InputStreamReader(fIn);
			input = new BufferedReader(isr);
			String line = "";
			while (((line = input.readLine()).trim()) != null) {
				if(line.length() == 0){
					line = "\n";
				}
				text += line + "\n";
			}
		} catch (Exception e) {
			e.getMessage();
		} finally {
			try {
				if (isr != null)
					isr.close();
				if (fIn != null)
					fIn.close();
				if (input != null)
					input.close();
			} catch (Exception e2) {
				e2.getMessage();
			}
		}
		return text;
	}

}
