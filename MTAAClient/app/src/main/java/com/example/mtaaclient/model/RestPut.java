package com.example.mtaaclient.model;

import android.content.Context;

import com.example.mtaaclient.Saver;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RestPut extends RestParent {
    private Context context = null;

    public RestPut(ResponseDelegate delegate) {
        super(delegate);
    }

    public RestPut(ResponseDelegate delegate, Context context){
        super(delegate);
        this.context = context;
    }

    @Override
    protected ResponseResult doInBackground(String... params) {
        URL endPointURL = null;
        try {
            endPointURL = new URL(SERVER_URL + params[0] + resolveParams(params));

            HttpURLConnection myConnection = initConnection(endPointURL);
            myConnection.setDoInput(true);
            myConnection.setDoOutput(true);
            myConnection.setRequestMethod("PUT");

            myConnection.setRequestProperty("Content-Type", "application/json");
            myConnection.setRequestProperty("Accept", "application/json, text/plain, */*");
//            if(context != null){
//                myConnection.setRequestProperty("Authorization", Saver.getInstance(context).getToken());
//
//            }

            processOutput(myConnection, params);

            return processInput(myConnection);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseResult(HttpURLConnection.HTTP_CLIENT_TIMEOUT);
    }

//    @Override
//    protected String resolveParams(String[] params) {
//        return params[1];
//    }

    @Override
    protected String resolveParams(String[] params) {
        if(params == null || params.length == 1){
            return super.resolveParams(params);
        }
        return "?" + super.resolveParams(params);
    }
}
