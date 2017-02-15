package com.uusafe.mobilesafe_plugin_framework;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        ((AndroidApplication) getApplication()).getPluginHost().startPluginActivity("plugina", "com.uusafe.plugina.MockActivity");
        return super.onTouchEvent(event);
    }
}
