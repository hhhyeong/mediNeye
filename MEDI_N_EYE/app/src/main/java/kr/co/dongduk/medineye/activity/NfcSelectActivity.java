package kr.co.dongduk.medineye.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import kr.co.dongduk.medineye.R;

public class NfcSelectActivity extends AppCompatActivity {

    TextView text, record;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_select);

        text = (TextView)findViewById(R.id.txt);
        record = (TextView)findViewById(R.id.record);

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (NfcSelectActivity.this, NFCWriteActivity.class);
                startActivity(intent);
            }
        });

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (NfcSelectActivity.this, NfcRecordActivity.class);
                startActivity(intent);
            }
        });
    }
}
