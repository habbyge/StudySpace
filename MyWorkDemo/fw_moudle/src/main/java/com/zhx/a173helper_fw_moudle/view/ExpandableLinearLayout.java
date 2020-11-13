package com.zhx.a173helper_fw_moudle.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.zhx.baselibrary.utils.LogUtil;
import com.zhx.baselibrary.utils.ScreenUtil;

/**
 * 可以展开的LinearLayout
 */
public class ExpandableLinearLayout extends LinearLayout implements View.OnClickListener {

    private static final String TAG = ExpandableLinearLayout.class.getSimpleName();

    private boolean isExpand = false;

    /**
     * 一开始展示的条目数
     */
    private final int defaultItemCount = 1;

    private int minTouchSlop;
    /**
     * 用于更新小悬浮窗的位置
     */
    private WindowManager mWindowManager;

    /**
     * 小悬浮窗的参数
     */
    private WindowManager.LayoutParams mParams;

    /**
     * 记录手指按下时在屏幕上的横坐标的值
     */
    private float xDownInScreen;

    /**
     * 记录手指按下时在屏幕上的纵坐标的值
     */
    private float yDownInScreen;

    /**
     * 记录手指按下时在小悬浮窗的View上的横坐标的值
     */
    private float xInView;

    /**
     * 记录手指按下时在小悬浮窗的View上的纵坐标的值
     */
    private float yInView;

    /**
     * 记录手指在屏幕中的真实位置
     *
     * @param context
     */
    float xInScreenNow;
    float yInScreenNow;

    /**
     * 状态栏高度
     */
    float mStatusBarHeight;

    /**
     * 是否处于靠左吸附状态
     *
     * @param context
     */
    private boolean isInAbsorbState;

    public ExpandableLinearLayout(Context context) {
        this(context, null);
    }

    public ExpandableLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandableLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        //获取系统检测最小滑动距离
        minTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mStatusBarHeight = ScreenUtil.getStatusBarHeight(getContext());
    }

    public void addItem(View view) {
        addView(view);
        if (!isExpand && getChildCount() > defaultItemCount) {
            hide();
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        Log.i(TAG, "childCount: " + childCount);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 展开
     */
    private void expand() {
        for (int i = defaultItemCount; i < getChildCount(); i++) {
            //从默认显示条目位置以下的都显示出来
            View view = getChildAt(i);
            view.setVisibility(VISIBLE);
        }
    }


    /**
     * 收起
     */
    private void hide() {
        int endIndex = getChildCount();//如果是使用默认底部，则结束的下标是到底部之前，否则则全部子条目都隐藏
        for (int i = defaultItemCount; i < endIndex; i++) {
            //从默认显示条目位置以下的都隐藏
            View view = getChildAt(i);
            view.setVisibility(GONE);
        }
    }

    @Override
    public void onClick(View v) {
        toggle();
    }

    /**
     * 当前布局状态 -- 是否展开
     *
     * @return
     */
    public void toggle() {
        if (isExpand) {
            hide();
        } else {
            expand();
        }
        isExpand = !isExpand;

        //回调
        if (mStateListener != null) {
            mStateListener.onStateChanged(isExpand);
        }
    }

    private OnStateChangeListener mStateListener;

    /**
     * 定义状态改变接口
     */
    public interface OnStateChangeListener {
        void onStateChanged(boolean isExpanded);
    }

    public void setOnStateChangeListener(OnStateChangeListener mListener) {
        this.mStateListener = mListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 手指按下时记录必要数据,纵坐标的值都需要减去状态栏高度
                xInView = event.getX();
                yInView = event.getY();
                xDownInScreen = event.getRawX();
                yDownInScreen = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                xInScreenNow = event.getRawX();
                yInScreenNow = event.getRawY();

                mParams.x = (int) (xInScreenNow - xInView);
                mParams.y = (int) (yInScreenNow - yInView - mStatusBarHeight);

                mWindowManager.updateViewLayout(this, mParams);
                break;
            case MotionEvent.ACTION_UP:
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    /**
     * 将小悬浮窗的参数传入，用于更新小悬浮窗的位置。
     *
     * @param params 小悬浮窗的参数
     */
    public void setParams(WindowManager windowManager, WindowManager.LayoutParams params) {
        mWindowManager = windowManager;
        mParams = params;
        mParams.y = mParams.y + ScreenUtil.getStatusBarHeight(getContext());
        LogUtil.e("setParams mParams.x = " + mParams.x + " mParams.y=" + mParams.y);
    }
}
