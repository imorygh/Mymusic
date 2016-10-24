package com.example.administrator.mymusic.Model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/17.
 */

public class MusicUtils {
    private static ArrayList<Music> musicList;
    public static final String[] AUDIO_KEYS = new String[]{
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
//            MediaStore.Audio.Media.TITLE_KEY,
            MediaStore.Audio.Media.ARTIST,
//            MediaStore.Audio.Media.ARTIST_ID,
//            MediaStore.Audio.Media.ARTIST_KEY,
//            MediaStore.Audio.Media.COMPOSER,
            MediaStore.Audio.Media.ALBUM,
//            MediaStore.Audio.Media.ALBUM_ID,
//            MediaStore.Audio.Media.ALBUM_KEY,
//            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.SIZE,
//            MediaStore.Audio.Media.YEAR,
//            MediaStore.Audio.Media.TRACK,
//            MediaStore.Audio.Media.IS_RINGTONE,
//            MediaStore.Audio.Media.IS_PODCAST,
//            MediaStore.Audio.Media.IS_ALARM,
//            MediaStore.Audio.Media.IS_MUSIC,
//            MediaStore.Audio.Media.IS_NOTIFICATION,
//            MediaStore.Audio.Media.MIME_TYPE,
            MediaStore.Audio.Media.DATA
    };

    public static ArrayList<Music> getMusicList(Context context) {
//        List<Music> musicList = new ArrayList<Music>();
        if (musicList!=null)
            return musicList;
        musicList=new ArrayList<>();
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                AUDIO_KEYS,
                null,
                null,
                null);
        if (cursor!=null&&cursor.getCount() > 0) {
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Bundle bundle = new Bundle();
            for (String key : AUDIO_KEYS) {
                final int columnIndex = cursor.getColumnIndex(key);
                final int type = cursor.getType(columnIndex);
                switch (type) {
                    case Cursor.FIELD_TYPE_BLOB:
                        break;
                    case Cursor.FIELD_TYPE_FLOAT:
                        float floatValue = cursor.getFloat(columnIndex);
                        bundle.putFloat(key, floatValue);
                        break;
                    case Cursor.FIELD_TYPE_INTEGER:
                        int intValue = cursor.getInt(columnIndex);
                        bundle.putInt(key, intValue);
                        break;
                    case Cursor.FIELD_TYPE_NULL:
                        break;
                    case Cursor.FIELD_TYPE_STRING:
                        String strValue = cursor.getString(columnIndex);
                        bundle.putString(key, strValue);
                        break;
                }
            }
            Music music = new Music(bundle);
            musicList.add(music);
        }
            cursor.close();
    }


        return musicList;
    }
}