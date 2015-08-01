package com.miti.citizenx;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.miti.citizenx.api.BaseURL;
import com.miti.citizenx.model.UserEntity;
import com.miti.citizenx.tabs.SlidingTabLayout;
import com.miti.citizenx.tabs.ViewPagerAdapter;
import com.squareup.picasso.Picasso;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import java.io.File;


public class User extends ActionBarActivity
{
    public static final String PREFS = "Citizenx preferences";
    CharSequence titles[];
    int nTabs = 5;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    TextView userName;
    ImageView userPhoto;
    UserEntity user;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // Main toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(R.string.user);

        assignViews();

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        titles = getResources().getStringArray(R.array.tabs);
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),titles,nTabs);

        // Assigning ViewPager View and setting the adapter
        pager.setAdapter(adapter);

        //tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width
        tabs.setCustomTabView(R.layout.custom_tab, R.id.tabTitle);

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer()
        {
            @Override
            public int getIndicatorColor(int position)
            {
                return getResources().getColor(R.color.main);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

        // Get user data
        Realm realm = Realm.getInstance(this);
        RealmQuery<UserEntity> query = realm.where(UserEntity.class);
        RealmResults<UserEntity> result = query.findAll();
        user = result.first();
        userName.setText(user.getName());
        //Photo
        File f = new File(getCacheDir(), "userPhoto.jpg");
        if(f.exists())
            userPhoto.setImageBitmap(BitmapFactory.decodeFile(f.getPath()));
        else
            Picasso.with(this).load(BaseURL.PHOTO_URL.toString()+user.getPicture()).into(userPhoto);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId())
        {
            case R.id.logout:
                logout();
                Intent launchLogin = new Intent(User.this, Login.class);
                launchLogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                launchLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                launchLogin.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(launchLogin);
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    private void logout()
    {
        // Delete user photo
        File f = new File(getCacheDir(), "userPhoto.jpg");
        if(f.exists())
            f.delete();

        // Delete persistence data
        Realm realm = Realm.getInstance(this);
        realm.beginTransaction();
        user.getAccount().removeFromRealm();
        user.removeFromRealm();
        realm.commitTransaction();

        // Delete login indication of shared preferences
        SharedPreferences prf = getSharedPreferences(PREFS, 0);
        SharedPreferences.Editor editor = prf.edit();
        editor.putInt("userLogin", 0);
        editor.apply();
    }

    private void assignViews()
    {
        pager = (ViewPager) findViewById(R.id.user_pager);
        tabs = (SlidingTabLayout) findViewById(R.id.user_tabs);
        userName = (TextView) findViewById(R.id.user_name);
        userPhoto = (ImageView) findViewById(R.id.user_photo);

    }
}
