package kr.co.dongduk.medineye.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.widget.ImageView;

import kr.co.dongduk.medineye.R;
import kr.co.dongduk.medineye.fragment.Main_Alarm_Fragment;
import kr.co.dongduk.medineye.fragment.Main_MediInfo_Fragment;
import kr.co.dongduk.medineye.fragment.Main_Nfc_Fragment;
import kr.co.dongduk.medineye.fragment.Main_Pharmacy_Fragment;
import kr.co.dongduk.medineye.fragment.Main_Setting_Fragment;

public class MainActivity extends NfcReadActivity {

    ViewPager viewPager = null;
    FragmentPagerAdapter adapterViewPager;

    ImageView indi1, indi2, indi3, indi4, indi5;

    MediaPlayer player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
         setContentView(R.layout.activity_main);//하단에 재정의

        startActivity(new Intent(this, SplashActivity.class));

        indi1 = (ImageView)findViewById(R.id.indicator1);
        indi2 = (ImageView)findViewById(R.id.indicator2);
        indi3 = (ImageView)findViewById(R.id.indicator3);
        indi4 = (ImageView)findViewById(R.id.indicator4);
        indi5 = (ImageView)findViewById(R.id.indicator5);

        player = new MediaPlayer();

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
        indi3.setImageResource(R.drawable.circle2);
        indi4.setImageResource(R.drawable.circle2);
        indi5.setImageResource(R.drawable.circle2);

        switch(pos){
            case 0 :  indi1.setImageResource(R.drawable.circle1); break;
            case 1 :  indi2.setImageResource(R.drawable.circle1); break;
            case 2 :  indi3.setImageResource(R.drawable.circle1); break;
            case 3 :   indi4.setImageResource(R.drawable.circle1); break;
            case 4 :  indi5.setImageResource(R.drawable.circle1); break;
        }
    }
    /***************** * * ViewPager Adapter * * **********************/
    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 5;

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
                case 0: return Main_MediInfo_Fragment.newInstance(0, "Page # 1"); //약정보검색
                case 1: return Main_Pharmacy_Fragment.newInstance(1, "Page # 2"); //약국정보제공
                case 2: return Main_Alarm_Fragment.newInstance(2, "Page # 3"); //약알람설정
                case 3 : return Main_Nfc_Fragment.newInstance(3, "Page # 4"); //NFC태그
                case 4 : return Main_Setting_Fragment.newInstance(4, "Page # 5"); //설정탭
                default:  return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

    }
    public void play(int position)
    {
        player.reset();
        switch (position){
            case 0 : player = MediaPlayer.create(this, R.raw.page_do); break;
            case 1 : player = MediaPlayer.create(this, R.raw.page_re); break;
            case 2 : player = MediaPlayer.create(this, R.raw.page_mi); break;
            case 3 : player = MediaPlayer.create(this, R.raw.page_pa); break;
            case 4 : player = MediaPlayer.create(this, R.raw.page_sol); break;
        }
        player.start();
    }

}
