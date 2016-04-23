package com.example.mistery.winddirection;

import android.content.Context;
import android.graphics.Typeface;
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
     * @param typeface typeface to apply
     */
    public void setTypeface(Typeface typeface) {
        try {
            // getting type of the field from superClass
            Field privateTextView = ResideMenuItem.class.getDeclaredField("tv_title");
            // transform this field to public
            privateTextView.setAccessible(true);
            // getting value from this field which is reference to a TextView
            TextView tv = (TextView)privateTextView.get(this);
            //finaly setting the Typface
            tv.setTypeface(typeface);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

}
