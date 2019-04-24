package com.example.mtaaclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mtaaclient.model.ResponseDelegate;
import com.example.mtaaclient.model.ResponseResult;
import com.example.mtaaclient.model.RestDelete;
import com.example.mtaaclient.model.RestPost;
import com.example.mtaaclient.model.RestPut;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class SettingsActivity extends Activity {

    EditText mWaterGoal = null;
    EditText mSleepGoal = null;
    Button mDeleteUser = null;
    Button mUpdateGoals = null;
    private ImageView mProfilePhoto = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initView();
    }

    private void initView() {
        mSleepGoal = (EditText) findViewById(R.id.insertSleepGoal);
        mWaterGoal = (EditText) findViewById(R.id.insertWaterGoal);
        mDeleteUser = (Button) findViewById(R.id.deleteUser);
        mUpdateGoals = (Button) findViewById(R.id.updateGoals);

        mProfilePhoto = (ImageView) findViewById(R.id.profilePhotoHolder);
//        Picasso.with(this).load("http://autocloud.sk/_mainappfiles/mtaa_test/1556027714137.png").into(mProfilePhoto);
        String url = URLSaver.getInstance(this).getImageURL();
        if(url != null && url.startsWith("http")){
            Picasso.with(this).load(url).into(mProfilePhoto);
        }
    }

    public void updateGoals(View view) {
        final Context context = this;
        RestPost restPost = new RestPost(new ResponseDelegate() {
            @Override
            public void processResponse(ResponseResult result) {
                processResult(result);
            }
        }, this);

        String sleep = mSleepGoal.getText().toString();
        if("".equals(sleep)){
            Toast.makeText(this, "Please enter sleep goal", Toast.LENGTH_SHORT).show();
            return;
        }
        int control = Integer.parseInt(sleep);
        if(control < 0){
            Toast.makeText(this, "Can't enter negative sleep goal", Toast.LENGTH_SHORT).show();
            return;
        }
        String water = mWaterGoal.getText().toString();
        if("".equals(water)){
            Toast.makeText(this, "Please enter water goal", Toast.LENGTH_SHORT).show();
            return;
        }
        control = Integer.parseInt(water);
        if(control < 0){
            Toast.makeText(this, "Can't enter negative water goal", Toast.LENGTH_SHORT).show();
            return;
        }
        restPost.execute("/update_goals", createJson(mSleepGoal.getText().toString(), mWaterGoal.getText().toString()));
    }

    private String createJson(String sleepGoal, String waterGoal) {
        JSONObject json = new JSONObject();
        try {
            json.put("sleep_goal", sleepGoal);
            json.put("water_goal", waterGoal);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    public void processResult(ResponseResult result){
        if(result.getCode() == 200){
            Toast.makeText(this, "Data saved "+result.getCode(), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Data not saved "+result.getCode(), Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteUser(View view) {
        RestDelete restDelete = new RestDelete(new ResponseDelegate() {
            @Override
            public void processResponse(ResponseResult result) {
                processDeleteResult(result);
            }
        }, this);
        restDelete.execute("/delete_user");
    }

    public void processDeleteResult(ResponseResult result){
        if(result.getCode() == 200){
            Toast.makeText(this, "User deleted "+result.getCode(), Toast.LENGTH_SHORT).show();
            Saver.getInstance(this).removeToken();
            openRegScreen();
        }else{
            Toast.makeText(this, "User not deleted "+result.getCode(), Toast.LENGTH_SHORT).show();
        }
    }

    private void openRegScreen() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }
}
