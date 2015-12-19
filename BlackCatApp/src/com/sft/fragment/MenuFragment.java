package com.sft.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.sft.adapter.MenuAdapter;
import com.sft.blackcatapp.OldMainActivity;
import com.sft.blackcatapp.R;
import com.sft.common.MenuInfo;

public class MenuFragment extends Fragment {
    
    private ListView mLvMenu;
    private MenuAdapter mMenuAdapter;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
    ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.menu_fragment, container, false);
        
        mLvMenu = (ListView) view.findViewById(R.id.lv_menu);
        mMenuAdapter = new MenuAdapter();
        
        mMenuAdapter.addMenuItem(new MenuInfo(R.drawable.app_logo, "首页"));
        mMenuAdapter.addMenuItem(new MenuInfo(R.drawable.app_logo, "查找驾校"));
        
        mLvMenu.setAdapter(mMenuAdapter);
        
        mLvMenu.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                if (position == 0) {
                    
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }
    
/*    public void openFragment(Class<? extends Fragment> fragmentClass, Bundle arguments) {
        Fragment fragment = Fragment.instantiate(getActivity(), fragmentClass.getName(), arguments);
        ((MainActivity) getActivity()).switchContent(fragment);
    }*/
}
