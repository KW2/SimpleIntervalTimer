package com.kw2.kw2.sit;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by SAMSUNG on 2018-01-21.
 */

public class ActivityTimer extends Activity {

    boolean bRunning = true;

    private int nowSet = 1;

    private int nCounter = 0;

    private int setNum = 0;

    private int readyTimes = 5;

    private int workTimes = 0;
    private int wkValue = 0;

    private int restTimes = 0;
    private int rtValue = 0;

    TimerTask mTimerTask;
    final Handler handler = new Handler();
    Timer t = new Timer();

    TextView timerName, timeValueText, setValue, currentState, nowText;
    Button spBtn, stopBtn, soundBtn, updateBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        final DBHelper helper = new DBHelper(ActivityTimer.this);

        final int id = getIntent().getExtras().getInt("id");
        ListViewItem timer = helper.getItem(id);

        setNum = timer.getSetNum();

        workTimes = stringToInt(timer.getWorkTime());
        wkValue = workTimes;

        restTimes = stringToInt(timer.getRestTime());
        rtValue = restTimes;


        timerName = (TextView) findViewById(R.id.timer_title);
        timeValueText = (TextView) findViewById(R.id.timer_timeValue);
        setValue = (TextView) findViewById(R.id.timer_setValue);
        currentState = (TextView) findViewById(R.id.timer_currentState);
        nowText = (TextView) findViewById(R.id.timer_nowText);

        updateBtn = (Button) findViewById(R.id.timer_updateBtn);
        spBtn = (Button) findViewById(R.id.timer_spBtn);
        stopBtn = (Button) findViewById(R.id.timer_stopBtn);
        soundBtn = (Button) findViewById(R.id.timer_soundBtn);


        timerName.setText(timer.getTimeName());
        timeValueText.setText(timer.getTimeValue());
        setValue.setText(nowSet + " / " + setNum);

        spBtn.setTag("start");

        // 시작/일시정지/재시작 버튼 클릭
        spBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateBtn.setEnabled(true);
                if(view.getTag().equals("start")){
                    // 시작 상태
                    if (mTimerTask == null) {
                        bRunning = true;
                        doTimerTask();
                        spBtn.setText("||");
                        spBtn.setTag("pause");
                    }

                }else if(view.getTag().equals("pause")){
                    // 일시정지 상태
                    doTimerPause();
                    spBtn.setText("▶");
                    spBtn.setTag("resume");

                }else if(view.getTag().equals("resume")){
                    // 재시작 상태
                    doTimerPause();
                    spBtn.setText("||");
                    spBtn.setTag("pause");
                }
            }
        });

        // 멈춤 버튼 클릭
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopTask();
            }
        });

        // 수정 버튼 클릭
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog cstDialog = DialogMaker.newDialog(ActivityTimer.this, id, null);

                cstDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {

                        ListViewItem item = helper.getItem(id);

                        setNum = item.getSetNum();

                        timerName.setText(item.getTimeName());
                        timeValueText.setText(item.getTimeValue());
                        setValue.setText(nowSet + " / " + setNum);

                    }
                });

                cstDialog.show();
            }
        });
    }


    public void doTimerTask() {

        mTimerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        if(!bRunning) {
                            return;
                        }

                        if(nCounter <= 4){
                            nowText.setText("00:" + DialogMaker.twoStrForm(readyTimes--));
                        }else if(nCounter <= 4 + workTimes){
                            currentState.setText(R.string.workTime);
                            nowText.setText(intToString(wkValue));
                            wkValue--;
                            if(nCounter == 4 + workTimes && nowSet == setNum){
                                stopTask();
                            }
                        }else if(nCounter <= 4 + workTimes + restTimes){
                            currentState.setText(R.string.restTime);
                            nowText.setText(intToString(rtValue));
                            rtValue--;
                        }else if(nCounter > 4 + workTimes + restTimes){
                            nowSet++;
                            wkValue = workTimes;
                            rtValue = restTimes;
                            nCounter = 5;
                            setValue.setText(nowSet + " / " + setNum);
                            nowText.setText(intToString(wkValue));
                            currentState.setText(R.string.workTime);
                            wkValue--;
                        }

                        nCounter++;

                    }
                });
            }
        };

        // public void schedule (TimerTask task, long delay, long period)
        t.schedule(mTimerTask, 0, 1000); //

    }

    public void doTimerPause() {
        bRunning = !bRunning;
    }

    public void stopTask() {

        if (mTimerTask != null) {
            updateBtn.setEnabled(false);
            nowSet = 1;
            readyTimes = 5;
            wkValue = workTimes;
            rtValue = restTimes;
            setValue.setText(nowSet + " / " + setNum);
            nowText.setText("00:05");

            nCounter = 0;
            bRunning = false;
            mTimerTask.cancel();
            mTimerTask = null;
            spBtn.setText("▶");
            spBtn.setTag("start");
        }
    }

    public int stringToInt(String timeValue){
        int intTimeValue = 0;

        int mInt = Integer.parseInt(timeValue.substring(0,2)) * 60;
        int sint = Integer.parseInt(timeValue.substring(3));

        intTimeValue = mInt + sint;

        return intTimeValue;
    }

    public String intToString(int intTimeValue){
        String timeValue = "00:00";

        String mString = "00";
        String sString = "00";

        if(intTimeValue != 0){
            mString = String.valueOf( DialogMaker.twoStrForm(intTimeValue / 60) );
            sString = String.valueOf( DialogMaker.twoStrForm(intTimeValue % 60) );
        }

        timeValue = mString + ":" + sString;

        return timeValue;
    }

}
