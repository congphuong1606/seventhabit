package com.discovertodo.phone.android.util;

import android.app.Application;
import android.graphics.Typeface;
import android.os.Build;

/**
 * Created by MR THANG on 9/18/2015.
 */
public class MyApplication extends Application {

    private Typeface msGothic;

    public void onCreate() {
        super.onCreate();

        msGothic = Typeface.createFromAsset(getAssets(), "msgothic.otf");
    }

    public Typeface getMsGothic() {
        return msGothic;
    }
}
