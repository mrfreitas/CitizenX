package com.miti.citizenx;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.miti.citizenx.api.BaseURL;
import com.miti.citizenx.api.UserAPI;
import com.miti.citizenx.model.UserEntity;
import com.miti.citizenx.validator.ValidatorFactory;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import io.realm.Realm;
import org.json.JSONObject;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;


public class Login extends ActionBarActivity
{
    public static final String PREFS = "Citizenx preferences";
    private Button create_account_bt;
    private EditText user_email_et;
    private EditText password_et;
    private Button login_bt;
    private Boolean exit = false;

    private ValidatorFactory validatorFactory;
    private ProgressBar pBar;
    public static final String ENDPOINT = BaseURL.BASE_URL.toString();

    // Custom target for the picasso image loader
    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from)
        {
            try
            {
                File f = new File(getCacheDir(), "userPhoto.jpg");
                if(!f.exists())
                {
                    f.createNewFile();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    byte[] bitmapdata = bos.toByteArray();

                    FileOutputStream fos = new FileOutputStream(f);
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();
                }
            }
            catch (IOException e) {}
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {}

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {}

    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        assignViews();

        isOnline();

        validatorFactory = new ValidatorFactory();
        pBar.setVisibility(View.INVISIBLE);

        // Email Validate
        validatorFactory.emailValidate(this, user_email_et, true);
        //Password Validate
        validatorFactory.passwordValidate(this, password_et, true);

        create_account_bt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent cAccount = new Intent(Login.this, CreateAccount.class);
                startActivityForResult(cAccount, 1);
            }
        });

        login_bt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (isOnline())
                {
                    String email = user_email_et.getText().toString();
                    String pass = password_et.getText().toString();
                    // Create json String
                    JsonObject data = new JsonObject();
                    data.addProperty("email", email);
                    data.addProperty("password", pass);

                    pBar.setVisibility(View.VISIBLE);
                    // Login  request
                    RestAdapter adapter = new RestAdapter.Builder()
                            .setEndpoint(ENDPOINT)
                            .build();
                    UserAPI api = adapter.create(UserAPI.class);
                    api.login(data, new Callback<JsonObject>()
                    {
                        @Override
                        public void success(JsonObject userEntity, Response response)
                        {
                            Realm realm = Realm.getInstance(Login.this);
                            realm.beginTransaction();
                            realm.createObjectFromJson(UserEntity.class, userEntity.toString());
                            realm.commitTransaction();
                            // Get User photo
                            Picasso.with(Login.this).load(BaseURL.PHOTO_URL.toString() + userEntity.get("picture").getAsString()).into(target);
                            // Save the indication of user login in shared preferences
                            SharedPreferences prf = getSharedPreferences(PREFS, 0);
                            SharedPreferences.Editor editor = prf.edit();
                            editor.putInt("userLogin", 1);
                            editor.apply();

                            pBar.setVisibility(View.INVISIBLE);
                            startApp();
                        }

                        @Override
                        public void failure(RetrofitError error)
                        {
                            pBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(Login.this, error.toString(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK)
        {
            if(data.getBooleanExtra("userCreated", false))
                startApp();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, R.string.exit, Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }

    private boolean isOnline()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if(!(netInfo != null && netInfo.isConnectedOrConnecting()))
        {
            Toast.makeText(this, R.string.network_validation, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void startApp()
    {
        Intent launchLogin = new Intent(Login.this, Dashboard.class);
        launchLogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        launchLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        launchLogin.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(launchLogin);
    }

    private void assignViews() {
        user_email_et = (EditText) findViewById(R.id.user_email_et);
        password_et = (EditText) findViewById(R.id.password_et);
        login_bt = (Button) findViewById(R.id.login_bt);
        create_account_bt = (Button) findViewById(R.id.create_account_bt);
        pBar = (ProgressBar) findViewById(R.id.progressBar);
    }
}
