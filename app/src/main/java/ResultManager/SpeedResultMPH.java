package ResultManager;

import com.znshadows.auxilary.Utils;

/**
 * Created by MisterY on 04.01.2016.
 */
public class SpeedResultMPH implements Result{
    /**
     * Converting speed from default knots, to Miles per Hour for displaying
     * @return speed in Miles per Hour
     */
    @Override
    public String getResult(float speedValue, String units) {
        float miles = speedValue * FACTOR_KNOTS_TO_MILES_PER_HOUR;
        return "" + Utils.round(miles, 1) + " " + units;
    }

}
