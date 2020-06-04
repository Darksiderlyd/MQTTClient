package lyd.demo.mqttclient;

import android.app.Application;

import lyd.demo.mqtt.IOT;

/**
 * @package lyd.demo.mqttclient
 * @user by lvyaodong
 * @date 2020-06-03
 * @email 1126220529@qq.com
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        IOT.getInstance().init(this);
    }
}
