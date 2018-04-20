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
        CoAPCommServer commServer = new CoAPCommServer("Harsh", "Bhautik", "Ronak");// this will run on server in real application
        CoAPCommClient coAPCommClient = new CoAPCommClient(); // this will run on sensor
        coAPCommClient.post("Harsh", "Dummy"); // I had to send dummy data first because for some reason first message is going blank
        //create different sensors to emulate different sensors
        PressureSensorEmulator pressureSensor = (PressureSensorEmulator) SensorEmulatorFactory.getInstance().createSensorEmulator(SensorTypeEnum.PRESSURE,0,80);
        GeolocationSensorEmulator geolocationSensor = (GeolocationSensorEmulator) SensorEmulatorFactory.getInstance().createSensorEmulator(SensorTypeEnum.GEOLOCATION, 10, 200);
        SpeedSensorEmulator speedSensor = (SpeedSensorEmulator) SensorEmulatorFactory.getInstance().createSensorEmulator(SensorTypeEnum.SPEED);
        float pressure; // these variables can be made inline to send data directly
        String gps;
        String unit;
        float speed;
        String msg = "";
        for (int i = 0; i < 10; i++) {
            speedSensor.run();// run it every time to generate random values
            geolocationSensor.run();
            pressureSensor.run();
            pressure = speedSensor.getCurrentThreshold();
            gps = speedSensor.getCurrentThreshold() + " + ipv6";
            unit = "Pounds";
            speed = speedSensor.getCurrentThreshold();
            String group = "Automotive efficiency";
            //embed the generated data into JSON format and send it to network
            msg = "\n{\n" +
                    " \"group\" : " + group + ",\n" +
                    " \"breakpressure\" : " + pressure + ",\n" +
                    " \"speed\" : " + speed + ",\n" +
                    " \"location\" : " + "\"" + gps + "\",\n" +
                    " \"unit\" : " + "\"" + unit + "\",\n" +
                    " \"time\" : " + "\"" + new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + "\"" + "\n" +
                    "}";
            coAPCommClient.post("Harsh", msg);
            try {
                //delay is must to make sure that data is completely traversed and doesn't exhaust sensor resources
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//          coAPCommClient.post("Bhautik", "I'm good"); // use sensor.getData(); to get data and forward it
//          coAPCommClient.post("Ronak", "Okay guys, see you then"); // use sensor.getData(); to get data and forward it
        }
        commServer.stop();
        _Logger.info("We're done here");
    }
}
