package com.zhx.otp_fadb_moudle.view.dfab;

import android.view.View;

/**
 * 对外提供的回调接口
 */
public interface ActionInterface {
    /**
     * 接入方实现该接口，根据不同actionType实现不同业务场景
     * @param actionType
     * @see Action
     * @param view
     */
    void doOnAction(Action actionType, View view);

    /**
     * 定义按钮行为
     */
    enum Action {
        /**
         * 弹出菜单
         */
        MENU,
        /**
         * 刷新当前页面
         */
        REFRESH,
        /**
         * 返回主页按钮
         */
        HOME,
        /**
         * 返回上一级按钮
         */
        BACK
    }
}
