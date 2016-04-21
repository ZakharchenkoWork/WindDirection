package ResultManager;

/**
 * Created by MisterY on 04.01.2016.
 */
public class SpeedResultBeaufort implements Result{

    /**
     * Converting speed from default knots, to Beaufort scale for displaying
     * @return speed in Beaufort
     */
    @Override
    public String getResult(float speedValue, String units) {


        for (int i = 0; i < TABLE_BEAUFORTS.length; i++)
        {
            if(speedValue <= TABLE_BEAUFORTS[i])
            {
                return "" + i + " " + units;
            }
        }

        return "" + TABLE_BEAUFORTS.length + " " + units;
    }
}
