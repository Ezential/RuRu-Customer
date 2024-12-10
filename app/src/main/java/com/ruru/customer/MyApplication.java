package com.ruru.customer;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.onesignal.OneSignal;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(this);
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId("658c2985-d4f3-41e6-a95f-554ce2633cf6");

    }


}

