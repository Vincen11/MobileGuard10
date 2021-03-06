package cn.edu.gdmec.android.mobileguard.m3communicationguard.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.edu.gdmec.android.mobileguard.m3communicationguard.AddBlackNumberActivity;
import cn.edu.gdmec.android.mobileguard.m3communicationguard.db.BlackNumberOpenHelper;
import cn.edu.gdmec.android.mobileguard.m3communicationguard.entity.BlackContactInfo;


public class BlackNumberDao {
    private BlackNumberOpenHelper blackNumberOpenHelper;

    public BlackNumberDao(Context context){
        super();
        blackNumberOpenHelper=new BlackNumberOpenHelper(context,"blackNumber.db",null,1);
    }
    //添加数据 @param blackContactInFo
    public boolean add(BlackContactInfo blackContactInfo){
        SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (blackContactInfo.phoneNumber.startsWith("+86")){
            blackContactInfo.phoneNumber=blackContactInfo.phoneNumber
                    .substring(3,blackContactInfo.phoneNumber.length());
            //删除+86
        }
        values.put("number",blackContactInfo.phoneNumber);
        values.put("name",blackContactInfo.contactName);
        values.put("mode",blackContactInfo.mode);
        values.put("type",blackContactInfo.contactType);
        long rowid = db.insert("blacknumber",null,values);
        if (rowid==-1){
            //插入失败
            return false;
        }else {
            return true;
        }
    }
    //删除数据
    public boolean delete(BlackContactInfo blackContactInfo){
        SQLiteDatabase db = blackNumberOpenHelper.getWritableDatabase();
        int rownumber = db.delete("blacknumber","number=?",
                new String[]{blackContactInfo.phoneNumber});
        //↑这段是啥不知道
        if (rownumber==0){
            return false;//删除失败
        }else {
            return true;
        }
    }

    //分页查询数据库记录? pagenumber 第几页页码 从0开始
    // pagesize 每个页面的大小
    public List<BlackContactInfo> getPageBlackNumber(int pagenumber,
                                                     int pagesize){
        SQLiteDatabase db = blackNumberOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "select number,type,name,mode from blacknumber limit ? offset ?",
                new String[]{String.valueOf(pagesize),
                String.valueOf(pagesize*pagenumber)});
        List<BlackContactInfo> mBlackContactInfos = new ArrayList<BlackContactInfo>();
        while (cursor.moveToNext()){
            BlackContactInfo info = new BlackContactInfo();
            info.phoneNumber=cursor.getString(0);
            info.mode=cursor.getInt(3);
            info.contactName=cursor.getString(2);
            info.contactType=cursor.getString(1);
            mBlackContactInfos.add(info);
        }
        cursor.close();
        db.close();
        SystemClock.sleep(30);//这是啥意思
        return mBlackContactInfos;
    }

    //判断号码是否在黑名单
    public boolean IsNumberExist(String number){
        SQLiteDatabase db = blackNumberOpenHelper.getReadableDatabase();
        Cursor cursor = db.query("blacknumber",null,"number=?",
                new String[] {number},null,null,null);
        if (cursor.moveToNext()){
            cursor.close();
            db.close();
            return true;
        }
        cursor.close();
        db.close();
        return false;
    }

    //根据号码查询黑名单信息
    public int getBlackContactMode(String number){
        Log.d("incoming phonenumber",number);
        SQLiteDatabase db = blackNumberOpenHelper.getReadableDatabase();
        Cursor cursor = db.query("blacknumber",null,"number=?",
                new String[] {number},null,null,null);
        int mode=0;
        if (cursor.moveToNext()){
            mode=cursor.getInt(cursor.getColumnIndex("mode"));
        }
        cursor.close();
        db.close();
        return mode;
    }
    //获取数据库的总条目数
    public int getTotalNumber(){
        SQLiteDatabase db = blackNumberOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from blacknumber",null);
        cursor.moveToNext();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count;
    }

}
