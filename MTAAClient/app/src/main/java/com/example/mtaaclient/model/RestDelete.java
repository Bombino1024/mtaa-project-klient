package com.example.mtaaclient.model;

import android.content.Context;

import com.example.mtaaclient.Saver;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RestDelete extends RestParent {
    private Context context;

    public RestDelete(ResponseDelegate delegate) {
        super(delegate);
    }

    public RestDelete(ResponseDelegate delegate, Context context){
        super(delegate);
        this.context = context;
    }

    @Override
    protected ResponseResult doInBackground(String... params) {
        URL endPointURL = null;
        try {
            endPointURL = new URL(SERVER_URL + params[0]);

            HttpURLConnection myConnection = initConnection(endPointURL);
            myConnection.setDoInput(true);
            myConnection.setDoOutput(true);
            myConnection.setRequestMethod("DELETE");

            myConnection.setRequestProperty("Content-Type", "application/json");
            myConnection.setRequestProperty("Accept", "application/json, text/plain, */*");

            if(context != null){
                myConnection.setRequestProperty("Authorization", Saver.getInstance(context).getToken());

            }

//            if(Saver.getInstance(this).getToken() )
//            myConnection.setRequestProperty("Authorization", "7b84e15c-acd8-47dc-b4ee-659eb929");

//            myConnection.setRequestProperty("Authorization", Saver.getInstance(this).getToken());

            processOutput(myConnection, params);

            return processInput(myConnection);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseResult(HttpURLConnection.HTTP_CLIENT_TIMEOUT);
    }

    @Override
    protected String resolveParams(String[] params) {
        return params[1];
    }

}
