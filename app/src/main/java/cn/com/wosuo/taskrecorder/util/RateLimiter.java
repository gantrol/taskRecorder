package cn.com.wosuo.taskrecorder.util;

import android.os.SystemClock;

import androidx.collection.ArrayMap;

import androidx.annotation.NonNull;

import java.util.concurrent.TimeUnit;


public class RateLimiter<T> {

    private final ArrayMap<T, Long> timestamps;
    private final long timeout;

    public RateLimiter(int timeout, @NonNull TimeUnit timeUnit) {
        this.timestamps = new ArrayMap<>();
        this.timeout = timeUnit.toMillis((long)timeout);
    }

    public final synchronized boolean shouldFetch(T key) {
        Long lastFetched = timestamps.get(key);
        long now = now();
        if (lastFetched == null) {
            timestamps.put(key, now);
            return true;
        } else if (now - lastFetched > this.timeout) {
            timestamps.put(key, now);
            return true;
        } else {
            return false;
        }
    }
    public final synchronized void reset(Object key) {
        this.timestamps.remove(key);
    }

    private final long now() {
        return SystemClock.uptimeMillis();
    }
}
