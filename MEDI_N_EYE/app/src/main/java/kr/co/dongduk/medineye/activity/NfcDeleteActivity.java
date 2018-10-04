package kr.co.dongduk.medineye.activity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.common.base.Charsets;
import com.google.common.primitives.Bytes;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;

import kr.co.dongduk.medineye.R;
import kr.co.dongduk.medineye.data.DatabaseHelper;
import kr.co.dongduk.medineye.data.NFCData;

public class NfcDeleteActivity extends AppCompatActivity {


    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;

    public static final int TYPE_TEXT = 1;
    public static final int TYPE_URI = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_delete);

        // NFC 관련 객체 생성
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        Intent intent = new Intent(this, getClass())
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
    }

    /************************************
     * 여기서부턴 NFC 관련 메소드
     ************************************/
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
            nfcAdapter
                    .enableForegroundDispatch(this, pendingIntent, null, null);
        }
    }

    // NFC 태그 스캔시 호출되는 메소드
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
//        Toast.makeText(getApplicationContext(), "NFC TAG", Toast.LENGTH_SHORT).show();

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag!=null){
            byte[] tagId = tag.getId();
            TAG_ID = toHexString(tagId);
        }
        if (intent != null) {
            processTag(intent); // processTag 메소드 호출
        }
    }
    public static final String CHARS = "0123456789ABCDEF";
    public static String TAG_ID = null;

    public static String toHexString(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; ++i) {
            sb.append(CHARS.charAt((data[i] >> 4) & 0x0F)).append(
                    CHARS.charAt(data[i] & 0x0F));
        }
        return sb.toString();
    }

    //Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

    // onNewIntent 메소드 수행 후 호출되는 메소드
    private void processTag(Intent intent) {
        // EditText에 입력된 값을 가져옴

        // 감지된 태그를 가리키는 객체
        Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        NdefMessage message = createTagMessage("", TYPE_TEXT);
        writeTag(message, detectedTag);

    }

    // 감지된 태그에 NdefMessage를 쓰는 메소드
    public boolean writeTag(NdefMessage message, Tag tag) {
        int size = message.toByteArray().length;
        try {
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();
                if (!ndef.isWritable()) {
                    return false;
                }

                if (ndef.getMaxSize() < size) {
                    return false;
                }

                ndef.writeNdefMessage(message);
//                Toast.makeText(getApplicationContext(), message.getByteArrayLength() + "삭제 성공!", Toast.LENGTH_SHORT).show();
                SharedPreferences preferences = getSharedPreferences("NFCData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

//                editor.putString("NFC_ID", "" + Alarm_Fragment_2.getTime() + " : " + Alarm_Fragment_3.getMinute());
                editor.putString("NFC_TEXT", ""+ message.getByteArrayLength());

                editor.commit();
                //20160824 finish

                DatabaseHelper dbHelper = new DatabaseHelper(this);

                if (dbHelper.isExistData(TAG_ID)) { // update
                    dbHelper.updataNFCData(TAG_ID, "");


                } else { //add
                    dbHelper.saveNFCData(new NFCData(TAG_ID,""));
                    Log.d("INSERT", TAG_ID + "");

                }
                finish();
            } else {
//                Toast.makeText(this, message.getByteArrayLength() + " / 포맷되지 않은 태그이므로 먼저 포맷하고 데이터를 씁니다.",
//                        Toast.LENGTH_SHORT).show();

                formatable = NdefFormatable.get(tag);
                if (formatable != null) {
                    try {
                        formatable.connect();
                        Log.d("message", message.toString());
                        formatable.format(message);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        Log.d("FORMAT EXCEPTION", ex.toString());
                    }
                }

                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
    NdefFormatable formatable;
    /**
     * Create a new tag message
     *
     * @param msg
     * @param type
     * @return
     */
    private NdefMessage createTagMessage(String msg, int type) {
        NdefRecord[] records = new NdefRecord[1];

        if (type == TYPE_TEXT) {
            records[0] = createTextRecord(msg, Locale.KOREAN, true);
        } else if (type == TYPE_URI) {
            records[0] = createUriRecord(msg.getBytes());
        }

        NdefMessage mMessage = new NdefMessage(records);

        return mMessage;
    }

    private NdefRecord createTextRecord(String text, Locale locale,
                                        boolean encodeInUtf8) {
        final byte[] langBytes = locale.getLanguage().getBytes(
                Charsets.US_ASCII);
        final Charset utfEncoding = encodeInUtf8 ? Charsets.UTF_8 : Charset
                .forName("UTF-16");
        final byte[] textBytes = text.getBytes(utfEncoding);
        final int utfBit = encodeInUtf8 ? 0 : (1 << 7);
        final char status = (char) (utfBit + langBytes.length);
        final byte[] data = Bytes.concat(new byte[] { (byte) status },
                langBytes, textBytes);
        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT,
                new byte[0], data);
    }

    private NdefRecord createUriRecord(byte[] data) {
        return new NdefRecord(NdefRecord.TNF_ABSOLUTE_URI, NdefRecord.RTD_URI,
                new byte[0], data);
    }

}
