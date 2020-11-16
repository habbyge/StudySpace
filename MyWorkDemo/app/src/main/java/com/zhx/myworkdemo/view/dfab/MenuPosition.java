package com.zhx.myworkdemo.view.dfab;

/**
 * Created by csy on 2017/10/10.
 * 最多显示5个子菜单,如果需要显示更多,可自己做拓展
 * 微信公众号:陈守印同学
 * https://github.com/chenshouyin/SatelliteMenu
 */

import android.view.View;

import com.zhx.myworkdemo.utils.Logger;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * 菜单点击后展开的方向
 */
public class MenuPosition {
    public static final int LEFT_TOP = 0;
    public static final int RIGHT_TOP = 1;
    public static final int LEFT_BOTTOM = 2;
    public static final int RIGHT_BOTTOM = 3;

    /**
     * 根据view位置，判断所属屏幕区域
     *
     * @param anchor
     * @return
     */
    public static int judgePosition(View anchor) {
        Logger.d("judgePosition x=" + anchor.getX() + " y=" + anchor.getY());
        int screenWidth = ScreenUtils.getScreenWidth(anchor.getContext());
        int screenHeight = ScreenUtils.getScreenHeight(anchor.getContext());

        LinkedList
        boolean isLeft = anchor.getX() < screenWidth >> 1;
        Logger.d("isLeft=" + isLeft);

        boolean isTop = anchor.getY() < screenHeight >> 1;
//        int popWindowHeight = ScreenUtils.getScreenHeight(anchor.getContext()) / 3;
//        boolean isTop = screenHeight - ScreenUtils.getStatusHeight(anchor.getContext()) - anchor.getY() >= popWindowHeight;
//        Logger.e("screenHeight - ScreenUtils.getStatusHeight(anchor.getContext()) - anchor.getY() = " + (screenHeight - ScreenUtils.getStatusHeight(anchor.getContext()) - anchor.getY()));
//        Logger.e("popWindowHeight = " + popWindowHeight);
        Logger.d("isTop=" + isTop);

        int position = LEFT_TOP;

        if (!isLeft && isTop) {
            position = RIGHT_TOP;
        }

        if (!isLeft && !isTop) {
            position = RIGHT_BOTTOM;
        }

        if (isLeft && !isTop) {
            position = LEFT_BOTTOM;
        }

        return position;
    }
}
