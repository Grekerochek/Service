package com.alexander.service;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button buttonService;
    private Button buttonActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initListeners();
    }

    private void initViews(){

        buttonService = findViewById(R.id.service);
        buttonActivity = findViewById(R.id.activity);
    }

    private void initListeners(){

        buttonService.setOnClickListener(new ButtonServiceClickListener());
        buttonActivity.setOnClickListener(new ButtonActivityClickListener());

    }

    private class ButtonServiceClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            startService(MyIntentService.newIntent(MainActivity.this));
        }
    }

    private class ButtonActivityClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            startActivity(SecondActivity.newIntent(MainActivity.this));
        }
    }
}
