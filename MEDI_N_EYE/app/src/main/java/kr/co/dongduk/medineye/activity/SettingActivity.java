package kr.co.dongduk.medineye.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import kr.co.dongduk.medineye.R;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_setting);

        LinearLayout tutorialLayout = (LinearLayout)findViewById(R.id.tutorial);
        LinearLayout phoneLayout = (LinearLayout)findViewById(R.id.phone_num);
        LinearLayout appInfoLayout = (LinearLayout)findViewById(R.id.app_info);

        tutorialLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        phoneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (SettingActivity.this, SettingPhoneActivity.class);
                startActivity(intent);
            }
        });

        appInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (SettingActivity.this, SettingInfoActivity.class);
                startActivity(intent);
            }
        });
    }
}
