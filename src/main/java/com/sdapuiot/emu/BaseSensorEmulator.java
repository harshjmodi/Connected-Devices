package com.sdapuiot.emu;

import java.util.Random;
import java.util.TimerTask;
import java.util.logging.Logger;

public abstract class BaseSensorEmulator extends TimerTask implements ISensorEmulator {
    // static
    public static final float DEFAULT_MIN_THRESHOLD = 0.0f;
    public static final float DEFAULT_MAX_THRESHOLD = 100.0f;
    public static final float DEFAULT_DIFF_THRESHOLD = 10.0f;
    private static final Logger _Logger = Logger.getLogger(BaseSensorEmulator.class.getName());


    // private var's
    private Random _randomizer = null;
    private int _sampleCount = 0;
    private float _maxThresholdLimit = 0.0f;
    private float _minThresholdLimit = 0.0f;
    private float _minObsThreshold = 0.0f;
    private float _maxObsThreshold = 0.0f;
    private float _avgThreshold = 0.0f;
    private float _curThreshold = 0.0f;
    private double _sumThresholds = 0.0d;


    // constructors

    /*** Default.      **/
    public BaseSensorEmulator() {
        this(DEFAULT_MIN_THRESHOLD, DEFAULT_MAX_THRESHOLD);
    }

    /**
     * Constructor.
     * <p>
     * *
     *
     * @param min The
     *            min threshold
     *            allowed.      *
     * @param max The
     *            max threshold
     *            allowed.
     */

    public BaseSensorEmulator(float min, float max) {
        super();
        _randomizer = new Random();
        _minThresholdLimit = (min < 0.0f ? DEFAULT_MIN_THRESHOLD : min);
        _maxThresholdLimit = (max <= _minThresholdLimit ? _minThresholdLimit + DEFAULT_DIFF_THRESHOLD : max);
    }               // public methods


    public float getAverageThreshold() {
        return _avgThreshold;
    }


    public float getCurrentThreshold() {
        return _curThreshold;
    }


    public float getDefinedMaxThresholdLimit() {
        return _maxThresholdLimit;
    }


    public float getDefinedMinThresholdLimit() {
        return _minThresholdLimit;
    }


    public float getMaxObservedThreshold() {
        return _maxObsThreshold;
    }


    public float getMinObservedThreshold() {
        return _minObsThreshold;
    }

    @Override
    public void run() {
        generateData();
    }

    // protected methods

    /**
     * Generates a random float
     */


    protected void generateData() {
        ++_sampleCount;
        _curThreshold = generateSensorValue();


        // check if we've observed a new min or max threshold
        // NOTE: if this is the first sample, set both to the current threshold
        if (_sampleCount == 1) {
            _minObsThreshold = _curThreshold;
            _maxObsThreshold = _curThreshold;
        } else {
            if (_curThreshold < _minObsThreshold) {
                _minObsThreshold = _curThreshold;
            } else if (_curThreshold > _maxObsThreshold) {
                _maxObsThreshold = _curThreshold;
            }
        }


        // shouldn't need a double, but we'll use double precision to store the sum
        _sumThresholds += _curThreshold;
        // generate average - we'll also drop double precision by casting back to float
        _avgThreshold = (float) (_sumThresholds / _sampleCount);
    }


    /**
     * Generates a random value between the min and max threshold limits set at construction time.
     *
     * @return float
     */
    protected float generateSensorValue() {
        // select a random float between the min and max thresholds
        return _randomizer.nextFloat() * (_maxThresholdLimit - _minThresholdLimit) + _minThresholdLimit;
    }


    // private methods
}
