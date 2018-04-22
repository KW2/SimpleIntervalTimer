package com.kw2.kw2.sit;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Locale;

/**
 * Created by SAMSUNG on 2018-01-20.
 */

public class ActivityLanguage extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        BootstrapButton koBtn = (BootstrapButton) findViewById(R.id.language_koBtn);
        BootstrapButton enBtn = (BootstrapButton) findViewById(R.id.language_enBtn);
        BootstrapButton jaBtn = (BootstrapButton) findViewById(R.id.language_jaBtn);

        koBtn.setOnClickListener(this);
        enBtn.setOnClickListener(this);
        jaBtn.setOnClickListener(this);

        AdView mAdView = (AdView) findViewById(R.id.language_ad);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onClick(View view) {
        Configuration config = new Configuration();
        switch (view.getId()){
            case R.id.language_koBtn:
                // 한국어 선택
                Locale ko = Locale.KOREA;
                config.setLocale(ko);
                getResources().updateConfiguration(config, getResources().getDisplayMetrics());

                myExit();
                break;
            case R.id.language_enBtn:
                // 영어 선택
                Locale en = Locale.US;
                config.setLocale(en);
                getResources().updateConfiguration(config, getResources().getDisplayMetrics());

                myExit();
                break;
            case R.id.language_jaBtn:
                // 일본어 선택
                Locale ja = Locale.JAPAN;
                config.setLocale(ja);
                getResources().updateConfiguration(config, getResources().getDisplayMetrics());

                myExit();
                break;

        }
    }

    public void myExit(){
        ActivityMain.mainActivity.finish();
        this.finish();
        startActivity(new Intent(this, ActivityMain.class));
    }
}
