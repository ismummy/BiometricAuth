package com.ismummy.biometricauth.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.ismummy.biometricauth.R;
import com.ismummy.biometricauth.ui.bases.BaseActivity;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class EnrollmentActivity extends BaseActivity {

    @BindView(R.id.medt_search_input)
    MaterialEditText searchInput;
    @BindView(R.id.layout_details)
    LinearLayout detailsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollment);
        detailsLayout.setVisibility(View.GONE);
    }

    @OnTextChanged(value = R.id.medt_search_input, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void AddressChanged() {
        if (searchInput.getText().toString().trim().length() >= 10) {
            enroll();
        }
    }

    @OnClick(R.id.miv_search)
    void searchClicked() {
        if (searchInput.getText().toString().trim().length() >= 10) {
            enroll();
        }
    }

    @OnClick(R.id.btn_enrollment_proceed)
    void proceedClicked() {
        Intent intent = new Intent(this, BiometricCaptureActivity.class);
        intent.putExtra("ACCOUNT_NUMBER", searchInput.getText().toString().trim());
        startActivity(intent);
        searchInput.setText("");
    }

    private void enroll() {
        detailsLayout.setVisibility(View.VISIBLE);
    }


}
