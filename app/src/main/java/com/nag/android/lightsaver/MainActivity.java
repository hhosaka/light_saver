package com.nag.android.lightsaver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.WindowManager;


public class MainActivity extends Activity {
    private final String PARAM_BRIGHTNESS = "brightness";
    private final int MAX_BRIGHTNESS = 255;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int brightness;
        if(getIntent().hasExtra(PARAM_BRIGHTNESS)) {
            brightness = getIntent().getIntExtra(PARAM_BRIGHTNESS, -1);
        }else{
            brightness = Integer.valueOf(Settings.System.getString(this.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS));
            if(brightness<MAX_BRIGHTNESS) {
                setBroadcastReceiver(brightness);
                brightness = MAX_BRIGHTNESS;
            }
        }
        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightness);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = (float)brightness / MAX_BRIGHTNESS;
        getWindow().setAttributes(lp);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 0);
    }

    void setBroadcastReceiver(final int brightness) {
        final BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                getApplicationContext().unregisterReceiver(this);
                Intent i = new Intent(context, MainActivity.class);
                i.putExtra(PARAM_BRIGHTNESS, brightness);
                startActivity(i);
            }
        };
        getApplicationContext().registerReceiver(receiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
        getApplicationContext().registerReceiver(receiver, new IntentFilter(Intent.ACTION_SHUTDOWN));
    }
}
