package ResultManager;

import com.znshadows.auxilary.Utils;

/**
 * Created by MisterY on 04.01.2016.
 */
public class SpeedResultKnots implements Result{
    /**
     * Returns speed in Knots
     * @return speed in Knots
     */
    @Override
    public String getResult(float speedValue, String units) {

        return Utils.round(speedValue, 1) + " " + units;
    }
}
