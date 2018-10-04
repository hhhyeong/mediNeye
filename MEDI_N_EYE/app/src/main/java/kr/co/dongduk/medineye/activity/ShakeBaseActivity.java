package kr.co.dongduk.medineye.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by KwonYeJin on 2016. 9. 22..
 */
public class ShakeBaseActivity extends FragmentActivity implements SensorEventListener {

    private long lastTime;
    private float speed, lastX, lastY, lastZ;
    private float x, y, z;
    public String result1, result2;
    public boolean checking = false;
    Intent i;

    public int cnt = 0;
    public Uri PhoneNumber;

    private final int GOOGLE_STT = 1000, MY_UI = 1001;
    private ArrayList<String> mResult1, mResult2;

    private static final int SHAKE_THRESHOLD = 7000;
    private static final int DATA_X = SensorManager.DATA_X;
    private static final int DATA_Y = SensorManager.DATA_Y;
    private static final int DATA_Z = SensorManager.DATA_Z;

    private SensorManager sensorManager;
    private Sensor accelerormeterSensor;

    SharedPreferences settingPreferences;
    String parentsPhone;

    private TextToSpeech myTTS;

    public String tts1 = "위급 상황시 연결할 대상을 말해주세요.";
    public String tts2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerormeterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
//
//        this.setRequestedOrientation(android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        settingPreferences = getSharedPreferences("SettingData", Context.MODE_PRIVATE);

    }

    @Override
    public void onStart() {
        super.onStart();
        if (accelerormeterSensor != null)
            sensorManager.registerListener(this, accelerormeterSensor,
                    SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (sensorManager != null)
            sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long currentTime = System.currentTimeMillis();
            long gabOfTime = (currentTime - lastTime);

            if (gabOfTime > 100) {
                lastTime = currentTime;
                x = event.values[SensorManager.DATA_X];
                y = event.values[SensorManager.DATA_Y];
                z = event.values[SensorManager.DATA_Z];

                speed = Math.abs(x + y + z - lastX - lastY - lastZ) / gabOfTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    //myTTS.speak(tts1, TextToSpeech.QUEUE_FLUSH, null);

                    Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);            //intent 생성
                    i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());    //음성인식을 호출한 패키지
                    i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");                            //음성인식 언어 설정
                    i.putExtra(RecognizerIntent.EXTRA_PROMPT, "위급 상황시 연결할 대상을 말해주세요.");        //음성인식 언어 설정


                    startActivityForResult(i, GOOGLE_STT);                                        //내가 만든 activity 실행
                }
                lastX = event.values[DATA_X];
                lastY = event.values[DATA_Y];
                lastZ = event.values[DATA_Z];
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && (requestCode == GOOGLE_STT || requestCode == MY_UI) && checking == false) {
            //결과가 있으면
            showReconfirm(requestCode, data);                //결과를 재확인
        } else if (resultCode == RESULT_OK && (requestCode == GOOGLE_STT || requestCode == MY_UI) && checking == true) {
            //결과가 있으면
            try {
                afterReconfirm(requestCode, data);
            } catch (Exception e){
//                Toast.makeText(this, "보호자 연락처가 없습니다", Toast.LENGTH_SHORT).show();
            }
        } else {                                                    //결과가 없으면 에러 메시지 출력
            String msg = null;

            //내가 만든 activity에서 넘어오는 오류 코드를 분류
            switch (resultCode) {
                case SpeechRecognizer.ERROR_AUDIO:
                    msg = "오디오 입력 중 오류가 발생했습니다.";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    msg = "단말에서 오류가 발생했습니다.";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    msg = "권한이 없습니다.";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    msg = "네트워크 오류가 발생했습니다.";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    msg = "일치하는 항목이 없습니다.";
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    msg = "음성인식 서비스가 과부하 되었습니다.";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    msg = "서버에서 오류가 발생했습니다.";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    msg = "입력이 없습니다.";
                    break;
            }
//            if (msg != null)        //오류 메시지가 null이 아니면 메시지 출력
//                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    private void showReconfirm(int requestCode, Intent data) {

        checking = true;
        String key = "";
        if (requestCode == GOOGLE_STT)               //구글음성인식이면
            key = RecognizerIntent.EXTRA_RESULTS;   //키값 설정
        else if (requestCode == MY_UI)               //내가 만든 activity 이면
            key = SpeechRecognizer.RESULTS_RECOGNITION;   //키값 설정

        mResult1 = data.getStringArrayListExtra(key);
        result1 = mResult1.get(0);

        Intent c = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);            //intent 생성
        c.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());    //음성인식을 호출한 패키지
        c.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");                            //음성인식 언어 설정
        c.putExtra(RecognizerIntent.EXTRA_PROMPT, result1 + "이 맞습니까? 맞으면 예, 틀리면 아니요를 말해주세요.");        //음성인식 언어 설정
        c.putExtra("cnt", 2);

        startActivityForResult(c, GOOGLE_STT);

    }

    private void afterReconfirm(int requestCode, Intent data) {

        String key = "";
        if (requestCode == GOOGLE_STT)               //구글음성인식이면
            key = RecognizerIntent.EXTRA_RESULTS;   //키값 설정
        else if (requestCode == MY_UI)               //내가 만든 activity 이면
            key = SpeechRecognizer.RESULTS_RECOGNITION;   //키값 설정

        mResult2 = data.getStringArrayListExtra(key);
        result2 = mResult2.get(0);

        if (mResult2.get(0).equals("네")) {
            if (result1.equals("보호자")) {
                Log.d("보호자", "네");
                PhoneNumber = Uri.parse(settingPreferences.getString("phone_number", null)); //전화와 관련된 Data는 'Tel:'으로 시작. 이후는 전화번호
                Intent shake_intent = new Intent();
                shake_intent.setAction(Intent.ACTION_DIAL);
                shake_intent.setData(Uri.parse("tel:" + PhoneNumber));
                startActivity(shake_intent);

            } else {
                PhoneNumber = Uri.parse(result1); //전화와 관련된 Data는 'Tel:'으로 시작. 이후는 전화번호
                Intent shake_intent = new Intent();
                shake_intent.setAction(Intent.ACTION_DIAL);
                shake_intent.setData(Uri.parse("tel:" + PhoneNumber));
                startActivity(shake_intent);
            }
        } else {
            Log.d("보호자", "아니오");
            checking = false;
        }

    }
}