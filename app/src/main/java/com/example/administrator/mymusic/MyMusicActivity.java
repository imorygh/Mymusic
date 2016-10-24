package com.example.administrator.mymusic;

import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TimeUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class MyMusicActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return MusicFragment.newInstance();
    }

    long time1= 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode== KeyEvent.KEYCODE_BACK){
            if (System.currentTimeMillis()-time1>2000) {
                Toast.makeText(this, "Click again to exit", Toast.LENGTH_SHORT).show();
                time1 = System.currentTimeMillis();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    public void barClick(View view) {
//        Intent intent=new Intent(this,FullscreenActivity.class);
//        startActivity(intent);
        Toast.makeText(this,"waiting",Toast.LENGTH_SHORT).show();
//        NewMessageNotification.notify(this,"??",1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
