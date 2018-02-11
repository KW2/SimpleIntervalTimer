package com.kw2.kw2.sit;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.beardedhen.androidbootstrap.font.MaterialIcons;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by SAMSUNG on 2018-01-21.
 */

public class ActivityTimer extends Activity {

    private Notification.Builder builder;
    private NotificationManager notificationManager;
    private int notification_id;
    private RemoteViews remoteViews;
    private Context context;


    boolean bRunning = true;
    String runCheck = "ready";

    boolean isSound = true;

    boolean isNotification = false;

    private int nowSet = 1;

    private int nCounter = 0;

    private int setNum = 0;

    private int readyTimes = 5;

    private int workTimes = 0;
    private int wkValue = 0;

    private int restTimes = 0;
    private int rtValue = 0;

    SoundPool sp;
    int soundStart;
    int soundBeep;
    int soundFinish;

    TimerTask mTimerTask;
    final Handler handler = new Handler();
    Timer t = new Timer();


    TextView timerName, timeValueText, setValue, currentState, nowText;
    BootstrapButton updateBtn, spBtn, stopBtn, soundBtn;


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

        updateBtn = (BootstrapButton) findViewById(R.id.timer_updateBtn);
        spBtn = (BootstrapButton) findViewById(R.id.timer_spBtn);
        stopBtn = (BootstrapButton) findViewById(R.id.timer_stopBtn);
        soundBtn = (BootstrapButton) findViewById(R.id.timer_soundBtn);


        timerName.setText(timer.getTimeName());
        timeValueText.setText(timer.getTimeValue());
        setValue.setText(nowSet + " / " + setNum);

        spBtn.setTag("start");
        soundBtn.setTag("on");

        sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        soundStart = sp.load(this, R.raw.start, 1);
        soundBeep = sp.load(this, R.raw.beep, 1);
        soundFinish = sp.load(this, R.raw.finish, 1);

