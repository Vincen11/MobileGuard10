package cn.edu.gdmec.android.mobileguard.m5virusscan.utils;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.File;

import cn.edu.gdmec.android.mobileguard.R;

import static android.content.ContentValues.TAG;

public class DownloadUtils1{

        String path;
        public static final String BROADCAST_ACTION =
                "com.example.android.threadsample.BROADCAST";
        public static final String EXTENDED_DATA_STATUS =
                "com.example.android.threadsample.STATUS";
        public long downloadApk(String url, String targetFile, Context context){

            DownloadManager.Request request = new DownloadManager.Request( Uri.parse(url));
            request.setAllowedOverRoaming(false);

            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
            request.setVisibleInDownloadsUi(true);

            request.setDestinationInExternalPublicDir("/download",targetFile);
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            long mTaskid = downloadManager.enqueue(request);
            Intent localIntent = new Intent(BROADCAST_ACTION);
            localIntent.putExtra(EXTENDED_DATA_STATUS,mTaskid);
            //查询下载信息
            DownloadManager.Query query=new DownloadManager.Query();

            try{
                boolean isGoging=true;
                while(isGoging){
                    Cursor cursor = downloadManager.query(query);
                    if (cursor != null && cursor.moveToFirst()) {
                        int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                        switch(status){
                            //如果下载状态为成功
                            case DownloadManager.STATUS_SUCCESSFUL:
                                isGoging=false;
                                Toast.makeText(context, "下载完成", Toast.LENGTH_LONG).show();
                                UpdateSQL(context,targetFile);
                                File srcFile = new File(Environment.getExternalStorageDirectory()+"/download/antivirus.db");
                                File tmpFile = new File("/data/data/"+context.getPackageName()+"/files/");

                                if (srcFile.renameTo(new File(tmpFile + "antivirus.db"))) {
                                    Toast.makeText(context, "移动成功!", Toast.LENGTH_LONG).show();
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setTitle("shibai");
                                    builder.setMessage(srcFile.toString());
                                    builder.setCancelable(false);
                                    builder.setNegativeButton("暂不升级", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    });
                                    builder.show();
                                }

                                break;
                            case DownloadManager.STATUS_FAILED:
                                isGoging=false;
                                Toast.makeText(context, "下载失败", Toast.LENGTH_LONG).show();
                                break;

                        }
                    }
                    if(cursor!=null){
                        cursor.close();
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            return mTaskid;
        }

    public  void UpdateSQL(Context context, String targetFile) {
        String dbname=Environment.getDownloadCacheDirectory().toString()+targetFile;

    }


}


