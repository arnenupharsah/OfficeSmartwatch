package com.example.aulick.officesmartwatch;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.onesignal.OneSignal;

import org.json.JSONObject;

public class ApplicationClass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // TODO: Add OneSignal initialization here

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        String EMAIL = "abhi1aa@outlook.com";



    }
}
