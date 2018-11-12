package com.eternity.mqttpahoutils.interfaces;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by 27150 on 2017/11/23 0023.
 */

public interface IMQTT {
    void onConnectSuccess(IMqttToken asyncActionToken);
    void onConnectFailed(IMqttToken asyncActionToken, Throwable exception);
    void onConnectionLost(Throwable cause);
    void onMsgArrived(final String topic, final MqttMessage message);
    void onMsgArrived(String topic, String msg);
    void onDeliveryComplete(IMqttDeliveryToken iMqttDeliveryToken);
}
