package com.example.miniapppointsofinterest;

import android.app.Application;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        My_Signal.init(this);
    }
}
