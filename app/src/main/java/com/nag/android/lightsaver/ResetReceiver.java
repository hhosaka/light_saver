package com.nag.android.lightsaver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class ResetReceiver extends BroadcastReceiver{
    final static String ACTION_MANUAL_RESET = "com.nag.android.light_saver.manual_reset";
    private final int brightness;

    static void start(Context context, final int brightness) {
        BroadcastReceiver receiver = new ResetReceiver(brightness);
        context.registerReceiver(receiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
        context.registerReceiver(receiver, new IntentFilter(Intent.ACTION_SHUTDOWN));
        context.registerReceiver(receiver, new IntentFilter(ACTION_MANUAL_RESET));
    }

    static void stop(Context context){
        context.sendBroadcast(new Intent(ACTION_MANUAL_RESET));
    }



    ResetReceiver(int brightness) {
        this.brightness = brightness;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d("H:", "H:" + action);
        if(action != null && (
                action.equals(Intent.ACTION_SCREEN_OFF)||
                        action.equals(Intent.ACTION_SHUTDOWN) ||
                                action.equals(ACTION_MANUAL_RESET))) {
            context.unregisterReceiver(this);
            context.stopService(new Intent(context, WatchService.class));
            MainActivity.start(context, brightness);
        }
    }
}
