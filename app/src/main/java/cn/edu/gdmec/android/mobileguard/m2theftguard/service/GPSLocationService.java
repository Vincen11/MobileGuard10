package cn.edu.gdmec.android.mobileguard.m2theftguard.service;

import android.app.Service;
import android.bluetooth.BluetoothClass;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;

import java.security.Provider;
import java.util.jar.Manifest;

/**
 * Created by user on 2017/10/29.
 */
//定位
public class GPSLocationService extends Service{
    private LocationManager lm;
    private MyListener listener;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        lm=(LocationManager)getSystemService(LOCATION_SERVICE);
        listener = new MyListener();
        //criteria　查询条件　ｔｒｕｅ只返回可用的位置提供者
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);//获取准确位置
        criteria.setCostAllowed(true);//允许产生开销
        String name = lm.getBestProvider(criteria,true);
        //权限检查
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            return;
        }
        lm.requestLocationUpdates(name,0,0,listener);
    }

    private class MyListener implements LocationListener{
        @Override
        public void onLocationChanged(Location location) {
            StringBuffer sb = new StringBuffer();
            sb.append("accuracy:"+location.getAccuracy()+"\n");
            sb.append("speed:"+location.getSpeed()+"\n");
            sb.append("Logitude:"+location.getLongitude()+"\n");
            sb.append("Latitude:"+location.getLatitude()+"\n");
            String result = sb.toString();
            SharedPreferences sp = getSharedPreferences("config",MODE_PRIVATE);
            String safenumber = sp.getString("safephone","");
            SmsManager.getDefault().sendTextMessage(safenumber,null,result,null,null);
            stopSelf();
        }

        //位置提供者状态发生变化时候调用
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
        //位置提供者可用的时候调用这个方法
        @Override
        public void onProviderEnabled(String provider) {

        }

        //位置提供者不可用的时候调用这个方法
        @Override
        public void onProviderDisabled(String provider) {

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lm.removeUpdates(listener);
        listener=null;
    }
}
