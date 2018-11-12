package com.eternity.mqttpahoutils.service.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.eternity.mqttpahoutils.interfaces.IMQTT;
import com.eternity.mqttpahoutils.service.MQTTPahoService;

import static android.content.Context.BIND_AUTO_CREATE;

public class MQTTPahoServUtils {
    private ServiceConnection serviceConnection;
    private MQTTPahoService.MyBinder mqttBinder;

    public MQTTPahoServUtils(Context context, final String url, final String clientID, final String userName, final String password, final String topics, final IMQTT imqtt) {
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                mqttBinder = (MQTTPahoService.MyBinder) iBinder;
                mqttBinder.initMqtt(url, clientID, userName, password, topics, imqtt);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                mqttBinder = null;
            }
        };
        Intent intent = new Intent(context, MQTTPahoService.class);
        context.bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    public MQTTPahoServUtils(Context context, final String url, final String clientID, final String userName, final String password, final String[] topics, final IMQTT imqtt) {
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                mqttBinder = (MQTTPahoService.MyBinder) iBinder;
                mqttBinder.initMqtt(url, clientID, userName, password, topics, imqtt);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                mqttBinder = null;
            }
        };
        Intent intent = new Intent(context, MQTTPahoService.class);
        context.bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    public boolean publishMsg(String topic, String msg) {
        if (mqttBinder != null) {
            return mqttBinder.publishMsg(topic, msg);
        } else {
            return false;
        }
    }

    public boolean subscribe(String topic) {
        if (mqttBinder != null) {
            return mqttBinder.subscribe(topic);
        } else {
            return false;
        }
    }

    public boolean unsubscribe(String topic) {
        if (mqttBinder != null) {
            return mqttBinder.unsubscribe(topic);
        } else {
            return false;
        }
    }

    public boolean isMQTTConnected() {
        if (mqttBinder != null) {
            return mqttBinder.isMQTTConnected();
        } else {
            return false;
        }
    }

    public void shutDown(Context context) {
        if (serviceConnection != null) {
            try {
                context.unbindService(serviceConnection);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
