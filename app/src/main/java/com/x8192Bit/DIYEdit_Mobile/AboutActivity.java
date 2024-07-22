package com.x8192Bit.DIYEdit_Mobile;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import x8192Bit.DIYEdit_Mobile.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        findViewById(R.id.AboutButton).setOnClickListener(this::OnAboutButtonClicked);
        findViewById(R.id.LicenseButton).setOnClickListener(this::OnLicenseButtonClicked);
        OnAboutButtonClicked(null);
        TextView tv = findViewById(R.id.InformationTextView);
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
        ((TextView) findViewById(R.id.InformationTextView)).setText(getString(R.string.aboutTextKey).replace("\\n", "\n"));
    }


    private void OnLicenseButtonClicked(View v) {

    }


}