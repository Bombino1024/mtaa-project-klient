package com.example.mtaaclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mtaaclient.model.ResponseDelegate;
import com.example.mtaaclient.model.ResponseResult;
import com.example.mtaaclient.model.RestGet;
import com.example.mtaaclient.model.RestParent;

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
//            Toast.makeText(this, "Token je " + result.getBody(), Toast.LENGTH_SHORT).show();
            Saver.getInstance(this).saveToken(result.getBody());
//           setContentView(R.layout.activity_main);
            openHomeScreen();
        } else {
            Toast.makeText(this, "NIeco je zle " + result.getCode(), Toast.LENGTH_SHORT).show();
        }
    }

    private void openHomeScreen() {
//        Saver.getInstance(this).saveToken("HelloFriend!");
//        String token = Saver.getInstance(this).getToken();

        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
        this.finish();
    }
}
