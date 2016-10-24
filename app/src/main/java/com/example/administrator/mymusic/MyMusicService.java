package com.example.administrator.mymusic;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import com.example.administrator.mymusic.Model.Constants;
import com.example.administrator.mymusic.Model.Music;

import java.io.IOException;
import java.util.ArrayList;

public class MyMusicService extends Service {
    private MediaPlayer mediaPlayer;
    private MyMusicReceiver receiver;
    private static int position;
    private ArrayList<Music> musicList;
    public MyMusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        return new MyMusicBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer=new MediaPlayer();
        receiver=new MyMusicReceiver();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(Constants.ACTION_PLAY);
        intentFilter.addAction(Constants.PLAY_BUTTON);
        intentFilter.addAction(Constants.NEXT_BUTTON);
        intentFilter.addAction(Constants.PREVIOUS_BUTTON);
        registerReceiver(receiver,intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    private void play(String path,int time){
        try {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.seekTo(time);
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Intent i=new Intent(Constants.NEXT_BUTTON);
                i.putParcelableArrayListExtra(Constants.TAG_MUSIC_LIST,musicList);
                sendBroadcast(i);
            }
        });
    }
    private void pause(){
        mediaPlayer.pause();
    }





    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    class MyMusicBinder extends Binder {


    }

    class MyMusicReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Intent updateUI=new Intent();
            musicList = intent.getParcelableArrayListExtra(Constants.TAG_MUSIC_LIST);
            switch (intent.getAction()) {
                case Constants.ACTION_PLAY:
                    position = intent.getIntExtra(Constants.TAG_POSITION, 0);
                    play(musicList.get(position).getPath(), 0);
                    updateUI.setAction(Constants.UPDATE_UI_TO_PAUSE)
                    .putExtra(Constants.TAG_POSITION,position);
                    break;
                case Constants.PREVIOUS_BUTTON:
                    if (position == 0) {
                        position = musicList.size() - 1;
                    } else position--;
                    play(musicList.get(position).getPath(), 0);
                    updateUI.setAction(Constants.UPDATE_UI_TO_PAUSE)
                            .putExtra(Constants.TAG_POSITION,position);
                    break;
                case Constants.NEXT_BUTTON:
                    if (position == musicList.size() - 1) {
                        position = 0;
                    } else position++;
                    play(musicList.get(position).getPath(), 0);
                    updateUI.setAction(Constants.UPDATE_UI_TO_PAUSE)
                            .putExtra(Constants.TAG_POSITION,position);
                    break;
                case Constants.PLAY_BUTTON:
                    if (mediaPlayer.isPlaying()){
                        pause();
                        updateUI.setAction(Constants.UPDATE_UI_TO_PLAY)
                                .putExtra(Constants.TAG_POSITION,position);
                        break;
                    }
                    int time=mediaPlayer.getCurrentPosition();
                    play(musicList.get(position).getPath(), time);
                    updateUI.setAction(Constants.UPDATE_UI_TO_PAUSE)
                            .putExtra(Constants.TAG_POSITION,position);
                    break;


            }
            sendBroadcast(updateUI);

        }
    }
}
