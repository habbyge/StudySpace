package com.zhx.myworkdemo.permission;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.ArrayMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.zhx.myworkdemo.utils.LogUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 悬浮窗权限申请框
 */
public abstract class BasePermissionFragment extends DialogFragment implements DialogInterface.OnDismissListener {

//    public static final String TAG = BasePermissionFragment.class.getSimpleName();

    private List<String> mPermissionList = new ArrayList<>();

    private List<String> mGrantedList = new ArrayList<>();

    private List<String> mRefuseList = new ArrayList<>();

    private List<String> mAlwaysRefuseList = new ArrayList<>();

    private Activity mActivity;

    private ArrayMap<Integer, List<String>> requestMap = new ArrayMap<>();

    protected OnDismissListener mOnDismissListener;

    /**
     * 是否强制授予权限开关
     */
    boolean isForceMode = true;

    public void setOnDismissListener(OnDismissListener mOnDismissListener) {
        this.mOnDismissListener = mOnDismissListener;
    }

    public interface OnDismissListener {
        void onResult(List<String> mGrantedList, List<String> mRefuseList, List<String> mAlwaysRefuseList);

        void onCancel();
    }

    abstract List<String> configPermissions();

    abstract @NonNull
    View createContentView();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
        mPermissionList.addAll(configPermissions());
        if (mPermissionList == null || mPermissionList.size() == 0) {
            throw new MissingConfigException("please config permissions to grant");
        }
    }

    static class MissingConfigException extends RuntimeException {
        public MissingConfigException(String msg) {
            super(msg);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LogUtil.e("onCreateDialog");
        View mRootView = createContentView();
        //初始化权限状态
        checkAllPermissions(mPermissionList);
        Dialog dialog = new Dialog(mRootView.getContext(), android.R.style.Widget_PopupWindow);
        dialog.setContentView(mRootView);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setOnDismissListener(this);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dismissAllowingStateLoss();
                    return true;
                }
                return false;
            }
        });
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        dialog.getWindow().setAttributes(lp);
        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        List<String> needList = checkAllPermissions(mPermissionList);
        if (needList == null && checkAllGranted()){
            onResult(mGrantedList,null,null);
        }
    }

    protected final void requestAllPermissions() {
        String[] array = new String[mPermissionList.size()];
        requestPermission(mPermissionList.toArray(array));
    }

    protected final void requestPermission(String... permissions) {
        LogUtil.e("requestPermission ");
        if (permissions == null || permissions.length == 0) {
            requestAllPermissions();
            return;
        }
        //本次请求的requestCode
        int requestCode = requestMap.size();
        LogUtil.e("requestPermission requestCode = " + requestCode);
        //检测未获得的权限
        List<String> needRequestList = checkAllPermissions(Arrays.asList(permissions));
        if (needRequestList != null && needRequestList.size() > 0) {
            LogUtil.e("requestPermission needRequestList size = " + needRequestList.size());
            requestMap.put(requestCode, needRequestList);
            doRealRequestPermission(needRequestList, requestCode);
        } else {
            updatePermissionResult();
        }
    }

    /**
     * 真正发起权限申请的方法, 可重载
     *
     * @param permissionList
     * @param requestCode
     */
    protected void doRealRequestPermission(List<String> permissionList, int requestCode) {
        if (permissionList == null || permissionList.size() == 0) {
            return;
        }
        String[] pArray = new String[permissionList.size()];
        for (int i = 0; i < pArray.length; i++) {
            pArray[i] = permissionList.get(i);
        }
        requestPermissions(pArray, requestCode);
    }

    /**
     * 检测已获得的权限，不需发起申请，直接放到mGrantedList
     *
     * @param permissions
     * @return
     */
    private List<String> checkAllPermissions(List<String> permissions) {
        if (permissions == null || permissions.size() == 0) {
            return null;
        }

        LogUtil.e("checkAllPermissions permissions = " + permissions.size());

        List<String> needRequestList = new ArrayList<>(permissions.size());
        for (String permission : permissions) {
            LogUtil.e("checkAllPermissions permission = " + permission);
            if (checkPermissionHasGranted(mActivity, permission)) {
                addToListIfNotContains(mGrantedList, permission);
                onPermissionUpdate(permission, Status.PERMISSION_GRANTED);
            } else {
                needRequestList.add(permission);
            }
        }

        return needRequestList;
    }

    /**
     * 检测方法 , 可重载
     *
     * @param activity
     * @param permission
     * @return
     */
    protected boolean checkPermissionHasGranted(Activity activity, String permission) {
        if (activity == null || activity.isFinishing()) {
            return false;
        }
        return ContextCompat.checkSelfPermission(mActivity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 引导至系统 settings页面
     *
     * @param context
     */
    protected void gotoSystemSettings(@NonNull Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        startActivity(intent);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (!checkAllDone() || (isForceMode && !checkAllGranted())) {
            onCancel();
        }
    }

    @Override
    public final void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        handleRequestResult(requestCode, permissions, grantResults);
    }

    protected void handleRequestResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        LogUtil.e("handleRequestResult requestCode = " + requestCode + " permissions = " + permissions.length + " grantResults=" + grantResults.length);
        if (requestMap.indexOfKey(requestCode) >= 0) {
            for (int i = 0; i < grantResults.length; i++) {

                String permissionName = permissions[i];
                Status status;

                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    removeFromListIfContains(mAlwaysRefuseList, permissionName);
                    removeFromListIfContains(mRefuseList, permissionName);
                    addToListIfNotContains(mGrantedList, permissionName);
                    status = Status.PERMISSION_GRANTED;

                } else if (mActivity.shouldShowRequestPermissionRationale(permissionName)) {
                    removeFromListIfContains(mGrantedList, permissionName);
                    removeFromListIfContains(mAlwaysRefuseList, permissionName);
                    addToListIfNotContains(mRefuseList, permissionName);
                    status = Status.PERMISSION_DENIED;

                } else {
                    removeFromListIfContains(mGrantedList, permissionName);
                    removeFromListIfContains(mRefuseList, permissionName);
                    addToListIfNotContains(mAlwaysRefuseList, permissionName);
                    status = Status.PERMISSION_NEVER;
                }
                onPermissionUpdate(permissionName, status);
            }
            updatePermissionResult();
        }
    }

    private void updatePermissionResult() {
        LogUtil.e("updatePermissionResult");

        if (checkAllDone()) {
            if (!isForceMode || checkAllGranted()) {
                onResult(mGrantedList, mRefuseList, mAlwaysRefuseList);
                dismissAllowingStateLoss();
            } else {
                //强制模式
                LogUtil.e("强制模式");

                if (mRefuseList.size() > 0) {
                    onRetryPermission(mRefuseList);

                } else if (mAlwaysRefuseList.size() > 0) {
                    onAlwaysDenied(mAlwaysRefuseList);
                }
            }
        }
    }

    /**
     * 提醒并重试requestPermission
     *
     * @param deniedList
     */
    abstract void onRetryPermission(List<String> deniedList);

    /**
     * 引导至系统 settings页面或直接退出app
     */
    abstract void onAlwaysDenied(List<String> neverList);

    private boolean checkAllDone() {
        return mAlwaysRefuseList.size() + mGrantedList.size() + mRefuseList.size() == mPermissionList.size();
    }

    private boolean checkAllGranted() {
        return mGrantedList.size() == mPermissionList.size();
    }

    /**
     * 当permission状态更新时回调，可重写此方法更新UI等
     *
     * @param permission
     * @param status
     */
    void onPermissionUpdate(String permission, Status status) {

    }

    private void onResult(List<String> mGrantedList, List<String> mRefuseList, List<String> mAlwaysRefuseList) {
        if (mOnDismissListener != null) {
            mOnDismissListener.onResult(mGrantedList, mRefuseList, mAlwaysRefuseList);
            mOnDismissListener = null;
        }
    }

    private void onCancel() {
        if (mOnDismissListener != null) {
            mOnDismissListener.onCancel();
            mOnDismissListener = null;
        }
    }


    public enum Status {
        PERMISSION_GRANTED,

        PERMISSION_DENIED,

        PERMISSION_NEVER
    }

    private void addToListIfNotContains(List<String> list, String s) {
        if (list == null || s == null) {
            return;
        }

        if (list.contains(s)) {
            return;
        }
        list.add(s);
    }

    private void removeFromListIfContains(List<String> list, String s) {
        if (list == null || s == null) {
            return;
        }

        if (!list.contains(s)) {
            return;
        }
        list.remove(s);
    }
}
