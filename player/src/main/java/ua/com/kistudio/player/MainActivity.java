package ua.com.kistudio.player;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ServiceConfigurationError;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String LOG_TAG = "Logs";
    Intent i;
    private boolean mServiceBound;
    private MusicService mMusicService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        i = new Intent(this,MusicService.class);
        findViewById(R.id.btnStart).setOnClickListener(this);
        findViewById(R.id.btnStop).setOnClickListener(this);
        findViewById(R.id.btnPause).setOnClickListener(this);
        startService(i);
        bindService(i, mServiceConnection, Context.BIND_AUTO_CREATE);
/*
        Intent i = getIntent();
        int flag = i.getIntExtra("todo",0);
        Log.d(LOG_TAG,"flag - "+flag);
        switch (flag){
            case FLAG_START_MUSIC:
                if (mServiceBound){
                    mMusicService.setPlay();
                }
                finish();
                break;
            case FLAG_STOP_MUSIC:
                if (mServiceBound){
                    mMusicService.setStop();
                }
                finish();
                break;
            case FLAG_PAUSE_MUSIC:
                if (mServiceBound){
                    mMusicService.setPause();
                }
                finish();
                break;
        }*/


    }


    public void onClick(View v){
        switch (v.getId()){
            case R.id.btnStart:
                if (mServiceBound){
                    mMusicService.setPlay();
                }
            break;
            case R.id.btnStop:
                if (mServiceBound){
                    mMusicService.setStop();
                }
//                unbindService(mServiceConnection);
//                stopService(i);
                break;
            case R.id.btnPause:
                if (mServiceBound){
                    mMusicService.setPause();
                }
        }
    }



    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MyBinder myBinder = (MusicService.MyBinder) service;
            mMusicService = myBinder.getService();
            mServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceBound = false;
        }
    };
}
