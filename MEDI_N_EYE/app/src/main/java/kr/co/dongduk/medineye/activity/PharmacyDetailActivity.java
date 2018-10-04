package kr.co.dongduk.medineye.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import kr.co.dongduk.medineye.R;
import kr.co.dongduk.medineye.data.PharmacyData;
import kr.co.dongduk.medineye.module.TTS_Module;

public class PharmacyDetailActivity extends TTS_Module {

    TextView tvCnt;
    TextView tvDis;
    TextView tvAddr;
    TextView tvDiv;
    TextView tvName;
    TextView tvTel;
    TextView tvEt;
    TextView tvLat;
    TextView tvLong;
    TextView tvSt;
    PharmacyData data;
    GestureDetector mGesDetector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_detail);

        _tts = new TextToSpeech(PharmacyDetailActivity.this, this);

        tvCnt = (TextView)findViewById(R.id.detTvCnt);
        tvDis = (TextView)findViewById(R.id.detTvDis);
        tvAddr = (TextView)findViewById(R.id.detTvAddr);
        tvDiv = (TextView)findViewById(R.id.detTvDiv);
        tvName = (TextView)findViewById(R.id.detTvName);
        tvTel = (TextView)findViewById(R.id.detTvTel);
        tvEt = (TextView)findViewById(R.id.detTvEt);
        tvLat = (TextView)findViewById(R.id.detTvLat);
        tvLong = (TextView)findViewById(R.id.detTvLong);
        tvSt = (TextView)findViewById(R.id.detTvSt);

        //호출할때 사용한 인텐트 가져오기
        Intent intent = getIntent();

        //인텐트에서 데이터 가져오기
        data = (PharmacyData) intent.getSerializableExtra("data");

        //가져온 데이터로 화면 설정
        tvCnt.setText(Integer.toString(data.getCnt()));
        tvDis.setText(Double.toString(data.getDistance()));

        tvDiv.setText(data.getDuty_div());
        tvTel.setText(data.getDuty_tel());
        tvEt.setText(Integer.toString(data.getEnd_time()));
        tvLat.setText(Double.toString(data.getLatitude()));
        tvLong.setText(Double.toString(data.getLongitude()));



        tvAddr.setText(handlingAddr(data.getDuty_addr()));
        tvName.setText(data.getDuty_name());
        Button callBtn = (Button)findViewById(R.id.btnCall);
        callBtn.setText(data.getDuty_tel());
        tvSt.setText(handlingTime(Integer.toString(data.getStart_time())) + " ~ " + handlingTime(Integer.toString(data.getEnd_time())));


        mGesDetector = new GestureDetector(this, new cGestureListener());
        Button btnCall = (Button) findViewById(R.id.btnCall);
        btnCall.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGesDetector.onTouchEvent(event);
                return true;
             }
        });

        _ttsActive = true;
        _tts.speak(tvName.getText().toString() + "\n" + tvAddr.getText().toString() + "\n" + "영업 시간은 " + data.getStart_time() + "부터 " + data.getEnd_time() + "입니다.\n", TextToSpeech.QUEUE_FLUSH, null);
    }

    public String handlingTime (String time)
    {
        int timeText = Integer.parseInt(time);
        String returnText = "";
        StringBuffer sb;
        if ( timeText < 1200){
            returnText += "AM ";
        } else {
            returnText += "PM ";
            timeText -= 1200;
        }
        sb = new StringBuffer(""+timeText);

        if ( timeText <= 959){
            sb.insert(1,":");
        } else {
            sb.insert(2,":");
        }

        return returnText + sb.toString();
    }

    public String handlingAddr (String addr){
        String[] addrArr = addr.split(" ");
        String str = "";
        for (int i = 0; i < addrArr.length; i++){
            str += addrArr[i] + " ";
            if ( i == 1 )
                str += "\n";
        }
        return str;
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancel:
                finish();
                break;
            case R.id. infoLayout :
                Log.d("infoClick", "infoclick");
                _ttsActive = true;
                _tts.speak(tvName.getText().toString() + "\n" + tvAddr.getText().toString() + "\n" + "영업 시간은 " + data.getStart_time() + "부터 " + data.getEnd_time() + "입니다.\n", TextToSpeech.QUEUE_FLUSH, null);
                break;
        }
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
            _tts.speak(tvName.getText().toString() + "에 전화 걸기", TextToSpeech.QUEUE_FLUSH, null);
            return false;
        }

        /****************
         * 더블탭 : 전화걸기
         *****************/
        public boolean onDoubleTap(MotionEvent e) {
            Log.d("Double_Tap", "Yes, double Clicked");
            Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+data.getDuty_tel()));
            startActivity(dialIntent);
            return true;
        }
    }
}