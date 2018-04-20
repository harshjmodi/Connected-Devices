package com.sdapuiot.comms;


import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Harsh Modi
 * This is our class to handle both mqtt clinets which are gateway and the actuator
 * _client represents gateway and _end represents the actuator device
 */
public class MqttCommClient implements MqttCallback {
    //defaults
    private static final String DEFAULT_PROTOCOL = "tcp";
    private static final String DEFAULT_HOST = "test.mosquitto.org"; // we're using this host as out broker
    private static final int DEFAULT_PORT = 1883;
    private static final Logger _Logger = Logger.getLogger(MqttCommClient.class.getName()); // logger to log our every step for debugging

    //each mqtt instance must have different clientID otherwise the broker will desconnect on more than one connection from same id
    private String _clientID;
    private String _endID;

    private String _protocol;
    private String _host;
    private int _port;
    private String _brokerAddr;

    private MqttClient _client;
    private MqttClient _end;

    public MqttCommClient() {
        super();
        _clientID = MqttClient.generateClientId();
        _endID = MqttClient.generateClientId();
        _protocol = DEFAULT_PROTOCOL;
        _host = DEFAULT_HOST;
        _port = DEFAULT_PORT;

        _brokerAddr = _protocol + "://" + _host + ":" + _port;
        _Logger.info("Broker address: " + _brokerAddr);
    }

    public boolean connect() {
        boolean success = false;
        //persistence is to avoid reconnecting every time and to be able to reuse same connection
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            _client = new MqttClient(_brokerAddr, _clientID, persistence);
            _end = new MqttClient(_brokerAddr, _endID, persistence);
            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.setCleanSession(true);
            _client.setCallback(this);
            _end.setCallback(this);
            //connection is established here to avoid reconnecting evetytime from setMqttConnection();
            _client.connect(connectOptions);
            _end.connect(connectOptions);
            //this is Bhautik topic
            _client.subscribe("devanshi");//this is to demonstrate the integration I'm subscribing to bhautics topic and will receive his messages
            success = true;
            _Logger.info("Client is created");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    public boolean sendMessage(String topic, int qoslevel, byte[] msg) {
        try {

            MqttMessage message = new MqttMessage(msg);
            message.setQos(qoslevel);
            //I'm subscribing to my own topic so that message will bounce back to me
            // this way I'll be able to act as gateway itself and after processing the data and performing analytics I'll forward the action to be taken to my actuator(enforcer component)
            _client.subscribeWithResponse("Harsh");
            _client.publish(topic, message);
            _Logger.info("Message published: " + message);
            _client.unsubscribe(topic);
            return true;
        } catch (MqttException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean disconnect() {
        boolean success = false;

        try {
            _client.disconnect();
            _Logger.info("Disconnected from broker: " + _brokerAddr);
            success = true;
        } catch (Exception e) {
            _Logger.log(Level.SEVERE, "Failed to disconnect from broker: " + _brokerAddr);
        }
        return success;
    }

    public void connectionLost(Throwable throwable) {
        _Logger.info("Connection is lost because of : " + throwable.toString());
    }

    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        _Logger.info("Message arrived: " + mqttMessage.toString());// this is the message that will reach the gateway
        //now I'll process the data and send what action do I need to take to the actuator
        _Logger.info("Acting on data");
        //however for demonstration purpose I'll calculate the probability of accident and send it to bhautik, this is done and he's getting the message
        _end.publish("HarshAction", new MqttMessage(analytics(mqttMessage.toString(),topic).getBytes()));
        _Logger.info("Sent to sensor");
    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        _Logger.info("Message delivered with token " + iMqttDeliveryToken.toString());
    }

    public String analytics(String data, String topic) {
        if(topic.equals("Harsh")){
            JSONObject object = new JSONObject(data);
            String result = "";
            Float speed = object.getFloat("speed");
            Float pressure = object.getFloat("speed");
            if (speed > 100 && pressure > 50) {
                result = "0.80";
            } else if (speed > 100) {
                result = ".75";
            } else if (pressure > 50) {
                result = ".70";
            } else {
                result = ".25";
            }
            return result;
        } else if (topic.equals("devanshi")){
            //this messages from bhautik are string messages in his own format so I'm looking out for exception in case he might change the format
            //so I'm processing his data on catch
            try{
                ArrayList<Integer> numbers = new ArrayList<Integer>();
                String[] arr1 = data.trim().split(", ");
                float temp = Float.parseFloat(arr1[1].split(" ")[1].replaceAll("C",""));
                // I'm parsing temperature from his message and based on the temperature I'll print one message or another
                if (temp < -1 && temp > -20) {
                    System.out.println("Weather is too cold!!");
                } else {
                    System.out.println("Lovely weather!!");
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        else {
            return "";
        }
        return "";
    }
}