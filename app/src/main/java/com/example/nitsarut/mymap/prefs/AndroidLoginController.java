package com.example.nitsarut.mymap.prefs;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Nitsarut on 1/17/2018.
 */

public class AndroidLoginController extends Application{

    public static final String TAG = AndroidLoginController.class.getSimpleName();
    private RequestQueue mRequestQueue;
    private static AndroidLoginController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized AndroidLoginController getInstance() {return mInstance;}

    public RequestQueue getRequestQueue(){
        if (mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag_string_request){
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void canclePedingRequest(Object tag){
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

}
