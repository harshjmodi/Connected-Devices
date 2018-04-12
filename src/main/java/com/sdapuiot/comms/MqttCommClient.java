package com.sdapuiot.comms;


import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Harsh Modi
 * <p>
 * This class implements Mqtt function for callback
 */
public class MqttCommClient implements MqttCallback {
    private static final String DEFAULT_PROTOCOL = "tcp";
    private static final String DEFAULT_HOST = "test.mosquitto.org"; // we're using this host as out broker
    private static final int DEFAULT_PORT = 1883;
    private static final Logger _Logger = Logger.getLogger(MqttCommClient.class.getName()); // logger to log our every step for debugging

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
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            _client = new MqttClient(_brokerAddr, _clientID, persistence);
            _end = new MqttClient(_brokerAddr, _endID, persistence);
            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.setCleanSession(true);
            _client.setCallback(this);
            _end.setCallback(this);
            _client.connect(connectOptions);
            _end.connect(connectOptions);
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

            _client.subscribeWithResponse(topic);
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

    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        _Logger.info("Message arrived: " + mqttMessage.toString());
        _end.publish("Action", mqttMessage);
        _Logger.info("Sent to sensor");
    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        _Logger.info("Message delivered with token " + iMqttDeliveryToken.toString());
    }
}
