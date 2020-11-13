package com.zhx.myworkdemo.view.dfab;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 *  DragFloatingButton 数据模型抽象
 * @see DragFloatingButton
 * @author zhx
 */
public interface IItem {

    Drawable getDrawable(Context context);

    void onAction(View v);

}