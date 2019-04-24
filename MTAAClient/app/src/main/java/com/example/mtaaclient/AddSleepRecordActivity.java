package com.example.mtaaclient;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mtaaclient.model.ResponseDelegate;
import com.example.mtaaclient.model.ResponseResult;
import com.example.mtaaclient.model.RestParent;
import com.example.mtaaclient.model.RestPost;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddSleepRecordActivity extends Activity {

    private Button mFallAsleepDate;
    private Button mFallAsleepTime;
    private Button mWakeUpTime;
    private Button mStoreSleepButton;
    private Button mWakeUpDate;

    private Date mChosenFallAsleepDate = null;
    private Date mChosenWakeUpDate = null;
    private String fallAsleepDateAsString;
    private String wakeUpDateAsString;
    private int minutes = 0;
    private int hours = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sleep_record);
        initView();
    }

    private void initView() {
        mFallAsleepDate = (Button) findViewById(R.id.pickDateSleepRecord);
        mFallAsleepTime = (Button) findViewById(R.id.fallAsleepTime);
        mWakeUpTime = (Button) findViewById(R.id.wakeUpTime);
        mStoreSleepButton = (Button) findViewById(R.id.storeSleepRecord);
        mWakeUpDate = (Button)findViewById(R.id.pickWakeUpDate);
    }

    public void pickDateSleepRecord(View view) {
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
                        fallAsleepDateAsString = String.format(Locale.getDefault(),
                                "%d-%02d-%02d", year, ++monthOfYear, dayOfMonth);
                        mFallAsleepDate.setText(fallAsleepDateAsString);

                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

                        try {
                            mChosenFallAsleepDate = formatter.parse(fallAsleepDateAsString);
                        } catch (ParseException ignored) {
//                            Log.d("msg", "bullshit");
                        }

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }

    private void fallAsleepTimePicker() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        final int[] mHour = {c.get(Calendar.HOUR_OF_DAY)};
        final int[] mMinute = {c.get(Calendar.MINUTE)};

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        mHour[0] = hourOfDay;
                        mMinute[0] = minute;

                        mFallAsleepTime.setText(hourOfDay + ":" + minute);
                    }
                }, mHour[0], mMinute[0], false);
        timePickerDialog.show();
    }

    public void storeSleepRecord(View view) {
        String wakeUpTime = mWakeUpTime.getText().toString();
        if("Wake Up Time".equals(wakeUpTime)){
            Toast.makeText(this, "Please enter wake up time ", Toast.LENGTH_SHORT).show();
            return;
        }
        String fallAsleepTime = mFallAsleepTime.getText().toString();
        if("Fall Asleep time".equals(fallAsleepTime)){
            Toast.makeText(this, "Please enter fall asleep time ", Toast.LENGTH_SHORT).show();
            return;
        }
        String wakeUpDate = mWakeUpDate.getText().toString();
        if("Wake Up Date".equals(wakeUpDate)){
            Toast.makeText(this, "Please enter wake up date ", Toast.LENGTH_SHORT).show();
            return;
        }
        String fallAsleepDate = mFallAsleepDate.getText().toString();
        if("Fall Asleep Date".equals(fallAsleepDate)){
            Toast.makeText(this, "Please enter fall asleep date ", Toast.LENGTH_SHORT).show();
            return;
        }
        if(getInterval(mWakeUpTime.getText().toString(), mFallAsleepTime.getText().toString(),
                mFallAsleepDate.getText().toString(), mWakeUpDate.getText().toString())){
            sendDataToDatabase(fallAsleepDateAsString, Saver.getInstance(this).getToken(), 8, 7.76, hours, minutes);
        }
    }

    private void sendDataToDatabase(String dataAsString, String token, int goal, double interval, int hours, int minutes) {
        RestParent restPost = new RestPost(new ResponseDelegate() {
            @Override
            public void processResponse(ResponseResult result) {
                processRegistrationResult(result);
            }
        }, this);
        restPost.execute("/store_sleep", transferDataToJson(dataAsString, goal, interval));
    }

    private String transferDataToJson(String dataAsString, int goal, double interval) {
        JSONObject json = new JSONObject();
        try {
            json.put("goal", goal);
            json.put("interval_of_sleep", interval);
            json.put("date", dataAsString);
            json.put("hours", hours);
            json.put("minutes", minutes);
            Log.d("json", "sleep");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("msg", "creating registration post error " + e.getMessage());
        }
        return json.toString();
    }

    private boolean getInterval(String mWakeUpButtonText, String mFallAsleepButtonText, CharSequence mFallAsleepDatePickButtonText,
                             CharSequence mWakeUpDatePickButtonText){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String fallAsleepTimestamp = mFallAsleepDatePickButtonText + " " + mFallAsleepButtonText + ":00";
        String wakeUpTimeTimestamp = mWakeUpDatePickButtonText + " " + mWakeUpButtonText + ":00";
        Date fallAsleep;
        Date wakeUp;
        try {
            fallAsleep = sdf.parse(fallAsleepTimestamp);
            wakeUp = sdf.parse(wakeUpTimeTimestamp);
            Long control = wakeUp.getTime() - fallAsleep.getTime();

            if(control < 0){
                    Toast.makeText(this, "Can't wake up before going to sleep", Toast.LENGTH_SHORT).show();
                    return false;
            }

            minutes = (int) ((control/ (1000*60)) % 60);
            hours   = (int) ((control / (1000*60*60)) % 24);

            Log.d("msg", "hey you");
//            fallAsleepMillis = date.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void processRegistrationResult(ResponseResult result) {
        if (result.getCode() == 200/*409*/) {
//            mRegistrationEmail.setError("Email already in use");
            Toast.makeText(this, "Data boli ulozene do databazy", Toast.LENGTH_SHORT).show();
            openHomeScreen();
//           setContentView(R.layout.activity_login);
//            openLogin();
        } else {
            Toast.makeText(this, "NIeco je zle " + result.getCode(), Toast.LENGTH_SHORT).show();
        }
    }

    private void openHomeScreen() {
//        Intent i = new Intent(this, HomeActivity.class);
//        startActivity(i);
        this.finish();
    }

    public void pickFallAsleepTime(View view) {
        fallAsleepTimePicker();
    }

    public void pickWakeUpTime(View view) {
        wakeUpTimePicker();
    }

    private void wakeUpTimePicker() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        final int[] mHour = {c.get(Calendar.HOUR_OF_DAY)};
        final int[] mMinute = {c.get(Calendar.MINUTE)};

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        mHour[0] = hourOfDay;
                        mMinute[0] = minute;

                        mWakeUpTime.setText(hourOfDay + ":" + minute);
                    }
                }, mHour[0], mMinute[0], false);
        timePickerDialog.show();
    }

    public void pickWakeUpDate(View view) {

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
                        wakeUpDateAsString = String.format(Locale.getDefault(),
                                "%d-%02d-%02d", year, ++monthOfYear, dayOfMonth);
                        mWakeUpDate.setText(wakeUpDateAsString);

                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

                        try {
                            mChosenWakeUpDate = formatter.parse(wakeUpDateAsString);
                        } catch (ParseException ignored) {
//                            Log.d("msg", "bullshit");
                        }

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
}
