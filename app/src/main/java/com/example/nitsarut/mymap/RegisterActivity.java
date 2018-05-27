package com.example.nitsarut.mymap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.nitsarut.mymap.prefs.AndroidLoginController;
import com.example.nitsarut.mymap.prefs.UserInfo;
import com.example.nitsarut.mymap.prefs.UserSession;
import com.example.nitsarut.mymap.prefs.Utils;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private OkHttpClient client = new OkHttpClient();
    private EditText username, password,rname,sname,telephone , provice;
    private Button singup;
    private ProgressDialog progressDialog;
    private UserSession session;
    private UserInfo userInfo;
    private String TAG = RegisterActivity.class.getSimpleName();
    String status = "user";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText) findViewById(R.id.etUsername);
        password = (EditText) findViewById(R.id.etPassword);
        rname = (EditText)findViewById(R.id.etName);
        sname = (EditText)findViewById(R.id.etSurName);
        telephone = (EditText)findViewById(R.id.etTel);
        provice = (EditText)findViewById(R.id.etProvine);
        singup = (Button) findViewById(R.id.btnRegister);
        progressDialog = new ProgressDialog(this);
        session = new UserSession(this);
        userInfo = new UserInfo(this);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);


        singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uName = username.getText().toString().trim();
                String pass = password.getText().toString().trim();
                String name = rname.getText().toString().trim();
                String surname = sname.getText().toString().trim();
                String tel = telephone.getText().toString().trim();
                String prov = provice.getText().toString().trim();

                if (uName.length() < 6 || username.length() > 10) {
                    username.setText("");
                    username.requestFocus();
                   // username.setError("กรุณากรอกชื้อผู้ใช้ 6-12 ตัวอักษร");
                    Toast.makeText(RegisterActivity.this, "กรุณากรอกชื้อผู้ใช้ 6-12 ตัวอักษร", Toast.LENGTH_SHORT).show();
                }else if(!uName.matches("[a-zA-Z-0-9 ]+")){
                    username.setText("");
                    username.requestFocus();
                  //  username.setError("กรุณากรอกภาษาอังกฤษ");
                    Toast.makeText(RegisterActivity.this, "กรุณากรอกชื้อผู้ใช้ เป็นภาษาอังกฤษ", Toast.LENGTH_SHORT).show();
                }  else if (pass.length() < 6 || password.length() > 16) {
                    password.setText("");
                    password.requestFocus();
                    Toast.makeText(RegisterActivity.this, "กรุณากรอกรหัสผ่าน 6-12 ตัวอักษร", Toast.LENGTH_SHORT).show();
                } else if(!pass.matches("[a-zA-Z-0-9]+")){
                    password.setText("");
                    password.requestFocus();
                    Toast.makeText(RegisterActivity.this, "กรุณากรอกรหัสผ่านเป็นภาษาอังกฤษหรือตัวเลข", Toast.LENGTH_SHORT).show();
                }else if (tel.length() < 9 || tel.length() > 10) {
                    telephone.setText("");
                    telephone.requestFocus();
                    Toast.makeText(RegisterActivity.this, "กรุณากรอกเบอร์โทรให้ถูกต้อง", Toast.LENGTH_SHORT).show();
                } else if (name.isEmpty()) {
                    rname.requestFocus();
                    Toast.makeText(RegisterActivity.this, "กรุณากรอกข้อมูลให้ครบ", Toast.LENGTH_SHORT).show();
                } else if (surname.isEmpty()) {
                    sname.requestFocus();
                    Toast.makeText(RegisterActivity.this, "กรุณากรอกข้อมูลให้ครบ", Toast.LENGTH_SHORT).show();
                }
                else if (prov.isEmpty()) {
                    provice.requestFocus();
                    Toast.makeText(RegisterActivity.this, "กรุณาเลือกจังหวัด", Toast.LENGTH_SHORT).show();
                }
                else{

                    singup(uName, pass, name, surname, tel , prov);
                    password.setText("");
                    telephone.setText("");
                }
            }
        });

    }

    private void toast(String x) {
        Toast.makeText(this,x,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private void singup(final String username, final String password , final String rname, final String sname
            , final String telephone , final String provice)  {
        //Tag used to cancel the request
        String tag_string_req = "req_singup";
        progressDialog.setMessage("Signing up...");
        progressDialog.show();

        StringRequest strReq = new StringRequest(com.android.volley.Request.Method.POST, Utils.Register_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG,"Register Response:" + response.toString());
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            boolean error = jsonObject.getBoolean("error");

                            if (!error){
                                JSONObject user = jsonObject.getJSONObject("user");
                                String uName = user.getString("username");

                                userInfo.setUsername(uName);
                            //    session.setLoggedin(true);

                                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                                finish();
                                String errorMsg = jsonObject.getString("error_msg");
                                toast(errorMsg);
                            } else {
                                String errorMsg = jsonObject.getString("error_msg");
                                toast(errorMsg);
                                progressDialog.hide();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            toast("Json error : " + e.getMessage());
                            progressDialog.hide();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError){
                    Toast.makeText(getApplicationContext(), "สมัครสมาชิกล้มเหลว" , Toast.LENGTH_LONG).show();
                    Intent y = new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(y);

                } else if (error instanceof NoConnectionError){
                    Toast.makeText(getApplicationContext(),"กรุณาเชื่อมต่ออินเทอร์เน็ต ", Toast.LENGTH_SHORT).show();
                }

               // Log.e(TAG, "Login Error" + error.getMessage());
                progressDialog.hide();
            }
        })  {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("u_username", username);
                params.put("u_password", password);
                params.put("u_name", rname);
                params.put("u_surname", sname);
                params.put("u_tel", telephone);
                params.put("u_status", status);
                params.put("u_status1", provice);
                return params;
            }
        };
        AndroidLoginController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }


    public static boolean isValiduser(String user) {

        String expression = "/[^A-Za-z0-9.#\\-$]/";
        CharSequence inputString = user;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputString);
        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

}
