package com.ismummy.biometricauth.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.ismummy.biometricauth.R;
import com.ismummy.biometricauth.ui.bases.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class BiometricCaptureActivity extends BaseActivity {

    @BindView(R.id.iv_left)
    ImageView leftImage;
    @BindView(R.id.iv_right)
    ImageView rightImage;


    String act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biometric_capture);
        if (getIntent() == null) {
            finish();
            return;
        }

        act = getIntent().getStringExtra("ACCOUNT_NUMBER");
    }

    @OnClick(R.id.layout_left)
    void leftClicked() {
        Intent intent = new Intent(this, ScanningActivity.class);
        intent.putExtra("OPERATION", 0);
        intent.putExtra("ACCOUNT_NUMBER", act+"_LEFT");
        startActivityForResult(intent, 100);
    }

    @OnClick(R.id.layout_right)
    void rightClicked() {
        Intent intent = new Intent(this, ScanningActivity.class);
        intent.putExtra("OPERATION", 0);
        intent.putExtra("ACCOUNT_NUMBER", act+"_RIGHT");
        startActivityForResult(intent, 200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == RESULT_OK) {
            if (requestCode == 100) {

                Bitmap bm = data.getParcelableExtra("IMAGE");
                leftImage.setImageBitmap(bm);
            } else if (requestCode == 200) {
                Bitmap bm = data.getParcelableExtra("IMAGE");
                rightImage.setImageBitmap(bm);
            }
        }
    }
}
