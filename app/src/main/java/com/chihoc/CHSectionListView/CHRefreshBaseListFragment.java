package com.chihoc.CHSectionListView;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.chihoc.chlistview.R;

import java.util.ArrayList;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by ChiHo on 2016/11/17.
 */
public abstract class CHRefreshBaseListFragment<T, H extends RecyclerView.ViewHolder> extends CHBaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate {

    final private int LIST_VIEW_HEADER_TYPE = 0x00000011;
    final private int LIST_VIEW_FOOTER_TYPE = 0x00000012;

    final private String LOG_TAG = "QKTRefreshListFragment";

    private boolean mShowHeader = false;
    private boolean mShowFooter = false;

    protected int mEmptyIcon = R.drawable.ch_list_empty_icon;
    public void setEmptyIcon(int mEmptyIcon) {
        this.mEmptyIcon = mEmptyIcon;
    }

    protected String mEmptyText = "列表为空";
    public void setEmptyText(String mEmptyText) {
        this.mEmptyText = mEmptyText;
    }

    protected BGARefreshLayout mRefreshLayout;
    protected QKTListBaseAdapter mAdapter;
    protected boolean mHasNoMoreData = false;
    protected ArrayList<T> mList;

    /**
     * 绑定header holder
     * @param holder holder
     */
    public void onBindListViewHeaderHolder(RecyclerView.ViewHolder holder) {

    }

    /**
     * 绑定footer holder
     * @param holder holder
     */
    public void onBindListViewFooterHolder(RecyclerView.ViewHolder holder) {

    }

