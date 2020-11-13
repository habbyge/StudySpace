package com.zhx.myworkdemo.view.dfab;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Map;

/**
 * 自定义FAB
 * 1、边框吸附、可拖拽
 * 2、点击展示菜单、菜单项可动态增减、自定义点击事件
 * 3、FAB及菜单项的 icon、颜色均可动态配置
 *
 * @author zhx
 */
public class DragFloatingButton extends FloatingActionButton implements View.OnClickListener {

    private int screenWidth;
    private int screenHeight;
    private int screenWidthHalf;
    private int statusHeight = 44;
    private int virtualHeight = 44;
    /**
     * 接入方根据实际情况，设置底部margin值，防止被ButtomBar遮住
     */
    private int bottomOffset = 50;
    private int lastX;
    private int lastY;
    boolean isDrag;

    private Map<String, IItem> mItemMap;
    private boolean isDragable;
    private AbstractItem mItem;
    private ActionInterface iActionCallback;

    public DragFloatingButton(Context context) {
        this(context, null);
    }

    public DragFloatingButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragFloatingButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        setOnClickListener(this);
    }

    /**
     * 属性初始化操作
     */

    private void init() {
        Log.e("dfab", "init() ....");
        screenWidth = ScreenUtils.getScreenWidth(getContext());
        screenWidthHalf = screenWidth / 2;
        screenHeight = ScreenUtils.getScreenHeight(getContext());
        statusHeight = ScreenUtils.getStatusHeight(getContext());
        virtualHeight = ScreenUtils.getVirtualBarHeigh(getContext());
    }


    public void initConfig(FabConfig config) {
        isDragable = config.isDragable();
        iActionCallback = config.getCallback();
        bottomOffset = config.getBottomMargin();
        mItemMap = config.getItemArray();
        update(config.getDefaultTag());
    }

    private void update(IItem item) {
        mItem = (AbstractItem) item;
        mItem.register(iActionCallback);
        Log.e("dfab", "getDrawable ");
        setImageDrawable(mItem.getDrawable(getContext()));
        postInvalidate();
    }

    public void update(String tag) {
        if (tag == null || "".equals(tag)) {
            Log.d("dfab", "invalid tag");
            return;
        }
        if (!mItemMap.containsKey(tag)) {
            return;
        }
        IItem item = mItemMap.get(tag);
        if (item != null) {
            this.update(item);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.e("dfab", "onAttachedToWindow() ....");
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.e("dfab", "onDetachedFromWindow() ....");
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int rawX = (int) event.getRawX();
        final int rawY = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                isDrag = false;
                getParent().requestDisallowInterceptTouchEvent(true);
                lastX = rawX;
                lastY = rawY;
                break;
            case MotionEvent.ACTION_MOVE:
                if (isDragable) {
                    isDrag = true;
                }
                int dx = rawX - lastX;
                int dy = rawY - lastY;
                int distance = (int) Math.sqrt(dx * dx + dy * dy);
                if (distance < 10) {
                    isDrag = false;
                    break;
                }
                float x = getX() + dx;
                float y = getY() + dy;

                //检测到达边缘
                x = x < 0 ? 0 : x > screenWidth - getWidth() ? screenWidth - getWidth() : x;
                // y = y < statusHeight ? statusHeight : (y + getHeight() >= screenHeight ? screenHeight - getHeight() : y);
                if (y < 0) {
                    y = 0;
                }
                if (y > screenHeight - statusHeight - getHeight() - ScreenUtils.dip2px(getContext(), bottomOffset)) {
                    y = screenHeight - statusHeight - getHeight() - ScreenUtils.dip2px(getContext(), bottomOffset);
                }
                setX(x);
                setY(y);

                lastX = rawX;
                lastY = rawY;
                break;
            case MotionEvent.ACTION_UP:
                if (isDrag) {
                    //防止触发click
                    setPressed(false);
                }
                if (rawX >= screenWidthHalf) {
                    animate().setInterpolator(new DecelerateInterpolator())
                            .setDuration(100)
                            .xBy(screenWidth - getWidth() - getX())
                            .start();
                } else {
                    ObjectAnimator oa = ObjectAnimator.ofFloat(this, "x", getX(), 0);
                    oa.setInterpolator(new DecelerateInterpolator());
                    oa.setDuration(100);
                    oa.start();
                }
                break;
            default:
                break;
        }
        return isDrag || super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e("dfab", "onMeasure ");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.e("dfab", "onLayout ");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void onClick(final View v) {
        if (mItem != null) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    mItem.onAction(v);
                }
            }, 200);
        }
    }
}
