package com.sdapuiot;

import com.sdapuiot.comms.CoAPCommClient;
import com.sdapuiot.comms.CoAPCommServer;
import com.sdapuiot.emu.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

/**
 * @author Harsh Modi
 * <p>
 * This is the main class tha instantiates and demo the MqttCommClient class
 */

public class SDAPIoT {

    //static
    private static final Logger _Logger = Logger.getLogger(SDAPIoT.class.getName());
    private static SDAPIoT _App;

    /**
     * @param args get commandline args in this variable
     */
    public static void main(String args[]) {
        _App = new SDAPIoT();
        _App.start();

    }

    public void start() {
        CoAPCommServer commServer = new CoAPCommServer("Harsh", "Bhautik", "Ronak");
        CoAPCommClient coAPCommClient = new CoAPCommClient();
        coAPCommClient.post("Harsh", "Dummy"); // use sensor.getData(); to get data and forward it
        PressureSensorEmulator pressureSensor = (PressureSensorEmulator) SensorEmulatorFactory.getInstance().createSensorEmulator(SensorTypeEnum.PRESSURE);
        GeolocationSensorEmulator geolocationSensor = (GeolocationSensorEmulator) SensorEmulatorFactory.getInstance().createSensorEmulator(SensorTypeEnum.GEOLOCATION);
        SpeedSensorEmulator speedSensor = (SpeedSensorEmulator) SensorEmulatorFactory.getInstance().createSensorEmulator(SensorTypeEnum.SPEED);
        float pressure;
        String gps;
        String unit;
        float speed;
        String msg = "";
        for (int i = 0; i < 10; i++) {
            speedSensor.run();
            geolocationSensor.run();
            pressureSensor.run();
            pressure = speedSensor.getCurrentThreshold();
            gps = speedSensor.getCurrentThreshold() + " + ipv6";
            unit = "Pounds";
            speed = speedSensor.getCurrentThreshold();
            String group = "Automotive efficiency";
            msg = "\n{\n" +
                    " \"group\" : " + group + ",\n" +
                    " \"breakpressure\" : " + pressure + ",\n" +
                    " \"speed\" : " + speed + ",\n" +
                    " \"location\" : " + "\"" + gps + "\",\n" +
                    " \"unit\" : " + "\"" + unit + "\",\n" +
                    " \"time\" : " + "\"" + new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + "\"" + "\n" +
                    "}";
            coAPCommClient.post("Harsh", msg); // use sensor.getData(); to get data and forward it
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            coAPCommClient.post("Bhautik", "I'm good"); // use sensor.getData(); to get data and forward it
//            coAPCommClient.post("Ronak", "Okay guys, see you then"); // use sensor.getData(); to get data and forward it
        }
        commServer.stop();
        _Logger.info("We're done here");
    }
}
