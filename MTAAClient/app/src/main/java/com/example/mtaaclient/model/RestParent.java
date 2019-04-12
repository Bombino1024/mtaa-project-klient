package com.example.mtaaclient.model;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class RestParent extends AsyncTask<String, String, ResponseResult> {

    public static final String SERVER_URL = "http://147.175.163.234:8080";
    protected ResponseDelegate delegate;

    public RestParent(ResponseDelegate delegate) {
        this.delegate = delegate;
    }

    protected HttpURLConnection initConnection(URL endPointURL) throws IOException {
        HttpURLConnection myConnection =
                (HttpURLConnection) endPointURL.openConnection();
        myConnection.setReadTimeout(5000);
        myConnection.setConnectTimeout(5000);
        return myConnection;
    }

    protected void processOutput(HttpURLConnection myConnection, String[] params) throws IOException {
        OutputStream os = null;
        os = myConnection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(os, "UTF-8"));
        writer.write(resolveParams(params));
        writer.flush();
    }

    protected ResponseResult processInput(HttpURLConnection myConnection) throws IOException {
        if (myConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            // Success
            // Further processing
            BufferedReader br = new BufferedReader(new InputStreamReader(myConnection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            return new ResponseResult(response.toString(), HttpURLConnection.HTTP_OK);
        } else {
            return new ResponseResult(myConnection.getResponseCode());
        }
    }

    protected String resolveParams(String[] params) {
        StringBuilder builderResult = new StringBuilder();
        if (params == null || params.length == 1) {
            return "";
        }
        for (int i = 1; i < params.length; i += 2) {
            if (i != 1) {
                builderResult.append("&");
            }
            builderResult.append(params[i]).append("=").append(params[i + 1]);
        }
        return builderResult.toString();
    }//return null;

    @Override
    protected void onPostExecute(ResponseResult result) {
        super.onPostExecute(result);
        if (delegate != null) {
            delegate.processResponse(result);
        }
    }

}
