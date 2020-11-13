package com.zhx.myworkdemo.view.dfab;

import java.util.HashMap;
import java.util.Map;

/**
 * DragFloatingButton 参数配置类
 * @author zhx
 */
public class FabConfig {
    /**
     * 按钮自身数据模型，实际场景中根据tag找到对应数据模型实现切换按钮样式以及功能
     */
    private Map<String, IItem> mItemArray;
    /**
     * 供接入方监听不同事件
     */
    private ActionInterface iCallback = null;
    /**
     * 是否启用拖拽模式
     */
    private boolean isDragable;
    /**
     * 自定义按钮距离屏幕底部距离
     */
    private int mBottomMargin;
    /**
     * 按钮默认使用的数据模型（UI+功能）
     */
    private String defaultTag = "";

    public boolean isDragable() {
        return isDragable;
    }

    public Map<String, IItem> getItemArray() {
        return mItemArray;
    }

    public ActionInterface getCallback() {
        return iCallback;
    }

    public String getDefaultTag() {
        return defaultTag;
    }

    private FabConfig(Builder builder) {
        this.isDragable = builder.isDragable;
        this.mItemArray = builder.mItemArray;
        this.iCallback = builder.iCallback;
        this.defaultTag = builder.defaultTag;
        this.mBottomMargin = builder.mBottomMargin;
    }

    public int getBottomMargin() {
        return mBottomMargin;
    }

    public static class Builder {

        private boolean isDragable;
        private int mBottomMargin = 44;
        private Map<String, IItem> mItemArray;
        private ActionInterface iCallback = null;
        private String defaultTag = "";

        public Builder() {
            mItemArray = new HashMap<>(2);
        }

        public Builder setDragable(boolean isDragable) {
            this.isDragable = isDragable;
            return this;
        }

        public Builder addItem(String tag, IItem item) {
            return addItem(tag, item, false);
        }

        public Builder addItem(String tag, IItem item, boolean isDefault) {
            //初始化时确定默认显示的item
            if (isDefault) {
                defaultTag = tag;
            } else {
                if (this.mItemArray.size() == 0) {
                    defaultTag = tag;
                }
            }
            this.mItemArray.put(tag, item);
            return this;
        }

        public Builder setCallback(ActionInterface iCallback) {
            this.iCallback = iCallback;
            return this;
        }


        public Builder setBottomMargin(int bottomOffset) {
            this.mBottomMargin = bottomOffset;
            return this;
        }


        public FabConfig build() {
            return new FabConfig(this);
        }
    }
}
