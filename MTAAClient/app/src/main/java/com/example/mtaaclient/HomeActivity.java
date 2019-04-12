package com.example.mtaaclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mtaaclient.model.ResponseDelegate;
import com.example.mtaaclient.model.ResponseResult;
import com.example.mtaaclient.model.RestGet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import  java.util.Date;

public class HomeActivity extends Activity {

    LinearLayout mTestLayout = null;
    Button mAddSleepRecord = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
       initView();
        getDataFromDatabase();
    }

    private void getDataFromDatabase() {
        RestGet get = new RestGet(new ResponseDelegate() {
            @Override
            public void processResponse(ResponseResult result) {
                Log.d("chalo", "tu som");
                processResult(result);
            }
        });
        String token = Saver.getInstance(this).getToken();
        get.execute("/get_days", "token", token);
//        Log.d("msg", "Here");
    }

    private void initView() {
        mTestLayout = (LinearLayout) findViewById(R.id.testButtons);
        mAddSleepRecord = (Button)findViewById(R.id.addSleepRecord);
    }

    private void processResult(ResponseResult result) {
//        mTestLayout = (LinearLayout) findViewById(R.id.testButtons);
        final List<String> dates = new ArrayList<>();
        JSONArray json = null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            json = new JSONArray(result.getBody());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(json != null){
            for(int i = 0; i < json.length(); i++){
                try {
                    JSONObject jsonObject = json.getJSONObject(i);
                    String date = jsonObject.getString("date");
                    dates.add(date);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            for(int i = 0; i < json.length(); i++){
                Button button = new Button(this);
                button.setText(dates.get(i));
                button.setId(i);
                mTestLayout.addView(button);
                final int id_ = button.getId();
                button = ((Button) findViewById(id_));
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        Intent intent = new Intent(HomeActivity.this,
                                DayActivity.class);
                        Bundle b = new Bundle();
                        int i = id_;
                        b.putString("date", dates.get(i)); //Your id
                        intent.putExtras(b); //Put your id to your next Intent
                        startActivity(intent);

//                        Toast.makeText(view.getContext(),
//                                "Button clicked index = " + id_, Toast.LENGTH_SHORT)
//                                .show();
                    }
                });
            }
        }
    }

    public void buttonClicked(View view) {
        Toast.makeText(this, "Button clicked", Toast.LENGTH_SHORT).show();
    }

    public void addSleepRecordClick(View view) {
        Log.d("msg", "oh shit");
    }
}
