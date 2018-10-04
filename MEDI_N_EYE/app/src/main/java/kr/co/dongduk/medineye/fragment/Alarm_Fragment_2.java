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


public class Alarm_Fragment_2 extends Fragment implements TextToSpeech.OnInitListener {
    // Store instance variables
    private String title;
    private int page;

    /*TTS*/
    TextToSpeech _tts;
    boolean _ttsActive = false;
    String str;


    // newInstance constructor for creating fragment with arguments
    public static Alarm_Fragment_2 newInstance(int page, String title) {
        Alarm_Fragment_2 fragmentFirst = new Alarm_Fragment_2();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        args.putInt("time", time);
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
        View view = inflater.inflate(R.layout.fragment_alarm_2, container, false);

        timeBtn = (Button) view.findViewById(R.id.button2);
        timeBtn.setText("" + time + "시");

        timeBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (time < 12) {
                    time++;
                } else {
                    time = 1;
                }

                timeBtn.setText("" + time + " 시");
                str = timeBtn.getText().toString();
                _ttsActive = true;
                _tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
//      TextView tvLabel = (TextView) view.findViewById(R.id.tvLabel);
//      tvLabel.setText(page + " -- " + title);
        return view;
    }

    static int time = 1;
    Button timeBtn;

    static public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public void onResume() {
        super.onResume();
        _tts = new TextToSpeech(getContext(), this);
    }

    @Override
    public void onInit(int initStatus) {
        //check for successful instantiation
        if (initStatus == TextToSpeech.SUCCESS) {
            if (_tts.isLanguageAvailable(Locale.US) == TextToSpeech.LANG_AVAILABLE)
                _tts.setLanguage(Locale.US);
        } else if (initStatus == TextToSpeech.ERROR) {
            //Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }

}