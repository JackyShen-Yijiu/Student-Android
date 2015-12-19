package com.sft.adapter;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sft.blackcatapp.R;
import com.sft.common.MenuInfo;

import java.util.LinkedList;
import java.util.List;

public class MenuAdapter extends BaseAdapter {

    private List<MenuInfo> mData = new LinkedList<MenuInfo>();
    
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder viewHolder;
        if(convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, null);
            
            viewHolder.mIcon = (ImageView) convertView.findViewById(R.id.iv_menu_item);
            viewHolder.mTitle = (TextView) convertView.findViewById(R.id.tv_menu_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        
        MenuInfo menu = (MenuInfo) getItem(position);
        viewHolder.mIcon.setImageResource(menu.mResID);
        viewHolder.mTitle.setText(menu.mName);
        return convertView;
    }
    
    public void addMenuItem(int picResId, String name, Class<? extends Fragment> fragmentClass) {
        mData.add(new MenuInfo(picResId, name, fragmentClass));
    }
    
    public void addMenuItem(MenuInfo menuInfo) {
        mData.add(menuInfo);
    }
    
    private class ViewHolder {
        public ImageView mIcon;
        public TextView mTitle;
    }
}
