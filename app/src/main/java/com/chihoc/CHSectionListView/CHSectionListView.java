package com.chihoc.CHSectionListView;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;

public class CHSectionListView extends RecyclerView {

    private Context mContext;

    public CHSectionListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        setLayoutManager(null);
        setHasFixedSize(true);
    }

    public CHSectionListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;

        setLayoutManager(null);
        setHasFixedSize(true);
    }

    public void setAdapter(CHSectionListBaseAdapter adapter) {
        super.setAdapter(adapter);
        adapter.setListView(this);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        if (getLayoutParams().height == LayoutParams.WRAP_CONTENT) {
            int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                    MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);

            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = getMeasuredHeight();
        }
        else
        {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    final public boolean isHorizontalFadingEdgeEnabled() {
        return false;
    }

    @Override
    final public boolean canScrollHorizontally(int direction) {
        return false;
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        super.setLayoutManager(linearLayoutManager);
    }
}
