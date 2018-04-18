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
        for(int i=0;i<10;i++){
            speedSensor.run();
            geolocationSensor.run();
            pressureSensor.run();
            float pressure = speedSensor.getCurrentThreshold();
            String gps = speedSensor.getCurrentThreshold() + " + ipv6";
            String time = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            String unit = "Pounds";
            float speed = speedSensor.getCurrentThreshold();
            String msg = "{\n" +
                    " \"BreakPressure\" : " + pressure + "\n" +
                    " \"Speed\" : " + "\"" + speed + "m/s" + "\"" + "\n" +
                    " \"Location\" : " + "\"" + gps + "\"\n" +
                    " \"Unit\" : " + "\"" + unit + "\" \n" +
                    " \"Time\" : " + "\"" + time + "\"" + "\n" +
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

//    public void start() {
//        try {
//            _mqttConnector = new MqttCommClient();
//            if (_mqttConnector.connect()) {

//                String topic = "SDAP";
//                int qos = 2;
//                _mqttConnector.sendMessage(topic, qos, msg);
//            } else {
//                _Logger.log(Level.WARNING, "Failed to discover to broker. ");
//            }
//        } catch (Exception e) {
//            _Logger.log(Level.SEVERE, "Failed to start app.", e);
//        } finally {
//            if (_mqttConnector.disconnect()) {
//                _Logger.info("Done");
//            } else {
//                _Logger.log(Level.WARNING, "Failed to disconnect from broker. ");
//            }
//        }
//
//    }

}
