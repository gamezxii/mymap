package com.example.nitsarut.mymap;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Sentimage extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 1;
    ImageView imv;
    Uri filr_uri;
    File file;
    private TextView txt_sent;
    private UserSession session;
    private UserInfo userInfo;
    private String username_recrive, username_sent, id, status = "เสร็จสิ้น",datetime ,encoded_string, image_name;
    Bitmap bitmap;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sentimage);

        session = new UserSession(this);
        userInfo = new UserInfo(this);
        username_recrive = userInfo.getKeyUsername();
        progressDialog = new ProgressDialog(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        Bundle bundle = getIntent().getExtras();
        username_sent = bundle.getString("name");
        //  String y = bundle.getString("id_table");
        int array_int = bundle.getInt("id_table");
        Log.d("getcom1",username_sent + ", " + array_int);

        id = Integer.toString(array_int);


        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         datetime = df.format(c.getTime());

        imv = (ImageView) findViewById(R.id.take_imv);
        txt_sent = (TextView) findViewById(R.id.sent_imv);

        txt_sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.setMessage("Upload...");
                progressDialog.show();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.update_image, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {



                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String Response = jsonObject.getString("response");
                            Toast.makeText(Sentimage.this,Response,Toast.LENGTH_LONG).show();

                            Intent a = new Intent(Sentimage.this,home_admin.class);
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

                        } else if (error instanceof NoConnectionError){
                            Toast.makeText(getApplicationContext(),"กรุณาเชื่อมต่ออินเทอร์เน็ต ", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.hide();

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        String convertImage = imageToString(bitmap);
                        params.put("r_recrive",username_recrive);
                        params.put("r_username",username_sent);
                        params.put("r_id",id);
                        params.put("r_status",status);
                        params.put("r_date_suc",datetime);
                        params.put("r_image_after",convertImage);
                        Log.d("mysentimage" , username_recrive + "," + username_sent + ","+ id + ","+ status + ","
                                + datetime + ",");


                        return params;
                    }
                };
                MySingleton.getmInstance(Sentimage.this).addToRequestQue(stringRequest);
            }
        });


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + timeStamp + ".jpg";
        File f = new File(Environment.getExternalStorageDirectory()
                , "DCIM/Camera/" + imageFileName);
        filr_uri = Uri.fromFile(f);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, filr_uri);
        startActivityForResult(Intent.createChooser(intent
                , "Take a picture with"), REQUEST_CAMERA);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
      //  bitmap = BitmapFactory.decodeFile(filr_uri.getAbsolutePath(), options);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                // Here you have the ImagePath which you can set to you image view
                Log.e("Image Name", filr_uri.getPath());

               bitmap = BitmapFactory.decodeFile(filr_uri.getPath());

                imv.setImageBitmap(bitmap);
                imv.setRotation(90);
            }
        }
    }

    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,30,outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes,Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
