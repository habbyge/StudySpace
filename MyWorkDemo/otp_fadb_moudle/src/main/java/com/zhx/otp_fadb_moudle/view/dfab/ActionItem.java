package com.zhx.otp_fadb_moudle.view.dfab;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.PopupWindow;

import androidx.core.content.ContextCompat;

import com.zhx.baselibrary.utils.Logger;
import com.zhx.otp_fadb_moudle.R;

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
            mPopupWindow.setHeight(ScreenUtils.getScreenHeight(view.getContext()) / 3);
            mPopupWindow.setWidth(ScreenUtils.getScreenWidth(view.getContext()) / 2);
            mPopupWindow.setContentView(satelliteMenuGroup);
        }


        // 更新PopupWindow及卫星菜单组件展开方向
        mPopupWindow.update(view, ScreenUtils.getScreenHeight(view.getContext()) / 3, ScreenUtils.getScreenWidth(view.getContext()) / 2);
        int position = MenuPosition.judgePosition(view);
        satelliteMenuGroup.setPostion(position);

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

        if (MenuPosition.LEFT_TOP == position) {
            int width = ScreenUtils.dip2px(view.getContext(), view.getWidth());
            int height = view.getHeight();
            Logger.d("LEFT_TOP width = " + width + " height = " + height);
            mPopupWindow.showAsDropDown(view, 0, -1 * height);

        } else if (MenuPosition.LEFT_BOTTOM == position) {
            int width = ScreenUtils.dip2px(view.getContext(), view.getWidth());
            int height = view.getHeight();
            Logger.d("LEFT_BOTTOM width = " + width + " height = " + height);
            mPopupWindow.showAsDropDown(view, 0, height);

        } else if (MenuPosition.RIGHT_TOP == position) {
            int width = ScreenUtils.dip2px(view.getContext(), view.getWidth());
            int height = view.getHeight();
            Logger.d("RIGHT_TOP width = " + width + " height = " + height);
            mPopupWindow.showAsDropDown(view, -1 * width, -1 * height);

        } else {
            int width = view.getWidth();
            int height = view.getHeight();
            Logger.d("RIGHT_BOTTOM width = " + width + " height = " + height);
            mPopupWindow.showAsDropDown(view, -1 * width, height);
        }
    }


    private void dismissPopWindow() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }
}