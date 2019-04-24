package com.example.mtaaclient;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class URLSaver {
    private static final String MY_PREFS_NAME = "MyPref";
    private Context context;
    private static URLSaver urlSaver;

    private URLSaver (Context context){
        this.context = context;
    }

    public static URLSaver getInstance(Context context){
        if(urlSaver == null){
            urlSaver = new URLSaver(context);
        }
        return urlSaver;
    }

    public void saveImageURL(String ImageURL){
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("ImageURL", ImageURL);
        editor.apply();
    }

    public String getImageURL(){
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        return prefs.getString("ImageURL", "");
    }

    public void removeImageURL() {
        SharedPreferences settings = context.getSharedPreferences("PreferencesName", Context.MODE_PRIVATE);
        settings.edit().clear().apply();
    }
}
