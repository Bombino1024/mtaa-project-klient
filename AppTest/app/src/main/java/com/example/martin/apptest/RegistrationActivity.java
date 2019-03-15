package com.example.martin.apptest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.martin.apptest.model.Connector;
import com.example.martin.apptest.model.ResponseDelegate;
import com.example.martin.apptest.model.ResponseResult;
import com.example.martin.apptest.model.RestGet;
import com.example.martin.apptest.model.RestParent;
import com.example.martin.apptest.model.RestPost;

import java.net.HttpURLConnection;

public class RegistrationActivity extends Activity {

    private EditText mRegistrationEmail;
    private EditText mRegistrationPassword;
    private Button mRegistrationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initView();
    }

    private void initView() {
        mRegistrationEmail = (EditText) findViewById(R.id.registrationEmail);
        mRegistrationPassword = (EditText) findViewById(R.id.registrationPassword);
        mRegistrationButton = (Button) findViewById(R.id.registrationButton);
    }

    public void buttonClicked(View view) {
        String email = mRegistrationEmail.getText().toString();
        String password = mRegistrationPassword.getText().toString();
        sendDataToDatabase(email, password);
    }

    private void sendDataToDatabase(final String email, final String password) {
//        Thread t = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Connector connector = new Connector(RegistrationActivity.this);
//                ResponseResult result = connector.execute("/register", "email", email, "password", password);
//                if(result.getCode() == HttpURLConnection.HTTP_OK){
//                    Log.d("check", "Je to OK");
//                }else{
//                    Log.e("check", "Neni to OK, lebo " + result.getCode());
//                }
//            }
//        });
//        t.start();
        RestParent restPost = new RestPost(new ResponseDelegate() {
            @Override
            public void processResponse(ResponseResult result) {
                processRegistrationResult(result);
            }
        });
        restPost.execute("/register", "email", email, "password", password);
    }

    private void processRegistrationResult(ResponseResult result) {
        if (result.getCode() == 200/*409*/) {
//            mRegistrationEmail.setError("Email already in use");
           Toast.makeText(this, "Pouzivatel sa uspesne registroval", Toast.LENGTH_SHORT).show();
//           setContentView(R.layout.activity_login);
            openLogin();
        } else {
            Toast.makeText(this, "NIeco je zle " + result.getCode(), Toast.LENGTH_SHORT).show();
        }
    }

    private void openLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
