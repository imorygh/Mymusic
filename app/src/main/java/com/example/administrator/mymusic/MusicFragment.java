package com.example.administrator.mymusic;


import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.administrator.mymusic.Model.Constants;
import com.example.administrator.mymusic.Model.Music;
import com.example.administrator.mymusic.Model.MusicUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MusicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MusicFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {
    ArrayList<Music> musicList;
    private MusicAdapter mAdapter;
    private ImageButton previousMusicButton;
    private ImageButton nextMusicButton;
    private ImageButton playMusicButton;
    public MusicFragment() {
    }


    public static MusicFragment newInstance() {
        MusicFragment fragment = new MusicFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyMusicReceiver receiver=new MyMusicReceiver();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(Constants.UPDATE_UI_TO_PLAY);
        intentFilter.addAction(Constants.UPDATE_UI_TO_PAUSE);
        intentFilter.addAction(Constants.HELP_NEXT_BUTTON);
        intentFilter.addAction(Constants.HELP_PLAY_BUTTON);
        intentFilter.addAction(Constants.HELP_PREVIOUS_BUTTON);
        getActivity().registerReceiver(receiver,intentFilter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, container, false);
        previousMusicButton= (ImageButton) view.findViewById(R.id.previous_button);
        nextMusicButton= (ImageButton) view.findViewById(R.id.next_button);
        playMusicButton= (ImageButton) view.findViewById(R.id.play_button);
        previousMusicButton.setOnClickListener(this);
        previousMusicButton.setOnTouchListener(this);

        nextMusicButton.setOnClickListener(this);
        nextMusicButton.setOnTouchListener(this);

        playMusicButton.setOnClickListener(this);
        playMusicButton.setOnTouchListener(this);

        RecyclerView musicRecyclerView = (RecyclerView) view.findViewById(R.id.music_recycler_view);
        musicRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        musicList = MusicUtils.getMusicList(getActivity());
        if (mAdapter == null) {
            mAdapter = new MusicAdapter(musicList);
            musicRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setMusicList(musicList);
            mAdapter.notifyDataSetChanged();
        }
        ServiceConnection serviceConnection=new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };

        Intent intentService=new Intent(getActivity(),MyMusicService.class);
        getActivity().startService(intentService);
        return view;
    }


    @Override
    public void onClick(View v) {
        Intent intent=new Intent();
        intent.putParcelableArrayListExtra(Constants.TAG_MUSIC_LIST,musicList);
        switch (v.getId()){
            case R.id.play_button:
                intent.setAction(Constants.PLAY_BUTTON);
                break;
            case R.id.previous_button:
                intent.setAction(Constants.PREVIOUS_BUTTON);
                break;
            case R.id.next_button:
                intent.setAction(Constants.NEXT_BUTTON);
                break;
        }
        getActivity().sendBroadcast(intent);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
            v.setAlpha(0.4f);
        else if (event.getAction() == MotionEvent.ACTION_UP)
            v.setAlpha(1f);
        Log.e("wqterwet","onTouch()");
        return false;

    }


    class MusicHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnTouchListener {
        private TextView musicNameTextView;
        private TextView musicArtistAndAlbumTextView;
        private Music mMusic;
        public MusicHolder(View itemView) {
            super(itemView);
            musicNameTextView = (TextView) itemView.findViewById(R.id.music_name);
            musicArtistAndAlbumTextView = (TextView) itemView.findViewById(R.id.music_artist_and_album);
            itemView.setOnClickListener(this);
            itemView.setOnTouchListener(this);
            itemView.setBackground(getResources().getDrawable(R.drawable.item_default));
        }
        public void bindView(Music music){
            mMusic=music;
            musicNameTextView.setText(music.getName());
            musicArtistAndAlbumTextView.setText(music.getArtist()+" - "+music.getAlbum());
        }

        @Override
        public void onClick(View v) {

            Intent intent=new Intent();
            intent.setAction(Constants.ACTION_PLAY);
            intent.putExtra(Constants.TAG_POSITION,musicList.indexOf(mMusic));
            intent.putParcelableArrayListExtra(Constants.TAG_MUSIC_LIST,musicList);
            getActivity().sendBroadcast(intent);
            v.setBackground(getResources().getDrawable(R.drawable.touch_bg));
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction()==MotionEvent.ACTION_DOWN) {
                v.setBackground(getResources().getDrawable(R.drawable.touch_bg));
            }
            else if (event.getAction()==MotionEvent.ACTION_UP)
                v.setBackground(getResources().getDrawable(R.drawable.item_default));
            return false;
        }
    }
    class MusicAdapter extends RecyclerView.Adapter<MusicHolder>{
        private List<Music> musicList;

        public MusicAdapter(List<Music> musicList) {
            this.musicList=musicList;
        }

        @Override
        public MusicHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(getActivity()).inflate(R.layout.music_item,parent,false);
            return new MusicHolder(view);
        }

        @Override
        public void onBindViewHolder(MusicHolder holder, int position) {
            Music music=musicList.get(position);
            holder.bindView(music);
        }

        @Override
        public int getItemCount() {
            return musicList.size();
        }

        public void setMusicList(List<Music> musicList) {
            this.musicList = musicList;
        }
    }

    class MyMusicReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int position;
            switch (intent.getAction()){
                case Constants.UPDATE_UI_TO_PLAY:
                    playMusicButton.setBackground(getResources().getDrawable(R.drawable.ic_action_play));
                    position=intent.getIntExtra(Constants.TAG_POSITION,0);
                    SingleFragmentActivity.notifyMyNoti(context,false,musicList.get(position).getName(),
                            musicList.get(position).getArtist()+" - "+musicList.get(position).getAlbum());
                    break;
                case Constants.UPDATE_UI_TO_PAUSE:
                    playMusicButton.setBackground(getResources().getDrawable(R.drawable.ic_action_pause));
                    position=intent.getIntExtra(Constants.TAG_POSITION,0);
                    SingleFragmentActivity.notifyMyNoti(context,true,musicList.get(position).getName(),
                            musicList.get(position).getArtist()+" - "+musicList.get(position).getAlbum());
                    break;
                case Constants.HELP_NEXT_BUTTON:
                    onClick(nextMusicButton);
                    break;
                case Constants.HELP_PLAY_BUTTON:
                    onClick(playMusicButton);
                    break;
                case Constants.HELP_PREVIOUS_BUTTON:
                    onClick(previousMusicButton);
                    break;
            }

        }
    }

}
