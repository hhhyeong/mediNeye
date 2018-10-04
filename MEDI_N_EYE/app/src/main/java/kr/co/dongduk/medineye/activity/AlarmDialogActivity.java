package kr.co.dongduk.medineye.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.io.IOException;

import kr.co.dongduk.medineye.R;
import kr.co.dongduk.medineye.module.TTS_Module;
import kr.co.dongduk.medineye.service.WakeupScreen;

/**
 * Created by Owner on 2016-08-25.
 */

public class AlarmDialogActivity extends TTS_Module {

    WakeupScreen ws;
    MediaPlayer player;
    FrameLayout layout;
    Button btn;
    TextView title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        getWindow().setFormat(PixelFormat.TRANSPARENT);

        setContentView(R.layout.popup_alarm);

        init();
        playMediaplayer();

        try {
            SharedPreferences preferences = this.getSharedPreferences("AlarmData", Context.MODE_PRIVATE);
            String memoText = preferences.getString("Alarm_Explain", null);
            Log.d("MEMO_TEXT", memoText);
            if (memoText.length() >= 14) {
                StringBuffer sb = new StringBuffer(memoText);
                sb.insert(memoText.length() / 3, "\n");
                memoText = sb.toString();
            }
            title.setText(memoText);
        } catch (Exception e){ e.printStackTrace(); }


        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(player != null)
                    player.release();
                player = null;

                ws.release();
                _ttsActive = true;
                _tts.speak(title.getText().toString() + "\n 알람이 울렸습니다.", TextToSpeech.QUEUE_FLUSH, null);
                /*************TTS*****************/
//                title.getText().toString() 으로
            }
        });
    }

    public void playMediaplayer()
    {
        Uri uri=Uri.parse("android.resource://"+getPackageName()+"/raw/alarm3");
        try {
            Log.d("SUCCESS", uri.toString());
            player.setDataSource(this, uri);
            player.prepare();
            player.setLooping(true);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("ERROR", e.toString());
        }
        player.start();
    }

    public void onDestroy(){
        super.onDestroy();
        if(player != null)
            player.release();
        player = null;

        ws.release();
    }

    public void onClick(View v){
        switch(v.getId()){
            //보호자에게 약 먹었다고 문자 추가?
            case R.id.btn : finish(); break;
        }
    }

    public void init()
    {
        // 화면 깨우기
        ws = new WakeupScreen();
        ws.acquire(getApplicationContext());

        layout = (FrameLayout)findViewById(R.id.popuplayout);
        btn = (Button)findViewById(R.id.btn);
        title = (TextView)findViewById(R.id.txt_memo);
        player = new MediaPlayer();
    }
}