    /**
     * 创建header holder
     * @param parent 父视图
     * @return holder
     */
    public RecyclerView.ViewHolder onCreateHeaderHolder(ViewGroup parent) {
        RelativeLayout relativeLayout = new RelativeLayout(mContext);
        RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, 0);
        relativeLayout.setLayoutParams(relativeParams);
        return new ViewHolder(relativeLayout);
    }

    /**
     * 创建footer holder
     * @param parent 父视图
     * @return holder
     */
    public RecyclerView.ViewHolder onCreateFooterHolder(ViewGroup parent) {
        RelativeLayout relativeLayout = new RelativeLayout(mContext);
        RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, 0);
        relativeLayout.setLayoutParams(relativeParams);
        return new ViewHolder(relativeLayout);
    }

    /**
     * 刷新数据
     */
    public void reloadData() {
        mAdapter.reloadData();
    }

    /**
     * 重新加载item
     * @param indexPath item位置
     */
    public void reloadItem(CHIndexPath indexPath) {
        mAdapter.reloadItem(indexPath);
    }

    /**
     * 插入item
     * @param indexPath indexPath
     * @param itemCount 数量
     */
    public void insertItems(CHIndexPath indexPath, int itemCount) {
        mAdapter.insertItems(indexPath, itemCount);
    }

    /**
     * 删除item
     * @param indexPath indexPath
     * @param itemCount 数量
     */
    public void removeItems(CHIndexPath indexPath, int itemCount) {
        mAdapter.removeItems(indexPath, itemCount);
    }

    /**
     * 插入section
     * @param sectionPos section位置
     */
    public void insertSections(int sectionPos, int sectionCount) {
        mAdapter.insertSections(sectionPos, sectionCount);
    }

    /**
     * 删除section
     * @param sectionPos section位置
     */
    public void removeSections(int sectionPos, int sectionCount) {
        mAdapter.removeSections(sectionPos, sectionCount);
    }

    /**
     * 响应点击行
     * @param row 行
     * @param dataSource 数据源
     */
    protected void onItemClickAtRow(int row, T dataSource) {
        Log.w(LOG_TAG, "Please override \"onItemClickAtRow\" method!");
    }

    /**
     * 是否高亮（不高亮则不响应点击事件)
     * @param row 行
     * @param dataSource 数据源
     * @return 是否高亮
     */
    protected boolean shouldHighlightAtRow(int row, T dataSource) {
        return false;
    }

    /**
     * 绑定holder
     * @param holder holder
     * @param dataSource 数据源
     */
    protected abstract void onBindViewHolder(H holder, T dataSource, int row);

    /**
     * 创建holder
     * @param parent parent
     * @return 创建holder
     */
    protected abstract H onCreateViewHolder(ViewGroup parent);

    /**
     * 设置是否显示分割线
     * @param isShow 是否显示
     */
    protected void setShowSeparator(boolean isShow) {
        mAdapter.setShowSeparator(isShow);
    }

    /**
     * 是否显示分割线
     * @return 是否显示
     */
    public boolean isShowSeparator() {
        return mAdapter.isShowSeparator();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContainerView(R.layout.ch_refresh_list_layout);
        mList = new ArrayList<>();
        mShowHeader = isMethodOverriden(this, "onCreateHeaderHolder", ViewGroup.class);
        mShowFooter = isMethodOverriden(this, "onCreateFooterHolder", ViewGroup.class);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initView(View view) {
        mRefreshLayout = (BGARefreshLayout) view.findViewById(R.id.qkt_refresh_layout);
        initRefreshLayout(mRefreshLayout);
        initListView(view);

        mAdapter.setIsRefreshing(true);
        mRefreshLayout.beginRefreshing();
    }

    /**
     * 初始化ListView
     */
    private void initListView(View view) {

        CHSectionListView listView = (CHSectionListView) view.findViewById(R.id.qkt_refresh_list_view);
        mAdapter = new QKTListBaseAdapter(mContext);
        listView.setAdapter(mAdapter);
    }

    /**
     * 设置行间距
     * @param rowSpacing 行间距
     */
    public void setRowSpacing(int rowSpacing) {
        mAdapter.setRowSpacing(rowSpacing);
    }

    /**
     * 初始化刷新空间
     * @param mRefreshLayout 刷新空间
     */
    private void initRefreshLayout(BGARefreshLayout mRefreshLayout) {
        // 为BGARefreshLayout设置代理
        mRefreshLayout.setDelegate(this);

        BGANormalRefreshViewHolder holder = new BGANormalRefreshViewHolder(mContext, true);
        mRefreshLayout.setRefreshViewHolder(holder);
    }


    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if(mHasNoMoreData || mList.size() == 0) {
            return false;
        }
        return true;
    }

    public class QKTListBaseAdapter extends CHSectionListBaseAdapter {

        private int QKT_ITEM_TYPE_CONTENT = 27;

        QKTListBaseAdapter(Context context) {
            super(context);
        }

        @Override
        protected void onBindHeaderHolder(RecyclerView.ViewHolder holder, int section) {
            onBindListViewHeaderHolder(holder);
        }

        @Override
        protected void onBindFooterHolder(RecyclerView.ViewHolder holder, int section) {
            onBindListViewFooterHolder(holder);
        }

        @Override
        protected int getHeaderTypeOfSection(int section) {
            if (mShowHeader) {
                return LIST_VIEW_HEADER_TYPE;
            } else {
                return RecyclerView.INVALID_TYPE;
            }
        }

        @Override
        protected int getFooterTypeOfSection(int section) {
            if (mShowFooter) {
                return LIST_VIEW_FOOTER_TYPE;
            } else {
                return RecyclerView.INVALID_TYPE;
            }
        }

        @Override
        protected int getEmptyIcon() {
            return mEmptyIcon;
        }

        @Override
        protected String getEmptyText() {
            return mEmptyText;
        }

        @Override
        protected int getSectionCount() {
            return 1;
        }

        @Override
        protected int getRowCountAtSection(int section) {
            return mList.size();
        }

        @Override
        protected int getItemViewType(CHIndexPath indexPath) {
            return QKT_ITEM_TYPE_CONTENT;
        }

        @Override
        protected void onBindViewHolder(RecyclerView.ViewHolder holder, CHIndexPath indexPath) {
            CHRefreshBaseListFragment.this.onBindViewHolder((H) holder, mList.get(indexPath.getRow()), indexPath.getRow());
        }

        @Override
        protected void onItemClickAtIndexPath(CHIndexPath indexPath, View view) {
            onItemClickAtRow(indexPath.getRow(), mList.get(indexPath.getRow()));
        }

        @Override
        protected boolean shouldHighlightAtIndexPath(CHIndexPath indexPath) {
            return shouldHighlightAtRow(indexPath.getRow(), mList.get(indexPath.getRow()));
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolderWithViewType(ViewGroup parent, int viewType) {
            if (viewType == LIST_VIEW_HEADER_TYPE) {
                return onCreateHeaderHolder(parent);
            } else if (viewType == LIST_VIEW_FOOTER_TYPE) {
                return onCreateFooterHolder(parent);
            }
            return CHRefreshBaseListFragment.this.onCreateViewHolder(parent);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
