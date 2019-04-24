package com.example.mtaaclient;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mtaaclient.model.ResponseDelegate;
import com.example.mtaaclient.model.ResponseResult;
import com.example.mtaaclient.model.RestGet;
import com.example.mtaaclient.model.RestParent;

import java.net.URL;

public class LoginActivity extends Activity {

    private static final int PERMISSION_ALLOWANCE_CODE = 15;
    private EditText mLoginUsername;
    private EditText mLoginPassword;
    private Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        askForPermisions();
        initView();
    }

    private void askForPermisions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_ALLOWANCE_CODE);
    }

    private void initView() {
        mLoginUsername = (EditText) findViewById(R.id.loginUsername);
        mLoginPassword = (EditText) findViewById(R.id.loginPassword);
        mLoginButton = (Button) findViewById(R.id.loginButton);
    }

    public void buttonClicked(View view) {
        String username = mLoginUsername.getText().toString();
        String password = mLoginPassword.getText().toString();
        sendDataToDatabase(username, password);
    }

    private void sendDataToDatabase(String username, String password) {
        RestParent restGet = new RestGet(new ResponseDelegate() {
            @Override
            public void processResponse(ResponseResult result) {
                processRegistrationResult(result);
            }
        });
        restGet.execute("/login", "email", username, "password", password);
    }

    private void processRegistrationResult(ResponseResult result) {
        if (result.getCode() == 200/*409*/) {
            Toast.makeText(this, "Login succesfull ", Toast.LENGTH_SHORT).show();
            Saver.getInstance(this).saveToken(result.getBody());
            saveURL();
            openHomeScreen();
        } else {
            Toast.makeText(this, "Incorrect username or password " + result.getCode(), Toast.LENGTH_SHORT).show();
        }
    }

    private void saveURL() {
        RestGet restGet = new RestGet(new ResponseDelegate() {
            @Override
            public void processResponse(ResponseResult result) {
                processUrlResult(result);
            }
        });
        restGet.execute("/get_url", "token", Saver.getInstance(this).getToken());
    }

    public void processUrlResult(ResponseResult result){
        if(result.getCode() == 200){
            URLSaver.getInstance(this).saveImageURL(result.getBody());
            Toast.makeText(this, "URL saved " + result.getCode(), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "URL not saved " + result.getCode(), Toast.LENGTH_SHORT).show();
        }
    }

    private void openHomeScreen() {
//        Saver.getInstance(this).saveToken("HelloFriend!");
//        String token = Saver.getInstance(this).getToken();

        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
        this.finish();
    }

    public void openRegister(View view) {
        Intent i = new Intent(this, RegistrationActivity.class);
        startActivity(i);
    }
}
