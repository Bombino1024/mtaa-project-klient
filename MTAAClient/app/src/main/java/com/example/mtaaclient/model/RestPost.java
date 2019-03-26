package com.example.mtaaclient.model;

import java.io.IOException;
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

            myConnection.setRequestProperty("Content-Type", "application/json");
            myConnection.setRequestProperty("Accept", "application/json, text/plain, */*");

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
