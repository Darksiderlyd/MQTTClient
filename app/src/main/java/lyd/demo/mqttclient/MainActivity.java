package lyd.demo.mqttclient;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import lyd.demo.mqtt.DeviceUuidFactory2;
import lyd.demo.mqtt.IOT;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MainActivity", DeviceUuidFactory2.getDeviceUuidStr(this));
        /* 通过按键发布消息 */
        Button pubBtn = findViewById(R.id.publish);
        Button reBtn = findViewById(R.id.reconnect);
        Button disBtn = findViewById(R.id.disconnect);
        Button destroyBtn = findViewById(R.id.destroy);
        pubBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IOT.getInstance().publishMessage("hello IoT");
            }
        });
        reBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IOT.getInstance().reInit();
            }
        });

        disBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IOT.getInstance().disconnect();
            }
        });

        destroyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IOT.getInstance().destroy();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IOT.getInstance().destroy();
    }
}
