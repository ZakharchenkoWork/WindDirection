package ResultManager;

/**
 * Created by MisterY on 04.01.2016.
 */
public class DirectionResultRumbs implements Result {
    /**
     * Converting direction from default degrees, to Rumbs for displaying
     * @return Rumb of direction
     */
    @Override
    public String getResult(float directionValue, String units) {
        float tempCourse = directionValue + FACTOR_DEGREES_TO_HALF_RUMB_INDEX;
        int index = (int)(tempCourse / FACTOR_DEGREES_TO_RUMB_INDEX) *2;
        return TABLE_RUMBS[index];
    }
}
