package com.sdapuiot;

import com.sdapuiot.comms.CoAPCommClient;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Harsh Modi
 * <p>
 * This is the main class tha instantiates and demo the CoAPCommClient class
 */

public class SDAPCoAP {

    //static
    private static final Logger _Logger = Logger.getLogger(SDAPCoAP.class.getName());
    private static SDAPCoAP _App;
    private CoAPCommClient _coapConnector;

    /**
     * @param args get commandline args in this variable
     */
    public static void main(String args[]) {
        _App = new SDAPCoAP();
        _App.start();

    }

    public void start() {
        try {
            _coapConnector = new CoAPCommClient();
            _coapConnector.get("test");
            _coapConnector.post("test", "Testing 123,123,123");
            _coapConnector.put("test", "Testing 123,123,123");
            _coapConnector.delete("test");
        } catch (Exception e) {
            _Logger.log(Level.SEVERE, e.toString());
        }
    }

}
