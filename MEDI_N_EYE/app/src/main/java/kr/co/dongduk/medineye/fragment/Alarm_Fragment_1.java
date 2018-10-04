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


public class Alarm_Fragment_1 extends Fragment implements TextToSpeech.OnInitListener {

    Button AMbtn;
    private String title;
    private int page;

    /*TTS*/
    TextToSpeech _tts;
    boolean _ttsActive = false;
    String str;

    // newInstance constructor for creating fragment with arguments
    public static Alarm_Fragment_1 newInstance(int page, String title) {
        Alarm_Fragment_1 fragmentFirst = new Alarm_Fragment_1();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        args.putBoolean("isAM",isAM);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }
    //
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
        View view = inflater.inflate(R.layout.fragment_alarm_1, container, false);
//      TextView tvLabel = (TextView) view.findViewById(R.id.tvLabel);
//      tvLabel.setText(page + " -- " + title);

        AMbtn = (Button)view.findViewById(R.id.button1);
        if (isAM){
            AMbtn.setText(getString(R.string.alarm_am));
        }else {
            AMbtn.setText(getString(R.string.alarm_pm));
        }
        AMbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (isAM){
                    isAM = false;
                    AMbtn.setText(getString(R.string.alarm_pm));
                    str = getString(R.string.alarm_pm);
                    _ttsActive = true;
                    _tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);
                } else {
                    isAM = true;
                    AMbtn.setText(getString(R.string.alarm_am));
                    str = getString(R.string.alarm_am);
                    _ttsActive = true;
                    _tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
        return view;
    }

    static Boolean isAM = true;
    public static boolean getIsAM(){
        return isAM;
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