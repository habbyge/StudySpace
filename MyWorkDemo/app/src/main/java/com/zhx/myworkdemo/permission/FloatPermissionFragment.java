package com.zhx.myworkdemo.permission;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.zhx.myworkdemo.R;
import com.zhx.myworkdemo.utils.LogUtil;

import java.util.Arrays;
import java.util.List;

/**
 * 悬浮窗权限申请框
 */
public class FloatPermissionFragment extends BasePermissionFragment {

    public static final String TAG = FloatPermissionFragment.class.getSimpleName();

    public static FloatPermissionFragment newInstance() {
        return new FloatPermissionFragment();
    }

    @Override
    List<String> configPermissions() {
        return Arrays.asList(Manifest.permission.SYSTEM_ALERT_WINDOW);
    }

    @NonNull
    @Override
    View createContentView() {
        View mRootView = LayoutInflater.from(getContext()).inflate(R.layout.home_layout_permission_landscape_float, null);
        mRootView.findViewById(R.id.tv_permission_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAllowingStateLoss();
            }
        });

        mRootView.findViewById(R.id.tv_permission_auth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission(Manifest.permission.SYSTEM_ALERT_WINDOW);
            }
        });
        return mRootView;
    }

    @Override
    void onRetryPermission(List<String> deniedList) {
        LogUtil.e("onRetryPermission ");
    }

    @Override
    void onAlwaysDenied(List<String> neverList) {
        LogUtil.e("onAlwaysDenied ");
        //引导至系统 settings页面或直接退出app
        Toast.makeText(getContext(), "请开启悬浮窗权限", Toast.LENGTH_SHORT).show();
    }

    public static boolean needPermission(Context context) {
        boolean hasPermission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(context.getApplicationContext());
        LogUtil.e("需要申请悬浮窗权限 " + (!hasPermission));
        return !hasPermission;
    }


    @Override
    public void onActivityResult(final int requestCode, int resultCode, @Nullable Intent data) {
        LogUtil.e("申请悬浮窗权限 onRequestPermissionsResult resultCode = " + resultCode);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int rst = !needPermission(getContext()) ? PackageManager.PERMISSION_GRANTED : PackageManager.PERMISSION_DENIED;
                handleRequestResult(requestCode, new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, new int[]{rst});
            }
        }, 200);
    }

    @Override
    protected void doRealRequestPermission(List<String> permissionList, int requestCode) {
        startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getActivity().getPackageName())), requestCode);

    }
}
