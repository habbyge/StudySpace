package com.zhx.a173helper_fw_moudle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.zhx.a173helper_fw_moudle.permission.BasePermissionFragment;
import com.zhx.a173helper_fw_moudle.permission.FloatPermissionFragment;
import com.zhx.a173helper_fw_moudle.view.ExpandableLinearLayout;
import com.zhx.baselibrary.utils.ARouterConstants;
import com.zhx.baselibrary.utils.LogUtil;
import com.zhx.baselibrary.utils.SharedPreferUtils;

import java.util.List;

@Route(path = ARouterConstants.PLAY_HELPER_HOME)
public class MainActivity extends AppCompatActivity {

    private WindowManager mWindowManager;

    private WindowManager.LayoutParams wmParams;

    //浮动布局
    private ExpandableLinearLayout mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hepler_activity_main);
        checkFloatWindowPermission();
    }

    /**
     * 初始化窗口
     */
    private void initWindow() {
        LogUtil.d("initWindow start");
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        //设置好悬浮窗的参数
        wmParams = getWmParams();
        // 获取浮动窗口视图所在布局
        mRootView = (ExpandableLinearLayout) LayoutInflater.from(this).inflate(R.layout.helper_float_layout, null);
        mRootView.setOnStateChangeListener(new ExpandableLinearLayout.OnStateChangeListener() {
            @Override
            public void onStateChanged(boolean isExpanded) {
            }
        });
        // 添加悬浮窗的视图
        mRootView.setParams(mWindowManager, wmParams);
        mWindowManager.addView(mRootView, wmParams);
        LogUtil.d("initWindow end");
    }

    private WindowManager.LayoutParams getWmParams() {
        LogUtil.d("getWmParams start");
        wmParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.gravity = Gravity.START | Gravity.TOP;
        wmParams.width = 700;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.x = 0;
        wmParams.y = 0;
        LogUtil.d("getWmParams end :: " + wmParams);
        return wmParams;
    }

    /**
     * 横屏游戏直播需要申请悬浮窗权限，但不强制
     */
    private void checkFloatWindowPermission() {
        boolean needPermission = FloatPermissionFragment.needPermission(this);
        if (needPermission) {
            FloatPermissionFragment floatPermissionFragment = FloatPermissionFragment.newInstance();
            floatPermissionFragment.setOnDismissListener(new BasePermissionFragment.OnDismissListener() {
                @Override
                public void onResult(List<String> mGrantedList, List<String> mRefuseList, List<String> mAlwaysRefuseList) {
                    initWindow();
                }

                @Override
                public void onCancel() {
                    finish();
                }
            });
            floatPermissionFragment.show(getSupportFragmentManager(), FloatPermissionFragment.TAG);
        } else {
            initWindow();
        }
    }
}
