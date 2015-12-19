package com.sft.blackcatapp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.sft.adapter.MenuAdapter;
import com.sft.common.MenuInfo;

import cn.sft.infinitescrollviewpager.BitMapURLExcepteionListner;
import cn.sft.infinitescrollviewpager.PageChangeListener;

/**
 * 首页
 * 
 * @author Administrator
 * 
 */
public class MainActivity extends FragmentActivity implements PageChangeListener, BitMapURLExcepteionListner {

	private DrawerLayout mDrawerLayout;
    private ListView mLvMenu;
    private MenuAdapter mMenuAdapter;
    private View mMenuHeaderView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.new_activity_main);

		//设置抽屉DrawerLayout
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		
        mLvMenu = (ListView) findViewById(R.id.lv_menu);
        mMenuAdapter = new MenuAdapter();
        
        mMenuAdapter.addMenuItem(new MenuInfo(R.drawable.home, "首页"));
        mMenuAdapter.addMenuItem(new MenuInfo(R.drawable.search_coach, "查找教练"));
        mMenuAdapter.addMenuItem(new MenuInfo(R.drawable.messagelist, "消息"));
        mMenuAdapter.addMenuItem(new MenuInfo(R.drawable.signin, "签到"));
        mMenuAdapter.addMenuItem(new MenuInfo(R.drawable.market, "商城"));
        mMenuAdapter.addMenuItem(new MenuInfo(R.drawable.me, "我"));
        
        mMenuHeaderView = LayoutInflater.from(this).inflate(R.layout.menu_top, null);
        mLvMenu.addHeaderView(mMenuHeaderView);
        mLvMenu.setAdapter(mMenuAdapter);
        
        mLvMenu.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                switch (position) {
                    case 0:
                        
                        break;

                    default:
                        break;
                }
            }
        });

	}

	@Override
	public void onURlError(Exception e) {

	}

	@Override
	public void onPageChanged(int position) {

	}
}
