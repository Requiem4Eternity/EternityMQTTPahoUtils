package com.eternity.mqttpahoutils;

import android.content.Context;
import android.support.annotation.Nullable;

import com.eternity.mqttpahoutils.interfaces.IMQTT;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by Eternity on 2017/11/23 0023.
 * 必须在project中的build.gradle中添加maven仓库
 * 必须在app中的build.gradle中添加依赖
 * implementation 'org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.0'
 * implementation 'org.eclipse.paho:org.eclipse.paho.android.service:1.1.1'
 * 需要权限：
 * <uses-permission android:name="android.permission.WAKE_LOCK" />
 **/
/*
 buildscript {
    repositories {
    //添加maven仓库：
        maven {
           url "https://repo.eclipse.org/content/repositories/paho-snapshots/"
         }
      }
 }
 */

public class MQTTPahoManager {
    private MqttAndroidClient mqttAndroidClient;
    private IMQTT imqtt;

    //检测连接状况
    public boolean isMQTTConnected() {
        if (mqttAndroidClient != null) {
            return mqttAndroidClient.isConnected();
        } else {
            return false;
        }
    }

    public MQTTPahoManager(final Context context, String serverUri, String clientId, @Nullable String userName, @Nullable String password, @Nullable final String subscribeTopics, final IMQTT imqtt) {
        this.imqtt = imqtt;
        mqttAndroidClient = new MqttAndroidClient(context.getApplicationContext(), serverUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                if (imqtt != null) {
                    imqtt.onConnectionLost(cause);
                }
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                if (imqtt != null) {
                    imqtt.onDeliveryComplete(token);
                }
            }
        });

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);
        if (userName != null && password != null) {
            mqttConnectOptions.setUserName(userName);
            mqttConnectOptions.setPassword(password.toCharArray());
        }
        try {
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    if (imqtt != null) {
                        imqtt.onConnectSuccess(asyncActionToken);
                    }
                    if (subscribeTopics != null) {
                        try {
                            subscribeTopics(subscribeTopics);
                        } catch (MqttException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    if (imqtt != null) {
                        imqtt.onConnectFailed(asyncActionToken, exception);
                    }
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public MQTTPahoManager(final Context context, String serverUri, String clientId, @Nullable String userName, @Nullable String password, @Nullable final String[] subscribeTopics, final IMQTT imqtt) {
        this.imqtt = imqtt;
        mqttAndroidClient = new MqttAndroidClient(context.getApplicationContext(), serverUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                if (imqtt != null) {
                    imqtt.onConnectionLost(cause);
                }
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                if (imqtt != null) {
                    imqtt.onDeliveryComplete(token);
                }
            }
        });

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);
        if (userName != null && password != null) {
            mqttConnectOptions.setUserName(userName);
            mqttConnectOptions.setPassword(password.toCharArray());
        }
        try {
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    if (imqtt != null) {
                        imqtt.onConnectSuccess(asyncActionToken);
                    }
                    if (subscribeTopics != null) {
                        for (int i = 0; i < subscribeTopics.length; i++) {
                            try {
                                subscribeTopics(subscribeTopics[i]);
                            } catch (MqttException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    if (imqtt != null) {
                        imqtt.onConnectFailed(asyncActionToken, exception);
                    }
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    //retained为true将会允许消息被多次消费
    public void publishMessage(String publishTopic, String msg, boolean retained) {
        if (mqttAndroidClient != null) {
            MqttMessage mqttMessage = new MqttMessage();
            mqttMessage.setPayload(msg.getBytes());
            mqttMessage.setRetained(retained);
            try {
                mqttAndroidClient.publish(publishTopic, mqttMessage);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    //断开连接
    public void disconnect() {
        if (mqttAndroidClient != null) {
            try {
                mqttAndroidClient.disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    //订阅消息
    public void subscribeTopics(String... topics) throws MqttException {
        if (mqttAndroidClient != null) {
            for (int i = 0; i < topics.length; i++) {
                mqttAndroidClient.subscribe(topics[i], 2, new IMqttMessageListener() {
                    @Override
                    public void messageArrived(final String topic, final MqttMessage message) throws Exception {
                        if (!message.isDuplicate()) {
                            if (imqtt != null) {
                                imqtt.onMsgArrived(topic, message);
                                final String msg = new String(message.getPayload());
                                imqtt.onMsgArrived(topic, msg);
                            }
                        }
                    }
                });
            }
        }
    }

    //反订阅消息
    public void unsubscribeTopics(String... topics) throws MqttException {
        if (mqttAndroidClient != null) {
            for (int i = 0; i < topics.length; i++) {
                try {
                    mqttAndroidClient.unsubscribe(topics[i]);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
