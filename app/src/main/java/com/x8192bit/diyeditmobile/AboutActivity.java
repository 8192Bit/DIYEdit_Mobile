package com.x8192bit.diyeditmobile;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;

import x8192bit.diyeditmobile.R;

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        findViewById(R.id.about_about_btn).setOnClickListener(this::OnAboutButtonClicked);
        findViewById(R.id.about_osl_btn).setOnClickListener(this::OnLicenseButtonClicked);
        OnAboutButtonClicked(null);
        TextView tv = findViewById(R.id.about_info_tv);
        tv.setText(getString(R.string.aboutTextKey).replace("\\n", "\n"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void OnAboutButtonClicked(View v) {
        ((TextView) findViewById(R.id.about_info_tv)).setText(getString(R.string.aboutTextKey).replace("\\n", "\n"));
    }


    private void OnLicenseButtonClicked(View v) {

    }


}