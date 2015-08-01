package com.miti.citizenx;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * mrfreitas
 * Date: 07/05/2015
 * Time: 00:30
 */
public class ReactFragment extends Fragment
{
    private ImageView imageView;
    private TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.card_dashboard, container, false);

        assignViews(v);

        return v;
    }

    public static ReactFragment newInstance(String text) {

        ReactFragment f = new ReactFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }

    private void assignViews(View v) {
        imageView = (ImageView)v.findViewById(R.id.imageView);
        textView = (TextView)v.findViewById(R.id.textView);
    }
}