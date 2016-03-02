package ua.com.kistudio.player;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

public class MusicService extends Service {

    private static final String FLAG_START_MUSIC = "music.start";
    private static final String FLAG_STOP_MUSIC = "music.stop";
    private static final String FLAG_PAUSE_MUSIC = "music.pause";

    private IBinder mBinder = new MyBinder();
    private boolean mIsStop = false;
    NotificationManager notificationManager;
    public MusicService() {
    }

    MediaPlayer mediaPlayer;
    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this,R.raw.music_for_player);

        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        IntentFilter keys = new IntentFilter();
        keys.addAction(FLAG_START_MUSIC);
        keys.addAction(FLAG_PAUSE_MUSIC);
        keys.addAction(FLAG_STOP_MUSIC);

        registerReceiver(receiver, keys);
     //   mediaPlayer.start();
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(FLAG_START_MUSIC)){
                setPlay();
            }
            else if (intent.getAction().equals(FLAG_STOP_MUSIC)){
                setStop();
            }
            else if (intent.getAction().equals(FLAG_PAUSE_MUSIC)){
                setPause();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }

    public void setPause(){
        mediaPlayer.pause();
        int pos = mediaPlayer.getCurrentPosition();
        int dur = mediaPlayer.getDuration();
        int posNotif = (pos/dur)*100;
        showNotification(this,"Service",1,30);
    }

    public void setPlay(){
        showNotification(this,"Service",1,0);
        if (mIsStop)
        {
            mediaPlayer = MediaPlayer.create(this,R.raw.music_for_player);
            mIsStop = false;
        }
        mediaPlayer.start();
    }

    public void setStop(){
        mediaPlayer.stop();
        mIsStop = true;
        notificationManager.cancel(1);
    }

    @Override
    public IBinder onBind(Intent intent) {
       return mBinder;
    }

    public class MyBinder extends Binder{
        MusicService getService(){
            return MusicService.this;
        }
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void showNotification(Context context, String s, int id, int progress) {

        Intent intent = new Intent(context, MainActivity.class);

        Intent intentStart = new Intent().setAction(FLAG_START_MUSIC);

        Intent intentStop =  new Intent().setAction(FLAG_STOP_MUSIC);

        Intent intentPause =  new Intent().setAction(FLAG_PAUSE_MUSIC);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,1,intent,0);
        PendingIntent pendingIntentStart = PendingIntent.getBroadcast(context, 1, intentStart, 0);
        PendingIntent pendingIntentStop = PendingIntent.getBroadcast(context, 1, intentStop, 0);
        PendingIntent pendingIntentPause = PendingIntent.getBroadcast(context, 1, intentPause, 0);

        Notification notification = new Notification.Builder(context)
                .setContentTitle("MyReceiver")
                .setContentText(s)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setProgress(100,progress,false)
                .setSmallIcon(R.mipmap.ic_launcher)
                .addAction(R.mipmap.ic_launcher, "Start", pendingIntentStart)
                .addAction(R.mipmap.ic_launcher, "Stop", pendingIntentStop)
                .addAction(R.mipmap.ic_launcher, "Pause", pendingIntentPause)
                .build();
        notificationManager.notify(id, notification);
    }


}
