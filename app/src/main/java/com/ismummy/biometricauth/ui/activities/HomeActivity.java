package com.ismummy.biometricauth.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ismummy.biometricauth.R;
import com.ismummy.biometricauth.ui.bases.BaseActivity;

import butterknife.OnClick;

public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    @OnClick(R.id.btn_home_enroll)
    void enrollClicked(){
        startActivity(new Intent(this, EnrollmentActivity.class));
    }

    @OnClick(R.id.btn_home_verify)
    void verifyClicked()
    {
        startActivity(new Intent(this, VerificationActivity.class));
    }
}
