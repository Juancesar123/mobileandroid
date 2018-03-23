package com.garuda.hcmobile.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by MBP on 12/8/15.
 */
public class CustomTextView extends TextView {

    private Context context;


    public CustomTextView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {

        Typeface myTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/"+"MyriadProRegular.otf");
        setTypeface(myTypeface);

    }

}
