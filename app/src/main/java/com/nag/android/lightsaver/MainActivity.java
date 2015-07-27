package com.nag.android.lightsaver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.nag.android.util.PreferenceHelper;


public class MainActivity extends Activity {
    private final static String PARAM_BRIGHTNESS = "brightness";
    private static final int MAX_BRIGHTNESS = 255;

    static void start(Context context, int brightness){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(PARAM_BRIGHTNESS, brightness);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(getIntent().hasExtra(PARAM_BRIGHTNESS)) {
            int brightness = getIntent().getIntExtra(PARAM_BRIGHTNESS, -1);
            Log.d("H:","H:reset brightness = "+brightness);
            activateBrightness(brightness);
        }else{
            int brightness = Integer.valueOf(Settings.System.getString(this.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS));
            if (brightness < MAX_BRIGHTNESS) {
                Log.d("H:", "H:register" + brightness);
                WatchService.start(this, brightness);
                activateBrightness(MAX_BRIGHTNESS);
            } else {
                ResetReceiver.stop(this);
                Log.d("H:", "H:ignore register" + brightness);
            }
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 0);
    }

    private void activateBrightness(int brightness){
        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightness);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = (float)brightness / MAX_BRIGHTNESS;
        getWindow().setAttributes(lp);
    }

//    void setBroadcastReceiver(final int brightness) {
//        BroadcastReceiver receiver = new ResetReceiver(brightness);
//        getApplicationContext().registerReceiver(receiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
//        getApplicationContext().registerReceiver(receiver, new IntentFilter(Intent.ACTION_SHUTDOWN));
//    }
}
