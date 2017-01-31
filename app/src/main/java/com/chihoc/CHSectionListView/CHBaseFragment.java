package com.chihoc.CHSectionListView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ChiHo on 2016/11/17.
 */
public abstract class CHBaseFragment extends Fragment {

    protected int mViewId;
    protected View mContainerView;
    protected Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity.getBaseContext();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public String getTitle(){
        return String.valueOf(getActivity().getTitle());
    }

    public void setTitle(String title) {
        getActivity().setTitle(title);
    }

    protected void setContainerView(int viewId) {
        mViewId = viewId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mContainerView == null) {
            mContainerView = inflater.inflate(mViewId, null);
            initView(mContainerView);
        }

        ViewGroup parent = (ViewGroup) mContainerView.getParent();
        if (parent != null) {
            parent.removeView(mContainerView);
        }
        return mContainerView;
    }

    protected void initView(View view) {
    }

    /**
     * 判断方法是否被重写
     * @param obj 对象
     * @param name 方法名
     * @return 是否重写
     */
    protected boolean isMethodOverriden(Object obj, String name, Class<?>... parameterTypes)
    {
        try {
            Class<?> clazz = obj.getClass();
            return clazz.getDeclaredMethod(name, parameterTypes) != null;
        }
        catch (SecurityException | NoSuchMethodException ignored) {
        }

        return false;
    }
}
