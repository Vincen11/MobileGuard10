package cn.edu.gdmec.android.mobileguard.m5virusscan.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by user on 2017/11/13.
 */

//这个方法写得我好懵
public class AntiVirusDao {
    //检查某个md5是否是病毒 md5是什么?
    // return Null 代表扫描安全
    private static Context context;
    private static String dbname;
    public AntiVirusDao(Context context){
        this.context=context;
        dbname="data/data/"+context.getPackageName()+"/files/antivirus.db";
    }

    //调用apk文件的md5值匹配病毒数据库
    public String checkVirus(String md5){
        String desc = null;
        //病毒数据库
        SQLiteDatabase db = SQLiteDatabase.openDatabase(
                dbname,null,
                SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = db.rawQuery("select desc from datable where md5=?",
                new String[]{md5});//没看懂
        if (cursor.moveToNext()){
            desc=cursor.getString(0);
        }
        cursor.close();
        db.close();
        return desc;
    }
    //获取数据库版本号
    public String getVersion(){
        String version;
        String year;
        String day;
        String month;
        SQLiteDatabase db = SQLiteDatabase.openDatabase(
                dbname,null,
                SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = db.rawQuery("select * from version",null);
        while (cursor.moveToLast()) {//moveToLast()移动至最新一行 表示数据库最新的版本
            year = cursor.getInt(0)+""; //获取第一列的值,第一列的索引从0开始
            month = cursor.getInt(1)+"";//获取第二列的值
            day = cursor.getInt(2)+"";//获取第三列的值
            version=year+"."+month+"."+day;
            return version;
        }
        db.close();
        return "";


    }
}
