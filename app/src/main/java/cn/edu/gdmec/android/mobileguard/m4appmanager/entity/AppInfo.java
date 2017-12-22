package cn.edu.gdmec.android.mobileguard.m4appmanager.entity;


import android.graphics.drawable.Drawable;

public class AppInfo {
    //应用程序的包名
    public String packageName;
    //图标
    public Drawable icon;
    //名称
    public String appName;
    //路径
    public String apkPath;
    //大小
    public long appSize;
    //是否是手机储存
    public boolean isInRoom;
    //是否是用户应用
    public boolean isUserApp;
    //是否选中 默认为false
    public boolean isSelected = false;

    public String getApplocation(boolean isInRoom){
        if (isInRoom){
            return "手机内存";
        }else {
            return "外部储存";
        }
    }
    public boolean isLock;

}
