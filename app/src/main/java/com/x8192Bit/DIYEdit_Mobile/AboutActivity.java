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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            overridePendingTransition(0, 0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void OnAboutButtonClicked(View v) {
        ((TextView) findViewById(R.id.InformationTextView)).setText("DIYEdit_Mobile 是 xperia64 的 DIYEdit 在 Android 上的移植版本。\n\n感谢：\n · FluidSynth 提供使用自定义 sf2 音色库播放音乐的功能。\n · xperia64 的 DIYEdit 提供了简单易用的类。\n · codekidX 的 storage-chooser 用于选择文件与目录，以及 mendhak 提供的 Android R (11) 支持。\n · kshoji 的 JFugue-for-Android 播放与处理 MIDI 文件。\n · kshoji 的 javax.sound.midi-for-Android 提供对 JFugue-for-Android 的支持。\n · 部分参考了 Creating a Fluidsynth Hello World App for Android -- Hector Ricardo \n\nDIYEdit_Mobile 是一个自由软件，受到 GPLv3 许可证的保护。使用本软件时，请遵守当地法律法规。\n\n软件版本 1.0.0\n内置的 FluidSynth 版本 2.3.3");
    }


    private void OnLicenseButtonClicked(View v) {

    }


}