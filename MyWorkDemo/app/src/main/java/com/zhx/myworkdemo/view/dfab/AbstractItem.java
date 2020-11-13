package com.zhx.myworkdemo.view.dfab;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * DragFloatingButton 数据模型抽象
 * @author zhx
 */
public abstract class AbstractItem implements IItem {

    public ActionInterface callback;

    public void register(ActionInterface callback) {
        this.callback = callback;
    }


    /**
     * 由子类实现，标识操作类型
     *
     * @return
     */
    abstract ActionInterface.Action getType();

    @Override
    public Drawable getDrawable(Context context) {
        return null;
    }

    @Override
    public void onAction(View view) {
        if (this.callback != null) {
            this.callback.doOnAction(getType(), view);
        }
    }
}
