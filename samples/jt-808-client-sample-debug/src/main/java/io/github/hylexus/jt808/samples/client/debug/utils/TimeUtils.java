package io.github.hylexus.jt808.samples.client.debug.utils;

import java.util.concurrent.TimeUnit;

/**
 * @author hylexus
 */
public class TimeUtils {
    public static void sleep(int time, TimeUnit unit) {
        try {
            unit.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
