package com.example.nitsarut.mymap.prefs;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Nitsarut on 1/15/2018.
 */

public class MySingleton {
    private static MySingleton mInstance;
    private RequestQueue requestQueue;
    private static Context mCtx;

    private MySingleton(Context context)
    {
        mCtx = context;
        requestQueue = getRequestQueue();
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue==null)
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        return requestQueue;
    }

    public static synchronized MySingleton getmInstance(Context context)
    {
        if (mInstance==null)
        {
            mInstance = new MySingleton(context);
        }
        return mInstance;
    }
    public<T> void addToRequestQue(Request<T> request)
    {
        getRequestQueue().add(request);
    }
}
