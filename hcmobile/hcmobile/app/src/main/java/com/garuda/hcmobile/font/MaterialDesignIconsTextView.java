package com.garuda.hcmobile.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;
import com.garuda.hcmobile.R;

import java.io.Console;

public class MaterialDesignIconsTextView extends TextView {
	
	private static Typeface sMaterialDesignIcons;

	public MaterialDesignIconsTextView(Context context) {
		this(context, null);
	}

	public MaterialDesignIconsTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MaterialDesignIconsTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if (isInEditMode()) return;//Won't work in Eclipse graphical layout
		setTypeface();
	}
	
	private void setTypeface() {
		if (sMaterialDesignIcons == null) {
			sMaterialDesignIcons = Typeface.createFromAsset(getContext().getAssets(), "fonts/MaterialDesignIcons.ttf");
			//Log.i("ICON",sMaterialDesignIcons.toString());
		}
		setTypeface(sMaterialDesignIcons);
	}
}
