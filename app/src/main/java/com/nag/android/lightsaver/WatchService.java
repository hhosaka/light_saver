package com.nag.android.lightsaver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;

/**
 * Created by hosaka on 2015/07/24.
 */
public class WatchService extends Service {
    private static final String PARAM_BRIGHTNESS = "brightness";
    private static final int MAX_BRIGHTNESS = 255;

    static void start(Context context, int brightness){
        Intent intent = new Intent(context, WatchService.class);
        intent.putExtra(PARAM_BRIGHTNESS, brightness);
        context.startService(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null && intent.hasExtra(PARAM_BRIGHTNESS)){
            ResetReceiver.start(getApplicationContext(), intent.getIntExtra(PARAM_BRIGHTNESS, 255));
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // no use case
        return null;
    }
}
