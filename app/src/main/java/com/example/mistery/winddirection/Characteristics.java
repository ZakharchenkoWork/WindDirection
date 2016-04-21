package com.example.mistery.winddirection;


import android.util.Log;

import com.znshadows.auxilary.Math2DVector;

/**
 * Created by MisterY on 10.12.2015.
 */
public class Characteristics extends Math2DVector{


    private float course;
    private float speed;

    private boolean isCourseSet = false;
    private boolean isSpeedSet = false;



    public Characteristics(float course, float speed) {
        super(course,speed);
        this.course = course;
        this.speed = speed;
        isCourseSet = true;
        isSpeedSet = true;
    }

    public Characteristics() {
        super(0,0);
        isCourseSet = false;
        isSpeedSet = false;
    }
    private Characteristics(Math2DVector vector) {
        super(vector);
        this.course = vector.getDirection();
        this.speed = vector.getLength();
        isCourseSet = true;
        isSpeedSet = true;
    }
    public void setCourse(float course) {
        this.course = course;
        isCourseSet = true;

        if(isSpeedSet())
        {
            setCoordinates(convertToCoordinates(course, speed));
        }
    }

    public void setSpeed(float speed) {
        this.speed = speed;
        isSpeedSet = true;
        if(isCourseSet())
        {
            setCoordinates(convertToCoordinates(course, speed));
        }
    }

    public float getCourse() {
        return course;
    }

    public float getSpeed() {
        return speed;
    }

    public boolean isCourseSet() {
        return isCourseSet;
    }

    public boolean isSpeedSet() {
        return isSpeedSet;
    }


    /**
     * if course and speed were set, then Charasteristic is ready
     * @return true if course and speed were set
     */
    public boolean isReady() {
        if(isCourseSet && isSpeedSet)
        {return true;}
        else {
            return false;
        }
    }


    /**
     * @param shipsCharacteristics prepared characteristics of Vessel
     * @param windCharacteristics prepared characteristics of Wind
     * @return True Wind Characteristics
     */
    public static Characteristics calculateTrueWind(Characteristics shipsCharacteristics, Characteristics windCharacteristics) {

        Math2DVector trueWindVector = substractVectors(shipsCharacteristics, windCharacteristics);

        Log.d("Characteristics", "" + trueWindVector.toString());
        // Returning characteristics of the true wind
        return new Characteristics(trueWindVector);
    }
}
