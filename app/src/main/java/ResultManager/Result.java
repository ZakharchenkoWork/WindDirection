package ResultManager;

/**
 * Created by MisterY on 04.01.2016.
 */
public interface Result {
    float[] TABLE_BEAUFORTS = {0, 3, 6, 10, 16, 21, 27, 33, 40, 47, 55, 63};
    String[] TABLE_RUMBS = {"N","NbE", "NNE", "NEbN",
            "NE", "NEbE", "ENE", "EbN",
            "E", "EbS","ESE", "SEbE",
            "SE","SEbS", "SSE", "SbE",
            "S", "SbW", "SSW", "SWbS",
            "SW", "SWbW", "WSW", "WbS",
            "W", "WbN",  "WNW","NWbW",
            "NW", "NWbN","NbW"};

    float FACTOR_DEGREES_TO_QAUTER_RUMB_INDEX = 5.625f;
    float FACTOR_DEGREES_TO_HALF_RUMB_INDEX = 11.25f;
    float FACTOR_DEGREES_TO_RUMB_INDEX = 22.5f;
    float FACTOR_KNOTS_TO_METERS = 0.5144444f;
    float FACTOR_KNOTS_TO_KILOMETERS_PER_HOUR = 1.852f;
    float FACTOR_KNOTS_TO_MILES_PER_HOUR = 1.150779f;

    String getResult(float directionValue, String units);
}
