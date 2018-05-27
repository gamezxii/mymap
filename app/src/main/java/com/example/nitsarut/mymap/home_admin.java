package com.example.nitsarut.mymap;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.nitsarut.mymap.fragment.FragmentRequest;
import com.example.nitsarut.mymap.fragment.FragmentStatusRecrive;
import com.example.nitsarut.mymap.fragment.ViewPageAdapter;
import com.example.nitsarut.mymap.prefs.Contact;
import com.example.nitsarut.mymap.prefs.ContactAdapter;
import com.example.nitsarut.mymap.prefs.Liststatus;
import com.example.nitsarut.mymap.prefs.ListstatusAdapter;
import com.example.nitsarut.mymap.prefs.MySingleton;
import com.example.nitsarut.mymap.prefs.Product;
import com.example.nitsarut.mymap.prefs.ProductAdapter;
import com.example.nitsarut.mymap.prefs.UserInfo;
import com.example.nitsarut.mymap.prefs.UserSession;
import com.example.nitsarut.mymap.prefs.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class home_admin extends AppCompatActivity{

    android.support.v7.widget.Toolbar toolbar;
    RecyclerView recyclerView;
    ListstatusAdapter adapter1;
    List<Liststatus> Lstatus;
    ProgressDialog progressDialog;
    private UserInfo userInfo;
    private UserSession session;
    private String userString,username,a;

    TabLayout tableLayout;
    ViewPager viewPager;
    ViewPageAdapter viewPagerAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        session = new UserSession(this);
        userInfo = new UserInfo(this);
        userString = userInfo.getKeyUsername();
        Lstatus = new ArrayList<>();

        tableLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        viewPagerAdapter = new ViewPageAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new FragmentRequest(), "รอรับเรื่อง");
        viewPagerAdapter.addFragments(new FragmentStatusRecrive(), "กำลังดำเนินการ");
        viewPager.setAdapter(viewPagerAdapter);

        tableLayout.setupWithViewPager(viewPager);


        if (!session.isUserLoggedin()) {
            startActivity(new Intent(home_admin.this, LoginActivity.class));
            finish();
        }

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission
                .ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

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



    @Override
    protected void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
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
                    Toast.makeText(home_admin.this,"ออกจากระบบแล้ว" ,
                            Toast.LENGTH_LONG).show();
                    session.setLoggedin(false);
                    userInfo.clearUserInfo();
                    startActivity(new Intent(home_admin.this,LoginActivity.class));
                    finish();
                }
            });
            adb.show();
        } return true;
    }

}
