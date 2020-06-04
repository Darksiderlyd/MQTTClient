package lyd.demo.mqtt;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * @package lyd.demo.mqtt
 * @user by lvyaodong
 * @date 2020-06-03
 * @email 1126220529@qq.com
 */
public class IOT {
    private final String TAG = "MyIOT";

    private Context mContext;

    /* 用于上报消息 */
    final private String PUB_TOPIC = "/message/publish";
    /* 用于接受消息 */
    final private String SUB_TOPIC = "/message/publish";
    /* 自己部署到本地的服务器地址（协议+地址+端口号） */
    private final String HOST = "tcp://192.168.1.94:1883";
    /* 用户名 */
    private String USERNAME = "admin";
    /* 密码 */
    private String PASSWORD = "admin";

    private MqttAndroidClient mqttAndroidClient;

    private static final class IOTHolder {
        private static final IOT instance = new IOT();
    }

    public static IOT getInstance() {
        return IOT.IOTHolder.instance;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context.getApplicationContext();
        connect();
    }

    /**
     * 订阅特定的主题
     *
     * @param topic mqtt主题
     */
    public void subscribeTopic(String topic) {
        try {
            mqttAndroidClient.subscribe(topic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i(TAG, "subscribed succeed");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i(TAG, "subscribed failed");
                }
            });

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 向默认的主题/user/update发布消息
     *
     * @param payload 消息载荷
     */
    public void publishMessage(String payload) {
        try {
            if (!mqttAndroidClient.isConnected()) {
                mqttAndroidClient.connect();
            }
            MqttMessage message = new MqttMessage();
            message.setPayload(payload.getBytes());
            message.setQos(0);
            mqttAndroidClient.publish(PUB_TOPIC, message, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i(TAG, "publish succeed!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i(TAG, "publish failed!");
                }
            });
        } catch (MqttException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }
    }

    /**
     * 断开连接销毁资源
     */
    public void destroy() {
        try {
            if (mqttAndroidClient.isConnected()) {
                mqttAndroidClient.disconnect();
                mqttAndroidClient.unregisterResources();
            }
            mqttAndroidClient = null;
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 断开连接
     */
    public void disconnect() {
        try {
            if (mqttAndroidClient.isConnected()) {
                mqttAndroidClient.disconnect();
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重新连接
     */
    public void reInit() {
        connect();
    }


    /**
     * 连接
     */
    private void connect() {
        if (mqttAndroidClient != null && mqttAndroidClient.isConnected()) return;

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setUserName(USERNAME);
        mqttConnectOptions.setPassword(PASSWORD.toCharArray());
        mqttConnectOptions.setCleanSession(false); // 不清理离线消息。qos=1的消息，在设备离线期间会保存在云端。
        mqttConnectOptions.setAutomaticReconnect(false); //本demo关闭自动重连。强烈建议生产环境开启自动重连。
        mqttConnectOptions.setKeepAliveInterval(300);
        /* 创建MqttAndroidClient对象, 并设置回调接口 */
        mqttAndroidClient = new MqttAndroidClient(mContext, HOST, DeviceUuidFactory2.getDeviceUuidStr(mContext));
        mqttAndroidClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Log.i(TAG, "connection lost");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.i(TAG, "topic: " + topic + ", msg: " + new String(message.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.i(TAG, "msg delivered");
            }
        });
        /* Mqtt建连 */
        try {
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i(TAG, "connect succeed");

                    //自动订阅
                    subscribeTopic(SUB_TOPIC);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.i(TAG, "connect failed");
                }
            });

        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

}
