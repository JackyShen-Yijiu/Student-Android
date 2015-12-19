package com.sft.common;

import android.app.Activity;

public class MenuInfo {
    public int mResID;
    public String mName;
    public Class<? extends Activity> mActivityClass;
    
    public MenuInfo(int picResId, String name, Class<? extends Activity> activityClass) {
        mResID = picResId;
        mName = name;
        mActivityClass = activityClass;
    }
}
