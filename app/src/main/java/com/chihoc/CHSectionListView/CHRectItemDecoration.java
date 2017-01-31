package com.chihoc.CHSectionListView;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * Created by ChiHo on 4/26/16.
 */
class CHRectItemDecoration extends RecyclerView.ItemDecoration {

    SparseArray<Rect> mRectMap;
    Rect mRect;

    CHRectItemDecoration() {
        mRectMap = new SparseArray<>();
        this.mRect = new Rect(0, 0, 0, 1);
    }

    /**
     * 添加Decoration
     * @param rect rect
     * @param position 位置，-1为全局
     */
    CHRectItemDecoration(Rect rect, int position) {
        mRectMap = new SparseArray<>();
        if (position == -1) {
            this.mRect = rect;
        } else {
            this.mRect = new Rect(0, 0, 0, 1);
            mRectMap.put(position, rect);
        }
    }

    /**
     * 添加Decoration
     * @param rect rect
     * @param position 位置，-1为全局
     */
    public void putItemDecoration(Rect rect, int position) {
        if (position == -1) {
            this.mRect = rect;
        } else {
            mRectMap.put(position, rect);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        final int itemPosition = parent.getChildAdapterPosition(view);
        if (itemPosition == RecyclerView.NO_POSITION) {
            return;
        }

        Rect rect = mRectMap.get(itemPosition);
        if (rect != null) {
            outRect.top = rect.top;
            outRect.left = rect.left;
            outRect.right = rect.right;
            outRect.bottom = rect.bottom;
        } else {
            outRect.top = mRect.top;
            outRect.left = mRect.left;
            outRect.right = mRect.right;
            outRect.bottom = mRect.bottom;
        }
    }
}