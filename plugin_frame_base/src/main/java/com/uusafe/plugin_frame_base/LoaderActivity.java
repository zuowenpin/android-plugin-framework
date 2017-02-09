package com.uusafe.plugin_frame_base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by ${zuowp291} on 2017/2/9.
 */

public abstract class LoaderActivity extends Activity {

    @Override
    protected void attachBaseContext(Context newBase) {
        newBase = Factory2.createActivityContext(this, newBase);
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Factory2.handleActivityCreate(this, savedInstanceState);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
    }

    @Override
    protected void onDestroy() {
        Factory2.handleActivityDestroy(this);
        super.onDestroy();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Factory2.handleRestoreInstanceState(this, savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void startActivity(Intent intent) {
        if (Factory2.startActivity(this, intent)) {
            return;
        }
        super.startActivity(intent);
    }
}
