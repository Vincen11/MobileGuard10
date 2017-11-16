package cn.edu.gdmec.android.mobileguard.m5virusscan.utils;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import cn.edu.gdmec.android.mobileguard.m5virusscan.VirusScanActicity;

import static android.content.ContentValues.TAG;

/**
 * Created by user on 2017/11/15.
 */

public class InstallUtils1 extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            installApk(context);
        }
    }

    // 安装Apk
    private void installApk(Context context) {

//        try {
//            Intent i = new Intent(Intent.ACTION_VIEW);
//            String filePath = CommonCons.APP_FILE_NAME;
//            i.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
//            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(i);a
//        } catch (Exception e) {
//            Log.e(TAG, "安装失败");
//            e.printStackTrace();
//        }
    }
}
