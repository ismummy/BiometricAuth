package com.ismummy.biometricauth.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ismummy.biometricauth.R;
import com.ismummy.biometricauth.ui.bases.BaseActivity;

public class ConfirmationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
    }
}
