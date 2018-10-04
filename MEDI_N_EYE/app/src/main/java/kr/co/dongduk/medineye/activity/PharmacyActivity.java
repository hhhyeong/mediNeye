package kr.co.dongduk.medineye.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import kr.co.dongduk.medineye.R;
import kr.co.dongduk.medineye.adapter.PharmacyAdapter;
import kr.co.dongduk.medineye.data.PharmacyData;
import kr.co.dongduk.medineye.service.PharmacyConnectServer;
import kr.co.dongduk.medineye.service.PharmacyDataParser;

public class PharmacyActivity extends AppCompatActivity {

    final static int MY_PERMISSIONS_REQUEST_COARSE_LOCATION = 100;
    final static int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 200;

    ArrayList<PharmacyData> pharmacyList;
    PharmacyAdapter myAdapter;
    ListView listView;

    double lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy);

        Intent introIntent = getIntent();

        lat = introIntent.getDoubleExtra("now_lat", 0.0);
        lng = introIntent.getDoubleExtra("now_lng", 0.0);

        Log.d("LATLNG", "" +lng+ "/ "+lat );

        if (lat!=0 && lng!=0) {
            //api 작업 시작
            PharmacyConnectServer api = new PharmacyConnectServer(lat,lng);
            PharmacyDataParser parser = new PharmacyDataParser();

            String result = "";
            try {
                result = api.getApiResult();
                Log.d("RESULT", "" + result);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ExecutionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            parser.PullParserFromXML_MyPharmacy(result);

            //오류확인법
//        Log.d("LOG", parser.getArrayList().get(0).duty_addr);
//        Toast.makeText(MainActivity.this,  parser.getArrayList().get(0).duty_addr, Toast.LENGTH_SHORT).show();

            pharmacyList = parser.getArrayList();
            Log.d("ListSize", "" + pharmacyList.size());
            myAdapter = new PharmacyAdapter(this, R.layout.item_pharmacy, pharmacyList);
            listView = (ListView) findViewById(R.id.listView);
            listView.setAdapter(myAdapter);

//            //리스트 항목 클릭시
//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    PharmacyData currentData = pharmacyList.get(position);
//                    Log.d("Click", ""+position);
//                    Intent intent = new Intent(PharmacyActivity.this, PharmacyDetailActivity.class);
//                    intent.putExtra("data", currentData);
//                    startActivity(intent);
//                }
//            });


//            listView.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//
//                    Log.d("id", ""+ v.getTag());
//                    gestureDectector.onTouchEvent(event);
//                    return true;
//                }
//            });
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_COARSE_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 권한 허가
                    // 해당 권한을 사용해서 작업을 진행할 수 있습니다
                } else {
                    // 권한 거부
                    // 사용자가 해당권한을 거부했을때 해주어야 할 동작을 수행합니다
                }
                return;
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION :
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 권한 허가
                    // 해당 권한을 사용해서 작업을 진행할 수 있습니다
                } else {
                    // 권한 거부
                    // 사용자가 해당권한을 거부했을때 해주어야 할 동작을 수행합니다
                }
                return;
        }
    }

}
