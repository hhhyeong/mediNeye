package kr.co.dongduk.medineye.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Locale;

import kr.co.dongduk.medineye.R;
import kr.co.dongduk.medineye.module.TTS_Module;
import kr.co.dongduk.medineye.service.AlarmReceiver;

public class AlarmActivity extends TTS_Module {
    RelativeLayout existLayout;
    RelativeLayout noneLayout;
    Button btn, btn2;
    TextView memo, info, repeat;

    GestureDetector mGesDetector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_alarm);

        init();
        settingLayout();

    }



    @Override
    public void onResume(){
        super.onResume();

        settingLayout();

    }

    public void init()
    {
        existLayout = (RelativeLayout)findViewById(R.id.layout_exist);
        noneLayout = (RelativeLayout)findViewById(R.id.layout_none);
        btn = (Button)findViewById(R.id.btn);
        btn2 = (Button)findViewById(R.id.btn2);

        btn.setText("알람 해제");

        memo = (TextView)findViewById(R.id.txt_memo);
        info = (TextView)findViewById(R.id.txt_info);
        repeat = (TextView)findViewById(R.id.txt_repeat);

        mGesDetector = new GestureDetector(this, new cGestureListener());
        btn2.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGesDetector.onTouchEvent(event);
                return true;
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TTS
                _ttsActive = true;
                _tts.speak("알람 삭제", TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //삭제
                cancelAlarm();
                return false;
            }
        });
    }

    public void settingLayout ()
    {

        SharedPreferences preferences = this.getSharedPreferences("AlarmData", Context.MODE_PRIVATE);

        if (isAlarmExistOnService() == true) {

//            Toast.makeText(this, "Exist", Toast.LENGTH_SHORT).show();
            existLayout.setVisibility(View.VISIBLE);
            noneLayout.setVisibility(View.GONE);

            String repeatText = "" +preferences.getInt("Alarm_Repeat", 0);
            if ( repeatText.equals("0")){
                repeatText = "반복 없음";
            } else if ( repeatText.equals("1")){
                repeatText = "(3회 반복)";
            }else if ( repeatText.equals("2")){
                repeatText = "(3회 반복)";
            }else if ( repeatText.equals("3")){
                repeatText = "(3회 반복)";
            }

            info.setText(""+preferences.getString("Alarm_AMPM", null) + " "
                    + preferences.getString("Alarm_Time", null));
            repeat.setText(repeatText);

            String memoText = "" + preferences.getString("Alarm_Explain","");
            if (memoText.length() >= 14) {
                memoText = memoText.substring(0, 12) + "...";
                StringBuffer sb = new StringBuffer(memoText);
                sb.insert(memoText.length() / 3, "\n");
                memoText = sb.toString();
            }
            memo.setText(memoText);

        } else { //?깅줉???뚮엺???놁쑝硫?
//            Toast.makeText(this, "NONE", Toast.LENGTH_SHORT).show();
            existLayout.setVisibility(View.GONE);
            noneLayout.setVisibility(View.VISIBLE);
        }
    }

    public boolean isAlarmExistOnService(){
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, AlarmSettingActivity.ALARM, intent, PendingIntent.FLAG_NO_CREATE);

        if (sender == null) {
            return false;
        } else {
            return true;
        }
    }

    public void cancelAlarm(){
        AlarmManager am = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, AlarmSettingActivity.ALARM, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (sender != null) {
            am.cancel(sender);
            sender.cancel();

//            Toast.makeText(this, "??젣", Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(this, "" +sender.toString(), Toast.LENGTH_SHORT).show();

        }
        settingLayout();
    }
    class cGestureListener extends GestureDetector.SimpleOnGestureListener {

        /****************
         * 싱글탭 : TTS로 연결 전화 번호 알려주기
         *****************/
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            // *****   한번 터치가 확실 할 경우 발생 된다. DoubleTap이 아님!   ******
            Log.d("Double_Tap", "Yes, single Clicked");
            _ttsActive = true;
            _tts.speak("알람 등록", TextToSpeech.QUEUE_FLUSH, null);
            return false;
        }

        public boolean onDoubleTap(MotionEvent e) {
            Log.d("Double_Tap", "Yes, double Clicked");
            Intent intent = new Intent(AlarmActivity.this, AlarmSettingActivity.class);
            startActivity(intent);
            return true;
        }
    }
    @Override
    public void onInit(int initStatus) {
        //check for successful instantiation

        if (initStatus == TextToSpeech.SUCCESS) {
            if(_tts.isLanguageAvailable(Locale.KOREA)==TextToSpeech.LANG_AVAILABLE)
                _tts.setLanguage(Locale.KOREA);
        }
        else if (initStatus == TextToSpeech.ERROR) {
            //Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
            _ttsActive = true;
            _tts.speak("오류가 발생했습니다", TextToSpeech.QUEUE_FLUSH, null);
        }
    }

}
