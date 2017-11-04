package com.isansc.desafioconcrete.util;

import android.util.Log;

/**
 * Created by Isan on 01-Nov-17.
 */

public class Logger {
    private static final String PREFIX_FORMAT = "[DesafioConcrete] - [%s] - [%s] - %s";

    public static void i(String tag, String className, String method, String message){
        String content = formatContent(className, method, message);
        Log.d(tag, content);
    }

    public static void d(String tag, String className, String method, String message){
        String content = formatContent(className, method, message);
        Log.d(tag, content);
    }

    public static void e(String tag, String className, String method, String message, Throwable ex){
        String content = formatContent(className, method, message);

        if(ex != null){
            Log.e(tag, content, ex);
        }
        else{
            Log.e(tag, content);
        }
    }

    private static String formatContent(String className, String method, String message){
        return String.format(PREFIX_FORMAT, className, method, message);
    }
}
