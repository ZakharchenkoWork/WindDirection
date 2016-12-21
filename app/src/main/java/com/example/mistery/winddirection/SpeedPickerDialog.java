package com.example.mistery.winddirection;

import android.content.Context;
import android.os.Build;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.znshadows.dialogs.PickerDialog;

/**
 * Created by MisterY on 28.12.2015.
 */
public class SpeedPickerDialog extends PickerDialog {

    private static final int NUMBER_OF_INTEGER_PICKERS = 2;
    private static final int NUMBER_OF_DECIMAL_PICKERS = 1;

    private static String innerResultUnits = " "; //will be used inside dialog only


    public SpeedPickerDialog(Context context, String dialogTitle, float startValue) {
        super(context, dialogTitle, startValue, NUMBER_OF_INTEGER_PICKERS, NUMBER_OF_DECIMAL_PICKERS);// 2 integers and 1 decimal picker

    }

    @Override
    protected void setUpStyle(NumberPicker[] integerNumberPickers, NumberPicker[] decimalNumberPickers, TextView result) {
        //all pickers will be from 0 to 9
        int color;
        if (Build.VERSION.SDK_INT < 23) {
            color = getContext().getResources().getColor(R.color.text_color);
        } else {
            color = getContext().getColor(R.color.text_color);
        }
        result.setTextColor(color);

        for (int i = 0; i < integerNumberPickers.length; i++)
        {
            integerNumberPickers[i].setMinValue(0);
            integerNumberPickers[i].setMaxValue(9);
            setNumberPickerTextColor(integerNumberPickers[i], color);
        }
        for (int i = 0; i < decimalNumberPickers.length; i++)
        {
            decimalNumberPickers[i].setMinValue(0);
            decimalNumberPickers[i].setMaxValue(9);
            setNumberPickerTextColor(decimalNumberPickers[i], color);
        }


    }


    @Override
    protected String getInnerResultString(float collectedValue) {
        //result inside the dialog will look like "10.9 Kn"
        return  collectedValue + " " + innerResultUnits;
    }

    /**
     * Sets the units of result value
     * @param units units to be added to the end of result inside dialog
     * @return this
     */
    public static void setInnerResultUnits(String units) {
        innerResultUnits = units;
    }

}
