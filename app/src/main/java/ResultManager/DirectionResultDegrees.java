package ResultManager;

import com.znshadows.auxilary.Utils;

/**
 * Created by MisterY on 04.01.2016.
 */
public class DirectionResultDegrees implements Result{
    /**
     *  returns direction in Degrees for displaying
     * @return degrees of direction
     */
    @Override
    public String getResult(float directionValue, String units) {
        return "" + Utils.formatAsDegrees(directionValue) + units;
    }
}
