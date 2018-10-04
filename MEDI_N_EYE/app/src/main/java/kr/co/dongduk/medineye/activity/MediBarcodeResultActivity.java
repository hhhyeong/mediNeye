package kr.co.dongduk.medineye.activity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

import kr.co.dongduk.medineye.R;
import kr.co.dongduk.medineye.data.BarcodeData;
import kr.co.dongduk.medineye.module.TTS_Module;
import kr.co.dongduk.medineye.service.BarcodeConnectServer;
import kr.co.dongduk.medineye.service.BarcodeDataParser;

public class MediBarcodeResultActivity extends TTS_Module {

    BarcodeData myBarcode;
    int type;
//
//    /*TTS*/
//    TextToSpeech _tts;
//    boolean _ttsActive = false;
//    String str;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_result);

        _tts = new TextToSpeech(MediBarcodeResultActivity.this, this);

        Intent intent = getIntent();
        String data = intent.getStringExtra("data");

        final TextView name = (TextView)findViewById(R.id.name);
        final TextView description = (TextView)findViewById(R.id.description);
        final TextView company = (TextView)findViewById(R.id.company);
        final TextView ctgList = (TextView)findViewById(R.id.ctgList);

        LinearLayout existLayout, noneLayout;
        existLayout = (LinearLayout)findViewById(R.id.layout_exist);
        noneLayout = (LinearLayout)findViewById(R.id.layout_none);

        type = intent.getIntExtra("type", -1);

        BarcodeConnectServer server = new BarcodeConnectServer(type, data);
        BarcodeDataParser parser = new BarcodeDataParser();

        //정보가 없는 경우 한번 더 핸들링을 해줘야 함.

        String result = "";
        try {
            result = server.call();
            Log.d("Log",result);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        str = "";

        parser.PullParserFromXML(result);
        myBarcode = parser.getBarcodeData();

        if(myBarcode.getName() == null) {
            existLayout.setVisibility(View.GONE);
            noneLayout.setVisibility(View.VISIBLE);

            name.setText("등록된 약이 없습니다.");
            description.setText("");
            company.setText("");
            ctgList.setText("");
            str = "등록된 약이 없습니다.";

            _ttsActive = true;
            _tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);
            //tts를 위한 토스트
//            Toast.makeText(this,"터치하면\n등록된 약이 없습니다. 들려줌",Toast.LENGTH_LONG).show();
            noneLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    _ttsActive = true;
                    _tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);              }
            });
        }
        else {
            existLayout.setVisibility(View.VISIBLE);
            noneLayout.setVisibility(View.GONE);

            if (type == 0){name.setText(data);}
            else {
                int index = myBarcode.getName().indexOf("(");
                Log.d("CHECK", ""+checkIndexOfString(myBarcode.getName()));
                if ( checkIndexOfString(myBarcode.getName()) != -1){
                    index = checkIndexOfString(myBarcode.getName());
                }
                name.setText(myBarcode.getName().substring(0, index));
            }
            description.setText(myBarcode.getDescription());
            company.setText(myBarcode.getCompany());
            ctgList.setText(myBarcode.getCategorizeList());

            //tts를 위한 토스트
//            Toast.makeText(this, "터치하면\n출력된 약정보 들려줌", Toast.LENGTH_LONG).show();
            str = "" + name.getText().toString() + "\n" + ctgList.getText().toString() + "\n" + description.getText().toString() + "\n" + company.getText().toString() ;


            _ttsActive = true;
            _tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);

            existLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    _ttsActive = true;
                    _tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);
                }
            });
        }
    }

    public int checkIndexOfString(String str){
        int check = -1;
        for (int i = 0; i < str.length(); i++){
            check = str.charAt(i);
            Log.d("CHECK", ""+str.charAt(i));
            if (Character.isDigit(check)){
                return i;
            }
        }
        return -1;
    }
}