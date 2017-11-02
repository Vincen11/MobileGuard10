package cn.edu.gdmec.android.mobileguard.m3communicationguard.entity;

/**
 * Created by user on 2017/10/31.
 */

public class BlackContactInfo {
    public String phoneNumber;//黑名单号码
    public String contactName;//黑名单号码名称
    public int mode;//拦截模式 1电话 2短信  3电话和短信

    public String getModeString(int mode){
        switch (mode){
            case 1:
                return "电话拦截";
            case 2:
                return "短信拦截";
            case 3:
                return "电话,短信拦截";
        }
        return "";
    }
}
