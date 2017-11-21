package cn.edu.gdmec.android.mobileguard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import cn.edu.gdmec.android.mobileguard.m1home.HomeActivity;
import cn.edu.gdmec.android.mobileguard.m1home.utils.MyUtils;
import cn.edu.gdmec.android.mobileguard.m1home.utils.VersionUpdateUtils;

public class SplashActivity extends AppCompatActivity {
    private TextView mVersionTV;
    private String mVersion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        mVersion = MyUtils.getVersion(getApplicationContext());
        mVersionTV = (TextView) findViewById(R.id.tv_splash_version);
        mVersionTV.setText("版本号:"+mVersion);
//        VersionUpdateUtils.DownloadCallback downloadCallback = new VersionUpdateUtils.DownloadCallback() {
//            @Override
//            public void afterDownload(String filename) {
//                MyUtils.installApk(SpalshActivity.this,filename);
//            }
//        };
//        final VersionUpdateUtils versionUpdateUtils = new VersionUpdateUtils(mVersion,SpalshActivity.this,downloadCallback,HomeActivity.class);
//        new Thread(){
//
//            @Override
//            public void run() {
//                versionUpdateUtils.getCloudVersion("http://android2017.duapp.com/updateinfo.html");
//            }
//        }.start();
        startActivity(new Intent(this, HomeActivity.class));
        finish();

    }
}