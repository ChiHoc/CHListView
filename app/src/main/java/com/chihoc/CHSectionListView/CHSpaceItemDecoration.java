package com.chihoc.CHSectionListView;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by ChiHo on 2016/11/30.
 */

public class CHSpaceItemDecoration extends CHRectItemDecoration {

    private Paint mPaint;

    public void setDelegate(DecorationDelegate mDelegate) {
        this.mDelegate = mDelegate;
    }

    private DecorationDelegate mDelegate;

    public CHSpaceItemDecoration(int spacing, int position) {
        super(new Rect(0, 0, 0, spacing), position);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(0);
        mPaint.setColor(Color.rgb(0xd3, 0xcc, 0xc6));
    }

    public CHSpaceItemDecoration(Rect rect, int position) {
        super(rect, position);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(0);
        mPaint.setColor(Color.rgb(0xd3, 0xcc, 0xc6));
    }

    public CHSpaceItemDecoration() {
        super();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(0);
        mPaint.setColor(Color.rgb(0xd3, 0xcc, 0xc6));
    }

    /**
     * 清除配置
     */
    public void clear() {
        mRectMap.clear();
    }

    /**
     * 添加ItemSpacing
     * @param spacing 间距
     * @param position 位置，-1为全局
     */
    public void putItemSpacing(int spacing, int position) {
        putItemDecoration(new Rect(0, 0, 0, spacing), position);
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(canvas, parent, state);
        if (mDelegate.isShowSeparator()) {
            final int childCount = parent.getChildCount();

            final int left = parent.getLeft() + parent.getPaddingLeft();
            final int right = parent.getRight() - parent.getPaddingRight();

            for (int position = 0; position < childCount; position++) {
                final View child = parent.getChildAt(position);
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int spacing;
                Rect rect = mRectMap.get(position);
                if (rect == null) {
                    spacing = mRect.bottom;
                } else {
                    spacing = rect.bottom;
                }
                if (spacing > 1 || position == childCount - 1) {
                    final int bottom = child.getBottom() + params.bottomMargin + Math.round(ViewCompat.getTranslationY(child));
                    canvas.drawLine(left, bottom, right, bottom, mPaint);
                }

                rect = mRectMap.get(position - 1);
                if (rect == null) {
                    spacing = mRect.bottom;
                } else {
                    spacing = rect.bottom;
                }
                if (spacing > 1) {
                    final int top = child.getTop() + params.topMargin;
                    canvas.drawLine(left, top, right, top, mPaint);
                } else if (spacing == 1){
                    final int top = child.getTop() + params.topMargin;
                    canvas.drawLine(child.getPaddingLeft() + child.getLeft(), top, child.getRight() - child.getPaddingRight(), top, mPaint);
                }
            }
        }
    }

    static abstract class DecorationDelegate {
        /**
         * 是否显示分割线
         * @return 是否显示
         */
        abstract public boolean isShowSeparator();
    }
}
