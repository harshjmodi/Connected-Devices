package com.sdapuiot.comms;

import org.eclipse.californium.core.CoapServer;

import java.util.logging.Logger;


public class CoAPCommServer {
    private static final Logger _Logger = Logger.getLogger(CoAPCommServer.class.getName());

    private CoapServer _server;
    private MqttCommClient _mqttClient;

    public CoAPCommServer() {
        this((String[]) null);
    }

    /**
     * @param resourceNames list of all the available resources
     */
    public CoAPCommServer(String... resourceNames) {
        _server = new CoapServer();
        _Logger.info("Created server");
        _mqttClient = new MqttCommClient();
        _mqttClient.connect();
        if (resourceNames != null) {
            for (String resourceName : resourceNames) {
                CoapToMqttResourceHandler cmrh = new CoapToMqttResourceHandler(resourceName);
                cmrh.setMqttClient(_mqttClient);
                _server.add(cmrh);
                _Logger.info("Added server resource handler: " + cmrh.getURI());
            }
        }
        _server.start();
        _Logger.info("Server is up");
    }

    public void stop() {
        _server.start();
        _Logger.info("Server is stopped");
    }
}
