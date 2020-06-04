package lyd.demo.mqtt;

import java.util.Random;

public class TextHelper {
    protected static Random sRandom = new Random();
    static final String RANDOM_BYTE = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public TextHelper() {
    }

    public static String getRandomString() {
        return getRandomString(16);
    }

    public static String getRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; ++i) {
            sb.append(RANDOM_BYTE.charAt(sRandom.nextInt(RANDOM_BYTE.length())));
        }
        return sb.toString();
    }


}
