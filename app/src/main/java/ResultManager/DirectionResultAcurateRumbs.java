package ResultManager;

/**
 * Created by MisterY on 04.01.2016.
 */
public class DirectionResultAcurateRumbs implements Result {
    /**
     * Converting direction from default degrees, to Acurate Rumbs for displaying
     * @return Rumb of direction
     */
    @Override
    public String getResult(float directionValue, String units) {
        float tempCourse = directionValue + FACTOR_DEGREES_TO_QAUTER_RUMB_INDEX;
        int index = (int)(tempCourse / FACTOR_DEGREES_TO_HALF_RUMB_INDEX);
        return TABLE_RUMBS[index];
    }
}
