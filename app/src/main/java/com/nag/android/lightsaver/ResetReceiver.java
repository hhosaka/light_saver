package com.nag.android.lightsaver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ResetReceiver extends BroadcastReceiver{
    private final int brightness;
    ResetReceiver(int brightness) {
        this.brightness = brightness;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d("H:", "H:" + action);
        if(action != null
                && (action.equals(Intent.ACTION_SCREEN_OFF)||action.equals(Intent.ACTION_SHUTDOWN))) {
            context.unregisterReceiver(this);
            Intent i = new Intent(context, MainActivity.class);
            i.putExtra(MainActivity.PARAM_BRIGHTNESS, brightness);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}
