package helpers;

import java.time.ZoneId;

/**
 * Function for Lambda to instantiate a system zone.
 */
public interface Zonify {

    ZoneId getDefZone(String zone);
}
