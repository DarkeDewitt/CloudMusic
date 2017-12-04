package com.chshux.cloudmusic;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

import java.io.File;

public class PlayerService extends Service {
    private IBinder mBinder = new MyBinder();
    public static MediaPlayer mediaPlayer;
    public PlayerService() {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(Environment.getExternalStorageDirectory() + "/bigiron.mp3");
            mediaPlayer.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer.reset();
        mediaPlayer.release();
        super.onDestroy();
    }

    public class MyBinder extends Binder {
        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 101:
                    if(mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        reply.writeInt(1);
                    } else {
                        mediaPlayer.start();
                        reply.writeInt(0);
                    }
                    break;
                case 102:
                    if(mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                        try {
                            mediaPlayer.prepare();
                            mediaPlayer.seekTo(0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 103:
                    if(mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        mediaPlayer.release();
                    }
                    break;
                case 104:
                    int currentPos = mediaPlayer.getCurrentPosition();
                    reply.writeInt(currentPos);
                    break;
                case 105:
                    int maxPos = mediaPlayer.getDuration();
                    reply.writeInt(maxPos);
                    break;
                case 106:
                    int pos = data.readInt();
                    mediaPlayer.seekTo(pos);
                    break;
            }
            return super.onTransact(code, data, reply, flags);
        }
    }

}
