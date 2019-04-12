package com.example.martin.apptest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.martin.apptest.model.ResponseDelegate;
import com.example.martin.apptest.model.ResponseResult;
import com.example.martin.apptest.model.RestGet;
import com.example.martin.apptest.model.RestParent;

public class LoginActivity extends Activity {

    private EditText mLoginUsername;
    private EditText mLoginPassword;
    private Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
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
//            mRegistrationEmail.setError("Email already in use");
            Toast.makeText(this, "Pouzivatel sa uspesne prihlasil", Toast.LENGTH_SHORT).show();
            openWater();
//           setContentView(R.layout.activity_main);
        } else {
            Toast.makeText(this, "NIeco je zle " + result.getCode(), Toast.LENGTH_SHORT).show();
        }
    }

    private void openWater() {
        Intent i = new Intent(this, GetWaterActivity.class);
        startActivity(i);
    }
}
