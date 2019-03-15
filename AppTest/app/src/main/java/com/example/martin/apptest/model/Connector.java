package com.example.martin.apptest.model;

import android.util.Log;

import com.example.martin.apptest.RegistrationActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;


public class Connector {

    public static final String SERVER_URL = "http://147.175.163.131:8080";
    RegistrationActivity registrationActivity;

    public Connector(RegistrationActivity registrationActivity) {
        this.registrationActivity = registrationActivity;
    }

    public ResponseResult execute(String endpoint, String... params) {
        // Create URL
        URL endPointURL = null;
        try {
            endPointURL = new URL(SERVER_URL + endpoint + resolveParams(params));
            // Create connection
            HttpURLConnection myConnection =
                    (HttpURLConnection) endPointURL.openConnection();
            myConnection.setDoInput(true);
            myConnection.setDoOutput(false);
            myConnection.setReadTimeout(5000);
            myConnection.setConnectTimeout(5000);
            myConnection.setRequestMethod("GET");
            StringBuilder response;
            String line;
            if (myConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Success
                // Further processing here
                BufferedReader br = new BufferedReader(new InputStreamReader(myConnection.getInputStream()));
                response = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                return new ResponseResult(response.toString(), HttpURLConnection.HTTP_OK);
            } else  {
                return new ResponseResult(myConnection.getResponseCode());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseResult(HttpURLConnection.HTTP_CLIENT_TIMEOUT);
    }

    private String resolveParams(String[] params) {
        StringBuilder builderResult = new StringBuilder();
        if (params == null || params.length == 0) {
            return "";
        }
        builderResult.append("?");
        for (int i = 0; i < params.length; i += 2) {
            if (i != 0) {
                builderResult.append("&");
            }
            builderResult.append(params[i]).append("=").append(params[i + 1]);
        }
        return builderResult.toString();
    }
}
