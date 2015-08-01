package com.miti.citizenx;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * mrfreitas
 * Date: 24/05/2015
 * Time: 00:59
 */
public class UserFollowing extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.activity_user_following,container,false);
    }
}
