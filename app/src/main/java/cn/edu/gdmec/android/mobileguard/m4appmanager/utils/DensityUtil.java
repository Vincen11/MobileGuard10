package cn.edu.gdmec.android.mobileguard.m4appmanager.utils;

import android.content.Context;

/**
 * Created by user on 2017/11/5.
 */

public class DensityUtil {
    //dip转换像素px
    public static int dip2px(Context context,float dpValue){
        try {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int)(dpValue*scale + 0.5f);//为啥要转换?
        }catch (Exception e){
            e.printStackTrace();;
        }
        return (int)dpValue;
    }
    //px转dip
    public static int px2dip(Context context,float pxValue){
        try {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int)(pxValue/scale+0.5f);//为啥要转换?
        }catch (Exception e){
            e.printStackTrace();;
        }
        return (int)pxValue;
    }
}
