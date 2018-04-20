package com.sdapuiot.comms;

import org.eclipse.californium.core.CoapServer;

import java.util.logging.Logger;

/**
 * @author Harsh Modi
 * This class is our CoAPServer implementation
 * it acts as our networking backbone or cloud
 * it also hanles the task of setting the mqtt client on the handler so handler is able to reuse the same MqttClient to send data of different topics
 */
public class CoAPCommServer {
    private static final Logger _Logger = Logger.getLogger(CoAPCommServer.class.getName());

    private CoapServer _server;
    //the Mqtt client which the handler uses to send data to the gateway
    private MqttCommClient _mqttClient;

    public CoAPCommServer() {
        this((String[]) null);
    }

    /**
     * @param resourceNames list of all the available resources
     *                      it'll create handler for all given resources and assign them same MqttClient which is use to send the data to gateway
     */
    public CoAPCommServer(String... resourceNames) {
        _server = new CoapServer();
        _Logger.info("Created server");
        _mqttClient = new MqttCommClient();
        _mqttClient.connect();//it's done outside for loop to avoid reconnecting and thus losing the connection
        if (resourceNames != null) {
            for (String resourceName : resourceNames) {
                CoAPToMqttResourceHandler cmrh = new CoAPToMqttResourceHandler(resourceName);
                cmrh.setMqttClient(_mqttClient);//reference of same object is assigned to each and every resourcehandler
                _server.add(cmrh);
                _Logger.info("Added server resource handler: " + cmrh.getURI());
            }
        }
        _server.start();//this is done here so that we don't have call the start method explicitly but for production use this will be moved to a separate method instead of implicit initialization
        _Logger.info("Server is up");
    }

    public void stop() {
        _server.stop();
        _Logger.info("Server is stopped");
    }
}
