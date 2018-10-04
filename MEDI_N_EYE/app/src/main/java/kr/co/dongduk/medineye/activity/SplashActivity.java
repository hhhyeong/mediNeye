package kr.co.dongduk.medineye.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;

import kr.co.dongduk.medineye.R;

public class SplashActivity extends AppCompatActivity {

    //음계 페이지 수별로 소리내주기
    //애니메이션 넣을거면 트렐로 링크 참고하기
    //일반적으로는 thread 줘서 타이머로 종료시킨다.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        MediaPlayer mp = new MediaPlayer();
        Uri uri=Uri.parse("android.resource://"+getPackageName()+"/raw/page_do_to_sol");
        try {
            Log.d("SUCCESS", uri.toString());
            mp.setDataSource(this, uri);
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("ERROR", e.toString());
        }
        mp.start();

        Handler handler = new Handler(){
            public void handleMessage(Message msg){ finish(); }
        };
        handler.sendEmptyMessageDelayed(0, 2000);

    }
    MediaPlayer player;
    public void onDestroy(){
        super.onDestroy();
        if(player != null)
            player.release();
        player = null;
    }

}
