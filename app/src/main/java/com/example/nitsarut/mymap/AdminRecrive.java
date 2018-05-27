package com.example.nitsarut.mymap;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.nitsarut.mymap.prefs.AndroidLoginController;
import com.example.nitsarut.mymap.prefs.UserInfo;
import com.example.nitsarut.mymap.prefs.UserSession;
import com.example.nitsarut.mymap.prefs.Utils;
import com.github.chrisbanes.photoview.PhotoView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AdminRecrive extends AppCompatActivity {
    private TextView namesent,datesent,status,des,current,get_datetime;
    private PhotoView imvafter;
    private static final String TAG = "ShowDetailActivity";
    private String  status1 = "กำลังดำเนินการ" , a,usernames, re_date_process, convert_id;
    private ImageView btnacp,btncancle;
    private UserInfo userInfo;
    private UserSession session;
    private EditText recrive_date;
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_recrive2);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        des = (TextView)findViewById(R.id.desc);
        imvafter = (PhotoView)findViewById(R.id.adm_recrive11111);
        namesent = (TextView)findViewById(R.id.name_s12);
        btnacp = (ImageView) findViewById(R.id.btnacp11);
        current = (TextView)findViewById(R.id.current);
        get_datetime = (TextView)findViewById(R.id.get_datetime);
        status = (TextView)findViewById(R.id.get_status);
        recrive_date = (EditText) findViewById(R.id.txtdescription12);
        btncancle = (ImageView)findViewById(R.id.btnnone);
        session = new UserSession(this);
        userInfo = new UserInfo(this);
        a = userInfo.getKeyUsername();

        progressDialog = new ProgressDialog(this);


        getIncom();

        btnacp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRecrive();
            }
        });
        btncancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder adb = new AlertDialog.Builder(AdminRecrive.this);

                adb.setTitle("ยกเลิกการส่ง?");
                adb.setMessage("คุณแน่ใจว่าต้องการยกเลิกการส่ง");
                adb.setNegativeButton("ยกเลิก", null);
                adb.setPositiveButton("ตกลง", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        // TODO Auto-generated method stub
                        Intent a = new Intent(AdminRecrive.this,home_admin.class);
                        startActivity(a);
                        finish();
                    }
                });
                adb.show();

            }
        });
    }


    private void getIncom(){
        Log.d(TAG, "check");
        if (getIntent().hasExtra("imagerecrive")) {
            Log.d(TAG,"found intent");
            Bundle bundle = getIntent().getExtras();
            String image1 = getIntent().getStringExtra("imagerecrive");
            String des1 = getIntent().getStringExtra("des");
            String namerec = getIntent().getStringExtra("name");
         //   String datesen = getIntent().getStringExtra("datetime");
         //   String status1 = getIntent().getStringExtra("status0");
            String cur = getIntent().getStringExtra("current");
            String g_dt = getIntent().getStringExtra("datetimes");
            String s_t = getIntent().getStringExtra("status0");
            int id = bundle.getInt("id2");

            convert_id = Integer.toString(id);
            Log.d("convertzas",convert_id);

            namesent.setText(namerec);
            status.setText(s_t);
          //  datesent.setText(datesen);
          //  status.setText(status1);
            get_datetime.setText(g_dt);
            des.setText(des1);
            current.setText(cur);
            setImvafter(image1);
        }
    }

    private void setImvafter(String image){

        Glide.with(this)
                .load(image)
                .into(imvafter);
        imvafter.setRotation(90);
    }

    private void updateRecrive() {

        String tag_string_req = "req_singup";
        progressDialog.setMessage("Upload...");
        progressDialog.show();
        usernames = namesent.getText().toString().trim();
        re_date_process = recrive_date.getText().toString().trim();


        StringRequest strReq = new StringRequest(com.android.volley.Request.Method.POST, Utils.update,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG,"Register Response:" + response.toString());
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String Response = jsonObject.getString("response");
                            Toast.makeText(AdminRecrive.this,Response,Toast.LENGTH_LONG).show();

                            Intent a = new Intent(AdminRecrive.this,home_admin.class);
                            startActivity(a);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error" + error.getMessage());

            }
        })  {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("r_status",status1);
                params.put("r_username", usernames);
                params.put("r_recrive" ,a);
                params.put("r_processtime",re_date_process);
                params.put("r_id",convert_id);
                Log.d("yyyyyyyyyyy",convert_id + "," + status1 + "," + usernames + "," + a );
                return params;
            }
        };
        AndroidLoginController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void toast(String x) {
        Toast.makeText(this,x,Toast.LENGTH_SHORT).show();
    }


}
