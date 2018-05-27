package com.example.nitsarut.mymap;

import android.Manifest;
import android.app.Activity;
import android.app.LocalActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.nitsarut.mymap.prefs.UserInfo;
import com.example.nitsarut.mymap.prefs.UserSession;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 0;
    private static final int TAKE_PHOTO_REQUEST_CODE = 1;
    private UserInfo userInfo;
    private UserSession userSession;

    String lati,longi,user;
    TextView result;
    List<Address> addressList;
    GPSTracker gps;
    Context mContext;
    double latitude,longitude;
    Uri mFileUri;
    private final Context mContexta = this;




    @Override    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        userInfo = new UserInfo(this);
        userSession = new UserSession(this);
        result = (TextView) findViewById(R.id.result);
        String username = userInfo.getKeyUsername();
        result.setText(username);
        mContext = this;
        user = userInfo.getKeyStatus1();
        Log.d("useruse" , user);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);


        if (!userSession.isUserLoggedin()){
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            finish();
        }

        // Android 6.0 multiple permissions
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission
                .ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        } else {
            // Android 6.0 multiple permissions

        }


    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }



    public void takeCapture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mFileUri = getOutputMediaFileUri(1);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri);
        startActivityForResult(Intent.createChooser(intent
                , "Take a picture with"), 100);
    }

    private Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {
        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "DCIM/Camera");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == 1) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        } else if (type == 2) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (mFileUri != null) {
                String   mFilePath = mFileUri.toString();
                if (mFilePath != null) {
                    Intent intent = new Intent(mContext, MapsActivity.class);
                    intent.putExtra("filepath", mFilePath);
                    startActivity(intent);
                    finish();
                }
            }
        }

        }


    public void getHistory(View view) {
        Intent a = new Intent(MainActivity.this,history.class);
        startActivity(a);
        finish();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mMenuInflater = getMenuInflater();
        mMenuInflater.inflate(R.menu.my_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            final AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle("ออกจากระบบ");
            adb.setMessage("คุณแน่ใจว่าต้องการออกจากระบบ");
            adb.setNegativeButton("ยกเลิก", null);
            adb.setPositiveButton("ตกลง", new AlertDialog.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {
                    // TODO Auto-generated method stub
                    Toast.makeText(MainActivity.this,"ออกจากระบบแล้ว" ,
                            Toast.LENGTH_LONG).show();
                    userSession.setLoggedin(false);
                    userInfo.clearUserInfo();
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    finish();
                }
            });
            adb.show();
        } return true;
    }
}


