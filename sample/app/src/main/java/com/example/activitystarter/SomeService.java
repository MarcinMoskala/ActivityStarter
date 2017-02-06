package com.example.activitystarter;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import activitystarter.ActivityStarter;
import activitystarter.Arg;
import activitystarter.Optional;

public class SomeService extends Service {

    @Arg @Optional String name = "";
    @Arg @Optional String surname = "";
    @Arg int id;

    public SomeService() {}

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ActivityStarter.fill(this, intent);
        Log.i("SomeService", name + " " + id);
        return super.onStartCommand(intent, flags, startId);
    }
}
