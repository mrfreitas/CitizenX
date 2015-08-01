package com.miti.citizenx;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.*;
import android.widget.*;
import com.google.gson.*;
import com.miti.citizenx.api.BaseURL;
import com.miti.citizenx.api.MyDeserialization;
import com.miti.citizenx.api.UserAPI;
import com.miti.citizenx.model.AccountEntity;
import com.miti.citizenx.model.UserEntity;
import com.miti.citizenx.validator.ValidatorFactory;
import io.realm.Realm;
import io.realm.RealmObject;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class CreateAccount extends ActionBarActivity
{
    private static final String PREFS = "Citizenx preferences";
    private SharedPreferences prf;
    private boolean userAsPhoto;
    private EditText nameCUser;
    private EditText emailCUser;
    private EditText cemailCUser;
    private EditText passwordCUser;
    private EditText cpasswordCUser;
    private Button confirmCUser;
    private ValidatorFactory validatorFactory;
    private ProgressBar pBar;
    private ImageView userImage;
    public static final String ENDPOINT = BaseURL.BASE_URL.toString();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        // Main toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(R.string.create_account);
        toolbar.inflateMenu(R.menu.menu_dashboard);

        prf = getSharedPreferences(PREFS, 0);
        userAsPhoto = prf.getInt("userPhoto",0) == 1;
        assignViews();

        // Check if network is available
        isOnline();

        validatorFactory = new ValidatorFactory();
        pBar.setVisibility(View.INVISIBLE);

        // Name Validate
        validatorFactory.nameValidate(this,nameCUser,true);
        // Email Validate
        validatorFactory.emailValidate(this,emailCUser,true);
        // Confirm Email Validate
        validatorFactory.cEmailValidate(this,cemailCUser,emailCUser,true);
        //Password Validate
        validatorFactory.passwordValidate(this,passwordCUser,true);
        //Confirm password Validate
        validatorFactory.cPasswordValidate(this, cpasswordCUser,passwordCUser,true);

        confirmCUser.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (validatorFactory.formaValidate() && isOnline())
                {
                    // Get system date
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formattedDate = df.format(c.getTime());
                    // Initialize account
                    AccountEntity account = new AccountEntity();
                    account.setStatus("N");
                    account.setType("N");
                    // Initialize user object
                    UserEntity userEntity = new UserEntity();
                    userEntity.setName(nameCUser.getText().toString());
                    userEntity.setEmail(emailCUser.getText().toString());
                    userEntity.setPassword(passwordCUser.getText().toString());
                    userEntity.setUserType("ADMIN");
                    account.setcDate(formattedDate);
                    userEntity.setAccount(account);
                    // Deserialize object to JsonObject
                    MyDeserialization deserialize = new MyDeserialization();
                    String user = deserialize.deserializeToString(userEntity);

                    // Get user photo
                    File userPhoto = new File(getCacheDir(), "userPhoto.jpg");
                    TypedFile photo;
                    if(userPhoto.exists())
                        photo  = new TypedFile("image/jpg", userPhoto);
                    else
                    {
                        String uri = "android.resource:/com.miti.citizenx/drawable/default_user.png";
                        userPhoto = new File(uri);
                        photo = new TypedFile("image/jpg", userPhoto);
                    }
                    pBar.setVisibility(View.VISIBLE);
                    // Create user account request
                    RestAdapter adapter = new RestAdapter.Builder()
                            .setEndpoint(ENDPOINT)
                            .build();
                    UserAPI api = adapter.create(UserAPI.class);
                    api.register(photo, user, new Callback<JsonObject>()
                    {
                        @Override
                        public void success(JsonObject userEntity, Response response)
                        {

                            pBar.setVisibility(View.INVISIBLE);
                            // Save the indication of user login in shared preferences
                            SharedPreferences.Editor editor = prf.edit();
                            editor.putInt("userLogin", 1);
                            editor.apply();

                            // Persist user data
                            Realm realm = Realm.getInstance(CreateAccount.this);
                            realm.beginTransaction();
                            realm.createObjectFromJson(UserEntity.class, userEntity.toString());
                            realm.commitTransaction();

                            // return to login
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("userCreated", true);
                            setResult(RESULT_OK, returnIntent);
                            CreateAccount.this.finish();
                        }

                        @Override
                        public void failure(RetrofitError error)
                        {
                            pBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(CreateAccount.this, "ERROR\n" + error, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        userImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String options[] = new String[] {"From camera", "From gallery"};
                int[] icons = {R.drawable.ic_camera_alt_grey600_24dp, R.drawable.ic_image_grey600_24dp};
                ListAdapter adapter = new MyArrayAdapter(CreateAccount.this, options,icons);

                AlertDialog.Builder builder = new AlertDialog.Builder(CreateAccount.this);
                builder.setTitle("Select Image");
                builder.setAdapter(adapter, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if(which == 0)
                        {
                            Intent i = new Intent("android.media.action.IMAGE_CAPTURE");
                            i.putExtra("crop", "true");
                            i.putExtra("outputX", 200);
                            i.putExtra("outputY", 200);
                            i.putExtra("aspectX", 1);
                            i.putExtra("aspectY", 1);
                            i.putExtra("scale", true);
                            i.putExtra("return-data", true);
                            startActivityForResult(i , 1);
                        }
                        else
                        {
                            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            i.setType("image/*");
                            i.putExtra("crop", "true");
                            i.putExtra("outputX", 200);
                            i.putExtra("outputY", 200);
                            i.putExtra("aspectX", 1);
                            i.putExtra("aspectY", 1);
                            i.putExtra("noFaceDetection", true);
                            i.putExtra("scale", true);
                            i.putExtra("return-data", true);
                            startActivityForResult(i, 1);
                        }
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data != null /*&& data.getData() != null*/)
            if (requestCode == 1)
            {
                Bundle extras = data.getExtras();
                Bitmap p = extras.getParcelable("data");
                Bitmap photo = Bitmap.createScaledBitmap(p, 150, 150, true);

                try
                {
                    File f = new File(getCacheDir(), "userPhoto.jpg");
                    f.createNewFile();

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    byte[] bitmapdata = bos.toByteArray();

                    FileOutputStream fos = new FileOutputStream(f);
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();
                    // Save the indications of photo set in shared preferences
                    SharedPreferences.Editor editor = prf.edit();
                    editor.putInt("userPhoto", 1);
                    editor.apply();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                // photo = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                userImage.setImageBitmap(photo);
            }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        boolean userIsLogIn = (prf.getInt("userLogin",0) == 1);
        File userPhoto = new File(getCacheDir(), "userPhoto.jpg");
        if(userPhoto.exists() && userIsLogIn)
            userPhoto.delete();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_account, menu);
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

    private void assignViews() {
        nameCUser = (EditText) findViewById(R.id.name_cuser_et);
        emailCUser = (EditText) findViewById(R.id.email_cuser_et);
        cemailCUser = (EditText) findViewById(R.id.cemail_cuser_et);
        passwordCUser = (EditText) findViewById(R.id.password_cuser_et);
        cpasswordCUser = (EditText) findViewById(R.id.cpassword_cuser_et);
        confirmCUser = (Button) findViewById(R.id.confirm_cuser_bt);
        pBar = (ProgressBar) findViewById(R.id.progressBar);
        userImage = (ImageView) findViewById(R.id.user_image);
        // Check image
        if(userAsPhoto)
        {
            File userPhoto = new File(getCacheDir(), "userPhoto.jpg");
            Bitmap myBitmap = BitmapFactory.decodeFile(userPhoto.getAbsolutePath());
            userImage.setImageBitmap(myBitmap);
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

}

class MyArrayAdapter extends ArrayAdapter<String>
{
    Context context;
    int[] imgs;
    String[] titles;
    MyArrayAdapter(Context context, String[] titles, int imgs[])
    {
        super(context ,android.R.layout.select_dialog_item, titles);
        this.context = context;
        this.imgs = imgs;
        this.titles = titles;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
      //  LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      //  View row = inflater.inflate(R.layout.t_alert_box_list, parrent, false);
        View row = super.getView(position, convertView, parent);

        TextView text_alert_dialog = (TextView)row.findViewById(android.R.id.text1);
        text_alert_dialog.setCompoundDrawablesWithIntrinsicBounds(imgs[position], 0, 0, 0);
        text_alert_dialog.setCompoundDrawablePadding(
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getContext().getResources().getDisplayMetrics()));

        return row;
    }
}