package com.isansc.desafioconcrete;

import android.app.Application;
import android.content.Context;

/**
 * Created by Isan on 31-Oct-17.
 */

public class ConcreteApplication extends Application {
    private static ConcreteApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

    }

    public static Context getAppContext() {
        return mInstance.getApplicationContext();
    }
}
