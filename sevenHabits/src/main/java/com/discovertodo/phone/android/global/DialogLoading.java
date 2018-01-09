package com.discovertodo.phone.android.global;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.KeyEvent;

public class DialogLoading {
	
	private static ProgressDialog dialog;
	private static Activity activity;
	private static String title = "";
	private static int load = -1;
	
	public static void showLoading(final Activity activity, String str){
		DialogLoading.activity = activity;
		DialogLoading.title = str;
		load = -1;
		cancel();
		activity.runOnUiThread(new Runnable() {
			public void run() {
				dialog = new ProgressDialog(activity){
					@Override
			        public boolean onKeyDown(int keyCode, KeyEvent event) {
			            if(keyCode == KeyEvent.KEYCODE_BACK) {
			            	return true;
			            }
			            return super.onKeyDown(keyCode, event);
			        }
				};
				dialog.setMessage(DialogLoading.title);
				dialog.setCanceledOnTouchOutside(false);
				dialog.show();
			}
		});
	}
	
	public static boolean isShowing(){
		if(dialog == null){
			return false;
		}
		return dialog.isShowing();
	}
	
	public static void cancel(){
		try{
			activity.runOnUiThread(new Runnable() {
				public void run() {
					if(dialog != null){
						dialog.dismiss();
					}
				}
			});
		}catch(Exception e){
			e.printStackTrace();
		}
		load = -1;
	}
	
	public static void setTitle(String title){
		DialogLoading.title = title;
		if(dialog != null)
			dialog.setMessage(DialogLoading.title);
	}
	
	public static void setLoading(int load){
		if(load > DialogLoading.load){
			DialogLoading.load = load;
			final String text = DialogLoading.title + "  " + load + "%";
			if(dialog != null){
				activity.runOnUiThread(new Runnable() {
					public void run() {
						dialog.setMessage(text);
					}
				});
			}
		}
	}
	
	public static String getTitle(){
		return DialogLoading.title;
	}
	
}
