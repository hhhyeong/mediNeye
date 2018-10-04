package kr.co.dongduk.medineye.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import kr.co.dongduk.medineye.R;

public class SettingPhoneActivity extends AppCompatActivity {
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_phone);



        final EditText phoneEdit = (EditText)findViewById(R.id.editText);

        SharedPreferences shared = getSharedPreferences("SettingData", Context.MODE_PRIVATE);
        String txt = shared.getString("phone_number", "");

        if (txt == null){
//            Toast.makeText(this, "phone num 비어있음", Toast.LENGTH_SHORT).show();
            phoneEdit.setHint("- 없이 입력해주세요.");
        } else {
            phoneEdit.setText(txt);
//            Toast.makeText(this, "phone num -> " + txt, Toast.LENGTH_SHORT).show();
        }

        Button btn = (Button)findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    preferences = getSharedPreferences("SettingData", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("phone_number", phoneEdit.getText().toString());
                    editor.commit();
//
//                    SharedPreferences sharedPreferences = getSharedPreferences("SettingData", Context.MODE_PRIVATE);
//                    String returnTxt = sharedPreferences.getString("phone_number", "");

//                    Toast.makeText(SettingPhoneActivity.this, "연락처가 저장되었습니다." + phoneEdit.getText().toString(), Toast.LENGTH_SHORT).show();
                    finish();
                } catch (Exception ex) {ex.printStackTrace();}
            }
        });
    }
}
