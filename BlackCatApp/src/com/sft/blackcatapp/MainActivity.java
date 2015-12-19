package com.sft.blackcatapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import cn.sft.infinitescrollviewpager.BitMapURLExcepteionListner;
import cn.sft.infinitescrollviewpager.PageChangeListener;

/**
 * 首页
 * 
 * @author Administrator
 * 
 */
@SuppressWarnings("deprecation")
public class MainActivity extends FragmentActivity implements PageChangeListener, BitMapURLExcepteionListner {

	private DrawerLayout mDrawerLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.new_activity_main);

		//设置抽屉DrawerLayout
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

	}

	@Override
	public void onURlError(Exception e) {

	}

	@Override
	public void onPageChanged(int position) {

	}
	
    public void switchContent(final Fragment fragment) {
        mDrawerLayout.closeDrawers();
        FragmentManager fm = getSupportFragmentManager();
        try {
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fm.beginTransaction().replace(R.id.frame_content, fragment).commit();
        } catch (Exception e) {
            fm.beginTransaction().replace(R.id.frame_content, fragment).commitAllowingStateLoss();
        }

/*        mHandler.postDelayed(new Runnable() {
            public void run() {
                getSlidingMenu().showContent();
            }
        }, 50);*/
    }
}
