package com.eternity.mqttpahoutils.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.eternity.mqttpahoutils.MQTTPahoManager;
import com.eternity.mqttpahoutils.interfaces.IMQTT;

import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * Created by 27150 on 2017/12/18 0018.
 */

public class MQTTPahoService extends Service {
    private MQTTPahoManager mqttPahoManager;
    private MyBinder myBinder;

    public class MyBinder extends Binder {

        public void initMqtt(String url, String clientID, String userName, String password, String topics, IMQTT imqtt) {
            if (mqttPahoManager == null) {
                mqttPahoManager = new MQTTPahoManager(getApplicationContext(), url, clientID,
                        userName, password, topics, imqtt);
            }
        }

        public void initMqtt(String url, String clientID, String userName, String password, String[] topics, IMQTT imqtt) {
            if (mqttPahoManager == null) {
                mqttPahoManager = new MQTTPahoManager(getApplicationContext(), url, clientID,
                        userName, password, topics, imqtt);
            }
        }

        public boolean publishMsg(String topic, String msg) {
            if (mqttPahoManager != null) {
                mqttPahoManager.publishMessage(topic, msg, false);
                return true;
            } else {
                return false;
            }
        }

        public boolean subscribe(String topic) {
            if (mqttPahoManager != null) {
                try {
                    mqttPahoManager.subscribeTopics(topic);
                    return true;
                } catch (MqttException e) {
                    e.printStackTrace();
                    return false;
                }
            } else {
                return false;
            }
        }

        public boolean unsubscribe(String topic) {
            if (mqttPahoManager != null) {
                try {
                    mqttPahoManager.unsubscribeTopics(topic);
                    return true;
                } catch (MqttException e) {
                    e.printStackTrace();
                    return false;
                }
            } else {
                return false;
            }
        }

        public boolean isMQTTConnected() {
            if (mqttPahoManager != null) {
                return mqttPahoManager.isMQTTConnected();
            } else {
                return false;
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myBinder = new MyBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mqttPahoManager != null) {
            mqttPahoManager.disconnect();
        }
    }
}
