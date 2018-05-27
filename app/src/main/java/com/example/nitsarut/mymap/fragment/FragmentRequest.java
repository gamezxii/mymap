package com.example.nitsarut.mymap.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
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
import com.example.nitsarut.mymap.R;
import com.example.nitsarut.mymap.home_admin;
import com.example.nitsarut.mymap.prefs.Contact;
import com.example.nitsarut.mymap.prefs.ContactAdapter;
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
public class FragmentRequest extends Fragment {

    RecyclerView recyclerView;
    ContactAdapter adapter;
    List<Contact> contactList;
    ProgressDialog progressDialog;
    private UserInfo userInfo;
    private UserSession session;
    private String userString,username,a;
    String status0 = "รอรับเรื่อง";
    Context stmx;

    public FragmentRequest() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_fragment_request, container,false);

        session = new UserSession(getActivity());
        userInfo = new UserInfo(getActivity());
        userString = userInfo.getKeyStatus1();


        recyclerView = (RecyclerView)rootView.findViewById(R.id.mainMenu);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        contactList = new ArrayList<>();

        ssss();



        return rootView;



    }


    private void ssss(){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.list_admin, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray products = new JSONArray(response);

                    for (int i = 0; i < products.length(); i++) {
                        JSONObject productObject = products.getJSONObject(i);

                        int id = productObject.getInt("r_id");
                        String image = productObject.getString("r_image");
                        String status = productObject.getString("r_status");
                        String des = productObject.getString("r_comment");
                        String namesent = productObject.getString("r_username");
                        String current = productObject.getString("r_current");
                        String datetime = productObject.getString("r_datetime");

                        Contact contact = new Contact(id, image , status ,  namesent,des,current,datetime);
                        contactList.add(contact);

                    }

                    adapter = new ContactAdapter(getActivity(), contactList);
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //  hidePDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("r_status",status0);
                params.put("r_name_province",userString);

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
