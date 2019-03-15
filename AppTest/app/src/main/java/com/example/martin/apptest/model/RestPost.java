package com.example.martin.apptest.model;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RestPost extends RestParent{

    public RestPost(ResponseDelegate delegate) {
        super(delegate);
    }

    @Override
    protected ResponseResult doInBackground(String... params) {
        URL endPointURL = null;
        try {
            endPointURL = new URL(SERVER_URL + params[0]);

            HttpURLConnection myConnection = initConnection(endPointURL);
            myConnection.setDoInput(true);
            myConnection.setDoOutput(true);
            myConnection.setRequestMethod("POST");

            processOutput(myConnection, params);

            return processInput(myConnection);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseResult(HttpURLConnection.HTTP_CLIENT_TIMEOUT);
    }
}
