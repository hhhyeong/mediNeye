package kr.co.dongduk.medineye.service;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * Created by Owner on 2016-08-24.
 */
public class GPSInfo extends Service implements LocationListener {

    private final Context mContext;

    private Activity activity;
    // 현재 GPS 사용유무
    boolean isGPSEnabled = false;

    // 네트워크 사용유무
    boolean isNetworkEnabled = false;

    // GPS 상태값
    boolean isGetLocation = false;

    Location location;
    double lat; // 위도
    double lon; // 경도

    // 최소 GPS 정보 업데이트 거리 10미터
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;

    // 최소 GPS 정보 업데이트 시간 밀리세컨이므로 1분
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    protected LocationManager locationManager;

    public GPSInfo(Activity activity) {
        this.mContext = activity.getApplicationContext();
        this.activity = activity;
        getLocation();
    }

    final static int MY_PERMISSIONS_REQUEST_COARSE_LOCATION = 100;
    final static int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 200;
    final static int MY_PERMISSIONS_REQUEST_INTERNET = 300;

    public void checkPermission(){

        int permissionCheck_ACL = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionCheck_AFL = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionCheck_INTERNET = ContextCompat.checkSelfPermission(activity, Manifest.permission.INTERNET);

        if(permissionCheck_ACL== PackageManager.PERMISSION_DENIED || permissionCheck_AFL== PackageManager.PERMISSION_DENIED
                ||  permissionCheck_INTERNET== PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_COARSE_LOCATION);
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.INTERNET}, MY_PERMISSIONS_REQUEST_INTERNET);

        }else{
            Log.d("Permission", "권한있음");
        }
        // Activity에서 실행하는경우
        if ((ContextCompat.checkSelfPermission(activity,Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(activity,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)) {

            // 이 권한을 필요한 이유를 설명해야하는가?
            if ( (ActivityCompat.shouldShowRequestPermissionRationale(activity,Manifest.permission.ACCESS_COARSE_LOCATION)) ||
                    (ActivityCompat.shouldShowRequestPermissionRationale(activity,Manifest.permission.ACCESS_FINE_LOCATION))){

                // 다이어로그같은것을 띄워서 사용자에게 해당 권한이 필요한 이유에 대해 설명합니다
                // 해당 설명이 끝난뒤 requestPermissions()함수를 호출하여 권한허가를 요청해야 합니다

            } else {

                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_COARSE_LOCATION);
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_FINE_LOCATION);

                // 필요한 권한과 요청 코드를 넣어서 권한허가요청에 대한 결과를 받아야 합니다

            }
        }
    }
    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            // GPS 정보 가져오기
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // 현재 네트워크 상태 값 알아오기
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // GPS 와 네트워크사용이 가능하지 않을때 소스 구현
            } else {
                this.isGetLocation = true;
                // 네트워크 정보로 부터 위치값 가져오기
                if (isNetworkEnabled) {
                    try {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                // 위도 경도 저장
                                lat = location.getLatitude();
                                lon = location.getLongitude();
                                Log.d("latlng", ""+ lat +", " + lon);
                            }
                        }
                    } catch (SecurityException ex) { ex.printStackTrace();}
                }

                if (isGPSEnabled) {
                    if (location == null) {
                        try {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,  MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            if (locationManager != null) {
                                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    lat = location.getLatitude();
                                    lon = location.getLongitude();
                                }
                            }
                        } catch (SecurityException ex) { ex.printStackTrace();}
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    /**
     * GPS 종료
     * */
    public void stopUsingGPS(){
        if(locationManager != null) {
            try {
                locationManager.removeUpdates(GPSInfo.this);
            } catch (SecurityException ex) { ex.printStackTrace();}
        }
    }

    /**
     * 위도값을 가져옵니다.
     * */
    public double getLatitude(){
        if(location != null){
            lat = location.getLatitude();
        }
        return lat;
    }

    /**
     * 경도값을 가져옵니다.
     * */
    public double getLongitude(){
        if(location != null){
            lon = location.getLongitude();
        }
        return lon;
    }

    /**
     * GPS 나 wife 정보가 켜져있는지 확인합니다.
     * */
    public boolean isGetLocation() {
        return this.isGetLocation;
    }
    /**
     * GPS 정보를 가져오지 못했을때
     * 설정값으로 갈지 물어보는 alert 창
     * */
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

        alertDialog.setTitle("GPS 사용유무셋팅");
        alertDialog.setMessage("GPS 셋팅이 되지 않았을수도 있습니다. \n 설정창으로 가시겠습니까?");

                // OK 를 누르게 되면 설정창으로 이동합니다.
                alertDialog.setPositiveButton("Settings",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                activity.startActivity(intent);
                            }
                        });
        // Cancle 하면 종료 합니다.
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub

    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }
}