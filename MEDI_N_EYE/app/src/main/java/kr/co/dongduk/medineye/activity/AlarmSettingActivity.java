package kr.co.dongduk.medineye.activity;

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

import java.io.IOException;

import kr.co.dongduk.medineye.R;
import kr.co.dongduk.medineye.fragment.Alarm_Fragment_1;
import kr.co.dongduk.medineye.fragment.Alarm_Fragment_2;
import kr.co.dongduk.medineye.fragment.Alarm_Fragment_3;
import kr.co.dongduk.medineye.fragment.Alarm_Fragment_4;
import kr.co.dongduk.medineye.fragment.Alarm_Fragment_5;
import kr.co.dongduk.medineye.fragment.Alarm_Fragment_6;

public class AlarmSettingActivity extends FragmentActivity {

    public static int ALARM = 1;

    ViewPager viewPager = null;
    FragmentPagerAdapter adapterViewPager;

    MediaPlayer player;
    ImageView indi1, indi2, indi3, indi4, indi5, indi6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setting);
        playAllpage();
        player = new MediaPlayer();
        indi1 = (ImageView)findViewById(R.id.indicator1);
        indi2 = (ImageView)findViewById(R.id.indicator2);
        indi3 = (ImageView)findViewById(R.id.indicator3);
        indi4 = (ImageView)findViewById(R.id.indicator4);
        indi5 = (ImageView)findViewById(R.id.indicator5);
        indi6 = (ImageView)findViewById(R.id.indicator6);

        viewPager = (ViewPager)findViewById(R.id.viewpager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
//                Toast.makeText(AlarmSettingActivity.this,
//                        "Selected page position: " + position, Toast.LENGTH_SHORT).show();
                setIndicator(position);
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
        indi6.setImageResource(R.drawable.circle2);

        switch(pos){
            case 0 :  indi1.setImageResource(R.drawable.circle1); break;
            case 1 :  indi2.setImageResource(R.drawable.circle1); break;
            case 2 :  indi3.setImageResource(R.drawable.circle1); break;
            case 3 :  indi4.setImageResource(R.drawable.circle1); break;
            case 4 :  indi5.setImageResource(R.drawable.circle1); break;
            case 5 :  indi6.setImageResource(R.drawable.circle1); break;
        }
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 6;

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
                case 0: // Fragment # 0 - This will show FirstFragment
                    return Alarm_Fragment_1.newInstance(0, "Page #1");
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return Alarm_Fragment_2.newInstance(1, "Page # 2");
                case 2: // Fragment # 1 - This will show SecondFragment
                    return Alarm_Fragment_3.newInstance(2, "Page # 3");
                case 3 : return Alarm_Fragment_4.newInstance(3, "Page # 4");
                case 4 : return Alarm_Fragment_5.newInstance(5, "Page # 5");
                //return Frag5.newInstance(4, "Page # 5");
                case 5 : return Alarm_Fragment_6.newInstance(6, "Page # 6");
                default:
                    return null;
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
            case 5 : player = MediaPlayer.create(this, R.raw.page_ra); break;
        }
        player.start();
    }

    public void playAllpage()
    {
        MediaPlayer mp = new MediaPlayer();
        Uri uri=Uri.parse("android.resource://"+getPackageName()+"/raw/page_do_to_ra");
        try {
            Log.d("SUCCESS", uri.toString());
            mp.setDataSource(this, uri);
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("ERROR", e.toString());
        }
        mp.start();
    }
}
