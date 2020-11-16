package com.zhx.myworkdemo;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.launcher.ARouter;
import com.zhx.baselibrary.annotation.InjectView;
import com.zhx.baselibrary.annotation.OnClick;
import com.zhx.baselibrary.utils.ARouterConstants;
import com.zhx.baselibrary.utils.InjectUtils;
import com.zhx.baselibrary.utils.Logger;

public class MainActivity extends AppCompatActivity {

//    @InjectView(R.id.tv_title)
//    TextView mTvTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InjectUtils.injectView(this);
        InjectUtils.injectEvent(this);
        InjectUtils.injectAutowired(this);

        TextView mTv;
        mTv = findViewById(R.id.tv_title);
        mTv.setText("你好！字节 ！");
        mTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "你好！字节 ！", Toast.LENGTH_SHORT).show();
            }
        });

        mTv = findViewById(R.id.tv_sub_title);
        mTv.setText("你好！广州 ！");
        mTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "你好！广州 ！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick({R.id.test_otp, R.id.test_show, R.id.test_helper})
    public void onEvent(View v) {
        switch (v.getId()) {
            case R.id.test_otp:
                ARouter.getInstance().build(ARouterConstants.GROUP_OTP_HOME).navigation(MainActivity.this);
                break;
            case R.id.test_show:
                ARouter.getInstance().build(ARouterConstants.PLAY_HELPER_HOME).navigation(MainActivity.this);
                break;
            case R.id.test_helper:
                break;
            default:
                break;
        }
    }
}
