package com.example.nitsarut.mymap;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.nitsarut.mymap.prefs.MySingleton;
import com.example.nitsarut.mymap.prefs.UserInfo;
import com.example.nitsarut.mymap.prefs.UserSession;
import com.example.nitsarut.mymap.prefs.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapsActivity  extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String adLat,adLong,add,username,des,datetime,adstatus,provine1;
    private TextView btn_sent,locationLL;
    private EditText bug33,bug44;
    private static final String URL = "http://10.0.2.2/android/image.php";
    private Bitmap bitmap;
    Context mContext;
    private ImageView imgView;
    ProgressDialog progressDialog;
    double latitude,longitude;
    LatLng coordinates;
    Geocoder geocoder;
    List<Address> addressList;
    GPSTracker gps;
    EditText description;
    private String userString , provine;
    private UserSession session;
    private UserInfo userInfo;
    private String TAG = MapsActivity.class.getSimpleName();
    String status = "รอรับเรื่อง";
    String mFilePath;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

     //   userInfo = new UserInfo(this);
     //   userSession = new UserSession(this);
        mContext = this;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);


        description = (EditText)findViewById(R.id.txtdescription1);
        locationLL = (TextView) findViewById(R.id.txta);
        Bundle bundle = getIntent().getExtras();
        session = new UserSession(this);
        userInfo = new UserInfo(this);

   //     reCrive();
        imgView = (ImageView)findViewById(R.id.jpg123);

        Intent intent = getIntent();
        mFilePath = intent.getStringExtra("filepath");
        previewMedia();

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String formattedDate = df.format(c.getTime());

        userString = userInfo.getKeyUsername();
        provine = userInfo.getKeyStatus1();
        Log.d("qqwqqq",provine);
      //  description.setText(formattedDate);
        progressDialog = new ProgressDialog(this);


        getlatlong();
        getAddress();


        final TextView bSent = (TextView) findViewById(R.id.txtSent2);

        bSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                username = userString;
                provine1 = provine;
                add = locationLL.getText().toString().trim();
                des = description.getText().toString().trim();
                adLat = String.valueOf(latitude);
                adLong = String.valueOf(longitude);
                datetime = String.format(formattedDate);
                adstatus = status.toString().trim();

              //  String ad1 = latitude
                if (des.length()==0) {
                    description.requestFocus();
                   Toast.makeText(MapsActivity.this,"กรุณากรอกรายละเอียด",Toast.LENGTH_SHORT).show();

                } else {
                    progressDialog.setMessage("Upload...");
                    progressDialog.show();

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.Sent_data, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {



                            try{
                                JSONObject jsonObject = new JSONObject(response);
                                String Response = jsonObject.getString("response");
                                Toast.makeText(MapsActivity.this,Response,Toast.LENGTH_LONG).show();

                                Intent a = new Intent(MapsActivity.this,MainActivity.class);
                                startActivity(a);
                                finish();


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {



                            if (error instanceof TimeoutError){
                                Toast.makeText(getApplicationContext(), "อัพโหลดล้มเหลว" , Toast.LENGTH_LONG).show();
                                Intent y = new Intent(MapsActivity.this,MainActivity.class);
                                startActivity(y);

                            } else if (error instanceof NoConnectionError){
                                Toast.makeText(getApplicationContext(),"กรุณาเชื่อมต่ออินเทอร์เน็ต ", Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.hide();

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            String imageData = imageToString(bitmap);
                            params.put("r_username", username);
                            params.put("r_lat1",  adLat);
                            params.put("r_long1", adLong);
                            params.put("r_current", add);
                            params.put("r_comment", des);
                            params.put("r_datetime", datetime);
                            params.put("r_image", imageData);
                            params.put("r_status", adstatus);
                            params.put("r_name_province", provine1);

                            return params;
                        }
                    };
                    MySingleton.getmInstance(MapsActivity.this).addToRequestQue(stringRequest);

                }
            }
        });



    }

    private void previewMedia() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8; // down sizing image as it throws OutOfMemory Exception for larger images
        mFilePath = mFilePath.replace("file://", ""); // remove to avoid BitmapFactory.decodeFile return null
        File imgFile = new File(mFilePath);
        if (imgFile.exists()) {
            bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), options);
            imgView.setImageBitmap(bitmap);
            imgView.setRotation(90);
        }
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(latitude, longitude);
      //  googleMap.setMapType(com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID);
        mMap.addMarker(new MarkerOptions().position(sydney).title("คุณอยู่ที่นี้"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
       mMap.animateCamera(CameraUpdateFactory.zoomTo(15));


    }

    public String getAddress() {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addressList = geocoder.getFromLocation(latitude, longitude, 1);

            String addressStr = addressList.get(0).getAddressLine(0);
          //  String      addressStr = addressList.get(0).getCountryCode();
         //   String postalcodeStr = addressList.get(0).getPostalCode();


            String fullAddress = addressStr + "";
            locationLL.setText(fullAddress);



            return fullAddress;
            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }





    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes,Base64.DEFAULT);
        return encodedImage;
    }


    public void cancle(View view) {
        final AlertDialog.Builder adb = new AlertDialog.Builder(this);

        adb.setTitle("ยกเลิกการส่ง?");
        adb.setMessage("คุณแน่ใจว่าต้องการยกเลิกการส่ง");
        adb.setNegativeButton("ยกเลิก", null);
        adb.setPositiveButton("ตกลง", new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                // TODO Auto-generated method stub
                Intent a = new Intent(MapsActivity.this,MainActivity.class);
                startActivity(a);
            }
        });
        adb.show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {


                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    gps = new GPSTracker(mContext, MapsActivity.this);

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

        gps = new GPSTracker(mContext, MapsActivity.this);

        if (gps.canGetLocation()) {

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

        }

    }

}
