 package kr.co.dongduk.medineye.fragment;

 import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Calendar;
import java.util.Locale;

import kr.co.dongduk.medineye.R;
import kr.co.dongduk.medineye.activity.AlarmSettingActivity;
import kr.co.dongduk.medineye.service.AlarmReceiver;


 public class Alarm_Fragment_6 extends Fragment implements TextToSpeech.OnInitListener {
     // Store instance variables
     private String title;
     private int page;

     /*TTS*/
     TextToSpeech _tts;
     boolean _ttsActive = false;
     String str;


     // newInstance constructor for creating fragment with arguments
     public static Alarm_Fragment_6 newInstance(int page, String title) {
         Alarm_Fragment_6 fragmentFirst = new Alarm_Fragment_6();
         Bundle args = new Bundle();
         args.putInt("someInt", page);
         args.putString("someTitle", title);
         fragmentFirst.setArguments(args);
         return fragmentFirst;
     }

     // Store instance variables based on arguments passed
     @Override
     public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         page = getArguments().getInt("someInt", 0);
         title = getArguments().getString("someTitle");
         if (getArguments() != null) {
             time = getArguments().getInt("time");
             minute = getArguments().getInt("minute");
             repeat = getArguments().getInt("repeatCnt");
             isAM = getArguments().getBoolean("isAM");
         } else {
//             Toast.makeText(getActivity(), "Argument is null", Toast.LENGTH_SHORT).show();
         }

         preferences = this.getActivity().getSharedPreferences("AlarmData", Context.MODE_PRIVATE);

     }

     SharedPreferences preferences;
     int time = -1, minute = -1, repeat = -1;
     Boolean isAM = false;

     // Inflate the view for the fragment based on layout XML
     @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.fragment_alarm_6, container, false);
         //      TextView tvLabel = (TextView) view.findViewById(R.id.tvLabel);
         //      tvLabel.setText(page + " -- " + title);

         btn = (Button) view.findViewById(R.id.button7);
         btn.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View v) {
                 // TODO Auto-generated method stub
//                 Toast.makeText(getActivity(), "" + Alarm_Fragment_1.getIsAM() + Alarm_Fragment_2.getTime() + Alarm_Fragment_3.getMinute() + Alarm_Fragment_4.getRepeat(), Toast.LENGTH_SHORT).show();
                 // 알람매니저에 등록
                 onTimeSet(Alarm_Fragment_1.getIsAM(), Alarm_Fragment_2.getTime(), Alarm_Fragment_3.getMinute(), Alarm_Fragment_4.getRepeat());
             }
         });

         return view;
     }

     Button btn;

     public void onTimeSet(boolean isAm, int time, int minute, int repeat) {

         Calendar calNow = Calendar.getInstance();
         Calendar calSet = (Calendar) calNow.clone();

         if (isAm) {
             calSet.set(Calendar.HOUR_OF_DAY, time);
         } else {
             calSet.set(Calendar.HOUR_OF_DAY, time + 12);
         }
         calSet.set(Calendar.MINUTE, minute);
         calSet.set(Calendar.SECOND, 0);
         calSet.set(Calendar.MILLISECOND, 0);

         if (calSet.compareTo(calNow) <= 0) {
             //Today Set time passed, count to tomorrow
             calSet.add(Calendar.DATE, 1);
         }
         //        SharedPreferences.Editor editor = preferences.edit();
         //        editor.putInt("Alarm_Repeat", repeat);
         //        editor.putInt("RepeatCnt", 0);
         //        editor.commit();

//         Toast.makeText(getContext(), "" + calSet.getTime().getHours() + ":" + calSet.getTime().getMinutes(), Toast.LENGTH_SHORT).show();
         setAlarm(calSet);

     }

     private void setAlarm(Calendar targetCal) {

         try {
             Intent intent = new Intent(getContext(), AlarmReceiver.class);
             PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), AlarmSettingActivity.ALARM, intent, 0);
             AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
             //     alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
             //반복설정
             long interval = 24 * 60 * 60 * 1000; // 24시간
             alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), interval, pendingIntent);

//             Toast.makeText(getContext(), "알람등록완료", Toast.LENGTH_SHORT).show();
             str = "알람등록완료";
             _ttsActive = true;
             _tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);

             SharedPreferences mPreferences = getActivity().getSharedPreferences("AlarmData", Context.MODE_PRIVATE);
             SharedPreferences.Editor editor = mPreferences.edit();

             if (Alarm_Fragment_1.getIsAM())
                 editor.putString("Alarm_AMPM", "AM");
             else
                 editor.putString("Alarm_AMPM", "PM");

             editor.putString("Alarm_Time", ""
                     + String.format("%02d", Alarm_Fragment_2.getTime())
                     + ":" + String.format("%02d", Alarm_Fragment_3.getMinute()));
             editor.putString("Alarm_Explain", "" + Alarm_Fragment_5.getStt());
             editor.putInt("Alarm_Repeat", Alarm_Fragment_4.getRepeat());
//             editor.putBoolean("Alarm_Exist", true);
             editor.commit();

             getActivity().finish();
         } catch (Exception ex) {
             ex.printStackTrace();
         }
     }

     @Override
     public void onResume() {
         super.onResume();
         _tts = new TextToSpeech(getContext(), this);
     }

     @Override
     public void onInit(int initStatus) {
         //check for successful instantiation
         if (initStatus == TextToSpeech.SUCCESS) {
             if (_tts.isLanguageAvailable(Locale.US) == TextToSpeech.LANG_AVAILABLE)
                 _tts.setLanguage(Locale.US);
         } else if (initStatus == TextToSpeech.ERROR) {
             //Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
         }
     }
 }