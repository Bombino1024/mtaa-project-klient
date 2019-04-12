package com.example.mtaaclient;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class Saver {
    private static final String MY_PREFS_NAME = "MyPref";
    private Context context;
    private static Saver saver;

    private Saver(Context context){
        this.context = context;
    }

    public static Saver getInstance(Context context){
        if(saver == null){
            saver = new Saver(context);
        }
        return saver;
    }

    public void saveToken(String token){
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("token", token);
        editor.apply();
    }

    public String getToken(){
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        return prefs.getString("token", null);
    }
}
