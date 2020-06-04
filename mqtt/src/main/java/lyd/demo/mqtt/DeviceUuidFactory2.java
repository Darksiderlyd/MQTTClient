package lyd.demo.mqtt;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Base64;


public class DeviceUuidFactory2 {

    protected static volatile String mDevicdId;

    public DeviceUuidFactory2() {
    }

    public static void init(Context context) {
        if (mDevicdId == null) {
            synchronized(DeviceUuidFactory2.class) {
                if (mDevicdId == null) {
                    SharedPreferences sp = context.getSharedPreferences("device_id.xml", 0);
                    String storeDeviceId = sp.getString("device_id", (String)null);
                    if (storeDeviceId != null) {
                        mDevicdId = storeDeviceId;
                    } else {
                        String androidId = Secure.getString(context.getContentResolver(), "android_id");

                        try {
                            if (!"9774d56d682e549c".equals(androidId)) {
                                mDevicdId = androidId;
                            } else {
                                String deviceId = ((TelephonyManager)context.getSystemService("phone")).getDeviceId();
                                mDevicdId = deviceId != null ? deviceId : new String(Base64.encode(TextHelper.getRandomString().getBytes(), 0));
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }

                        sp.edit().putString("device_id", mDevicdId.toString()).commit();
                    }
                }
            }
        }

    }

    public static String getDeviceUuidStr(Context context) {
        init(context.getApplicationContext());
        return mDevicdId;
    }
}
