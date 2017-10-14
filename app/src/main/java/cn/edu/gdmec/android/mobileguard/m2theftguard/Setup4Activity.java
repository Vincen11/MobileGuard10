package cn.edu.gdmec.android.mobileguard.m2theftguard;

import android.os.Bundle;
import android.widget.RadioButton;

import cn.edu.gdmec.android.mobileguard.R;

/**
 * Created by user on 2017/10/14.
 */

public class Setup4Activity extends BaseSetupActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_4);
        //设置第4个圆点
        ((RadioButton)findViewById(R.id.rb_four)).setChecked(true);
    }

    @Override
    public void showNext() {
        startActivityAndFinishSelf(LostFindActivity.class);
    }

    @Override
    public void showPre() {
        startActivityAndFinishSelf(Setup3Activity.class);
    }
}
