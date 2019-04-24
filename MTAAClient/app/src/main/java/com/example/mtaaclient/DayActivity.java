package com.example.mtaaclient;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mtaaclient.model.ResponseDelegate;
import com.example.mtaaclient.model.ResponseResult;
import com.example.mtaaclient.model.RestGet;
import com.example.mtaaclient.model.RestPut;

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
    private Button mUpdateWater;
    private final String TAG = "DayActivityErrorMessage";

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
        mUpdateWater = (Button)findViewById(R.id.updateWater);
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
            int hours = json.getInt("hours");
            int minutes = json.getInt("minutes");
            mSleepText.setText("You slept for " + hours + " hour and "+ minutes +" minutes");
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
//            setContentView(R.layout.activity_day);
        } else {
            finish();
        }
        if(mSleepLayout.getVisibility() == View.VISIBLE){
            mSleepLayout.setVisibility(View.GONE);
//            setContentView(R.layout.activity_day);
        }else{
            finish();
        }
    }

    public void updateWater(final View view) {
        final EditText taskEditText = new EditText(this);
        taskEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Update Water")
                .setMessage("How much did you drink?")
                .setView(taskEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        waterUpdateCofirm(view, String.valueOf(taskEditText.getText()));

                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    public void waterUpdateCofirm(View view, String amount) {
        final Context context = this;
        RestPut restPut = new RestPut(new ResponseDelegate() {
            @Override
            public void processResponse(ResponseResult result) {
//                Log.d("msg", "db updated");
                processResult(result);
            }
        });
        int value = Integer.parseInt(amount);
        String mChosenDate = mTextDate.getText().toString();
        int control = Integer.parseInt(amount);
        if(control < 0){
                Toast.makeText(this, "Can't enter negative amount", Toast.LENGTH_SHORT).show();
                return;
        }
        restPut.execute("/update_water", "token", Saver.getInstance(this).getToken(),
                "date", mChosenDate, "amount", amount);
    }

    public void processResult(ResponseResult result){
        if(result.getCode() == 200){
            Toast.makeText(this, "Water amount updated", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Water amount not updated", Toast.LENGTH_SHORT).show();
        }
    }
}
