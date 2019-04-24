package com.example.mtaaclient;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mtaaclient.model.ResponseDelegate;
import com.example.mtaaclient.model.ResponseResult;
import com.example.mtaaclient.model.RestParent;
import com.example.mtaaclient.model.RestPost;
import com.example.mtaaclient.model.utils.FTPAsyncStuff;
import com.example.mtaaclient.model.utils.ImageFilePath;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class RegistrationActivity extends Activity {

    private static final String TAG = RegistrationActivity.class.getSimpleName();
    private static final int PICK_IMAGE = 10;
    private static final int PERMISSION_ALLOWANCE_CODE = 15;
    private EditText mRegistrationEmail;
    private EditText mRegistrationPassword;
    private Button mRegistrationButton;
    private EditText mUsernameText = null;
    private EditText mFirstNameText = null;
    private EditText mLastNameText = null;
    private ImageView mPhotoHolder = null;
    private String mPickedFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initView();
//        openLogin();
    }


    private void initView() {
        mRegistrationEmail = (EditText) findViewById(R.id.registrationEmail);

        mUsernameText = (EditText) findViewById(R.id.registrationUserName);

        mFirstNameText = (EditText) findViewById(R.id.registrationFirstName);

        mLastNameText = (EditText) findViewById(R.id.registrationLastName);

        mRegistrationPassword = (EditText) findViewById(R.id.registrationPassword);

        mRegistrationButton = (Button) findViewById(R.id.registrationButton);

        mPhotoHolder = (ImageView) findViewById(R.id.imageShower);
    }

    public void buttonClicked(View view) {
        String email = mRegistrationEmail.getText().toString();
        String password = mRegistrationPassword.getText().toString();
        String firstName = mFirstNameText.getText().toString();
        String lastName = mLastNameText.getText().toString();
        String username = mUsernameText.getText().toString();

        sendDataToDatabase(email, password, username, firstName, lastName);
    }

    private void sendDataToDatabase(final String email, final String password, final String username, final String firstName, final String lastName) {
        FTPAsyncStuff at = new FTPAsyncStuff(this, new ResponseDelegate() {
            @Override
            public void processResponse(ResponseResult result) {
                if (result.getCode() == 200) {
                    RestParent restPost = new RestPost(new ResponseDelegate() {
                        @Override
                        public void processResponse(ResponseResult result) {
                            processRegistrationResult(result);
                        }
                    });
                    restPost.execute("/register", transferRegistrationDataToJson(email, password, username, firstName, lastName, result.getBody()));
                }

            }
        });
        if(mPickedFilePath != null){
            at.execute(new File(mPickedFilePath));
        }else{
            Toast.makeText(this, "Profile picture not entered", Toast.LENGTH_SHORT).show();
        }
    }

    private String transferRegistrationDataToJson(String email, String password, String username, String firstName, String lastName, String fileUrl) {
        JSONObject json = new JSONObject();
        try {
            json.put("email", email);
            json.put("username", username);
            json.put("firstName", firstName);
            json.put("lastName", lastName);
            json.put("password", password);
            json.put("image", fileUrl);
        } catch (JSONException e) {
            Log.e(TAG, "creating registration post error " + e.getMessage());
        }
        return json.toString();
    }

    private void processRegistrationResult(ResponseResult result) {
        if (result.getCode() == 200/*409*/) {
            Toast.makeText(this, "User successfully registered", Toast.LENGTH_SHORT).show();
            openLogin();
        } else if(result.getCode() == 400){
            Toast.makeText(this, "Email not entered " + result.getCode(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Email already used " + result.getCode(), Toast.LENGTH_SHORT).show();
        }
    }

    private void openLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void selectImageFromGalery(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            if (resultCode != Activity.RESULT_OK) {
                Toast.makeText(this, "Problem with image", Toast.LENGTH_SHORT).show();
                return;
            }
            if (data == null) {
                Toast.makeText(this, "Problem with image", Toast.LENGTH_SHORT).show();
                return;
            }
            Uri uri = data.getData();
            mPickedFilePath = ImageFilePath.getPath(this, uri);
            Bitmap bMap = BitmapFactory.decodeFile(mPickedFilePath);
//            Picasso.with(this).load(mPickedFilePath).into(mPhotoHolder);
            mPhotoHolder.setImageBitmap(bMap);
        }
    }
}
