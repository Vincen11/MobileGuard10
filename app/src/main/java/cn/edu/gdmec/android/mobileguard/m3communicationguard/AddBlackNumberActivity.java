package cn.edu.gdmec.android.mobileguard.m3communicationguard;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.edu.gdmec.android.mobileguard.R;
import cn.edu.gdmec.android.mobileguard.m2theftguard.ContactSelectActivity;
import cn.edu.gdmec.android.mobileguard.m3communicationguard.db.dao.BlackNumberDao;
import cn.edu.gdmec.android.mobileguard.m3communicationguard.entity.BlackContactInfo;

/**
 * Created by user on 2017/10/31.
 */

public class AddBlackNumberActivity extends AppCompatActivity implements View.OnClickListener {
    private CheckBox mSmsCB;
    private CheckBox mTelCB;
    private EditText mNumET;
    private EditText mNameET;
    private EditText mTypeET;
    private BlackNumberDao dao;

    private void initView(){
        findViewById(R.id.rl_titlebar).setBackgroundColor(
                getResources().getColor(R.color.bright_purple));
        ((TextView)findViewById(R.id.tv_title)).setText("添加黑名单");
        ImageView mLeftImgv = (ImageView)findViewById(R.id.imgv_leftbtn);
        mLeftImgv.setOnClickListener(this);
        mLeftImgv.setImageResource(R.drawable.back);

        mSmsCB=(CheckBox)findViewById(R.id.cb_blacknumber_sms);
        mTelCB=(CheckBox)findViewById(R.id.cb_blacknumber_tel);
        mNumET=(EditText)findViewById(R.id.et_balcknumber);
        mNameET=(EditText)findViewById(R.id.et_blackname);
        mTypeET=(EditText)findViewById(R.id.et_blacktype);
        findViewById(R.id.add_blacknum_btn).setOnClickListener(this);
        findViewById(R.id.add_fromcontact_btn).setOnClickListener(this);
    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode){
//            case 0:
//                if(data==null) { return; }
//                //处理返回的data,获取选择的联系人信息
//                Uri uri=data.getData(); String[] contacts=getPhoneContacts(uri);
//                mNameET.setText(contacts[0]);
//                mNumET.setText(contacts[1]);
//                break;
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
    private String[] getPhoneContacts(Uri uri){
        String[] contact=new String[2];
        //得到ContentResolver对象
        ContentResolver cr = getContentResolver();
        //取得电话本中开始一项的光标
        Cursor cursor=cr.query(uri,null,null,null,null);
        if(cursor!=null) {
            cursor.moveToFirst();
            //取得联系人姓名
            int nameFieldColumnIndex=cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            contact[0]=cursor.getString(nameFieldColumnIndex);
            //取得电话号码
            String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);
            if(phone != null){
                phone.moveToFirst();
                contact[1] = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }
            phone.close();
            cursor.close();
        } else {
            return null;
        }
        return contact;
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null){
            //获取选中的联系人信息
            String phone = data.getStringExtra("phone");
            String name=data.getStringExtra("name");
            mNameET.setText(name);
            mNumET.setText(phone);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_black_number);
        this.getSupportActionBar().hide();//去除标题栏
        dao=new BlackNumberDao(AddBlackNumberActivity.this);
        initView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgv_leftbtn:
                finish();
                break;
            case R.id.add_blacknum_btn:
                String number = mNumET.getText().toString().trim();
                String name = mNameET.getText().toString().trim();
                String type = mTypeET.getText().toString().trim();
                if (TextUtils.isEmpty(number)||TextUtils.isEmpty(name)||TextUtils.isEmpty(type)){
                    Toast.makeText(this,"电话号码和手机号和类型不能为空!",Toast.LENGTH_LONG).show();
                return;
                }else {
                    //电话号码和名称都不为空
                    BlackContactInfo blackContactInfo = new BlackContactInfo();
                    blackContactInfo.phoneNumber=number;
                    blackContactInfo.contactName=name;
                    blackContactInfo.contactType=type;
                    if (mSmsCB.isChecked()&mTelCB.isChecked()){
                        //两种拦截模式都选
                        blackContactInfo.mode=3;
                    }else if (mSmsCB.isChecked()&!mTelCB.isChecked()){
                        //短信拦截
                        blackContactInfo.mode=2;
                    }else if (!mSmsCB.isChecked()&mTelCB.isChecked()){
                        //电话拦截
                        blackContactInfo.mode=1;
                    }else{
                        Toast.makeText(this,"请选择拦截模式!",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (!dao.IsNumberExist(blackContactInfo.phoneNumber)){
                        dao.add(blackContactInfo);
                    }else {
                        Toast.makeText(this,"该号码已经被添加至黑名单",Toast.LENGTH_SHORT).show();
                    }
                    finish();
                }
                break;
            case R.id.add_fromcontact_btn:
//                Uri uri = Uri.parse("content://contacts/people");
//                Intent intent = new Intent(Intent.ACTION_PICK, uri);
//                startActivityForResult(intent, 0);

//                Intent intent = new Intent(Intent.ACTION_PICK, uri,this, ContactSelectActivity.class);
//                startActivityForResult(intent, 0);

                startActivityForResult(
                        new Intent(this, ContactSelectActivity.class),0);
                //0是什么意思?
                break;
            //我自己从网上找了个新方法替代了老师原来的

        }
    }
}
