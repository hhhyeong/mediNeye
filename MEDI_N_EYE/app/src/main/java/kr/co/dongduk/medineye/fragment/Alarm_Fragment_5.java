package kr.co.dongduk.medineye.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import kr.co.dongduk.medineye.R;


//STT 넣는 곳
public class Alarm_Fragment_5 extends Fragment  implements TextToSpeech.OnInitListener {
    // Store instance variables
    final static int GOOGLE_STT = 100;

    private String title;
    private int page;
    private static String stt;

    Button btnStt;


    /*TTS*/
    TextToSpeech _tts;
    boolean _ttsActive = false;
    String str;


    // newInstance constructor for creating fragment with arguments
    public static Alarm_Fragment_5 newInstance(int page, String title) {
        Alarm_Fragment_5 fragmentFirst = new Alarm_Fragment_5();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        args.putString("someStt", getStt());
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm_5, container, false);
        v = view;
        btnStt = (Button)view.findViewById(R.id.btn_stt);
        btnStt.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {

                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);         //intent 생성
                i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getActivity().getPackageName());   //음성인식을 호출한 패키지
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");                     //음성인식 언어 설정
                i.putExtra(RecognizerIntent.EXTRA_PROMPT, "말을 하세요.");                  //사용자에게 보여 줄 글자
//                str = "등록하실 알람 내용을 말하세요.";
//                _ttsActive = true;
//                _tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);
                startActivityForResult(i, GOOGLE_STT);
            }
        });

        return view;
    }

    View v;
    ArrayList<String> mResult;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if( resultCode == getActivity().RESULT_OK  && (requestCode == GOOGLE_STT)){      //결과가 있으면
            String key = "";
            if(requestCode == GOOGLE_STT)               //구글음성인식이면
                key = RecognizerIntent.EXTRA_RESULTS;   //키값 설정         //결과를 다이얼로그로 출력.

            mResult = new ArrayList<>();
            mResult = data.getStringArrayListExtra(key);      //인식된 데이터 list 받아옴.
            String[] result = new String[mResult.size()];         //배열생성. 다이얼로그에서 출력하기 위해
            mResult.toArray(result);

//            btnStt.setText(mResult.get(0).toString());

            setStt(mResult.get(0).toString());
        }
        else{                                             //결과가 없으면 에러 메시지 출력
            String msg = null;


            //내가 만든 activity에서 넘어오는 오류 코드를 분류
            switch(resultCode){
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

            if(msg != null)      //오류 메시지가 null이 아니면 메시지 출력
                Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }

    public static String getStt()
    {
        return stt;
    }
    public void setStt(String stt) { this.stt = stt;}

    @Override
    public void onResume(){
        super.onResume();
        _tts = new TextToSpeech(getContext(), this);
    }

    @Override
    public void onInit(int initStatus) {
        //check for successful instantiation
        if (initStatus == TextToSpeech.SUCCESS) {
            if(_tts.isLanguageAvailable(Locale.US)==TextToSpeech.LANG_AVAILABLE)
                _tts.setLanguage(Locale.US);
        }
        else if (initStatus == TextToSpeech.ERROR) {
            //Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }

}