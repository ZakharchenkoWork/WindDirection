package com.znshadows.auxilary;

/**
 * Created by MisterY on 04.01.2016.
 */
public class Math2DVector {

    float coordX = 0;
    float coordY = 0;

    public Math2DVector(float[] coordinates) {
            this.coordX = coordinates[0];
            this.coordY = coordinates[1];
    }

    public Math2DVector(float direction, float length) {
            float[] coordinates = convertToCoordinates(direction,length);
            setCoordinates(coordinates);
    }
    protected Math2DVector(Math2DVector math2dVector) {
        this.coordX = math2dVector.coordX;
        this.coordY = math2dVector.coordY;
    }

    public void setCoordinates(float[] coordinates) {
        this.coordX = coordinates[0];
        this.coordY = coordinates[1];
    }


    public float[] getCoordinates() {
    return new float[] {coordX, coordY};
    }

    /**
     * Prepare coordinates of vector for direction and length
     * @param direction direction of movement of object in degrees
     * @param length of the vector
     * @return coordinates of vector
     */
    public float[] convertToCoordinates(float direction, float length) {
            // Converting direction to Radians as Math works with them
        float courseInRads = (float) (direction * Math.PI / 180f);

            // Getting X coordinates for vector
        float tempPosX = (float) (length * Math.cos( courseInRads ));

        // Getting Y coordinates for vector
        float tempPosY =  (float) (length * Math.sin(courseInRads));

        // Returns result as array
        return new float[]{tempPosX,tempPosY};
    }

    /**
     * Subtraction of the vectors
     * @return new vector which contains resulting vector
     */
    public static Math2DVector substractVectors(Math2DVector firstVector, Math2DVector secondVector) {

        // Substraction of coordinates
        float[] coordinates = new float[] { (secondVector.coordX - firstVector.coordX), (secondVector.coordY - firstVector.coordY)};

        return new Math2DVector( coordinates );
    }



    /**
     * Length of this Math2DVector
     * @return length calculated from vectors coordinates
     */
    public float getLength() {
        // Length of vector = sqrt(x*x + y*y)
        return (float) Math.hypot(coordX, coordY);
    }


    /**
     * Direction of this Math2DVector
     * @return direction calculated from vectors coordinates
     */
    public float getDirection( ) {

        // Getting angle
        float directionInRads = (float) Math.acos (  coordY / getLength() );
        // Converting radians to degrees
        float direction =  (float) (  directionInRads * 180 / Math.PI) ;
        // Reversing of acos result if wind is blowing from port side
        if(coordX < 0){ direction = 180 + ( 180 - direction );}
        // Magical turning of angle to acquire 0 at N otherwise it is on E
        direction = 450 - direction;
        // Returning back to 0 - 360 degrees range
        direction = direction % 360;
        return direction;

    }

    @Override
    public String toString() {
        return "X: " + coordX + " Y: " + coordY + " length: " + getLength() + " Direction: " + getDirection();
    }
}
