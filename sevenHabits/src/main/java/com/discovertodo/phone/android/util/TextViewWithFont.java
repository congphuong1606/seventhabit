package com.discovertodo.phone.android.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

import com.discovertodo.phone.android.R;
import com.discovertodo.phone.android.activity.BaseActivity;
import com.discovertodo.phone.android.activity.MainActivity;

/**
 * Created by MR THANG on 9/18/2015.
 */
public class TextViewWithFont extends TextView {

    private int defaultDimension = 0;
    private int TYPE_BOLD = 1;
    private int TYPE_ITALIC = 2;
    private int FONT_MSGOTHIC = 1;
    private int BLACK = 1;
    private int BLUE = 2;
    private int fontType;
    private int fontName;
    private int textColor;
    private int textSize;
    private int textSizeNomal = 1;
    private int textSizeNomalx = 2;
    private int textSizeNomalxx = 3;

    public TextViewWithFont(Context context) {
        super(context);
        init(null, 0);
    }

    public TextViewWithFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public TextViewWithFont(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {
        setFontType(MainActivity.msGothic);

        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.font, defStyle, 0);
        fontName = a.getInt(R.styleable.font_name, defaultDimension);
        fontType = a.getInt(R.styleable.font_type, defaultDimension);
        textColor = a.getInt(R.styleable.font_textcolor, defaultDimension);
        textSize = a.getInt(R.styleable.font_textsize, defaultDimension);
        a.recycle();

        setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3.0f, getResources().getDisplayMetrics()), 1.0f);
        setTextColors();
        setTextSize();
        MyApplication application = (MyApplication) getContext().getApplicationContext();
        setFontType(application.getMsGothic());
    }

    private void setFontType(Typeface font) {
        if (fontType == TYPE_BOLD) {
            setTypeface(font, Typeface.BOLD);
        } else if (fontType == TYPE_ITALIC) {
            setTypeface(font, Typeface.ITALIC);
        } else {
            setTypeface(font);
        }
    }

    private void setTextColors() {
        if (textColor == BLACK) {
            setTextColor(getResources().getColor(R.color.black));
        } else if (textColor == BLUE) {
            setTextColor(getResources().getColor(R.color.bluex));
        }
    }

    private void setTextSize() {
        if (textSize == textSizeNomal) {
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        } else if (textSize == textSizeNomalx) {
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        } else if (textSize == textSizeNomalxx) {
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        }
    }
}
