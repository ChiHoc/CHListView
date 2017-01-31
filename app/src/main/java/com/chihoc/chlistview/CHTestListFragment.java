package com.chihoc.chlistview;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chihoc.CHSectionListView.CHIndexPath;
import com.chihoc.CHSectionListView.CHRefreshBaseListFragment;

import java.util.HashMap;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by ChiHo on 2017/1/30.
 */
public class CHTestListFragment extends CHRefreshBaseListFragment <HashMap<String, Object>, CHTestListFragment.TestViewHolder> {

    public static final String TEST_LIST_ITEM_TITLE = "title";
    public static final String TEST_LIST_ITEM_HIGHLIGHT = "highlight";

    private void addDataSource() {
        mList.add(new HashMap<String, Object>() {{
            put(TEST_LIST_ITEM_TITLE, "不可点 - Unclickable");
            put(TEST_LIST_ITEM_HIGHLIGHT, false);
        }});
        mList.add(new HashMap<String, Object>() {{
            put(TEST_LIST_ITEM_TITLE, "可点 - Clickable");
            put(TEST_LIST_ITEM_HIGHLIGHT, true);
        }});
        mList.add(new HashMap<String, Object>() {{
            put(TEST_LIST_ITEM_TITLE, "显示/隐藏分割线 - Show / Hide separator");
            put(TEST_LIST_ITEM_HIGHLIGHT, true);
        }});
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
    }

    @Override
    protected void onBindViewHolder(TestViewHolder holder, HashMap<String, Object> dataSource, int row) {
        holder.mTitle.setText((String)dataSource.get(TEST_LIST_ITEM_TITLE));
    }

    @Override
    protected CHTestListFragment.TestViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.ch_test_list_item, parent, false);
        return new TestViewHolder(v);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mList.clear();
                addDataSource();
                mAdapter.setIsRefreshing(false);
                mRefreshLayout.endRefreshing();
                reloadData();
            }
        }, 2000);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if(mHasNoMoreData || mList.size() == 0) {
            return false;
        }
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int count = mList.size();
                addDataSource();
                mHasNoMoreData = true;
                insertItems(new CHIndexPath(0, count), mList.size() - count);
                mRefreshLayout.endLoadingMore();
            }
        }, 2000);
        return true;
    }

    @Override
    protected void onItemClickAtRow(int row, final HashMap<String, Object> dataSource) {
        if (row == 2) {
            setShowSeparator(!isShowSeparator());
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            AlertDialog alertDialog = builder.setTitle("点击提示")
                    .setMessage((String) dataSource.get(TEST_LIST_ITEM_TITLE))
                    .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create();
            alertDialog.show();
        }
    }

    @Override
    protected boolean shouldHighlightAtRow(int row, final HashMap<String, Object> dataSource) {
        return (boolean)dataSource.get(TEST_LIST_ITEM_HIGHLIGHT);
    }

    class TestViewHolder extends RecyclerView.ViewHolder {

        TextView mTitle;

        TestViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.list_item_title);
        }
    }
}
