package com.sdapuiot.comms;

import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.network.config.NetworkConfig;

import java.net.InetSocketAddress;
import java.util.logging.Logger;


public class CoAPCommServer {
    private static final Logger _Logger = Logger.getLogger(CoAPCommServer.class.getName());
    private static final int COAP_PORT = NetworkConfig.getStandard().getInt(NetworkConfig.Keys.COAP_PORT);

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

        _mqttClient = new MqttCommClient();
        if (resourceNames != null) {
            for (String resourceName : resourceNames) {
                CoapToMqttResourceHandler cmrh = new CoapToMqttResourceHandler(resourceName);
                cmrh.setMqttClient(_mqttClient);
                _server.add(cmrh);
                _Logger.info("Adding server resource handler: " + cmrh.getURI());
            }
        }
    }

    public void start(){
        _server.start();
    }

    public void stop(){
        _server.start();
    }
}
