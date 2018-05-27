package com.example.nitsarut.mymap.prefs;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Nitsarut on 1/17/2018.
 */

public class UserInfo {
    private static final String TAG = UserSession.class.getSimpleName();
    private static final String PREF_NAME = "login";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_STATUS = "status";
    private static final String KEY_STATUS1 = "status1";

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context ctx;

    public UserInfo(Context ctx){
        this.ctx = ctx;
        prefs = ctx.getSharedPreferences(PREF_NAME,ctx.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void setUsername(String username){
        editor.putString(KEY_USERNAME,username);
        editor.commit();
    }

    public void setStatus(String status){
        editor.putString(KEY_STATUS, status);
        editor.commit();
    }

    public void setStatus1(String status1){
        editor.putString(KEY_STATUS1, status1);
        editor.commit();
    }

    public void clearUserInfo() {
        editor.clear();
        editor.commit();
    }

    public String getKeyUsername() {return  prefs.getString(KEY_USERNAME,"");}

    public String getKeyStatus() {return prefs.getString(KEY_STATUS,"");}

    public String getKeyStatus1() {return prefs.getString(KEY_STATUS1,"");}



}
