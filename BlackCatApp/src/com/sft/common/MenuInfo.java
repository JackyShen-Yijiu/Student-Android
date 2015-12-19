package com.sft.common;

import android.support.v4.app.Fragment;

public class MenuInfo {
    public int mResID;
    public String mName;
    public Class<? extends Fragment> mFragmentClass;
    
    public MenuInfo(int picResId, String name, Class<? extends Fragment> fragmentClass) {
        mResID = picResId;
        mName = name;
        mFragmentClass = fragmentClass;
    }
}
