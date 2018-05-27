package com.example.nitsarut.mymap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = LoginActivity.class.getSimpleName();
    EditText username, password;
    ProgressDialog progressDialog;
    private Button login, register1;
    private UserSession session;
    private UserInfo userInfo;
    private TextView repass;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    private String userSatus;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);


        username = (EditText) findViewById(R.id.etUsername);
        password = (EditText) findViewById(R.id.etPassword);
        login = (Button) findViewById(R.id.btnLogin);
        register1 = (Button) findViewById(R.id.lRegister);
        repass = (TextView)findViewById(R.id.forgetpass);
        progressDialog = new ProgressDialog(this);
        session = new UserSession(this);
        userInfo = new UserInfo(this);
        userSatus = userInfo.getKeyStatus();


        if (session.isUserLoggedin()){

            if (userSatus.equals("admin")){
                startActivity(new Intent(this, home_admin.class));
                finish();
            } else if (userSatus.equals("user")){
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        }

        login.setOnClickListener(this);
        register1.setOnClickListener(this);
        repass.setOnClickListener(this);



    }
    private void toast(String x) {
        Toast.makeText(this,x,Toast.LENGTH_SHORT).show();
    }

    private void login(final String username, final String password) {
        //Tag used tocancel the request
        String tag_string_request = "req_login";
        progressDialog.setMessage("Logging in...");
        progressDialog.show();

        StringRequest strReq = new StringRequest(com.android.volley.Request.Method.POST, Utils.Login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    Log.d(TAG,"Login Response" + response.toString());

                    try {
                        JSONObject jObj = new JSONObject(response);
                        boolean error = jObj.getBoolean("error");

                        if (!error){
                            JSONObject user = jObj.getJSONObject("user");
                          //  JSONObject status = jObj.getJSONObject("status");
                            String uStatus = user.getString("status");
                            String uName = user.getString("username");
                            String uProvine = user.getString("status1");

                            userInfo.setUsername(uName);
                            userInfo.setStatus(uStatus);
                            userInfo.setStatus1(uProvine);
                            session.setLoggedin(true);
                            Log.d("alykkkkkk",uProvine);

                            Log.d("tagString","," + user  );

                            if (user.getString("status").equals("admin")) {
                                startActivity(new Intent(LoginActivity.this,home_admin.class));
                                Toast.makeText(LoginActivity.this,"ยินดีตอนรับคุณ" + uName,Toast.LENGTH_SHORT).show();

                            } else if (user.getString("status").equals("user"))   {
                                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                Toast.makeText(LoginActivity.this,"ยินดีตอนรับคุณ" + uName,Toast.LENGTH_SHORT).show();
                            }
                            finish();

                        } else {
                            //Error in login
                            String errorMsg = jObj.getString("error_msg");
                            toast(errorMsg);
                            progressDialog.hide();


                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        toast("Json error" + e.getMessage());
                    }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                if (error instanceof TimeoutError){

                    Toast.makeText(getApplicationContext(), "error_msg " + error.toString(), Toast.LENGTH_LONG).show();
                } else if (error instanceof NoConnectionError){
                    Toast.makeText(getApplicationContext(),"กรุณาเชื่อมต่ออินเทอร์เน็ต ", Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError){
                    Toast.makeText(getApplicationContext(),"Authentication Failure Error ", Toast.LENGTH_SHORT).show();
                } else if (error instanceof com.android.volley.NetworkError){
                    Toast.makeText(getApplicationContext(),"Network Error ", Toast.LENGTH_SHORT).show();
                } else if (error instanceof com.android.volley.ServerError){
                    Toast.makeText(getApplicationContext(),"Server Error ", Toast.LENGTH_SHORT).show();
                } else if (error instanceof com.android.volley.ParseError){
                    Toast.makeText(getApplicationContext(),"JSON Parse Error ", Toast.LENGTH_SHORT).show();
                }
                progressDialog.hide();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Posting parameters to long url
                Map<String,String> params = new HashMap<>();
                params.put("u_username",username);
                params.put("u_password",password);
                return params;
            }
        };

        //adding request to sever
        AndroidLoginController.getInstance().addToRequestQueue(strReq, tag_string_request);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:
                String uName = username.getText().toString().trim();
                String pass = password.getText().toString().trim();

               // username.setText("");
                password.setText("");

                login(uName,pass);
                break;
            case R.id.lRegister:
                startActivity(new Intent(this,RegisterActivity.class));
                break;

            case R.id.forgetpass:
               Intent a = new Intent(LoginActivity.this,repass.class);
                startActivity(a);
                finish();
        }
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


