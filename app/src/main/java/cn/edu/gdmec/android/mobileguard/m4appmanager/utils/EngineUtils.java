package cn.edu.gdmec.android.mobileguard.m4appmanager.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;

import android.widget.Toast;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;

import cn.edu.gdmec.android.mobileguard.R;
import cn.edu.gdmec.android.mobileguard.m4appmanager.entity.AppInfo;



public class EngineUtils {
    //分享应用
    public static void shareApplication(Context context, AppInfo appInfo) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,
                "推荐您使用一款软件,名称叫:" + appInfo.appName
                        + "下载路径:https://play.google.com/store/apps/details?id="
                        + appInfo.packageName);
        context.startActivity(intent);
    }
    //开启应用程序

    public static void startApplication(Context context, AppInfo appInfo) {
        //打开这个应用程序的入口acticty
        PackageManager pm = context.getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(appInfo.packageName);
        if (intent != null) {
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "该应用没有启动界面", Toast.LENGTH_SHORT).show();
        }
    }

    //开启应用设置页面
    public static void SettingAppDetail(Context context, AppInfo appInfo) {
        Intent intent = new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("package:" + appInfo.packageName));
        context.startActivity(intent);
    }
    //卸载应用

    public static void uninstallApplication(Context context, AppInfo appInfo) {
        if (appInfo.isUserApp) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_DELETE);
            intent.setData(Uri.parse("package:" + appInfo.packageName));
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "系统应用无法卸载", Toast.LENGTH_LONG).show();
        }
    }


    //关于应用
    public static void AboutApplication(Context context, AppInfo appInfo) {
        PackageManager pm = context.getPackageManager();
        //获取版本信息
        String version = pm.getPackageArchiveInfo(appInfo.apkPath, PackageManager.GET_PERMISSIONS).versionName;//banbenhao

        //获取安装时间
        final String fileDir = appInfo.apkPath;
        String date = new Date(new File(fileDir).lastModified()).toLocaleString();

//        //获取证书信息
//        PackageInfo pis = pm.getPackageArchiveInfo(appInfo.apkPath, PackageManager.GET_SIGNATURES);
//        byte[] b = pis.signatures[0].toByteArray();
//
//            StringBuilder sb = new StringBuilder();
//            for (byte digestByte : b)
//            {
//                sb.append((Integer.toHexString((digestByte & 0xFF) | 0x100)).substring(1, 3));
//            }
        //获取证书信息
        String[] certMsg = new String[2];
        try {
            PackageManager pm1 = context.getPackageManager();
            PackageInfo  pis = pm1.getPackageInfo(appInfo.appName,PackageManager.GET_SIGNATURES);
            Signature[] sigs = pis.signatures; //签名
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");//获取证书
            X509Certificate cert = (X509Certificate) certFactory.generateCertificate(
                    new ByteArrayInputStream(sigs[0].toByteArray()));
            //获取证书发行者   可根据证书发行者来判断该应用是否被二次打包（被破解的应用重新打包后，签名与原包一定不同，据此可以判断出该应用是否被人做过改动）
            certMsg[0] = cert.getIssuerDN().toString();
            certMsg[1] = cert.getSubjectDN().toString();

            //获取权限信息
        String[] per = pm.getPackageArchiveInfo(appInfo.apkPath, PackageManager.GET_PERMISSIONS).requestedPermissions;//quanxian
        StringBuilder permissions = new StringBuilder();
        if (per != null) {
            for (int i = 0; i < per.length; i++) {
                permissions.append(per[i]);
                permissions.append("\n");
            }
        } else {
            permissions.append("没有需要的权限");
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(appInfo.appName);
            builder.setMessage("Version:" + version + "\n" + "Install time:" + date + "\n"+ "Certificate issuer:" + certMsg[0]+certMsg[1] + "\n" + "\n"  +"Permissions:"+ "\n"  + permissions);
        builder.setCancelable(false);
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
        }catch (CertificateException e) {

        } catch (Exception e) {


        }
    }

}
