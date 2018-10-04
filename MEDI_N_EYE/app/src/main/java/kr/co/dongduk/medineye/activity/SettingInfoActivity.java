package kr.co.dongduk.medineye.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import kr.co.dongduk.medineye.R;

public class SettingInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_info);

        TextView text = (TextView)findViewById(R.id.appname);
        text.setText("MEDI&EYE");
    }
}
