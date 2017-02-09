package com.uusafe.plugina;

import android.os.Bundle;
import android.view.Menu;

import com.uusafe.plugin_frame_base.LoaderActivity;

/**
 * Created by ${zuowp291} on 2017/2/9.
 */

public class MockActivity extends LoaderActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }
}
