package cn.edu.gdmec.android.mobileguard.m4appmanager.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.edu.gdmec.android.mobileguard.m4appmanager.entity.AppInfo;

/**
 * Created by user on 2017/11/5.
 */

public class AppInfoParser {
    //获取手机里面的所有应用程序
    public static List<AppInfo> getAppInfo(Context context){
        //获取包管理器
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> packInfos = pm.getInstalledPackages(0);
        List<AppInfo> appInfos = new ArrayList<AppInfo>();
        for (PackageInfo packageInfo:packInfos){
            AppInfo appinfo = new AppInfo();
            String packname= packageInfo.packageName;
            appinfo.packageName=packname;
            Drawable icon=packageInfo.applicationInfo.loadIcon(pm);
            appinfo.icon = icon;
            String appname= packageInfo.applicationInfo.loadLabel(pm).toString();
            appinfo.appName = appname;
            //应用程序apk包的路径
            String apkpath = packageInfo.applicationInfo.sourceDir;
            appinfo.apkPath=apkpath;
            File file = new File(apkpath);
            long appSize = file.length();
            appinfo.appSize=appSize;
            //应用程序的安装位置
            int flags = packageInfo.applicationInfo.flags;//二进制映射 大Bit=map
            if( (ApplicationInfo.FLAG_EXTERNAL_STORAGE & flags)!=0){ //注意这里不懂
                appinfo.isInRoom =  false;

            }else {
                //手机内存
                appinfo.isInRoom = true;
            }

            if ((ApplicationInfo.FLAG_SYSTEM & flags )!=0){
                //系统应用
                appinfo.isUserApp=false;
            }else {
                appinfo.isUserApp = true;
            }

            appInfos.add(appinfo);
            appinfo=null;

        }
        return appInfos;
    }
}
