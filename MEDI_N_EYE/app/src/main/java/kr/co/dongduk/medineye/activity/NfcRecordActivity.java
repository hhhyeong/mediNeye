package kr.co.dongduk.medineye.activity;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;

import kr.co.dongduk.medineye.R;
import kr.co.dongduk.medineye.data.DatabaseHelper;
import kr.co.dongduk.medineye.data.NFCData;


public class NfcRecordActivity extends AppCompatActivity {

    NfcAdapter mNfcAdapter;
    PendingIntent mPendingIntent;
    IntentFilter[] mFilters;
    String[][] mTechLists;

    TextView txt_tagId;
    LinearLayout getNfcIdLayout, recordLayout, saveNfcLayout;
    Button btnPlay, btnPlayStop, btnRecord, btnRecordStop, btnSetting;

    boolean isGetTagId = false;

    boolean playBtn = false, recordBtn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcrecord);

        checkPermission();
        init();
        checkDevice(); // 단말기 지원 여부 확인
        settingNfc(); // NFC Read, Write Setting
    }

    public void init() {
        txt_tagId = (TextView)findViewById(R.id.txt_nfc);
        getNfcIdLayout = (LinearLayout)findViewById(R.id.layout_1);
        recordLayout = (LinearLayout)findViewById(R.id.layout_2);
        saveNfcLayout = (LinearLayout)findViewById(R.id.layout_3);

        btnPlay = (Button)findViewById(R.id.btn_play);
        btnPlayStop = (Button)findViewById(R.id.btn_play_stop);
        btnRecord = (Button)findViewById(R.id.btn_record);
        btnRecordStop = (Button)findViewById(R.id.btn_record_stop);
        btnSetting = (Button)findViewById(R.id.btn_setting);

        btnPlay.setText("");
        btnRecord.setText("");
        btnPlayStop.setText("");
        //최초에는 false인 상태.
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recordBtn == false){
                    // 시작
                    startRecord();
//                    btnRecord.setText("녹음 중지");
                    btnRecord.setBackgroundResource(R.drawable.img_record_stop);
                    recordBtn = true;
                } else {
                    stopRecord();
//                    btnRecord.setText("녹음");
                    btnRecord.setBackgroundResource(R.drawable.img_record_start);
                    recordBtn = false;
                }
            }
        });

//        btnRecordStop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(playBtn == false){
                    // 시작

                    startPlay();
