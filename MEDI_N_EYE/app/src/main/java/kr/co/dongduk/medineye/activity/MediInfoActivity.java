package kr.co.dongduk.medineye.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ImageView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;

import kr.co.dongduk.medineye.R;
import kr.co.dongduk.medineye.fragment.Medi_Barcode_Fragment;
import kr.co.dongduk.medineye.fragment.Medi_Voice_Fragment;

public class MediInfoActivity extends FragmentActivity {
    ViewPager viewPager = null;
    FragmentPagerAdapter adapterViewPager;

    ImageView indi1, indi2;

    MediaPlayer player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medi_info);

        indi1 = (ImageView)findViewById(R.id.indicator1);
        indi2 = (ImageView)findViewById(R.id.indicator2);

        player = new MediaPlayer();

        Uri uri=Uri.parse("android.resource://"+getPackageName()+"/raw/page_do_to_re");
        try {
            Log.d("SUCCESS", uri.toString());
            player.setDataSource(this, uri);
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("ERROR", e.toString());
        }
        player.start();

        viewPager = (ViewPager)findViewById(R.id.viewpager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
//                Toast.makeText(MainActivity.this,
//                        "Selected page position: " + position, Toast.LENGTH_SHORT).show();
                setIndicator(position); //indicator 색 변화
                play(position);

            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
            }
        });


    }

    public void setIndicator(int pos)
    {
        //init
        indi1.setImageResource(R.drawable.circle2);
        indi2.setImageResource(R.drawable.circle2);

        switch(pos){
            case 0 :  indi1.setImageResource(R.drawable.circle1); break;
            case 1 :  indi2.setImageResource(R.drawable.circle1); break;
        }
    }
    /***************** * * ViewPager Adapter * * **********************/
    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 2;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: return Medi_Voice_Fragment.newInstance(0, "Page # 1");
                case 1: return Medi_Barcode_Fragment.newInstance(1, "Page # 2");
                default:  return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

    }
    public void onDestroy(){
        super.onDestroy();
        if(player != null)
            player.release();
        player = null;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        System.out.println("the code is catch");

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if(result != null) {
            Log.d("ERROR", "result not null");
            if(result.getContents() == null) {

                Log.d("ERROR", "result get contents are null");
                //Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                //가져온 바코드 정보가 있으면 서버에 요청하는 코드를 수행하기 위해서 다음 화면으로 넘긴다.
                //Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                String barcode = result.getContents();

                Log.d("ERROR", barcode);

                Intent newintent = new Intent(this, MediBarcodeResultActivity.class);
                newintent.putExtra("type", BARCODE);
                newintent.putExtra("data",barcode);
                startActivity(newintent);
            }
        } else {
            Log.d("ERROR", "result is null");
            super.onActivityResult(requestCode, resultCode, intent);
        }
    }

    public final static int BARCODE = 1;
    public final static int VOICE = 0;
    public void play(int position)
    {
        player.reset();
        switch (position){
            case 0 : player = MediaPlayer.create(this, R.raw.page_do); break;
            case 1 : player = MediaPlayer.create(this, R.raw.page_re); break;
        }
        player.start();
    }
}
