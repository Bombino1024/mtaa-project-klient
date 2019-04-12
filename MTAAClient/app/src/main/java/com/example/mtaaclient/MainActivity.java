package com.example.mtaaclient;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mtaaclient.model.User;

public class MainActivity extends Activity {

    private TextView mUserNameText;
    private ImageView mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
//        mUserNameText = (TextView) findViewById(R.id.userNameText);
//        mImage = (ImageView) findViewById(R.id.lilkoImage);
    }

    public void buttonClicked(View view) {
        User actualUser = new User(19,"Dominik", "Ekans");
        mImage.setVisibility(mImage.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        mUserNameText.setText(String.format(getResources().getString(R.string.user_name_and_surname), actualUser.getName(), actualUser.getSurname()));
    }
}
