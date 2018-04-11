package com.sdapuiot.emu;

import java.util.logging.Logger;

public class SensorEmulatorFactory {

    // static
    private static final Logger _Logger = Logger.getLogger(SensorEmulatorFactory.class.getName());
    private static SensorEmulatorFactory _Instance = new SensorEmulatorFactory();

    /**
     * Constructor.
     */
    private SensorEmulatorFactory() {
        super();
    }
    // constructors

    /**
     * Singleton accessor – returns the singleton instance of this factory.
     */
    public static SensorEmulatorFactory getInstance() {
        return _Instance;
    }

    // public methods

    /**
     * Create a new instance of the specified sensor emulator.
     *
     * @return ISensorEmulator
     */
    public ISensorEmulator createSensorEmulator(SensorTypeEnum type) {
        ISensorEmulator emulator = null;

        switch (type)

        {
            case HUMIDITY:
                emulator = new HumiditySensorEmulator();
        }
        return emulator;
    }

    /**
     * Create a new instance of the specified sensor emulator.
     *
     * @return ISensorEmulator
     */
    public ISensorEmulator createSensorEmulator(SensorTypeEnum type, float min, float max) {
        ISensorEmulator emulator = null;
        switch (type) {
            case HUMIDITY:
                emulator = new HumiditySensorEmulator(min, max);
        }
        return emulator;
    }
}
