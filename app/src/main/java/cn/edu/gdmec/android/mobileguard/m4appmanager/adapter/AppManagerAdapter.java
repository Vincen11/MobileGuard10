package cn.edu.gdmec.android.mobileguard.m4appmanager.adapter;

import android.content.Context;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

import cn.edu.gdmec.android.mobileguard.R;
import cn.edu.gdmec.android.mobileguard.m4appmanager.entity.AppInfo;
import cn.edu.gdmec.android.mobileguard.m4appmanager.utils.DensityUtil;
import cn.edu.gdmec.android.mobileguard.m4appmanager.utils.EngineUtils;

/**
 * Created by user on 2017/11/5.
 */

public class AppManagerAdapter extends BaseAdapter{
    private List<AppInfo> UserAppInfos;
    private List<AppInfo> SystemAppInfos;
    private Context context;

    public AppManagerAdapter(List<AppInfo> userAppInfos,
                             List<AppInfo> systemAppInfos,Context context){
        super();
        UserAppInfos=userAppInfos;
        SystemAppInfos = systemAppInfos;
        this.context=context;
    }

    @Override
    public int getCount() {
        //因为有两个条目需要用于显示用户进程,系统进程因此需要加2
        return UserAppInfos.size()+SystemAppInfos.size()+2;
    }

    @Override
    public Object getItem(int i) {
        if (i==0){
            //第一个位置显示的应该是用户程序个数的标签
            return null;
        }else if(i==(UserAppInfos.size())+1){
            return null;
        }
        AppInfo appInfo;
        if (i<(UserAppInfos.size()+1)){
            //用户程序
            appInfo=UserAppInfos.get(i-1);//多了一个textview的标签
            //位置需要-1
        }else {
            //系统程序
            int location = i - UserAppInfos.size()-2;
            appInfo=SystemAppInfos.get(location);
        }
        return appInfo;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
       //如果position =0 为textview+
        if (i==0){
            TextView tv = getTextView();
            tv.setText("用户程序:"+UserAppInfos.size()+"个");
            return tv;
        }else if(i==(UserAppInfos.size()+1)){
            TextView tv = getTextView();
            tv.setText("系统程序:"+SystemAppInfos.size()+"个");
            return tv;
        }
        //获取到当前的app对象
        AppInfo appInfo;
        if (i<(UserAppInfos.size()+1)){
            //0为TEXTVIEW
            appInfo=UserAppInfos.get(i-1);
        }else {
            //系统应用
            appInfo=SystemAppInfos.get(i-UserAppInfos.size()-2);
        }
        ViewHolder viewHolder = null;
        if (view!=null&view instanceof LinearLayout){
            //上面的是什么鬼!!!!
            viewHolder=(ViewHolder)view.getTag();
        }else {
            viewHolder = new ViewHolder();
            view = View.inflate(context, R.layout.item_appmanager_list,null);
            //这是什么用法卧槽
            viewHolder.mAppIconImgv=(ImageView)view.findViewById(R.id.imgv_appicon);
            viewHolder.mAppLocationTV=(TextView)view.findViewById(R.id.tv_sppisroom);
            viewHolder.mAppSizeTV=(TextView)view.findViewById(R.id.tv_appsize);
            viewHolder.mAppNameTV=(TextView)view.findViewById(R.id.tv_appname);
            viewHolder.mLuanchAppTV=(TextView)view.findViewById(R.id.tv_launch_app);
            viewHolder.mSettingAppTV=(TextView)view.findViewById(R.id.tv_setting_app);
            viewHolder.mShareAppTV=(TextView)view.findViewById(R.id.tv_share_app);
            viewHolder.mUninstallTV=(TextView)view.findViewById(R.id.tv_uninstall_app);
            viewHolder.mAppOptionLL=(LinearLayout) view.findViewById(R.id.ll_option_app);
            view.setTag(viewHolder);
        }
        if (appInfo!=null) {
            viewHolder.mAppLocationTV.setText(appInfo.getApplocation(appInfo.isInRoom));
            viewHolder.mAppIconImgv.setImageDrawable(appInfo.icon);
            viewHolder.mAppSizeTV.setText(Formatter.formatFileSize(context,
                    appInfo.appSize));
            viewHolder.mAppNameTV.setText(appInfo.appName);
            if (appInfo.isSelected) {
                viewHolder.mAppOptionLL.setVisibility(view.VISIBLE);
            } else {
                viewHolder.mAppOptionLL.setVisibility(View.GONE);
            }
        }
            MyclickListener listener = new MyclickListener(appInfo);
            viewHolder.mLuanchAppTV.setOnClickListener(listener);
            viewHolder.mSettingAppTV.setOnClickListener(listener);
            viewHolder.mShareAppTV.setOnClickListener(listener);
            viewHolder.mUninstallTV.setOnClickListener(listener);
            return view;

    }

    //创建一个textView
    private TextView getTextView(){
        TextView tv =new TextView(context);
        tv.setBackgroundColor(ContextCompat.getColor(context,R.color.graye5));
        tv.setPadding(DensityUtil.dip2px(context,5),
                DensityUtil.dip2px(context,5),
                DensityUtil.dip2px(context,5),
                DensityUtil.dip2px(context,5));
        tv.setTextColor(ContextCompat.getColor(context,R.color.black));
        return tv;
    }

    static class ViewHolder{
        //启动app
        TextView mLuanchAppTV;
        //卸载
        TextView mUninstallTV;
        //分享
        TextView mShareAppTV;
        //设置
        TextView mSettingAppTV;
        //图标
        ImageView mAppIconImgv;
        //位置
        TextView mAppLocationTV;
        //大小
        TextView mAppSizeTV;
        //名称
        TextView mAppNameTV;
        //操作app的线性布局
        LinearLayout mAppOptionLL;
    }

    class MyclickListener implements View.OnClickListener{
        private AppInfo appInfo;
        public  MyclickListener(AppInfo appInfo){
            super();
            this.appInfo=appInfo;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                //启动应用
                case R.id.tv_launch_app:
                    EngineUtils.startApplication(context,appInfo);
                    break;
                //分享
                case  R.id.tv_share_app:
                    EngineUtils.shareApplication(context,appInfo);
                    break;
                case R.id.tv_setting_app:
                    //设置
                    EngineUtils.SettingAppDetail(context,appInfo);
                    break;
                case R.id.tv_uninstall_app:
                    //卸载应用 需要注册广播接收者
                    if (appInfo.packageName.equals(context.getPackageName())){
                        Toast.makeText(context,"您没有权限卸载此应用!",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    EngineUtils.uninstallApplication(context,appInfo);
                    break;
            }
        }
    }
}
