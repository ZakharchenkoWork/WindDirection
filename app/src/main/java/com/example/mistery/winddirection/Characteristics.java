package com.example.mistery.winddirection;


import android.util.Log;

import com.znshadows.auxilary.Math2DVector;

/**
 * Created by MisterY on 10.12.2015.
 */
public class Characteristics extends Math2DVector {


    private float course; //course in degrees
    private float speed; //speed in Knots

    //used for checks, is the objects is filled allready
    private boolean isCourseSet = false;
    private boolean isSpeedSet = false;


    /**
     * Use if Values are known in time of creating of the object
     *
     * @param course in Degrees
     * @param speed  in Knots
     */
    public Characteristics(float course, float speed) {
        super(course, speed);
        this.course = course;
        this.speed = speed;
        isCourseSet = true;
        isSpeedSet = true;
    }

    /**
     * Empty object, marked as nonfilled
     */
    public Characteristics() {
        super(0, 0);
        isCourseSet = false;
        isSpeedSet = false;
    }

    /**
     * in case of fill characteristics from vector
     *
     * @param vector
     */
    private Characteristics(Math2DVector vector) {
        super(vector);
        this.course = vector.getDirection();
        this.speed = vector.getLength();
        isCourseSet = true;
        isSpeedSet = true;
    }

    /**
     * for setting or changing course
     *
     * @param course in Degrees
     */
    public void setCourse(float course) {
        this.course = course;
        isCourseSet = true;
        if (isSpeedSet()) {
            setCoordinates(convertToCoordinates(course, speed));
        }
    }

    /**
     * for setting or changing speed
     *
     * @param speed in knots
     */
    public void setSpeed(float speed) {
        this.speed = speed;
        isSpeedSet = true;
        if (isCourseSet()) {
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
     * In case of any calculations Characteristics shold be ready
     *
     * @return true if course and speed were set already
     */
    public boolean isReady() {
        if (isCourseSet && isSpeedSet) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * For calculation of the true wind characteristics, from the values of the vessel's characteristics
     * and wind characteristics
     *
     * @param shipsCharacteristics prepared characteristics of Vessel
     * @param windCharacteristics  prepared characteristics of Wind
     * @return True Wind Characteristics
     */
    public static Characteristics calculateTrueWind(Characteristics shipsCharacteristics, Characteristics windCharacteristics) {

        Math2DVector trueWindVector = substractVectors(shipsCharacteristics, windCharacteristics);

        Log.d("Characteristics", "" + trueWindVector.toString());
        // Returning characteristics of the true wind
        return new Characteristics(trueWindVector);
    }
}
