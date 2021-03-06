package com.sdapuiot.comms;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.server.resources.CoapExchange;

import java.util.logging.Logger;

/**
 * @author Harsh Modi
 * This class it responsible for handling response to all the POST call to the server for given resources
 */
public class CoAPToMqttResourceHandler extends CoapResource {
    public static final int DEFAULT_QOS_LEVEL = 2;
    private static final Logger _Logger = Logger.getLogger(CoAPCommServer.class.getName());
    //every handler object has it's own mqtt reference so that we can use different mqtt clients if desired
    private MqttCommClient _mqttClient;

    /**
     * @param name Name of the new resource
     */

    public CoAPToMqttResourceHandler(String name) {
        super(name);
    }

    /**
     * @param name    Name of the resources
     * @param visible Indicate if it's visible or not
     *                if this parameter is set to false then it won't show up in discover call
     */
    public CoAPToMqttResourceHandler(String name, boolean visible) {
        super(name, visible);
    }

    /**
     * @param context this object contains all the contextual data about given request
     *                We'll use our mqttclient reference in this method to forward data to the gateway
     */
    @Override
    public void handlePOST(CoapExchange context) {
        context.accept();
        if (_mqttClient.sendMessage(context.getRequestOptions().getUriPathString(), DEFAULT_QOS_LEVEL, context.getRequestPayload())) {
            context.respond(ResponseCode.CREATED, "Message is completely traversed");
        } else {
            context.respond(ResponseCode.SERVICE_UNAVAILABLE, "Oops - can't create content.");
            _Logger.warning("Failed to publish message to the gateway.");
        }

    }

    /**
     * @param mqttClient share the mqtt reference to given resource handler
     */
    public void setMqttClient(MqttCommClient mqttClient) {
        if (mqttClient != null) {
            _mqttClient = mqttClient;
        }
    }
}
