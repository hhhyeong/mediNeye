package kr.co.dongduk.medineye.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.util.Log;

import kr.co.dongduk.medineye.activity.AlarmDialogActivity;

public class AlarmReceiver extends BroadcastReceiver {

	 @Override
	 public void onReceive(Context arg0, Intent arg1) {

		 SharedPreferences preferences = arg0.getSharedPreferences("AlarmData", Context.MODE_PRIVATE);
		 SharedPreferences settingPreferences = arg0.getSharedPreferences("SettingData", Context.MODE_PRIVATE);

//	  	Toast.makeText(arg0, "Alarm received!", Toast.LENGTH_LONG).show();
		 Log.d("AlarmReceived", "AlarmRing1");
		 try {
			String phoneNum = settingPreferences.getString("phone_number", null);
			Log.d("AlarmReceived", "AlarmRing2");
//			Toast.makeText(arg0, phoneNum, Toast.LENGTH_LONG).show();
			SendSMS(phoneNum, "친구의 알람이 울렸습니다.");
			Log.d("phoneNum", phoneNum);
			 
            Intent intent = new Intent(arg0, AlarmDialogActivity.class);
            PendingIntent pi = PendingIntent.getActivity(arg0, 0, intent, PendingIntent.FLAG_ONE_SHOT);

             pi.send();


         } catch (Exception e) {
			 e.printStackTrace();
			 Log.d("AlarmReceived", "AlarmRing3");
		 }


	  int repeat = preferences.getInt("Repeat", -1);
	  int repeatCnt = preferences.getInt("RepeatCnt", -1);

	  SharedPreferences.Editor editor = preferences.edit();
	  
	  if ( repeat == repeatCnt){

		  editor.clear();
		  releaseAlarm(arg0);
		  
	  } else { 
		  editor.putInt("RepeatCnt", repeatCnt++); //repeatCnt ++   
	  }
	  editor.commit();
	 }
    //			SendSMS(phoneNum, "친구의 알람이 울렸습니다.");
//			Log.d("phoneNum", phoneNum);
	 private void SendSMS(String phonenumber, String message) {
	        SmsManager smsManager = SmsManager.getDefault();
	        String sendTo = phonenumber;
	        String myMessage = message;
	        smsManager.sendTextMessage(sendTo, null, myMessage, null, null);
 	}
	 
	 public void releaseAlarm (Context context)
	 {
		 AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

		 Intent intent = new Intent(context, AlarmReceiver.class);
		 PendingIntent sender = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		 if (sender != null) {
		     am.cancel(sender);
		     sender.cancel();
		 }
	 }
}
