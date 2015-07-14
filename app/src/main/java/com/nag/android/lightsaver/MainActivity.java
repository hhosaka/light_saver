package com.nag.android.lightsaver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;


public class MainActivity extends Activity {
    private final String PARAM_BRIGHTNESS = "brightness";
    private final String PREF_BRIGHTNESS = "pref_brightness";
    private final int MAX_BRIGHTNESS = 255;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int brightness = getIntent().getIntExtra(PARAM_BRIGHTNESS, -1);
        if(brightness==-1) {
            brightness = Integer.valueOf(Settings.System.getString(this.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS));
            if(brightness!=MAX_BRIGHTNESS) {
                setBroadcastReceiver(brightness);
                brightness = MAX_BRIGHTNESS;
            }
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 0);
        Settings.System.putInt(getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS, brightness);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = Float.valueOf(Settings.System.getString(this.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS))/256;
        getWindow().setAttributes(lp);
    }

    void setBroadcastReceiver(final int brightness) {
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        final BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                getApplicationContext().unregisterReceiver(this);
                Intent i = new Intent(context, MainActivity.class);
                i.putExtra(PARAM_BRIGHTNESS, brightness);
                startActivity(i);
            }
        };
        getApplicationContext().registerReceiver(receiver, filter);
    }
}
