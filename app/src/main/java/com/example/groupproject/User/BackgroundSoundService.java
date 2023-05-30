package com.example.groupproject.User;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.example.groupproject.R;

public class BackgroundSoundService extends Service {
    private static final String TAG = null;
    MediaPlayer mediaPlayer;
    public IBinder onBind(Intent arg0) {

        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.bg_music);
        mediaPlayer.setLooping(true); // Set looping
        mediaPlayer.setVolume(100,100);
    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer.start();
        return Service.START_STICKY;
    }

    public void onStart(Intent intent, int startId) {
    }
    public IBinder onUnBind(Intent arg0) {
        return null;
    }

    public void onStop() {
    }
    public void onPause() {
    }
    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    @Override
    public void onLowMemory() {

    }
}
