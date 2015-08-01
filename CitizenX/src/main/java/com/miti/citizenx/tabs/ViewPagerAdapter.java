package com.miti.citizenx.tabs;

/**
 * mrfreitas
 * Date: 24/05/2015
 * Time: 01:04
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.miti.citizenx.UserFollowing;
import com.miti.citizenx.UserInvites;

public class ViewPagerAdapter extends FragmentStatePagerAdapter
{
    CharSequence Titles[]; // This will Store the Titles of the Tabs
    int NumbOfTabs; // Store the number of tabs

    public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position)
    {
        if(position == 0)
        {
            return new UserFollowing();
        }
        else
        {
            return new UserInvites();
        }
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return Titles[position];
    }

    @Override
    public int getCount()
    {
        return NumbOfTabs;
    }
}

