package com.example.mtaaclient;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mtaaclient.model.ResponseDelegate;
import com.example.mtaaclient.model.ResponseResult;
import com.example.mtaaclient.model.RestGet;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class GetWaterActivity extends Activity {

    private Button mDatePickingButton = null;
    private Date mChosenDate = null;
    private RelativeLayout mWaterLayout;
    private TextView mWaterText = null;
    private TextView mWaterGoalText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_water);
        initView();
    }

    private void initView() {
        mDatePickingButton = (Button) findViewById(R.id.pickDate);

        mWaterLayout = (RelativeLayout) findViewById(R.id.resutlayout);

        mWaterText = (TextView) findViewById(R.id.resultWaterText);

        mWaterGoalText = (TextView) findViewById(R.id.resultWaterGoalText);
    }

    public void pickDate(View view) {
        pickDate();
    }

    private void pickDate() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

//                        txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        String dateAsString = String.format(Locale.getDefault(),
                                "%d-%02d-%02d", year, ++monthOfYear, dayOfMonth);
                        mDatePickingButton.setText(dateAsString);

                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

                        try {
                            mChosenDate = formatter.parse(dateAsString);
                        } catch (ParseException ignored) {

                        }

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void buttonClicked(View view) {
    }

    public void getWaterClicked(View view) {
        if (mChosenDate == null) {
            Toast.makeText(this, getResources().getString(R.string.choose_date), Toast.LENGTH_SHORT).show();
        } else {
            getWaterByDate(mChosenDate);
        }
    }

    private void getWaterByDate(Date mChosenDate) {
        RestGet get = new RestGet(new ResponseDelegate() {
            @Override
            public void processResponse(ResponseResult result) {
                Log.d("chalo", "tu som");
                processResult(result);
            }
        });
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String requiredDate = df.format(mChosenDate).toString();
        get.execute("/get_water", "token", "35cb6bfa-7b32-4e7a-8341-f9ceb220", "date", requiredDate);
    }

    private void processResult(ResponseResult result) {
        mWaterLayout.setVisibility(View.VISIBLE);
        try {
            JSONObject json = new JSONObject(result.getBody());
            int amount = json.getInt("amount");
            mWaterText.setText("You drank " + amount + " of water");
            int goal = json.getInt("goal");
            mWaterGoalText.setText("Your goal is " + goal);
        } catch (Exception ignored) {

        }
    }

    @Override
    public void onBackPressed() {
        if (mWaterLayout.getVisibility() == View.VISIBLE) {
            mWaterLayout.setVisibility(View.GONE);
        } else {
            finish();
        }
    }
}
