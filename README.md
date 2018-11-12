# EternityMQTTPahoUtils
用于安卓客户端的MQTT工具

使用方法：
0、导入MQTT Paho库：

  在项目的Build.gradle中添加：
  implementation 'org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.0'
  implementation 'org.eclipse.paho:org.eclipse.paho.android.service:1.1.1'
  
  buildscript {
    repositories {
        maven {
           url "https://repo.eclipse.org/content/repositories/paho-snapshots/"
         }
      }
 }
 
  具体版本号以MQTT Paho Wiki为准（https://github.com/eclipse/paho.mqtt.android）
  
1、在Android Studio中点击File->New->Import module，导入这个库
2、添加权限：
<uses-permission android:name="android.permission.WAKE_LOCK" />

示例代码：
* 创建MQTTPahoManager对象并进行连接：
  IMQTT imqtt=        new IMQTT(){
            @Override
            public void onConnectSuccess(IMqttToken asyncActionToken) {
                //TODO:连接成功
            }

            @Override
            public void onConnectFailed(IMqttToken asyncActionToken, Throwable exception) {
                //TODO:连接失败
            }

            @Override
            public void onConnectionLost(Throwable cause) {
                //TODO:连接丢失
            }

            @Override
            public void onMsgArrived(String topic, MqttMessage message) {
                //TODO:收到消息（MqttMessage类型）
            }

            @Override
            public void onMsgArrived(String topic, String msg) {
                //TODO:收到消息（已转换成String类型）
            }

            @Override
            public void onDeliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                //TODO:消费完成
            }
        };

  new MQTTPahoManager(Context context, String serverUri, String clientId, @Nullable String userName, @Nullable String password, @Nullable final String subscribeTopics, IMQTT imqtt);
