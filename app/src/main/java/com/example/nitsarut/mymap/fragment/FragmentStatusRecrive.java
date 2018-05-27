package com.example.nitsarut.mymap.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.nitsarut.mymap.R;
import com.example.nitsarut.mymap.home_admin;
import com.example.nitsarut.mymap.prefs.Liststatus;
import com.example.nitsarut.mymap.prefs.ListstatusAdapter;
import com.example.nitsarut.mymap.prefs.MySingleton;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentStatusRecrive extends Fragment {

    RecyclerView recyclerView;
    ListstatusAdapter adapter;
    List<Liststatus> Lstatus;
    ProgressDialog progressDialog;
    private UserInfo userInfo;
    private UserSession session;
    private String userString,a;
    private String b = " วัน";

    public FragmentStatusRecrive() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_fragment_status_recrive, container,false);


        session = new UserSession(getActivity());
        userInfo = new UserInfo(getActivity());
        userString = userInfo.getKeyUsername();
        Lstatus = new ArrayList<>();

        recyclerView = (RecyclerView)rootView.findViewById(R.id.mainStatusProcess);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recriveStatus();
        return rootView;




    }

    private  void recriveStatus(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.list_status, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {



                try {
                    JSONArray products = new JSONArray(response);

                    for (int i = 0; i < products.length(); i++) {
                        JSONObject productObject = products.getJSONObject(i);

                        int id = productObject.getInt("r_id");
                        //    String title = productObject.getString("r_processtime");
                        //    String shortdesc = productObject.getString("r_datetime");
                        String image = productObject.getString("r_image");
                        String status = productObject.getString("r_status");
                        String des = productObject.getString("r_processtime");
                        String namesent = productObject.getString("r_username");
                        String latti = productObject.getString("r_lat1");
                        String longti = productObject.getString("r_long1");

                        Liststatus liststatus = new Liststatus(id, image , status , namesent, des + b , latti,longti);
                        Lstatus.add(liststatus);


                    }


                    adapter = new ListstatusAdapter(getActivity(), Lstatus);
                    recyclerView.setAdapter(adapter);
                //    hidePDialog();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {



                if (error instanceof TimeoutError){
                    Toast.makeText(getActivity(), "อัพโหลดล้มเหลว" + error.toString(), Toast.LENGTH_LONG).show();

                } else if (error instanceof NoConnectionError){
                    Toast.makeText(getActivity(),"กรุณาเชื่อมต่ออินเทอร์เน็ต ", Toast.LENGTH_SHORT).show();
                }
              //  hidePDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("r_recrive",userString);
                Log.d("fragment112",userString);

                return params;
            }
        };
        MySingleton.getmInstance(getActivity()).addToRequestQue(stringRequest);
    }

}
