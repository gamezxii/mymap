package com.example.nitsarut.mymap;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsRequest extends FragmentActivity implements OnMapReadyCallback,View.OnClickListener {

    private GoogleMap mMap;
    Button buttonNormal,buttonSatellite,buttonHybrid,btnNagative;
    GPSTracker gps;
    double latitude,longitude;
    Context mContext;
    double d ,b;
    private static final String TAG = "ShowDetailActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_request);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        buttonNormal = (Button) findViewById(R.id.buttonNormal);
        buttonSatellite = (Button) findViewById(R.id.buttonSatellite);
        buttonHybrid = (Button) findViewById(R.id.buttonHybrid);
        btnNagative = (Button)findViewById(R.id.buttonNavigate);

        buttonNormal.setOnClickListener(this);
        buttonSatellite.setOnClickListener(this);
        buttonHybrid.setOnClickListener(this);
        btnNagative.setOnClickListener(this);
        mContext = this;

        getIncom();
        getlatlong();



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng youhere = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(youhere).title("คุณอยู่ที่"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(youhere));

        LatLng road = new LatLng(d, b);
        mMap.addMarker(new MarkerOptions().position(road).title("ปลายทาง").snippet("จุดหมายสายใยแก้ว"));


        LatLng center = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 12));



    }

    private void getIncom(){
        // Log.d(TAG, "check");
       Bundle bundle = getIntent().getExtras();
        String z = bundle.getString("lat1");
        String y = bundle.getString("long1");
        Log.d("pppppppp",z + ", " + y);
        d = Double.parseDouble(z);
        b = Double.parseDouble(y);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonNavigate:
                final LatLng myHome = new LatLng(d, b);
                final LatLng sydney = new LatLng(latitude,longitude);
                openGoogleMap(sydney, myHome);
                break;
            case R.id.buttonSatellite:
                mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                break;
            case R.id.buttonHybrid:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            default:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
        }
    }

    private void openGoogleMap(LatLng src, LatLng dest) {
        String url = "http://maps.google.com/maps?saddr="+src.latitude+","+src.longitude+"&daddr="+dest.latitude+","+dest.longitude+"&mode=driving";
        Uri gmmIntentUri = Uri.parse(url);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {


                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    gps = new GPSTracker(mContext, MapsRequest.this);

                    if (gps.canGetLocation()) {
                        latitude = gps.getLatitude();
                        longitude = gps.getLongitude();

                    } else {
                        gps.showSettingsAlert();
                    }
                } else {
                    Toast.makeText(mContext,"กรุณาเปิด Permission (Allow) เพื่อใช้ App", Toast.LENGTH_SHORT).show();
                }
                return;

            }
        }
    }

    private void getlatlong() {

        gps = new GPSTracker(mContext, MapsRequest.this);

        if (gps.canGetLocation()) {

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

        }

    }
}
