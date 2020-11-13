package com.zhx.otp_fadb_moudle.view.dfab;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.zhx.otp_fadb_moudle.R;


/**
 * DragFloatingButton数据模型 --  刷新按钮实现类
 *
 * @author zhx
 */
public class RefreshItem extends AbstractItem {

    @Override
    ActionInterface.Action getType() {
        return ActionInterface.Action.REFRESH;
    }

    @Override
    public Drawable getDrawable(Context context) {
        return ContextCompat.getDrawable(context, R.drawable.dfab_refresh);
    }

    @Override
    public void onAction(View v) {
        super.onAction(v);
        // 在此处理一些与业务无关的内部逻辑
        Log.d("dfab", "refresh button clicked !!! ");
    }
}
