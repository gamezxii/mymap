package com.example.nitsarut.mymap;

import android.app.LauncherActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nitsarut.mymap.prefs.ViewPagerAdapter;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShowDetailActivity extends AppCompatActivity {

    private ImageView photoView,imvafter;
    TextView name;
    private static final String TAG = "ShowDetailActivity";
    ViewPager viewPager;
    LinearLayout linearLayout;
    private int dotscount;
    private ImageView[] dots;
    ScrollView scrollView;
    TextView namere,datesent,status,datesucuss;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        scrollView = (ScrollView)findViewById(R.id.scrollView12);
        namere = (TextView)findViewById(R.id.recrive1);
        imvafter = (PhotoView)findViewById(R.id.photo_view_after);
        datesent = (TextView)findViewById(R.id.datesent1);
        status = (TextView)findViewById(R.id.statussuc1);
        datesucuss = (TextView)findViewById(R.id.datesuc1);

        getIncom();
    }


    private void getIncom(){
        Log.d(TAG, "check");
        if (getIntent().hasExtra("image")) {
            Log.d(TAG,"found intent");
            String image = getIntent().getStringExtra("image");
            String image1 = getIntent().getStringExtra("image_after");
            String namerec = getIntent().getStringExtra("name");
            String datesen = getIntent().getStringExtra("datetime");
            String status1 = getIntent().getStringExtra("status0");
            String datesuccess = getIntent().getStringExtra("datesuc");
            namere.setText(namerec);
            datesent.setText(datesen);
            status.setText(status1);
            datesucuss.setText(datesuccess);
            setImv(image);
            setImvafter(image1);
        }
    }

    private void setImvafter(String image){

        Glide.with(this)
                .load(image)
                .into(imvafter);
        imvafter.setRotation(90);
    }

    private void setImv(String image){
        photoView = (PhotoView) findViewById(R.id.photo_view);


        Glide.with(this)
                .load(image)
                .into(photoView);
        photoView.setRotation(90);
    }


}
