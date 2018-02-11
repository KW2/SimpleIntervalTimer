package com.kw2.kw2.sit;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.beardedhen.androidbootstrap.BootstrapButton;

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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.language_koBtn:
                // 한국어 선택
                Locale ko = Locale.KOREA;
                Configuration config = new Configuration();
                config.locale = ko;
                getResources().updateConfiguration(config, getResources().getDisplayMetrics());

                ActivityMain.mainActivity.finish();
                this.finish();
                startActivity(new Intent(this, ActivityMain.class));
                break;
            case R.id.language_enBtn:
                // 영어 선택
                Locale en = Locale.US;
                Configuration config1 = new Configuration();
                config1.locale = en;
                getResources().updateConfiguration(config1, getResources().getDisplayMetrics());

                ActivityMain.mainActivity.finish();
                this.finish();
                startActivity(new Intent(this, ActivityMain.class));
                break;
            case R.id.language_jaBtn:
                // 일본어 선택
                Locale ja = Locale.JAPAN;
                Configuration config2 = new Configuration();
                config2.locale = ja;
                getResources().updateConfiguration(config2, getResources().getDisplayMetrics());

                ActivityMain.mainActivity.finish();
                this.finish();
                startActivity(new Intent(this, ActivityMain.class));
                break;

        }
    }
}
