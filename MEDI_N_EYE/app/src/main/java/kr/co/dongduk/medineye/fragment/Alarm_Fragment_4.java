package kr.co.dongduk.medineye.fragment;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Locale;

import kr.co.dongduk.medineye.R;


public class Alarm_Fragment_4 extends Fragment  implements TextToSpeech.OnInitListener {
    // Store instance variables
    private String title;
    private int page;

    /*TTS*/
    TextToSpeech _tts;
    boolean _ttsActive = false;
    String str;


    // newInstance constructor for creating fragment with arguments
    public static Alarm_Fragment_4 newInstance(int page, String title) {
        Alarm_Fragment_4 fragmentFirst = new Alarm_Fragment_4();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        args.putInt("repeat", repeatCnt);
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
        View view = inflater.inflate(R.layout.fragment_alarm_4, container, false);
//      TextView tvLabel = (TextView) view.findViewById(R.id.tvLabel);
//      tvLabel.setText(page + " -- " + title);

        repeatCnt = NONE;

        repeatBtn = (Button)view.findViewById(R.id.button4);
        repeatBtn.setText("없음");

        repeatBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                switch (repeatCnt){
                    case NONE :
                        repeatBtn.setText("3일");
                        str = repeatBtn.getText().toString();
                        _ttsActive = true;
                        _tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);
                        repeatCnt = THREEDAYS; break;
                    case THREEDAYS :
                        repeatBtn.setText("5일");
                        str = repeatBtn.getText().toString();
                        _ttsActive = true;
                        _tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);
                        repeatCnt = FIVEDAYS; break;
                    case FIVEDAYS :
                        repeatBtn.setText("7일");
                        str = repeatBtn.getText().toString();
                        _ttsActive = true;
                        _tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);
                        repeatCnt = SEVENDAYS; break;
                    case SEVENDAYS :
                        repeatBtn.setText("없음");
                        str = repeatBtn.getText().toString();
                        _ttsActive = true;
                        _tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);
                        repeatCnt = 0;break;
                }
            }
        });
        return view;
    }

    Button repeatBtn;

    static int repeatCnt;

    final int NONE = 0;
    final int THREEDAYS = 1;
    final int FIVEDAYS = 2;
    final int SEVENDAYS = 3;

    static public int getRepeat(){
        return repeatCnt;
    }

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