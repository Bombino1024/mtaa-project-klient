package com.example.mtaaclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mtaaclient.model.ResponseDelegate;
import com.example.mtaaclient.model.ResponseResult;
import com.example.mtaaclient.model.RestParent;
import com.example.mtaaclient.model.RestPost;

import org.json.JSONException;
import org.json.JSONObject;

public class RegistrationActivity extends Activity {

    private static final String TAG = RegistrationActivity.class.getSimpleName();
    private EditText mRegistrationEmail;
    private EditText mRegistrationPassword;
    private Button mRegistrationButton;
    private EditText mUsernameText = null;
    private EditText mFirstNameText = null;
    private EditText mLastNameText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initView();
        openLogin();
    }

    private void initView() {
        mRegistrationEmail = (EditText) findViewById(R.id.registrationEmail);

        mUsernameText = (EditText) findViewById(R.id.registrationUserName);

        mFirstNameText = (EditText) findViewById(R.id.registrationFirstName);

        mLastNameText = (EditText) findViewById(R.id.registrationLastName);

        mRegistrationPassword = (EditText) findViewById(R.id.registrationPassword);

        mRegistrationButton = (Button) findViewById(R.id.registrationButton);
    }

    public void buttonClicked(View view) {
        String email = mRegistrationEmail.getText().toString();
        String password = mRegistrationPassword.getText().toString();
        String firstName = mFirstNameText.getText().toString();
        String lastName = mLastNameText.getText().toString();
        String username = mUsernameText.getText().toString();

        sendDataToDatabase(email, password, username, firstName, lastName);
    }

    private void sendDataToDatabase(final String email, final String password, String username, String firstName, String lastName) {
        RestParent restPost = new RestPost(new ResponseDelegate() {
            @Override
            public void processResponse(ResponseResult result) {
                processRegistrationResult(result);
            }
        });
        restPost.execute("/register", transferRegistrationDataToJson(email, password, username, firstName, lastName));
//        restPost.execute("/register", "email", email, "password", password);
    }

    private String transferRegistrationDataToJson(String email, String password, String username, String firstName, String lastName) {
        JSONObject json = new JSONObject();
        try {
            json.put("email", email);
            json.put("username", username);
            json.put("firstName", firstName);
            json.put("lastName", lastName);
            json.put("password", password);
        } catch (JSONException e) {
            Log.e(TAG, "creating registration post error " + e.getMessage());
        }
        return json.toString();
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