//                    btnPlay.setText("재생 중지");
                    playBtn = true;
                } else {
                    if (recordBtn == true){
//                        Toast.makeText(NfcRecordActivity.this, "녹음중입니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnPlayStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( playBtn == true) {
                    stopPlay();
//                    btnPlay.setText("재생");
                    playBtn = false;
                } else {
                    if (recordBtn == true){
//                        Toast.makeText(NfcRecordActivity.this, "녹음중입니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(NfcRecordActivity.this, "녹음완료. NFC 태그를 대어 쓰기를 완료하세요.", Toast.LENGTH_SHORT).show();

                saveNfcLayout.setVisibility(View.VISIBLE);
                recordLayout.setVisibility(View.GONE);
                getNfcIdLayout.setVisibility(View.GONE);
            }
        });
        getNfcIdLayout.setVisibility(View.VISIBLE);
        recordLayout.setVisibility(View.GONE);
        saveNfcLayout.setVisibility(View.GONE);
    }
    public void checkDevice ()
    {
        mNfcAdapter =  NfcAdapter.getDefaultAdapter(this) ;
        if (mNfcAdapter == null) {// NFC 미지원단말
//            Toast.makeText(getApplicationContext(), "NFC를 지원하지 않는 단말기입니다.", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void startPlay()
    {
        if (player != null){
            player.stop();
            player.release();
            player = null;
        }
//        Toast.makeText(NfcRecordActivity.this, "재생" , Toast.LENGTH_SHORT).show();
        try{
            player = new MediaPlayer();

            player.setDataSource(RECORD_FILE + getTagId() + RECORD_TYPE);
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopRecord()
    {
        if (recorder == null) return;
        recorder.stop();
        recorder.release();
        recorder = null;
//        Toast.makeText(NfcRecordActivity.this, "녹음 중지" , Toast.LENGTH_SHORT).show();
    }

    public void stopPlay()
    {
        if (player == null) return;
//        Toast.makeText(NfcRecordActivity.this, "재생 중지" , Toast.LENGTH_SHORT).show();
        player.stop();
        player.release();
        player = null;
    }
    public void startRecord ()
    {
        if (recorder != null){ // recorder에 뭐가 들어있으면 초기화해줌
            recorder.stop();
            recorder.release();
            recorder = null;
        }

        try {
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

            recorder.setOutputFile(RECORD_FILE + getTagId() + RECORD_TYPE);

//            Toast.makeText(NfcRecordActivity.this, "녹음을 시작합니다.", Toast.LENGTH_SHORT).show();
            recorder.prepare();
            recorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void settingNfc()
    {
        Intent targetIntent = new Intent(this, NfcRecordActivity.class);
        targetIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mPendingIntent = PendingIntent.getActivity(this, 0, targetIntent, 0);
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndef.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }

        mFilters = new IntentFilter[] { ndef, };

        mTechLists = new String[][] { new String[] { NfcF.class.getName() } };

        Intent passedIntent = getIntent();
        if (passedIntent != null) {
            String action = passedIntent.getAction();
            if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
                processTag(passedIntent);
            }
        }
    }

    /************************************
     * 여기서부턴 NFC 관련 메소드
     ************************************/
    public void onResume() {
        super.onResume();

        if (mNfcAdapter != null) {
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters,
                    mTechLists);
        }
    }

    public void onPause() {
        super.onPause();

        if (mNfcAdapter != null) {
            mNfcAdapter.disableForegroundDispatch(this);
        }
    }

    // NFC 태그 스캔시 호출되는 메소드
    // call back이니까 구분지어서 write하고 read하자. boolean으로 하든지...!
    @Override
    public void onNewIntent(Intent passedIntent) {
        // NFC 태그
        Tag tag = passedIntent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//        Toast.makeText(getApplicationContext(), "NFC TAG", Toast.LENGTH_SHORT).show();

        if (!isGetTagId) { // 아직 TagId값이 없다면. 첫번째 NFC 태그시.
            if (tag != null) {
                byte[] tagId = tag.getId();
//            readResult.append("태그 ID : " + toHexString(tagId) + "\n"); // TextView에 태그 ID 덧붙임
//                Toast.makeText(this, toHexString(tagId), Toast.LENGTH_SHORT).show();
                setTagId(toHexString(tagId));
                isGetTagId = true;
                txt_tagId.setText(toHexString(tagId));
                //Tag Id 확보했으면 layout 2 보이도록 해야함. 그래서 Record 받아야해.
                recordLayout.setVisibility(View.VISIBLE);
                getNfcIdLayout.setVisibility(View.GONE);
                saveNfcLayout.setVisibility(View.GONE);

                mUri = Uri.parse(RECORD_FILE + getTagId() + RECORD_TYPE); // /sdcard/UID.mp4로 uri 생성.
            }
        } else { //태그 ID가 확보가 되었고, Record 된 정보가 있다면. 두번째 태그시. uri write
            if (tag!=null){
                byte[] tagId = tag.getId();
                //첫번째 태그랑 맞는지 비교! 아니면 다르다고하고 write 중지.
                if(!getTagId().equals(toHexString(tagId))){
//                    Toast.makeText(NfcRecordActivity.this, "첫번째 태그와 다릅니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            if (passedIntent != null) {
                processTag_2(passedIntent); // processTag 바꾸기 Read랑 중복됨..
            }
        }
    }

    // NFC 태그 ID를 리턴하는 메소드
    public static final String CHARS = "0123456789ABCDEF";

    public static String toHexString(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; ++i) {
            sb.append(CHARS.charAt((data[i] >> 4) & 0x0F)).append(
                    CHARS.charAt(data[i] & 0x0F));
        }
        return sb.toString();
    }
    private void processTag_2(Intent intent) {
        // 감지된 태그를 가리키는 객체
        Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);


        /*
        // Record에 값이 들어있는지 확인한 후, 없으면 내용 입력하라는 Toast
        String s = writeText.getText().toString();
        // 아무것도 입력받지 않으면 태그에 쓰지 않음
        if (s.equals("")) {
            Toast.makeText(getApplicationContext(), "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
        }
        // 입력받은 값을 감지된 태그에 씀
        else {
            NdefMessage message = createTagMessage(s, TYPE_URI);
            writeTag(message, detectedTag);
        }
        */
        NdefMessage message = createTagMessage(mUri.toString(), TYPE_URI);
        writeTag(message, detectedTag);
    }

    // onNewIntent 메소드 수행 후 호출되는 메소드
    private void processTag(Intent passedIntent) {
        Parcelable[] rawMsgs = passedIntent
                .getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (rawMsgs == null) {
            return;
        }
        // 참고! rawMsgs.length : 스캔한 태그 개수
//        Toast.makeText(getApplicationContext(), "스캔 성공!", Toast.LENGTH_SHORT).show();
    }

    private NdefMessage createTagMessage(String msg, int type) {
        NdefRecord[] records = new NdefRecord[1];

        if (type == TYPE_URI) {
            records[0] = createUriRecord(msg.getBytes());
        }

        NdefMessage mMessage = new NdefMessage(records);

        return mMessage;
    }

    private NdefRecord createUriRecord(byte[] data) {
        return new NdefRecord(NdefRecord.TNF_ABSOLUTE_URI, NdefRecord.RTD_URI,
                new byte[0], data);
    }

    // 감지된 태그에 NdefMessage를 쓰는 메소드
    public  boolean writeTag(NdefMessage message, Tag tag) {
        int size = message.toByteArray().length;
        try {
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();

                if (!ndef.isWritable()) {return false; }
                if (ndef.getMaxSize() < size) { return false;}

                ndef.writeNdefMessage(message);
//                Toast.makeText(getApplicationContext(), message.getByteArrayLength() + "쓰기 성공!", Toast.LENGTH_SHORT).show();
                //20160824 finish

                DatabaseHelper dbHelper = new DatabaseHelper(this);

                if (dbHelper.isExistData(getTagId())) { // update
                    dbHelper.updataNFCData(getTagId(), message.toString());
                    Log.d("UPDATE", getTagId() + message.toString());
                } else { //add
                    dbHelper.saveNFCData(new NFCData(getTagId() ,message.toString()));
                    Log.d("INSERT", getTagId() +message.toString());

                }
                Log.d("NFCData", getTagId() + message.toString());
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

    private String tagId = "";
    public void setTagId (String id ) { this.tagId = id;}
    public String getTagId () {return this.tagId; }

    private final int TYPE_URI = 100;

    final private static String RECORD_FILE = "/sdcard/";
    final private static String RECORD_TYPE = ".mp4";
    Uri mUri;

    //record resource
    MediaPlayer player;
    MediaRecorder recorder;

    final static int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 100;
    final static int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 200;

    public void checkPermission(){

        int permissionCheck_RECORD = ContextCompat.checkSelfPermission(NfcRecordActivity.this, Manifest.permission.RECORD_AUDIO);
        int permissionCheck_WRITE = ContextCompat.checkSelfPermission(NfcRecordActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck_RECORD == PackageManager.PERMISSION_DENIED){
            Log.d("RECORD 권한 없음", "");
        } else {
            Log.d("RECORD 권한 있음", "");
        }

        if (ContextCompat.checkSelfPermission(NfcRecordActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(NfcRecordActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
        }

        if (permissionCheck_WRITE == PackageManager.PERMISSION_DENIED){
            Log.d("WRITE 권한 없음", "");
        } else {
            Log.d("WRITE 권한 있음", "");
        }

        if (ContextCompat.checkSelfPermission(NfcRecordActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(NfcRecordActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE :
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){}
                else {}
                return;
            case MY_PERMISSIONS_REQUEST_RECORD_AUDIO :
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){}
                else {}
                return;
        }
    }
}
