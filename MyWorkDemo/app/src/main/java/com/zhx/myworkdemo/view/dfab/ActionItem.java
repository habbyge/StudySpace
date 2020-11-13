package com.zhx.myworkdemo.view.dfab;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

import androidx.core.content.ContextCompat;

import com.zhx.myworkdemo.MyApplication;
import com.zhx.myworkdemo.R;
import com.zhx.myworkdemo.utils.Logger;

import java.util.ArrayList;

/**
 * 数据模型 -- 菜单按钮实现类
 *
 * @author zhx
 */
public class ActionItem extends AbstractItem {

    private ActionInterface.Action mAction = ActionInterface.Action.MENU;
    private PopupWindow mPopupWindow = null;
    private SatelliteMenu satelliteMenuGroup = null;
    private static int sPopWindowWidth = 0;
    private static int sPopWindowHeight = 0;

    @Override
    ActionInterface.Action getType() {
        return mAction;
    }

    @Override
    public Drawable getDrawable(Context context) {
        return ContextCompat.getDrawable(context, R.drawable.dfab_menu);
    }

    /**
     * 菜单按钮点击事件
     *
     * @param view
     */
    @Override
    public void onAction(final View view) {
        Log.e("dfab", "onAction view x = " + view.getX() + " y = " + view.getY() + " width = " + view.getWidth() + " height=" + view.getHeight());
        if (sPopWindowWidth <= 0) {
            sPopWindowWidth = ScreenUtils.getScreenHeight(view.getContext()) / 3;
            sPopWindowHeight = ScreenUtils.getScreenHeight(view.getContext()) / 3;
        }

        if (mPopupWindow == null) {
            // create satellite menu
            satelliteMenuGroup = new SatelliteMenu(view.getContext());

            ArrayList<Integer> menuItemResources = new ArrayList<>();
            menuItemResources.add(R.drawable.dfab_back);
            menuItemResources.add(R.drawable.dfab_home);
            menuItemResources.add(R.drawable.dfab_refresh);

            satelliteMenuGroup.getBuilder()
                    .setRadius(100)
                    .setMenuItemImageWidth(25)
                    .setMenuItemImageResource(menuItemResources)
                    .setOnMenuItemClickListener(new SatelliteMenu.OnMenuItemClickListener() {
                        @Override
                        public void onClick(View view, int position) {
                            switch (position) {
                                case 0:
                                    mAction = ActionInterface.Action.BACK;
                                    break;
                                case 1:
                                    mAction = ActionInterface.Action.HOME;
                                    break;
                                case 2:
                                    mAction = ActionInterface.Action.REFRESH;
                                    break;
                                default:
                                    break;
                            }

                            if (position >= 0) {
                                //callback
                                ActionItem.super.onAction(view);
                            }

                            //close popup window
                            dismissPopWindow();
                        }
                    })
                    .create();

            //create PopupWindow
            mPopupWindow = new PopupWindow();
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setFocusable(true);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mPopupWindow.setHeight(sPopWindowHeight);
            mPopupWindow.setWidth(sPopWindowWidth);
            mPopupWindow.setContentView(satelliteMenuGroup);
        }


        // 更新PopupWindow及卫星菜单组件展开方向
        mPopupWindow.update(view, sPopWindowWidth, sPopWindowHeight);
        int position = MenuPosition.judgePosition(view);
        satelliteMenuGroup.setPostion(position);
        satelliteMenuGroup.setBackgroundColor(Color.BLUE);
        //show pop window
        showPopupWindow(position, view);
    }

    /**
     * 根据anchor所属位置，显示popwindow
     *
     * @param position 上下左右
     * @param view     anchor view 即 菜单按钮
     */
    private void showPopupWindow(int position, View view) {
        if (mPopupWindow == null || mPopupWindow.isShowing()) {
            return;
        }
        int width = view.getWidth();
        int height = view.getHeight();
        int[] realLocation = new int[2];
        view.getLocationOnScreen(realLocation);
        Logger.d("RIGHT_BOTTOM width = " + width + " height = " + height);
        Logger.d("RIGHT_BOTTOM realLocation.x = " + realLocation[0] + " realLocation.y = " + realLocation[1]);

        if (MenuPosition.LEFT_TOP == position) {
            mPopupWindow.showAtLocation(view, Gravity.NO_GRAVITY, (width >> 1), realLocation[1] + (height >> 1));

        } else if (MenuPosition.LEFT_BOTTOM == position) {
            mPopupWindow.showAtLocation(view, Gravity.NO_GRAVITY, (width >> 1), realLocation[1] - sPopWindowHeight + (height >> 1));

        } else if (MenuPosition.RIGHT_TOP == position) {
            mPopupWindow.showAtLocation(view, Gravity.NO_GRAVITY, realLocation[0] - sPopWindowWidth + (width >> 1), realLocation[1] + (height >> 1));
        } else {
            mPopupWindow.showAtLocation(view, Gravity.NO_GRAVITY, realLocation[0] - sPopWindowWidth + (width >> 1), realLocation[1] - sPopWindowHeight + (height >> 1));
        }
    }


    private void dismissPopWindow() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }
}