        // 시작/일시정지/재시작 버튼 클릭
        spBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateBtn.setEnabled(false);
                if (view.getTag().equals("start")) {
                    // 시작 상태
                    if (mTimerTask == null) {
                        bRunning = true;
                        doTimerTask();
                        spBtn.setMaterialIcon(MaterialIcons.MD_PAUSE);
                        spBtn.setBootstrapBrand(DefaultBootstrapBrand.SUCCESS);
                        spBtn.setTag("pause");
                    }

                } else if (view.getTag().equals("pause")) {
                    // 일시정지 상태
                    doTimerPause();
                    spBtn.setMaterialIcon(MaterialIcons.MD_PLAY_ARROW);
                    spBtn.setBootstrapBrand(DefaultBootstrapBrand.PRIMARY);
                    spBtn.setTag("resume");

                } else if (view.getTag().equals("resume")) {
                    // 재시작 상태
                    doTimerPause();
                    spBtn.setMaterialIcon(MaterialIcons.MD_PAUSE);
                    spBtn.setBootstrapBrand(DefaultBootstrapBrand.SUCCESS);
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

                        setNum = item.getSetNum();

                        workTimes = stringToInt(item.getWorkTime());
                        wkValue = workTimes;

                        restTimes = stringToInt(item.getRestTime());
                        rtValue = restTimes;



                    }
                });

                cstDialog.show();
            }
        });

        soundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (soundBtn.getTag().equals("on")) {
                    isSound = false;
                    soundBtn.setTag("off");
                    soundBtn.setMaterialIcon(MaterialIcons.MD_VOLUME_OFF);
                    soundBtn.setBootstrapBrand(DefaultBootstrapBrand.WARNING);
                } else {
                    isSound = true;
                    soundBtn.setTag("on");
                    soundBtn.setMaterialIcon(MaterialIcons.MD_VOLUME_UP);
                    soundBtn.setBootstrapBrand(DefaultBootstrapBrand.INFO);
                }
            }
        });
    }


    public void doTimerTask() {

        mTimerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        if (!bRunning) {
                            return;
                        }

                        if (nCounter <= 4) {

                            mySoundPlay(readyTimes, soundBeep, 0, 2);

                            nowText.setText("00:" + DialogMaker.twoStrForm(readyTimes--));

                        } else if (nCounter <= 4 + workTimes) {
                            if (nCounter == 5) {
                                mySoundPlay(soundStart, 0, 1);
                                runCheck = "work";
                                if (isNotification) {
                                    remoteViews.setImageViewResource(R.id.notification_icon, R.drawable.work);
                                    builder.setSmallIcon(R.drawable.work);
                                }
                            }

                            mySoundPlay(wkValue, soundBeep, 0, 2);

                            currentState.setText(R.string.workTime);
                            nowText.setText(intToString(wkValue));
                            wkValue--;

                        } else if (nCounter <= 4 + workTimes + restTimes) {
                            if ( nCounter == 4 + workTimes + 1 && nowSet == setNum  ) {
                                mySoundPlay(soundFinish, 0, 1);
                                Toast.makeText(ActivityTimer.this, R.string.success, Toast.LENGTH_SHORT).show();
                                if (isNotification) {
                                    remoteViews.setImageViewResource(R.id.notification_icon, R.drawable.success);
                                    builder.setSmallIcon(R.drawable.success);
                                    remoteViews.setViewVisibility(R.id.notification_spBtn, View.INVISIBLE);
                                    remoteViews.setViewVisibility(R.id.notification_endBtn, View.INVISIBLE);
                                    remoteViews.setTextViewText(R.id.notification_textView, getResources().getString(R.string.success));
                                    builder.setOngoing(false);
                                    notificationManager.notify(notification_id, builder.build());
                                }
                                stopTask();
                                return;
                            } else if (nCounter == 4 + workTimes + 1) {
                                runCheck = "rest";
                                mySoundPlay(soundBeep, 1, 2);
                                if (isNotification) {
                                    remoteViews.setImageViewResource(R.id.notification_icon, R.drawable.rest);
                                    builder.setSmallIcon(R.drawable.rest);
                                }
                            }

                            mySoundPlay(rtValue, soundBeep, 0, 2);

                            currentState.setText(R.string.restTime);
                            nowText.setText(intToString(rtValue));
                            rtValue--;

                        } else if (nCounter > 4 + workTimes + restTimes) {
                            if ( nCounter == 4 + workTimes + 1 && nowSet == setNum  ) {
                                mySoundPlay(soundFinish, 0, 1);
                                Toast.makeText(ActivityTimer.this, R.string.success, Toast.LENGTH_SHORT).show();
                                if (isNotification) {
                                    remoteViews.setImageViewResource(R.id.notification_icon, R.drawable.success);
                                    builder.setSmallIcon(R.drawable.success);
                                    remoteViews.setViewVisibility(R.id.notification_spBtn, View.INVISIBLE);
                                    remoteViews.setViewVisibility(R.id.notification_endBtn, View.INVISIBLE);
                                    remoteViews.setTextViewText(R.id.notification_textView, getResources().getString(R.string.success));
                                    builder.setOngoing(false);
                                    notificationManager.notify(notification_id, builder.build());
                                }
                                stopTask();
                                return;
                            }else {
                                nowSet++;
                                wkValue = workTimes;
                                rtValue = restTimes;
                                nCounter = 5;
                                mySoundPlay(soundStart, 0, 1);
                                setValue.setText(nowSet + " / " + setNum);
                                nowText.setText(intToString(wkValue));
                                currentState.setText(R.string.workTime);
                                wkValue--;
                                runCheck = "work";
                                if (isNotification) {
                                    remoteViews.setImageViewResource(R.id.notification_icon, R.drawable.work);
                                    builder.setSmallIcon(R.drawable.work);
                                }
                            }
                        }

                        nCounter++;
                        if (isNotification) {
                            remoteViews.setTextViewText(R.id.notification_textView, setValue.getText().toString() + " - " + nowText.getText().toString());
                            notificationManager.notify(notification_id, builder.build());
                        }
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
            notificationManager = null;
            updateBtn.setEnabled(true);
            nowSet = 1;
            readyTimes = 5;
            wkValue = workTimes;
            rtValue = restTimes;
            setValue.setText(nowSet + " / " + setNum);
            nowText.setText("00:05");
            runCheck = "ready";

            nCounter = 0;
            bRunning = false;
            mTimerTask.cancel();
            mTimerTask = null;
            spBtn.setMaterialIcon(MaterialIcons.MD_PLAY_ARROW);
            spBtn.setBootstrapBrand(DefaultBootstrapBrand.PRIMARY);
            spBtn.setTag("start");


        }
    }

    public int stringToInt(String timeValue) {
        int intTimeValue = 0;

        int mInt = Integer.parseInt(timeValue.substring(0, 2)) * 60;
        int sint = Integer.parseInt(timeValue.substring(3));

        intTimeValue = mInt + sint;

        return intTimeValue;
    }

    public String intToString(int intTimeValue) {
        String timeValue = "00:00";

        String mString = "00";
        String sString = "00";

        if (intTimeValue != 0) {
            mString = String.valueOf(DialogMaker.twoStrForm(intTimeValue / 60));
            sString = String.valueOf(DialogMaker.twoStrForm(intTimeValue % 60));
        }

        timeValue = mString + ":" + sString;

        return timeValue;
    }

    public void mySoundPlay(int soundId, int option, int speed) {
        if (isSound) {
            // 준비한 soundID   볼륨 0.0(작은소리)~1.0(큰소리)  우선순위 반복회수 int -1:무한반복, 0:반복안함 재생속도
            sp.play(soundId, 1, 1, 0, option, speed);
        }
    }

    public void mySoundPlay(int value, int soundId, int option, int speed) {
        if (isSound && value <= 3) {
            sp.play(soundId, 1, 1, 0, option, speed);
        }
    }

    public void sendNotification() {
        if (notificationManager == null) {
            context = ActivityTimer.this;
            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            remoteViews = new RemoteViews(getPackageName(), R.layout.notification_custom);


            remoteViews.setTextViewText(R.id.notification_textView, setValue.getText().toString() + " - " + nowText.getText().toString());

            notification_id = (int) System.currentTimeMillis();
            Intent endBtn_intent = new Intent("endBtn_clicked");
            Intent spBtn_intent = new Intent("spBtn_clicked");


            PendingIntent endBtn_pIntent = PendingIntent.getBroadcast(context, 123, endBtn_intent, 0);
            PendingIntent spBtn_pIntent = PendingIntent.getBroadcast(context, 123, spBtn_intent, 0);
            remoteViews.setOnClickPendingIntent(R.id.notification_endBtn, endBtn_pIntent);
            remoteViews.setOnClickPendingIntent(R.id.notification_spBtn, spBtn_pIntent);

            Intent notification_intent = new Intent(context, ActivityMain.class);
            notification_intent.setAction(Intent.ACTION_MAIN);
            notification_intent.addCategory(Intent.CATEGORY_LAUNCHER);

            notification_intent.putExtra("id", getIntent().getExtras().getInt("id"));
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notification_intent, 0);

            builder = new Notification.Builder(context);
            builder.setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(true)
                    .setOngoing(true)
                    .setCustomContentView(remoteViews)
                    .setContentIntent(pendingIntent);


        }
        if (runCheck.equals("ready")) {
            remoteViews.setImageViewResource(R.id.notification_icon, R.drawable.ready);
            builder.setSmallIcon(R.drawable.ready);
        } else if (runCheck.equals("work")) {
            remoteViews.setImageViewResource(R.id.notification_icon, R.drawable.work);
            builder.setSmallIcon(R.drawable.work);
        } else if (runCheck.equals("rest")) {
            remoteViews.setImageViewResource(R.id.notification_icon, R.drawable.rest);
            builder.setSmallIcon(R.drawable.rest);
        }
        remoteViews.setTextViewText(R.id.notification_spBtn, "Ⅱ");
        if (!bRunning) {
            remoteViews.setImageViewResource(R.id.notification_icon, R.drawable.pauseicon);
            remoteViews.setTextViewText(R.id.notification_spBtn, "▶");
        }
        notificationManager.notify(notification_id, builder.build());
        isNotification = true;
    }

    // 백키 클릭시 timer 작동 중에는 홈 화면 이동, timer 대기 중에는 뒤로 이동
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK && mTimerTask != null) {
                sendNotification();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    // 홈버튼 클릭시 timer 작동 중에는 notification 생성, tiemr 대기 중에는 그냥 이동
    @Override
    protected void onUserLeaveHint() {
        if (mTimerTask != null) {
            sendNotification();
        }
        super.onUserLeaveHint();
    }

    /*public class EndButton_Listener extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(intent.getExtras().getInt("noti_id"));
            stopTask();
        }
    }*/

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("endBtn_clicked")) {
                isNotification = false;
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(notification_id);
                stopTask();
            } else if (intent.getAction().equals("spBtn_clicked")) {
                doTimerPause();
                if (bRunning) {
                    if (runCheck.equals("ready")) {
                        remoteViews.setImageViewResource(R.id.notification_icon, R.drawable.ready);
                    } else if (runCheck.equals("work")) {
                        remoteViews.setImageViewResource(R.id.notification_icon, R.drawable.work);
                    } else if (runCheck.equals("rest")) {
                        remoteViews.setImageViewResource(R.id.notification_icon, R.drawable.rest);
                    }
                    remoteViews.setTextViewText(R.id.notification_spBtn, "||");
                } else {
                    remoteViews.setImageViewResource(R.id.notification_icon, R.drawable.pauseicon);
                    remoteViews.setTextViewText(R.id.notification_spBtn, "▶");
                }
                notificationManager.notify(notification_id, builder.build());
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        isNotification = false;
        if (notificationManager != null) {
            notificationManager.cancel(notification_id);
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction("endBtn_clicked");
        filter.addAction("spBtn_clicked");
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}


