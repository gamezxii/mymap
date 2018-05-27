package com.example.nitsarut.mymap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.nitsarut.mymap.prefs.AndroidLoginController;
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

public class history extends AppCompatActivity {


    private static final String PRODUCT_URL = "http://10.0.2.2/android/select.php";
    private static final String TAG = history.class.getSimpleName();


    RecyclerView recyclerView;
    ProductAdapter adapter;
    List<Product> productList;
    ProgressDialog progressDialog;
    private UserInfo userInfo;
    private UserSession session;
    private String userString,username,a;
    String dayy =" วัน";



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);


        session = new UserSession(this);
        userInfo = new UserInfo(this);
     //   userString = userInfo.getKeyUsername();
        productList = new ArrayList<>();
        TextView b = (TextView)findViewById(R.id.aaa);
         a = userInfo.getKeyUsername();
        b.setText(a);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressDialog = new ProgressDialog(this);


        loadreport();

    //    if (!session.isUserLoggedin()){
    //        startActivity(new Intent(history.this,LoginActivity.class));
    //        finish();
    //    }


    }
    private  void loadreport(){
        progressDialog.setMessage("กรุณารอสักครู่...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.list_data, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {



                try {
                    JSONArray products = new JSONArray(response);

                    for (int i = 0; i < products.length(); i++) {
                        JSONObject productObject = products.getJSONObject(i);

                        int id = productObject.getInt("r_id");
                        String title = productObject.getString("r_processtime");
                        String shortdesc = productObject.getString("r_datetime");
                        String image = productObject.getString("r_image");
                        String status = productObject.getString("r_status");
                        String namerecrive = productObject.getString("r_recrive");
                        String datesuc = productObject.getString("r_date_suc" );
                        String image_after = productObject.getString("r_image_after");

                        Product product = new Product(id, title + dayy, shortdesc, image, status,namerecrive,datesuc,image_after);
                        productList.add(product);
                        Log.d("asdbss", image);


                    }


                    adapter = new ProductAdapter(history.this, productList);
                    recyclerView.setAdapter(adapter);
                    hidePDialog();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {



                if (error instanceof TimeoutError){
                    Toast.makeText(getApplicationContext(), "ขออภัยไม่สามารถโหดลข้อมูลได้" + error.toString(), Toast.LENGTH_LONG).show();

                } else if (error instanceof NoConnectionError){
                    Toast.makeText(getApplicationContext(),"กรุณาเชื่อมต่ออินเทอร์เน็ต ", Toast.LENGTH_SHORT).show();
                }
                hidePDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("r_username", a);
                Log.d("aaaaaaaaaaaaa",a);

                return params;
            }
        };
        MySingleton.getmInstance(history.this).addToRequestQue(stringRequest);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mMenuInflater = getMenuInflater();
        mMenuInflater.inflate(R.menu.my_menu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_back) {
            Intent a = new Intent(history.this, MainActivity.class);
            startActivity(a);
            finish();
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



}
