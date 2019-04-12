package com.example.mtaaclient;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mtaaclient.model.ResponseDelegate;
import com.example.mtaaclient.model.ResponseResult;
import com.example.mtaaclient.model.RestGet;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DayActivity extends Activity {

    private EditText mTextDate;
    private Button mGetWaterButton;
    private Button mGetSleepButton;
    private RelativeLayout mWaterLayout;
    private TextView mWaterText;
    private TextView mWaterGoalText;
    private RelativeLayout mSleepLayout;
    private TextView mSleepGoalText;
    private TextView mSleepText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        initView();
    }

    private void initView() {
        mTextDate = (EditText)findViewById(R.id.dateText);
        Bundle b = getIntent().getExtras();
        String value = ""; // or other values
        if(b != null)
            value = b.getString("date");
        mTextDate.setText(value);
        mGetWaterButton = (Button)findViewById(R.id.getWaterButton);
        mGetSleepButton = (Button)findViewById(R.id.getSleepButton);
        mWaterText = (TextView)findViewById(R.id.resultWaterText);
        mWaterGoalText = (TextView)findViewById(R.id.resultWaterGoalText);
        mWaterLayout = (RelativeLayout) findViewById(R.id.resutWaterlayout);
        mSleepText = (TextView) findViewById(R.id.resultSleepText);
        mSleepGoalText = (TextView)findViewById(R.id.resultSleepGoalText);
        mSleepLayout = (RelativeLayout)findViewById(R.id.resutSleeplayout);
    }

    public void getSleepButtonClicked(View view) {
        RestGet get = new RestGet(new ResponseDelegate() {
            @Override
            public void processResponse(ResponseResult result) {
//                Log.d("chalo", "tu som");
                processSleepResult(result);
            }
        });
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String mChosenDate = mTextDate.getText().toString();
//        String requiredDate = df.format(mChosenDate).toString();
        get.execute("/get_sleep", "token", Saver.getInstance(this).getToken(), "date", mChosenDate);
    }

    private void processSleepResult(ResponseResult result){
        mSleepLayout.setVisibility(View.VISIBLE);
        try {
            JSONObject json = new JSONObject(result.getBody());
            double interval = json.getDouble("interval");
            mSleepText.setText("You slept for " + interval + " hour");
            int goal = json.getInt("goal");
            mSleepGoalText.setText("Your goal is " + goal);
            Log.d("msg", "msg");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getWaterButtonClicked(View view) {
        RestGet get = new RestGet(new ResponseDelegate() {
            @Override
            public void processResponse(ResponseResult result) {
//                Log.d("chalo", "tu som");
                processWaterResult(result);
            }
        });
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String mChosenDate = mTextDate.getText().toString();
//        String requiredDate = df.format(mChosenDate).toString();
        get.execute("/get_water", "token", Saver.getInstance(this).getToken(), "date", mChosenDate);
    }

    private void processWaterResult(ResponseResult result) {
        mWaterLayout.setVisibility(View.VISIBLE);
        try {
            JSONObject json = new JSONObject(result.getBody());
            int amount = json.getInt("amount");
            mWaterText.setText("You drank " + amount + " of water");
            int goal = json.getInt("goal");
            mWaterGoalText.setText("Your goal is " + goal);
        } catch (Exception ignored) {
            Log.d("msg", "Day Activity Failed!");
        }
    }

    @Override
    public void onBackPressed() {
        if (mWaterLayout.getVisibility() == View.VISIBLE) {
            mWaterLayout.setVisibility(View.GONE);
        } else {
            finish();
        }
        if(mSleepLayout.getVisibility() == View.VISIBLE){
            mSleepLayout.setVisibility(View.GONE);
        }else{
            finish();
        }
    }
}
