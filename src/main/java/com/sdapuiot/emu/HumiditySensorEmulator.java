package com.sdapuiot.emu;

public class HumiditySensorEmulator extends BaseSensorEmulator {

    // static
    // private var's
    // constructors

    /**
     * Default.
     */
    public HumiditySensorEmulator() {
        super();
    }

    /**
     * Constructor.
     *
     * @param min The min threshold allowed.
     * @param max The max threshold allowed.
     */

    public HumiditySensorEmulator(float min, float max) {
        super(min, max);
    }


    // public methods
    // protected methods


    // NOTE: The implementation will likely benefit from the creation of an
    // overridden version of generateSensorValue(), which returns a float
    // specific to this specific emulator (e.g. Humidity). This entire
    // infrastructure concept could be used to generalize access to a real     // humidity sensor, for example.

    // private methods
}
