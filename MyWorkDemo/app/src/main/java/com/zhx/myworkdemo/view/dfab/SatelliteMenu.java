package com.zhx.myworkdemo.view.dfab;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhx.myworkdemo.utils.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by csy on 2017/10/10.
 * 最多显示5个子菜单,如果需要显示更多,可自己做拓展
 * 微信公众号:陈守印同学
 * https://github.com/chenshouyin/SatelliteMenu
 */

public class SatelliteMenu extends RelativeLayout implements View.OnClickListener {
    /**
     * menu items
     */
    private List<TextView> textViews;

    private Context mContext;
    //弧形半径 默认值
    private float mRadius = 300;
    //左上,右上，左下，右下
    private int mPostion;
    private float mMenuItemImageWidth = 90;
    //正还是反
    private int flag = 1;
    private OnMenuItemClickListener mOnMenuItemClickListener;


    public SatelliteMenu(Context context) {
        this(context, null);
    }

    public SatelliteMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SatelliteMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        mContext = context;
//        //获取xml中自定义属性的值
//        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SatelliteMenu);
//        mRadius = a.getDimension(R.styleable.SatelliteMenu_radius, 200);
//        //默认左上
//        mPostion = a.getInteger(R.styleable.SatelliteMenu_menu_postion, 0);
//        //get dp will be convert to px
//        mMenuItemImageWidth = a.getDimension(R.styleable.SatelliteMenu_menu_item_image_width, 45);
//        a.recycle();

