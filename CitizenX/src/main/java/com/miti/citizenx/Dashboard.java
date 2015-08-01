package com.miti.citizenx;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;


public class Dashboard extends ActionBarActivity
{
    public static final String PREFS = "Citizenx preferences";

    private LinearLayout reactContent;
    private LinearLayout reactCarousel;
    private LinearLayout organizeContent;
    private LinearLayout organizeCarousel;
    private LinearLayout trailContent;
    private LinearLayout trailCarousel;

    private View.OnClickListener listener;

    /**
     * Define the number of items visible when the carousel is first shown.
     */
    private static final float INITIAL_ITEMS_COUNT = 1.5F;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        first_time_check();
        setContentView(R.layout.activity_dashboard);

        // Main toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.inflateMenu(R.menu.menu_dashboard);

        iniReactToolBars();
        iniOrganizeToolbar();
        iniTrailblazeToolbar();

        assignViews();

        // Compute the width of a carousel item based on the screen width and number of initial items.
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int layoutWidth = (int) (displayMetrics.widthPixels / INITIAL_ITEMS_COUNT);

        listener = new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                int vid = v.getId();
                Object tagObj = v.getTag();
                if (tagObj.toString().equals("React"))
                {
                    Intent reactDetail = new Intent(Dashboard.this, ReactDetail.class);
                    startActivity(reactDetail);
                }
                else if (tagObj.toString().equals("Organize"))
                {
                  //  Intent trail = new Intent(Dashboard.this, Trailblaze.class);
                  //  startActivity(trail);
                }
                else if(tagObj.toString().equals("Trailblaze"))
                {
                    Intent trailDetail = new Intent(Dashboard.this, TrailDetail.class);
                    startActivity(trailDetail);
                }
               // String text = tag.toString()+" "+Integer.toString(vid);
            }
        };

        // Populate the carousel with items
        for (int i = 0; i < 3 ; i++)
        {
            populateCarousel(i);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == R.id.user_profile)
        {
            Intent userProfile = new Intent(Dashboard.this, User.class);
            startActivity(userProfile);
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateCarousel(int carousel)
    {
        View ll;
        for (int i = 0 ; i < 5 ; ++i) {
            // Create LinearLayout and inflate a xml layout
            ll = getLayoutInflater().inflate(R.layout.card_dashboard,null);
            // Attach dynamic id to the view
            ll.setId(i);
            //Attach one click listener to each of the views
            ll.setOnClickListener(listener);

            /// Add image view to the carousel container
            switch (carousel)
            {
                case 0:  // React
                    ll.setTag("React");
                    reactCarousel.addView(ll);
                    break;
                case 1:  //Organize
                    ll.setTag("Organize");
                    organizeCarousel.addView(ll);
                    break;
                case 2: //Trailblaze
                    ll.setTag("Trailblaze");
                    trailCarousel.addView(ll);
                    break;
            }
        }
    }

    private void iniReactToolBars()
    {
        Toolbar reactToolbar = (Toolbar)findViewById(R.id.react_toolbar);
        reactToolbar.setTitle(R.string.react);
        reactToolbar.setBackgroundResource(R.drawable.back_react);
        reactToolbar.inflateMenu(R.menu.menu_react);
        reactToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem)
            {

                if (menuItem.getItemId() == R.id.react_add)
                {
                    Intent cReact = new Intent(Dashboard.this, CreateReact.class);
                    startActivity(cReact);
                }
                return false;
            }
        });

        reactToolbar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent react = new Intent(Dashboard.this, React.class);
                startActivity(react);
            }
        });
    }

    private void iniOrganizeToolbar()
    {
        Toolbar organizeToolbar = (Toolbar)findViewById(R.id.organize_toolbar);
        organizeToolbar.setTitle(R.string.organize);
        organizeToolbar.setBackgroundResource(R.drawable.back_organize);
        organizeToolbar.inflateMenu(R.menu.menu_organize);
        organizeToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                if(item.getItemId() == R.id.search_organize)
                {
                    Intent findOrganize = new Intent(Dashboard.this, FindOrganizedGroup.class);
                    startActivity(findOrganize);
                }
                return false;
            }
        });

        organizeToolbar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent organize = new Intent(Dashboard.this, Organize.class);
                startActivity(organize);
            }
        });
    }
    private void iniTrailblazeToolbar()
    {
        Toolbar trailToolbar = (Toolbar)findViewById(R.id.trail_toolbar);
        trailToolbar.setTitle(R.string.trailblaze);
        trailToolbar.setBackgroundResource(R.drawable.back_trailblaze);
        trailToolbar.inflateMenu(R.menu.menu_trailblaze);


        trailToolbar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent trail = new Intent(Dashboard.this, Trailblaze.class);
                startActivity(trail);
            }
        });
    }

    private void assignViews()
    {
        reactContent = (LinearLayout) findViewById(R.id.reactContent);
        reactCarousel = (LinearLayout) findViewById(R.id.reactCarousel);
        organizeContent = (LinearLayout) findViewById(R.id.organizeContent);
        organizeCarousel = (LinearLayout) findViewById(R.id.organizeCarousel);
        trailContent = (LinearLayout) findViewById(R.id.trailContent);
        trailCarousel = (LinearLayout) findViewById(R.id.trailCarousel);
    }

    private void first_time_check()
    {
        SharedPreferences prf = getSharedPreferences(PREFS, 0);
        boolean userIsLogIn = (prf.getInt("userLogin",0) == 1);
        if(!userIsLogIn)
        {
            Intent launchLogin = new Intent(Dashboard.this, Login.class);
            launchLogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            launchLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            launchLogin.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(launchLogin);
        }
    }

    private boolean isOnline()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnectedOrConnecting());
    }

}
