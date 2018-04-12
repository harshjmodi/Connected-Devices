package com.sdapuiot.comms;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.server.resources.CoapExchange;

import java.util.logging.Logger;


public class CoapToMqttResourceHandler extends CoapResource {
    public static final int DEFAULT_QOS_LEVEL = 2;
    private static final Logger _Logger = Logger.getLogger(CoAPCommServer.class.getName());

    private MqttCommClient _mqttClient;

    /**
     * @param name Name of the new resource
     */
    public CoapToMqttResourceHandler(String name) {
        super(name);
    }

    /**
     * @param name Name of the resources
     * @param visible Indicate if it's visible or not
     */
    public CoapToMqttResourceHandler(String name, boolean visible) {
        super(name, visible);
    }


    @Override
    public void handleGET(CoapExchange context) {
        // TODO: handle GET as appropriate
        String responseMsg = "This is a generic response to the GET request for path: " + super.getName();
        context.accept();

        context.respond(responseMsg);
        _Logger.info("Handling GET: " + responseMsg);
        _Logger.info(context.getRequestCode().toString() + ": " + context.getRequestText());
    }

    @Override
    public void handlePOST(CoapExchange context) {
        // TODO: handle POST as appropriate
        try {
            context.accept();
            if (_mqttClient.sendMessage(context.getRequestOptions().getUriPathString(), DEFAULT_QOS_LEVEL, context.getRequestPayload())) {
                context.respond(ResponseCode.CREATED, "Created content.");
            } else {
                context.respond(ResponseCode.SERVICE_UNAVAILABLE, "Oops - can't create content.");
                _Logger.warning("Failed to publish message to MQTT broker.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            _Logger.warning("Failed to handle POST!");
        }
    }

    public void setMqttClient(MqttCommClient mqttClient) {
        if (mqttClient != null) {
            _mqttClient = mqttClient;
            _mqttClient.connect();
        }
    }
}
