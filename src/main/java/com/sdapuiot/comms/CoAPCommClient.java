package com.sdapuiot.comms;


import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.WebLink;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CoAPCommClient {
    private static final String DEFAULT_PROTOCOL = "coap";
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 5683;
    private static final Logger _Logger = Logger.getLogger(MqttCommClient.class.getName());

    private String _protocol;
    private String _host;
    private int _port;
    private String _serverAddr;

    private CoapClient _client;

    public CoAPCommClient() {
        super();
        _protocol = DEFAULT_PROTOCOL;
        _host = DEFAULT_HOST;
        _port = DEFAULT_PORT;

        _serverAddr = _protocol + "://" + _host + ":" + _port;
        _Logger.info("CoAP Server address is : " + _serverAddr);

    }

    public void discover(String topic) {
        try {
            if (_client == null) {
                _client = new CoapClient(_serverAddr);
            } else {
                _client.setURI(_serverAddr + "/" + topic);
            }
            _Logger.info("Discovering all of it!");
            Set<WebLink> webLinkSet = _client.discover();
            for (WebLink link : webLinkSet
                    ) {
                _Logger.info(link.getURI());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void get(String topic) {
        try {
            if (_client == null) {
                _client = new CoapClient(_serverAddr);
            } else {
                _client.setURI(_serverAddr + "/" + topic);
            }
            _Logger.info("Getting " + topic);
            _Logger.info(_client.get().getResponseText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void post(String topic, String data) {
        try {
            if (_client == null) {
                _client = new CoapClient(_serverAddr);
            } else {
                _client.setURI(_serverAddr + "/" + topic);
            }
            _Logger.info("Posting " + topic + " with data " + data);
            _Logger.info(_client.post(data, MediaTypeRegistry.TEXT_PLAIN).getResponseText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void put(String topic, String data) {
        try {
            if (_client == null) {
                _client = new CoapClient(_serverAddr);
            } else {
                _client.setURI(_serverAddr + "/" + topic);
            }
            _Logger.info("Putting " + topic + " with data " + data);
            _Logger.info(_client.put(data, MediaTypeRegistry.TEXT_PLAIN).getResponseText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(String topic) {
        try {
            if (_client == null) {
                _client = new CoapClient(_serverAddr);
            } else {
                _client.setURI(_serverAddr + "/" + topic);
            }
            _Logger.info("Deleting " + topic);
            _Logger.info(_client.delete().getResponseText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
