package com.sft.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by pengdonghua on 2016/2/3.
 */
public class ViewPagerAdapter  extends FragmentPagerAdapter {

    private final String[] titles ;

//    public static ItemFragment[] fragments = {new ItemFragment(),new ItemFragment(),new ItemFragment()};

    private Fragment[] frags = null;

    public ViewPagerAdapter(FragmentManager fm,String[] titles ,Fragment[] students) {
        super(fm);
        this.titles = titles;
        this.frags = students;
    }


    @Override
    public Fragment getItem(int position) {
        if(frags[position].isAdded()){
            return frags[position];
        }
//        Bundle args = new Bundle();
//        args.putInt("type", position);
////        args.putString("type", titles[position]);
//        students[position].setArguments(args);
        return frags[position];
    }



    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position % titles.length];
    }
}
