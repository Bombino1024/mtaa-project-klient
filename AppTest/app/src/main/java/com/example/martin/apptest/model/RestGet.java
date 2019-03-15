package com.example.martin.apptest.model;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RestGet extends RestParent {

    public RestGet(ResponseDelegate delegate) {
        super(delegate);
    }

    @Override
    protected ResponseResult doInBackground(String... params) {
        URL endPointURL = null;
        try {
            endPointURL = new URL(SERVER_URL + params[0] + resolveParams(params));

            HttpURLConnection myConnection = initConnection(endPointURL);
            myConnection.setDoInput(true);
            myConnection.setDoOutput(false);
            myConnection.setRequestMethod("GET");

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
        if(params == null || params.length == 1){
            return super.resolveParams(params);
        }
        return "?" + super.resolveParams(params);
    }
}
