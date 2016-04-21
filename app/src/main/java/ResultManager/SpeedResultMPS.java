package ResultManager;

import com.znshadows.auxilary.Utils;

/**
 * Created by MisterY on 04.01.2016.
 */
public class SpeedResultMPS implements Result{

    /**
     * Converting speed from default knots, to Meters per Seconds for displaying
     * @return speed in Kilometers per Hour
     */
    @Override
    public String getResult(float speedValue, String units) {
        float meters = speedValue * FACTOR_KNOTS_TO_METERS;
        return "" + Utils.round(meters, 1) + " " + units;
    }

}
