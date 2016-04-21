package ResultManager;

import com.znshadows.auxilary.Utils;

/**
 * Created by MisterY on 04.01.2016.
 */
public class SpeedResultKPH implements Result{
    /**
     * Converting speed from default knots, to Kilometers per Hour for displaying
     * @return speed in Kilometers per Hour
     */
    @Override
    public String getResult(float speedValue, String units) {

            float kilometers = speedValue * FACTOR_KNOTS_TO_KILOMETERS_PER_HOUR;
            return  "" + Utils.round(kilometers, 1) + " " + units;

    }
}
