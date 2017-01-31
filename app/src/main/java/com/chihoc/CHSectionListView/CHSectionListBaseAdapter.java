package com.chihoc.CHSectionListView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.chihoc.chlistview.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


/**
 * Created by Chiho on 16-11-28.
 */
public abstract class CHSectionListBaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int EMPTY_ITEM_TYPE = 2727;
    private final static int LOADING_ITEM_TYPE = 2728;

    protected LayoutInflater inflater;
    protected Context mContext;
    private ArrayList<Integer> mItemsStatus = new ArrayList<>();

    /**
     * 若在刷新时，则显示刷新视图
     */
    private boolean mIsRefreshing;
    public void setIsRefreshing(boolean mIsRefreshing) {
        if (this.mIsRefreshing != mIsRefreshing) {
            this.mIsRefreshing = mIsRefreshing;
            reloadData();
        }
    }

    /**
     * 若为空，且不在刷新状态，则显示空视图
     */
    private boolean mIsEmpty;
    public void setIsEmpty(boolean mIsEmpty) {
        if (this.mIsEmpty = mIsEmpty) {
            this.mIsEmpty = mIsEmpty;
            reloadData();
        }
    }

    private CHSpaceItemDecoration mItemDecoration;
    private CHSpaceItemDecoration getItemDecoration() {
        if (mItemDecoration == null) {
            mItemDecoration = new CHSpaceItemDecoration();

            mItemDecoration.setDelegate(new CHSpaceItemDecoration.DecorationDelegate() {
                @Override
                public boolean isShowSeparator() {
                    return !mIsRefreshing && !mIsEmpty && mIsShowSeparator;
                }
            });
        }
        return mItemDecoration;
    }

    /**
     * 显示分割线
     */
    private boolean mIsShowSeparator = true;
    public boolean isShowSeparator() {
        return mIsShowSeparator;
    }

    public void setShowSeparator(boolean showSeparator) {
        if (this.mIsShowSeparator != showSeparator) {
            this.mIsShowSeparator = showSeparator;
            getListView().invalidate();
        }
        this.mIsShowSeparator = showSeparator;
    }

    /**
     * section距离
     */
    private int mSectionSpacing = 0;
    public int getSectionSpacing() {
        return mSectionSpacing;
    }

    public void setSectionSpacing(int sectionSpacing) {
        if (this.mSectionSpacing != sectionSpacing) {
            this.mSectionSpacing = sectionSpacing;
            setSectionSpacing();
        }
    }

    /**
     * row距离
     */
    private int mRowSpacing = 0;
    public int getRowSpacing() {
        return mRowSpacing;
    }

    public void setRowSpacing(int rowSpacing) {
        if (this.mRowSpacing != rowSpacing) {
            this.mRowSpacing = rowSpacing;
            setRowSpacing();
        }
    }

    /**
     * listView弱引用
     */
    private WeakReference<CHSectionListView> mWeakListView;
    void setListView(CHSectionListView listView) {
        mWeakListView = new WeakReference<>(listView);

        reloadItemsStatus();
        listView.addItemDecoration(getItemDecoration(), 0);
        resetSectionSpacing();

        registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
            }

            @Override
            public void onChanged() {
                super.onChanged();
            }
        });
    }

    private CHSectionListView getListView() {
        return mWeakListView.get();
    }

    public CHSectionListBaseAdapter(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        mRowSpacing = 1;
    }

    /**
     * 获取 section数
     * @return section数
     */
    protected abstract int getSectionCount();

    /**
     * 获取section对应行数
     * @param section section
     * @return 行数
     */
    protected abstract int getRowCountAtSection(int section);

    /**
     * 获取indexPath对应item类型
     * @param indexPath indexPath
     * @return item类型
     */
    protected abstract int getItemViewType(CHIndexPath indexPath);

    /**
     * 绑定holder
     * @param holder holder
     * @param indexPath indexPath
     */
    protected abstract void onBindViewHolder(RecyclerView.ViewHolder holder, CHIndexPath indexPath);

    /**
     * 绑定header
     * @param holder holder
     * @param section section
     */
    protected void onBindHeaderHolder(RecyclerView.ViewHolder holder, int section) {}

    /**
     * 绑定footer
     * @param holder holder
     * @param section section
     */
    protected void onBindFooterHolder(RecyclerView.ViewHolder holder, int section) {}

    /**
     * 响应点击item
     * @param indexPath indexPath
     * @param view itemView
     */
    protected abstract void onItemClickAtIndexPath(CHIndexPath indexPath, View view);

    /**
     * 是否高亮（不高亮则不响应点击事件)
     * @param indexPath indexPath
     * @return 是否高亮
     */
    protected abstract boolean shouldHighlightAtIndexPath(CHIndexPath indexPath);

    /**
     * 获取section对应header类型
     * @param section section
     * @return item类型
     */
    protected int getHeaderTypeOfSection(int section) {
        return RecyclerView.INVALID_TYPE ;
    }

    /**
     * 获取section对应footer类型
     * @param section section
     * @return item类型
     */
    protected int getFooterTypeOfSection(int section) {
        return RecyclerView.INVALID_TYPE ;
    }

    /**
     * 创建holder
     * @param parent parent
     * @param viewType viewType
     * @return holder
     */
    protected abstract RecyclerView.ViewHolder onCreateViewHolderWithViewType(ViewGroup parent, int viewType);

    /**
     * 获取空列表icon
     * @return icon资源
     */
    protected int getEmptyIcon() {
        return R.drawable.ch_list_empty_icon;
    }

    /**
     * 获取空列表文字
     * @return 文字
     */
    protected String getEmptyText() {
        return "列表为空";
    }

    /**
     * 重新加载item情况
     */
    private void reloadItemsStatus() {
        int sectionCount = getSectionCount();
        mItemsStatus.clear();
        Integer sum = 0;
        mItemsStatus.add(sum);
        for (int section = 0; section < sectionCount; section ++) {
            int rowCount = getRowCountAtSection(section) + (getHeaderTypeOfSection(section) != RecyclerView.INVALID_TYPE ? 1 : 0) + (getFooterTypeOfSection(section) != RecyclerView.INVALID_TYPE  ? 1 : 0);
            mItemsStatus.add(rowCount);
            sum += rowCount;
        }
        if (sum == 0) {
            mIsEmpty = true;
            mItemsStatus.set(0, 1);
            mItemsStatus.set(1, 1);
        } else {
            mIsEmpty = false;
            mItemsStatus.set(0, sum);
        }
    }

    /**
     * 获取position相应位置
     * @param position position
     * @return 位置
     */
    private CHItemIndexPath getIndexPathOfPostion(int position) {
        int section;
        int row = position;
        int sectionCount = mItemsStatus.size() - 1;
        for (section = 0; section < sectionCount; section ++) {
            int rowCount = mItemsStatus.get(section + 1);
            if (row < rowCount) {
                break;
            }
            row -= rowCount;
        }
        CHItemIndexPath indexPath = new CHItemIndexPath(section, row);
        return indexPath;
    }

    /**
     * 获取indexPath相应位置position
     * @param itemIndexPath indexPath
     * @return position
     */
    private int getPostionOfIndexPath(CHItemIndexPath itemIndexPath) {
        int position = 0;
        for (int section = 0; section < itemIndexPath.mSection; section ++) {
            position += mItemsStatus.get(section + 1);
        }
        position += itemIndexPath.mRow;
        return position;
    }

    @Override
    final public int getItemViewType(int position)
    {
        if (mIsRefreshing) {
            return LOADING_ITEM_TYPE;
        } else if (mIsEmpty) {
            return EMPTY_ITEM_TYPE;
        } else {
            CHItemIndexPath itemIndexPath = getIndexPathOfPostion(position);
            if (itemIndexPath.isHeader()) {
                return getHeaderTypeOfSection(itemIndexPath.mSection);
            } else if (itemIndexPath.isFooter()) {
                return getFooterTypeOfSection(itemIndexPath.mSection);
            } else {
                return getItemViewType(itemIndexPath.getItemIndexPath());
            }
        }
    }

    @Override
    final public int getItemCount() {
        return mItemsStatus.get(0);
    }

    @Override
    final public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (mIsRefreshing) {

        } else if (mIsEmpty) {
            ((EmptyViewHolder)holder).mIcon.setImageResource(getEmptyIcon());
            ((EmptyViewHolder)holder).mText.setText(getEmptyText());
        } else {
            final CHItemIndexPath itemIndexPath = getIndexPathOfPostion(position);
            if (itemIndexPath.isHeader()) {
                onBindHeaderHolder(holder, itemIndexPath.mSection);
            } else if (itemIndexPath.isFooter()) {
                onBindFooterHolder(holder, itemIndexPath.mSection);
            } else {
                final CHIndexPath indexPath = itemIndexPath.getItemIndexPath();
                onBindViewHolder(holder, indexPath);
                if (shouldHighlightAtIndexPath(indexPath)) {
                    holder.itemView.setBackgroundResource(R.drawable.ch_layout_click_sel);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onItemClickAtIndexPath(indexPath, v);
                        }
                    });
                }
            }
        }
    }

    @Override
    final public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == LOADING_ITEM_TYPE) {
            View v = new View(mContext);

            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.height = (int) (parent.getHeight() * 0.8);
            v.setLayoutParams(layoutParams);

            return new LoadingViewHolder(v);
        } else if (viewType == EMPTY_ITEM_TYPE) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.ch_empty_list_item, parent, false);

            AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
                    AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT);
            layoutParams.height = parent.getHeight();
            v.setLayoutParams(layoutParams);

            return new EmptyViewHolder(v);
        }
        return onCreateViewHolderWithViewType(parent, viewType);
    }

    /**
     * 重新加载数据
     */
    public void reloadData() {
        reloadItemsStatus();
        notifyDataSetChanged();
        resetSectionSpacing();
    }

    /**
     * 插入item
     * @param indexPath indexPath
     * @param itemCount 数量
     */
    public void insertItems(CHIndexPath indexPath, int itemCount) {
        if (itemCount == 0) {
            return;
        }
        reloadItemsStatus();
        int position = getPostionOfIndexPath(new CHItemIndexPath(indexPath));
        notifyItemRangeInserted(position, itemCount);
        if (position > 0) {
            position --;
        }
        notifyItemRangeChanged(position, mItemsStatus.get(0) - position);
        resetSectionSpacing();
    }

    /**
     * 删除item
     * @param indexPath indexPath
     * @param itemCount 数量
     */
    public void removeItems(CHIndexPath indexPath, int itemCount) {
        if (itemCount == 0) {
            return;
        }
        reloadItemsStatus();
        int position = getPostionOfIndexPath(new CHItemIndexPath(indexPath));
        notifyItemRangeRemoved(position, itemCount);
        if (position > 0) {
            position --;
        }
        notifyItemRangeChanged(position, mItemsStatus.get(0) - position);
        resetSectionSpacing();
    }

    /**
     * 插入section
     * @param sectionPos section位置
     */
    public void insertSections(int sectionPos, int sectionCount) {
        if (sectionCount == 0) {
            return;
        }
        reloadItemsStatus();
        int position = getPostionOfIndexPath(new CHItemIndexPath(sectionPos, 0));
        int rowCount = 0;
        for (int count = 0; count < sectionCount; count ++) {
            rowCount += getRowCountAtSection(sectionPos + count);
        }
        notifyItemRangeInserted(position, rowCount);
        if (position > 0) {
            position --;
        }
        notifyItemRangeChanged(position, mItemsStatus.get(0) - position);
        resetSectionSpacing();
    }

    /**
     * 删除section
     * @param sectionPos section位置
     */
    public void removeSections(int sectionPos, int sectionCount) {
        if (sectionCount == 0) {
            return;
        }
        int position = getPostionOfIndexPath(new CHItemIndexPath(sectionPos, 0));
        int rowCount = 0;
        for (int count = 0; count < sectionCount; count ++) {
            rowCount += getRowCountAtSection(sectionPos + count);
        }
        reloadItemsStatus();
        notifyItemRangeRemoved(position, rowCount);
        if (position > 0) {
            position --;
        }
        notifyItemRangeChanged(position, mItemsStatus.get(0) - position);
        resetSectionSpacing();
    }

    /**
     * 重新加载item
     * @param indexPath item位置
     */
    public void reloadItem(CHIndexPath indexPath) {
        int position = getPostionOfIndexPath(new CHItemIndexPath(indexPath));
        notifyItemChanged(position);
    }

    /**
     * 重置垂直间隔
     */
    private void resetSectionSpacing() {
        setSectionSpacing();
        setRowSpacing();
    }

    /**
     * 设置行间隔
     */
    private void setRowSpacing() {
        if (getListView() != null) {
            getItemDecoration().putItemSpacing(mRowSpacing, -1);
            CHSectionListView listView = getListView();
            listView.setPadding(listView.getPaddingLeft(), mRowSpacing, listView.getPaddingRight(), mRowSpacing);
        }
    }

    /**
     * 设置section间隔
     */
    private void setSectionSpacing() {
        if (getListView() != null) {
            getItemDecoration().clear();
            int sectionCount = mItemsStatus.size() - 1;
            for (int section = 0; section < sectionCount - 1; section++) {
                setSectionDecoration(mSectionSpacing, section);
            }
        }
    }

    /**
     * 获取section间距
     * @param spacing 间距
     * @param section section
     */
    private void setSectionDecoration(int spacing, int section) {
        int position = getPostionOfIndexPath(new CHItemIndexPath(section, mItemsStatus.get(section + 1) - 1));
        getItemDecoration().putItemSpacing(spacing, position);
    }

    private class EmptyViewHolder extends RecyclerView.ViewHolder {

        ImageView mIcon;
        TextView mText;

        EmptyViewHolder(View itemView) {
            super(itemView);
            mIcon = (ImageView) itemView.findViewById(R.id.ch_empty_list_icon);
            mText = (TextView) itemView.findViewById(R.id.ch_empty_list_text);
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class CHItemIndexPath extends CHIndexPath {

        int mSectionRowsCount;
        boolean mIsFirst = false;
        boolean mIsLast = false;
        boolean mHasHeader = false;
        boolean mHasFooter = false;

        CHItemIndexPath(CHIndexPath indexPath) {
            setSection(indexPath.mSection);
            setRow(indexPath.mRow + (mHasHeader ? 1 : 0));
        }

        CHItemIndexPath(int section, int row) {
            setSection(section);
            setRow(row);
        }

        /**
         * 是否Header
         * @return 是否Header
         */
        boolean isHeader() {
            return mIsFirst && mHasHeader;
        }

        /**
         * 是否Footer
         * @return 是否Footer
         */
        boolean isFooter() {
            return mIsLast && mHasFooter;
        }

        @Override
        public void setSection(int section) {
            super.setSection(section);
            mSectionRowsCount = mItemsStatus.get(section + 1);
            mIsLast = mRow == mSectionRowsCount - 1;
            mHasHeader = getHeaderTypeOfSection(section) != RecyclerView.INVALID_TYPE;
            mHasFooter = getFooterTypeOfSection(section) != RecyclerView.INVALID_TYPE;
        }

        @Override
        public void setRow(int row) {
            super.setRow(row);
            mIsFirst = row == 0;
            mIsLast = row == mSectionRowsCount - 1;
        }

        CHIndexPath getItemIndexPath() {
            return new CHIndexPath(mSection, mRow - (mHasHeader ? 1 : 0));
        }

    }
}

