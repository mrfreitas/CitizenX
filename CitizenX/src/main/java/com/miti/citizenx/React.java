package com.miti.citizenx;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.*;


public class React extends ActionBarActivity
{
    private CardView cardView;
    private TextView userName;
    private TextView date;
    private TextView reactTitle;
    private TextView txtMessage;
    private ImageButton imageButton1;
    private ImageButton imageButton2;
    private ImageButton imageButton3;
    private ImageView userIMG;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_react);

        // Main toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(R.string.create_reaction);
        toolbar.setBackgroundResource(R.color.red_react);
        // Set colour status bar
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.red_react_dark));
        }

        // Set colour status bar
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.red_react_dark));
        }

        assignViews();

        //Creating the instance of PopupMenu
        final PopupMenu popup = new PopupMenu(this, imageButton3);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.menu_popup, popup.getMenu());


        imageButton3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               popup.show();

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                {
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        Toast.makeText(React.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_react, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.react_add:
                Intent intent = new Intent(React.this, CreateReact.class);
                startActivity(intent);

        }
        return super.onOptionsItemSelected(item);

    }


    private void assignViews() {
        cardView = (CardView) findViewById(R.id.card_view);
        userName = (TextView) findViewById(R.id.userName);
        date = (TextView) findViewById(R.id.date);
        reactTitle = (TextView) findViewById(R.id.reactTitle);
        txtMessage = (TextView) findViewById(R.id.txtMessage);
        imageButton1 = (ImageButton) findViewById(R.id.imageButton1);
        imageButton2 = (ImageButton) findViewById(R.id.imageButton2);
        imageButton3 = (ImageButton) findViewById(R.id.imageButton3);
        userIMG = (ImageView) findViewById(R.id.card_UserIMG);
    }
}
