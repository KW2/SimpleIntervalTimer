package com.kw2.kw2.sit;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

/**
 * Created by SAMSUNG on 2018-01-20.
 */

public class ActivityLanguage extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        Button koBtn = (Button) findViewById(R.id.language_koBtn);
        Button enBtn = (Button) findViewById(R.id.language_enBtn);
        Button jaBtn = (Button) findViewById(R.id.language_jaBtn);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.language_koBtn:
                // 한국어 선택

                break;
            case R.id.language_enBtn:
                // 영어 선택

                break;
            case R.id.language_jaBtn:
                // 일본어 선택

                break;

        }
    }
}
