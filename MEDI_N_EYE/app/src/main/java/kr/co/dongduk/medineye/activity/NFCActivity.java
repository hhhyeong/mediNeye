package kr.co.dongduk.medineye.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import kr.co.dongduk.medineye.R;
import kr.co.dongduk.medineye.adapter.mNfcAdapter;
import kr.co.dongduk.medineye.data.DatabaseHelper;
import kr.co.dongduk.medineye.data.NFCData;

public class NFCActivity extends NfcReadActivity  {

    LinearLayout exist, none;
    TextView txt;

    //ListView로 관리 할 것.

    DatabaseHelper dbHelper;

    mNfcAdapter adapter;
    ListView listView;

    ArrayList<NFCData> NfcList;

    LinearLayout addLinear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);



        init();
        settingLayout();
//        dbHelper.deleteAllData();
        addLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //NFC 등록
                Intent intent = new Intent(NFCActivity.this, NfcSelectActivity.class);
                startActivity(intent);

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(NFCActivity.this, NfcDeleteActivity.class);
                startActivity(intent);
//                팝업 띄워서 nfc 태그 받게 하면 됨.
                String id = NfcList.get(i).getNFCId();
                dbHelper.deleteNFCData(id);
                settingLayout();
//                NfcList.remove(i);
//                adapter.notifyDataSetChanged();
            }
        });

    }
    public void settingLayout()
    {
        dbHelper = new DatabaseHelper(this);

        NfcList = new ArrayList<>();
        NfcList = dbHelper.getNFCList();

        for (int i = 0; i < NfcList.size(); i++){
            if(NfcList.get(i).getNFCtext().equals(""))
                NfcList.remove(i);
        }
        if ( NfcList.size() != 0){
            none.setVisibility(View.GONE);
            exist.setVisibility(View.VISIBLE);

            Log.d("NFCList", "" + NfcList.size());
            Log.d("NFCData", "" + NfcList.get(0).getNFCtext());
            adapter = new mNfcAdapter(this, R.layout.item_nfc, NfcList);

            listView.setAdapter(adapter);
        } else {
            none.setVisibility(View.VISIBLE);
            exist.setVisibility(View.GONE);
        }
        /**
        if(sharedPreferencesNFC()){
            //화면에 NFC 쓰여진 정보를 보여줌
            none.setVisibility(View.GONE);
            exist.setVisibility(View.VISIBLE);
            txt.setText(getNfcData());
        } else {
            //안보여줌
            none.setVisibility(View.VISIBLE);
            exist.setVisibility(View.GONE);
        }
         */
    }
    public void init(){
        exist = (LinearLayout)findViewById(R.id.layout);
        none = (LinearLayout)findViewById(R.id.layout2);
        addLinear = (LinearLayout)findViewById(R.id.layout_add_nfc);
        txt = (TextView)findViewById(R.id.text);
        listView = (ListView)findViewById(R.id.listview);
        dbHelper = new DatabaseHelper(this);
    }
/*
    public String getNfcData()
    {
        SharedPreferences preferences = getSharedPreferences("NFCData", Context.MODE_PRIVATE);
        String id = preferences.getString("NFCID", null);
        String txt = preferences.getString("NFCTEXT", null);

        return id + "\n" + txt;
    }

    public boolean sharedPreferencesNFC () {
        SharedPreferences preferences = getSharedPreferences("NFCData", Context.MODE_PRIVATE);
        if (preferences.getBoolean("isExistData", false)) {
            return true;
        }
        return false;
    }
*/
    @Override
    public void onResume(){
        super.onResume();
        settingLayout();
    }
}