        setOnClickListener(this);
    }

    public void setRadius(float mRadius) {
        this.mRadius = ScreenUtils.dip2px(mContext, mRadius);
    }

    public void setMenuItemImageWidth(float itemWidth) {
        this.mMenuItemImageWidth = ScreenUtils.dip2px(mContext, itemWidth);
        Logger.d("setMenuItemImageWidth itemWidth=" + itemWidth + " mMenuItemImageWidth=" + mMenuItemImageWidth);
    }

    @Override
    public void onClick(View view) {
        if (mOnMenuItemClickListener == null) {
            return;
        }
        handleClick(view);
    }

    private boolean handleClick(View view) {
        for (int i = 0; i < textViews.size(); i++) {
            if (view == textViews.get(i)) {
                //菜单点击回调
                mOnMenuItemClickListener.onClick(view, i);
                return true;
            }
        }
        mOnMenuItemClickListener.onClick(view, -1);
        return false;
    }

    /**
     * 打开和关闭只是x和y不一样
     *
     * @param isMenuOpen
     */
    private void perfomMenuAnim(final boolean isMenuOpen) {
        if (isMenuOpen) {
            //需要打开菜单
            flag = 1;
        } else {
            //收回菜单,相反的操作
            flag = -1;
        }
        animMenuItem();
    }


    /**
     * 菜单点击回调,提供给外部
     */
    public interface OnMenuItemClickListener {
        void onClick(View view, int postion);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //测量子view大小，不然下面获取到的宽高是0
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
        }
        //默认只有这一句 这个只是父类的measure所以getMeasuredWidth()是有值的
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //循环计算子view的位置
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);

            if (mPostion == MenuPosition.LEFT_TOP) {

                if (child instanceof ViewGroup) {
                    ViewGroup viewGroup = (ViewGroup) child;
                    for (int j = 0; j < viewGroup.getChildCount(); j++) {
                        child = viewGroup.getChildAt(j);
                        l = 0;
                        t = 0;
                        r = child.getMeasuredWidth();
                        b = child.getMeasuredHeight();
                        child.layout(l, t, r, b);
                    }
                } else {
                    l = 0;
                    t = 0;
                    r = child.getMeasuredWidth();
                    b = child.getMeasuredHeight();
                    child.layout(l, t, r, b);
                }
            } else if (mPostion == MenuPosition.RIGHT_TOP) {

                if (child instanceof ViewGroup) {
                    ViewGroup viewGroup = (ViewGroup) child;
                    //子菜单位置
                    for (int j = 0; j < viewGroup.getChildCount(); j++) {
                        child = viewGroup.getChildAt(j);
                        l = getMeasuredWidth() - child.getMeasuredWidth();
                        t = 0;
                        r = getMeasuredWidth();
                        b = child.getMeasuredHeight();
                        child.layout(l, t, r, b);
                    }
                } else {
                    l = getMeasuredWidth() - child.getMeasuredWidth();
                    t = 0;
                    r = getMeasuredWidth();
                    b = child.getMeasuredHeight();
                    child.layout(l, t, r, b);
                }

            } else if (mPostion == MenuPosition.LEFT_BOTTOM) {


                if (child instanceof ViewGroup) {
                    ViewGroup viewGroup = (ViewGroup) child;
                    //子菜单位置
                    for (int j = 0; j < viewGroup.getChildCount(); j++) {
                        child = viewGroup.getChildAt(j);
                        l = 0;
                        t = getHeight() - child.getMeasuredHeight();
                        r = child.getMeasuredWidth();
                        b = getHeight();
                        child.layout(l, t, r, b);
                    }
                } else {
                    l = 0;
                    t = getHeight() - child.getMeasuredHeight();
                    r = child.getMeasuredWidth();
                    b = getHeight();
                    child.layout(l, t, r, b);
                }

            } else if (mPostion == MenuPosition.RIGHT_BOTTOM) {


                if (child instanceof ViewGroup) {
                    ViewGroup viewGroup = (ViewGroup) child;
                    //子菜单位置
                    for (int j = 0; j < viewGroup.getChildCount(); j++) {
                        child = viewGroup.getChildAt(j);
                        l = getWidth() - child.getMeasuredWidth();
                        t = getHeight() - child.getMeasuredHeight();
                        r = getWidth();
                        b = getHeight();
                        child.layout(l, t, r, b);
                    }
                } else {
                    l = getWidth() - child.getMeasuredWidth();
                    t = getHeight() - child.getMeasuredHeight();
                    r = getWidth();
                    b = getHeight();
                    child.layout(l, t, r, b);
                }

            }
        }
    }

    /**
     * 子菜单动画
     */
    private void animMenuItem() {

        //子菜单 平移过程中发生变化的属性translationX,translationY
        //菜单移动的位移
        final float distance = mRadius;
        for (int i = 0; i < textViews.size(); i++) {
            //Math.PI 3.14也就是180°    Math.PI/2=90°  注意不能直接用90参与运算,因为单位不一样
            float mX = 0;
            float mY = 0;
            if (mPostion == MenuPosition.LEFT_TOP) {
                mX = ((float) (distance * Math.sin(Math.PI / 2 / (textViews.size() - 1) * i))) * flag;
                mY = ((float) (distance * Math.cos(Math.PI / 2 / (textViews.size() - 1) * i))) * flag;
            } else if (mPostion == MenuPosition.RIGHT_TOP) {
                //位移位置是相对于本身原来所在的点
                mX = (float) (-distance * Math.sin(Math.PI / 2 / (textViews.size() - 1) * i)) * flag;
                mY = ((float) (distance * Math.cos(Math.PI / 2 / (textViews.size() - 1) * i))) * flag;
            } else if (mPostion == MenuPosition.LEFT_BOTTOM) {
                mX = ((float) (distance * Math.sin(Math.PI / 2 / (textViews.size() - 1) * (textViews.size() - 1 - i)))) * flag;
                mY = -((float) (distance * Math.cos(Math.PI / 2 / (textViews.size() - 1) * (textViews.size() - 1 - i)))) * flag;
            } else if (mPostion == MenuPosition.RIGHT_BOTTOM) {
                //位移位置是相对于本身原来所在的点
                mX = (float) (-distance * Math.sin(Math.PI / 2 / (textViews.size() - 1) * (textViews.size() - 1 - i))) * flag;
                mY = ((float) -(distance * Math.cos(Math.PI / 2 / (textViews.size() - 1) * (textViews.size() - 1 - i)))) * flag;
            }
            //平移动画
            ObjectAnimator animatorX = ObjectAnimator.ofFloat(textViews.get(i), "translationX", 0, mX);
            ObjectAnimator animatorY = ObjectAnimator.ofFloat(textViews.get(i), "translationY", 0, mY);

            animatorX.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    Logger.d("onAnimationEnd");
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                    Logger.d("onAnimationCancel");
                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                    Logger.d("onAnimationRepeat");
                }
            });
            AnimatorSet mAnimatorSet = new AnimatorSet();
            mAnimatorSet.playTogether(animatorX, animatorY);
            mAnimatorSet.setDuration(200);
            mAnimatorSet.setInterpolator(new AccelerateInterpolator());
            mAnimatorSet.start();
        }
    }

    /**
     * 6.0以下
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Logger.d("onAttachedToWindow ");
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            perfomMenuAnim(true);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        Logger.d("onDetachedFromWindow ");
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            perfomMenuAnim(false);
        }
        super.onDetachedFromWindow();
    }

    /**
     * 6.0以上
     *
     * @param changedView
     * @param visibility
     */
    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        Logger.d("onVisibilityChanged " + visibility);
        if (View.VISIBLE == visibility) {
            perfomMenuAnim(true);
        }
    }

    /**
     * 设置图片资源,根据传入的图片个数添加对应个数的子菜单
     *
     * @param imageResource
     */
    public void setMenuItemImage(List<Integer> imageResource) {
        if (imageResource == null) {
            return;
        }
        textViews = new ArrayList<>();
        //Dynamic add view
        for (int i = 0; i < imageResource.size(); i++) {
            TextView tv = new TextView(mContext);
            LayoutParams params = new LayoutParams((int) mMenuItemImageWidth, (int) mMenuItemImageWidth);
            tv.setLayoutParams(params);
            Logger.d("setMenuItem mMenuItemImageWidth = " + mMenuItemImageWidth);
            //不设置文字
            tv.setBackgroundResource(imageResource.get(i));
            textViews.add(tv);
            addView(tv);
            tv.setOnClickListener(this);
        }
    }


    public void setOnMenuItemClickListener(OnMenuItemClickListener mOnMenuItemClickListener) {
        this.mOnMenuItemClickListener = mOnMenuItemClickListener;
    }

    public void setPostion(int mPostion) {
        this.mPostion = mPostion;
    }

    public Builder getBuilder() {
        return new Builder();
    }


    /**
     * 构造器
     */
    public class Builder {
        private List<Integer> imageMenuItemImageResource;
        private OnMenuItemClickListener mOnMenuItemClickListener;
        private int position;
        private float radius;
        private float itemWidth;

        public Builder() {
        }

        public Builder setMenuItemImageResource(List<Integer> imageMenuItemImageResource) {
            this.imageMenuItemImageResource = imageMenuItemImageResource;
            return this;
        }

        public Builder setOnMenuItemClickListener(OnMenuItemClickListener mOnMenuItemClickListener) {
            this.mOnMenuItemClickListener = mOnMenuItemClickListener;
            return this;
        }

        public Builder setPosition(int position) {
            this.position = position;
            return this;
        }

        public Builder setRadius(float r) {
            this.radius = r;
            return this;
        }

        public Builder setMenuItemImageWidth(float itemWidth) {
            this.itemWidth = itemWidth;
            return this;
        }

        public void create() {
            SatelliteMenu.this.setMenuItemImage(this.imageMenuItemImageResource);
            SatelliteMenu.this.setPostion(this.position);
            SatelliteMenu.this.setMenuItemImageWidth(this.itemWidth);
            SatelliteMenu.this.setRadius(this.radius);
            SatelliteMenu.this.setOnMenuItemClickListener(this.mOnMenuItemClickListener);
        }
    }
}