package com.example.administrator.mymusic.Model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/17.
 */

public class Music implements Parcelable{
    private int mId;
    private String name;
    private String artist;
    private String album;
    private String path;
    private long duration;
    private int size;
//    private Bundle mBundle;

    public Music(Bundle bundle) {
//        mBundle=bundle;
        mId = bundle.getInt(MediaStore.Audio.Media._ID);
        name = bundle.getString(MediaStore.Audio.Media.TITLE);
        artist = bundle.getString(MediaStore.Audio.Media.ARTIST);
        album = bundle.getString(MediaStore.Audio.Media.ALBUM);
        path = bundle.getString(MediaStore.Audio.Media.DATA);

        duration = bundle.getInt(MediaStore.Audio.Media.DURATION);
        size = bundle.getInt(MediaStore.Audio.Media.SIZE);

    }

    protected Music(Parcel in) {
        mId = in.readInt();
        name = in.readString();
        artist = in.readString();
        album = in.readString();
        path = in.readString();
        duration = in.readLong();
        size = in.readInt();
    }

    public static final Creator<Music> CREATOR = new Creator<Music>() {
        @Override
        public Music createFromParcel(Parcel in) {
            return new Music(in);
        }

        @Override
        public Music[] newArray(int size) {
            return new Music[size];
        }
    };

    public int getId() {
        return mId;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getPath() {
        return path;
    }

    public long getDuration() {
        return duration;
    }

    public int getSize() {
        return size;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeBundle(mBundle);
        dest.writeInt(mId);
        dest.writeString(name);
        dest.writeString(artist);
        dest.writeString(album);
        dest.writeString(path);
        dest.writeInt(size);
        dest.writeLong(duration);
    }
}
