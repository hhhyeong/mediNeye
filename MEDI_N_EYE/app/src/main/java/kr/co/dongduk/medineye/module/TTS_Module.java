package kr.co.dongduk.medineye.module;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.Button;
import android.widget.EditText;

import java.util.Locale;

/**
 * Created by Owner on 2016-08-23.
 */
public class TTS_Module extends Activity implements TextToSpeech.OnInitListener {
        public TextToSpeech _tts;
        public boolean _ttsActive = false;
        public String str;
        EditText editT;
        Button bt_start;
        //public int iAlertVolume = 100;
        //public AudioManager am;
        //public int amStreamMusicMaxVol;

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            //볼륨조절
//            setVolumeControlStream(AudioManager.STREAM_MUSIC);
//            AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//            int sb2value = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//            am.setStreamVolume(AudioManager.STREAM_MUSIC,5, 0);
            //am.getStreamVolume(am.STREAM_MUSIC);


//        setContentView(R.layout.activity_main);

//        bt_start.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                str = editT.getText().toString();
//                //_tts.setLanguage(Locale.KOREA);
//                _ttsActive = true;
//                _tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);
//            }
//        });
        }

        @Override
        public void onResume(){
            super.onResume();
            _tts = new TextToSpeech(getApplicationContext(), this);
        }

        @Override
        public void onInit(int initStatus) {
            //check for successful instantiation
            if (initStatus == TextToSpeech.SUCCESS) {
                if(_tts.isLanguageAvailable(Locale.KOREA)==TextToSpeech.LANG_AVAILABLE)
                    _tts.setLanguage(Locale.KOREA);
                else if(_tts.isLanguageAvailable(Locale.KOREAN)==TextToSpeech.LANG_AVAILABLE)
                    _tts.setLanguage(Locale.KOREAN);
                else if(_tts.isLanguageAvailable(Locale.US)==TextToSpeech.LANG_AVAILABLE)
                    _tts.setLanguage(Locale.US);
            }
            else if (initStatus == TextToSpeech.ERROR) {
//                Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
            }
        }
    }