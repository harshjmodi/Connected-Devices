package com.sdapuiot;

import com.sdapuiot.comms.CoAPCommClient;
import com.sdapuiot.comms.CoAPCommServer;
import com.sdapuiot.comms.MqttCommClient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
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
    private MqttCommClient _mqttConnector;

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
        commServer.start();
//        coAPCommClient.get("Harsh");
//        coAPCommClient.get("Bhautik");
//        coAPCommClient.get("Ronak");
        coAPCommClient.post("Harsh", "Hiiii, How are you");
        coAPCommClient.post("Bhautik", "I'm good");
        coAPCommClient.post("Ronak", "Okay guys, see you then");
        commServer.stop();
    }

//    public void start() {
//        try {
//            _mqttConnector = new MqttCommClient();
//            if (_mqttConnector.connect()) {
//                int pressure = 15;
//                String gps = "long, lat + ipv6";
//                String time = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
//                String unit = "Pounds";
//                String msg = "{\n" +
//                        " \"Pressure\" : " + pressure + "\n" +
//                        " \"Location\" : " + "\"" + gps + "\"\n" +
//                        " \"Unit\" : " + "\"" + unit + "\" \n" +
//                        " \"Time\" : " + "\"" + time + "\"" + "\n" +
//                        "}";
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
