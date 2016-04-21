package com.znshadows.auxilary;

import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;

/**
 * Created by MisterY on 04.01.2016.
 */
public class Utils {
    /**
     * Round value 0 to get integer, max value is 4
     * @param value value to round
     * @param decimalPointPrecision number of digits after point
     * @return rounded value
     */
    public static float round(float value, @IntRange(from=0,to=4) int decimalPointPrecision) {
        value += 0.0001f;
        if(decimalPointPrecision > 4)
        {decimalPointPrecision = 4;}

        int integer = (int)value;
        int mantis = (int)( (value - (int)value) *Math.pow(10, decimalPointPrecision));

        String result = "";
        result = "" + integer;
        if(mantis != 0)
        {
            result +=".";
            String mantisString = "" + mantis;
            for (int i = 0; i < decimalPointPrecision && i < mantisString.length(); i++)
            {
                result += mantisString.charAt(i);
            }
        }
        return Float.parseFloat(result);
    }
    /**
     * Converting course from float to correct String
     * @param direction value to convert
     * @return String value of Course int from 0 to 359
     */
    @FloatRange(from=0.0,to=359.9)
    public static String formatAsDegrees(float direction) {
        String resultValue = "";
        if(direction < 10) { //to get result that looks like 003
            resultValue += "00";
        }else if (direction < 100) { //to get result that looks like 043
            resultValue += "0";
        }
        //assemble result //return 359
        return resultValue + (int)round(direction, 0);
    }
}
