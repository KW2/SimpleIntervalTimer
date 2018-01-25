package com.kw2.kw2.sit;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by SAMSUNG on 2018-01-21.
 */

public class ActivityTimer extends Activity {

    private boolean isPaused = false;

    private boolean isCanceled = false;

    private int nowSet = 1;

    private long timeRemaining = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        final DBHelper helper = new DBHelper(ActivityTimer.this);

        final int id = getIntent().getExtras().getInt("id");
        ListViewItem timer = helper.getItem(id);

        final TextView timerName = (TextView) findViewById(R.id.timer_title);
        final TextView timeValueText = (TextView) findViewById(R.id.timer_timeValue);
        final TextView setValue = (TextView) findViewById(R.id.timer_setValue);
        TextView currentState = (TextView) findViewById(R.id.timer_currentState);
        TextView nowText = (TextView) findViewById(R.id.timer_nowText);

        final Button updateBtn = (Button) findViewById(R.id.timer_updateBtn);
        final Button spBtn = (Button) findViewById(R.id.timer_spBtn);
        final Button stopBtn = (Button) findViewById(R.id.timer_stopBtn);


        timerName.setText(timer.getTimeName());
        timeValueText.setText(timer.getTimeValue());
        setValue.setText(nowSet + " / " + timer.getSetNum());

        stopBtn.setEnabled(false);
        spBtn.setTag(0, "start");

        // 시작/일시정지/재시작 버튼 클릭
        spBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getTag(0).equals("start")){
                    // 시작 상태
                    /*isPaused = false;
                    isCanceled = false;

                    spBtn.setTag(0, "pause");
                    updateBtn.setEnabled(false);

                    stopBtn.setEnabled(true);

                    CountDownTimer timer;
                    long millisInFuture = 30 * 1000; //30 seconds
                    long countDownInterval = 1000; //1 second


                    //Initialize a new CountDownTimer instance
                    timer = new CountDownTimer(millisInFuture,countDownInterval){
                        public void onTick(long millisUntilFinished){
                            //do something in every tick
                            if(isPaused || isCanceled)
                            {
                                //If the user request to cancel or paused the
                                //CountDownTimer we will cancel the current instance
                                cancel();
                            }
                            else {
                                //Display the remaining seconds to app interface
                                //1 second = 1000 milliseconds
                                tView.setText("" + millisUntilFinished / 1000);
                                //Put count down timer remaining time in a variable
                                timeRemaining = millisUntilFinished;
                            }
                        }
                        public void onFinish(){
                            //Do something when count down finished
                            tView.setText("Done");

                            //Enable the start button
                            btnStart.setEnabled(true);
                            //Disable the pause, resume and cancel button
                            btnPause.setEnabled(false);
                            btnResume.setEnabled(false);
                            btnCancel.setEnabled(false);
                        }
                    }.start();*/

                }else if(view.getTag(0).equals("pause")){
                    // 일시정지 상태
                }else if(view.getTag(0).equals("resume")){
                    // 재시작 상태
                }
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

                        timerName.setText(item.getTimeName());
                        timeValueText.setText(item.getTimeValue());
                        setValue.setText(nowSet + " / " + item.getSetNum());
                    }
                });

                cstDialog.show();
            }
        });
    }
}
