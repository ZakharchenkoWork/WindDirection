package com.example.mistery.winddirection;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

import com.special.ResideMenu.ResideMenuItem;

import java.lang.reflect.Field;

/**
 * Created by MisterY on 23.04.2016.
 */
public class CustomizableResideMenuItem extends ResideMenuItem {

    public CustomizableResideMenuItem(Context context) {
        super(context);
    }

    public CustomizableResideMenuItem(Context context, int icon, int title) {
        super(context, icon, title);
    }

    public CustomizableResideMenuItem(Context context, int icon, String title) {
        super(context, icon, title);
    }

    /**
     * Hack for the ResideMenu library to change items typeface
     *
     * @param typeface typeface to apply
     */
    public void setTypeface(Typeface typeface) {
        try {
            // getting type of the field from superClass
            Field privateTextView = ResideMenuItem.class.getDeclaredField("tv_title");
            // transform this field to public
            privateTextView.setAccessible(true);
            // getting value from this field which is reference to a TextView
            TextView tv = (TextView) privateTextView.get(this);
            //finaly setting the Typface


            if (Build.VERSION.SDK_INT < 23) {
                tv.setTextAppearance(getContext(), R.style.AppTheme);
                tv.setTextColor(getContext().getResources().getColor(R.color.text_color));

            } else {
                tv.setTextColor(getContext().getColor(R.color.text_color));
                tv.setTextAppearance(R.style.AppTheme);
            }
            tv.setTypeface(typeface);
            tv.setMaxLines(1);
            tv.setTextSize(getContext().getResources().getDimension(R.dimen.text_size_normal)/ getContext().getResources().getDisplayMetrics().density);
Log.d("Size", "" + getContext().getResources().getDimension(R.dimen.text_size_normal));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

}
