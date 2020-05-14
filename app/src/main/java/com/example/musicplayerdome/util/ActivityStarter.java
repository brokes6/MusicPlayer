package com.example.musicplayerdome.util;

import android.app.Activity;
import android.content.Intent;

import com.example.musicplayerdome.main.view.HomeActivityMusic;
import com.example.musicplayerdome.login.view.SelectLoginActivity;


public class ActivityStarter {
    private static ActivityStarter instance;

    public ActivityStarter() {
    }

    public static synchronized ActivityStarter getInstance() {
        if (instance == null) {
            instance = new ActivityStarter();
        }
        return instance;
    }

    public void startMainActivity(Activity activity) {
        Intent intent = new Intent(activity, HomeActivityMusic.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public void startLoginActivity(Activity activity) {
        Intent intent = new Intent(activity, SelectLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

}
