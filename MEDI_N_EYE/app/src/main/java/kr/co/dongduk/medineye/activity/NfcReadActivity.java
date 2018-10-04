package kr.co.dongduk.medineye.activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.media.MediaPlayer;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.speech.tts.TextToSpeech;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import kr.co.dongduk.medineye.service.nfc.NdefMessageParser;
import kr.co.dongduk.medineye.service.nfc.ParsedRecord;
import kr.co.dongduk.medineye.service.nfc.TextRecord;
import kr.co.dongduk.medineye.service.nfc.UriRecord;


/**
 * Created by Owner on 2016-08-24.
 */
public class NfcReadActivity extends ShakeBaseActivity implements TextToSpeech.OnInitListener{
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        Intent intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
    }
    @Override
    protected void onPause() {
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
            _tts = new TextToSpeech(getApplicationContext(), this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag != null) {
            byte[] tagId = tag.getId();
//            Toast.makeText(this, toHexString(tagId), Toast.LENGTH_SHORT).show();
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs == null) {
//                Toast.makeText(this, "rawMsg 비어있음", Toast.LENGTH_SHORT).show();
                return; }
            else {
                NdefMessage[] msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++){
                    msgs[i] = (NdefMessage)rawMsgs[i];
                    showTag(msgs[i]);
                }
            }
        }
    }
    private int showTag(NdefMessage mMessage) {
        List<ParsedRecord> records = NdefMessageParser.parse(mMessage);
        final int size = records.size();
        for (int i = 0; i < size; i++) {
            ParsedRecord record = records.get(i);

            int recordType = record.getType();
            String recordStr = ""; // NFC 태그로부터 읽어들인 텍스트 값
            if (recordType == ParsedRecord.TYPE_TEXT) {
                recordStr = "TEXT : " + ((TextRecord) record).getText();
                //TTS
                _ttsActive = true;
                _tts.speak(((TextRecord) record).getText(), TextToSpeech.QUEUE_FLUSH, null);
            } else if (recordType == ParsedRecord.TYPE_URI) {
                recordStr = "URI : " + ((UriRecord) record).getUri().toString();
                //재생
                MediaPlayer mp = new MediaPlayer();
                try {
                    mp.setDataSource(((UriRecord) record).getUri().toString());
                    mp.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mp.start();
            }

//            readResult.append(recordStr + "\n"); // 읽어들인 텍스트 값을 TextView에 덧붙임
//            Toast.makeText(this, recordStr, Toast.LENGTH_SHORT).show();
        }
        return size;
    }

    public static final String CHARS = "0123456789ABCDEF";

    public static String toHexString(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; ++i) {
            sb.append(CHARS.charAt((data[i] >> 4) & 0x0F))
                    .append(CHARS.charAt(data[i] & 0x0F));
        }
        return sb.toString();
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

    public TextToSpeech _tts;
    public boolean _ttsActive = false;
    public String str;
}
