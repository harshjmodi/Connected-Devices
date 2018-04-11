package com.sdapuiot.emu;

public interface ISensorEmulator extends Runnable {
    /**
     * Returns the average threshold value. This will be the average of
     * all sampled values taken from the sensor (or emulator).
     *
     * @return float
     */
    float getAverageThreshold();

    /**
     * Returns the current threshold value. This will be the latest sampled
     * value from the sensor (or emulator).
     *
     * @return float
     */
    float getCurrentThreshold();

    /**
     * Returns the maximum threshold limit set at construction time.
     *
     * @return float
     */
    float getDefinedMaxThresholdLimit();

    /**
     * Returns the minimum threshold limit set at construction time.
     *
     * @return float
     */
    float getDefinedMinThresholdLimit();

    /**
     * Returns the maximum threshold value observed.
     *
     * @return float
     */
    float getMaxObservedThreshold();

    /**
     * Copyright © 2018. Andrew D. King. All rights reserved. P a g e | 5
     * Returns the minimum threshold value observed.
     *
     * @return float
     */
    float getMinObservedThreshold();
}
