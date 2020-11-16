package com.zhx.baselibrary.utils;

import android.util.Log;

/**
 * @author lyldding
 * @date 2019/11/13
 */
public class LogUtil {
    private static final String TAG = "test_log";

    private LogUtil() {
    }

    private static String createLog(String log) {
        StackTraceElement element = new Throwable().getStackTrace()[2];
        StringBuffer buffer = new StringBuffer();
//        buffer.append(element.getClassName());
//        buffer.append(".");
//        buffer.append(element.getMethodName());
//        buffer.append("(").append(element.getFileName()).append(":").append(element.getLineNumber()).append(") \n");
        buffer.append(log);
        return buffer.toString();
    }

    public static void e(String message) {
        if (true) {
            Log.e(TAG, createLog(message));
        }
    }

    public static void e(String msg, String message) {
        if (true) {
            Log.e(TAG, createLog(msg + " @@ " + message));
        }
    }


    public static void i(String message) {
        if (true) {
            Log.i(TAG, createLog(message));
        }
    }

    public static void d(String message) {
        if (true) {
            Log.d(TAG, createLog(message));
        }
    }

    public static void v(String message) {
        if (true) {
            Log.v(TAG, createLog(message));
        }
    }

    public static void w(String message) {
        if (true) {
            Log.w(TAG, createLog(message));
        }
    }
}
