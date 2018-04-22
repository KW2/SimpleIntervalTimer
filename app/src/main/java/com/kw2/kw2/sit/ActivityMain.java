package com.kw2.kw2.sit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.fsn.cauly.CaulyAdInfo;
import com.fsn.cauly.CaulyAdInfoBuilder;
import com.fsn.cauly.CaulyCloseAd;
import com.fsn.cauly.CaulyCloseAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class ActivityMain extends Activity implements View.OnClickListener {
    private CaulyCloseAd closeAd;
    public static Activity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TypefaceProvider.registerDefaultIconSets();
        setContentView(R.layout.activity_main);

        initClose();

        mainActivity = ActivityMain.this;

        BootstrapButton listBtn = (BootstrapButton) findViewById(R.id.main_listBtn);
        BootstrapButton manualBtn = (BootstrapButton) findViewById(R.id.main_manualBtn);
        BootstrapButton languageBtn = (BootstrapButton) findViewById(R.id.main_languageBtn);
        BootstrapButton exitBtn = (BootstrapButton) findViewById(R.id.main_exitBtn);

        listBtn.setOnClickListener(this);
        manualBtn.setOnClickListener(this);
        languageBtn.setOnClickListener(this);
        exitBtn.setOnClickListener(this);

        AdView mAdView = (AdView) findViewById(R.id.main_ad);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        mAdView.loadAd(adRequest);

    }

    private void initClose(){
        CaulyAdInfo adInfo = new CaulyAdInfoBuilder("tT5SmYEn").build();       // CaulyAdInfo 생성, "CAULY"에 발급 ID 입력
        closeAd =new CaulyCloseAd();                                        // CaulyCloseAd 생성
        closeAd.setAdInfo(adInfo);                                         // CaulyAdView에 AdInfo 적용
        closeAd.setButtonText(getResources().getString(R.string.ad_no), getResources().getString(R.string.ad_yes));                             // 버튼 텍스트 사용자 지정
        closeAd.setDescriptionText(getResources().getString(R.string.ad_exit));                       // 질문 텍스트 사용자 지정
        // 종료 광고 리스너 작성
        closeAd.setCloseAdListener(new CaulyCloseAdListener() {
            // 종료 광고 수신 시
            @Override
            public void onReceiveCloseAd(CaulyCloseAd caulyCloseAd, boolean b) {}

            // 종료 광고가 보여질 시
            @Override
            public void onShowedCloseAd(CaulyCloseAd caulyCloseAd, boolean b) {}

            // 종료 광고 수신 실패 시
            @Override
            public void onFailedToReceiveCloseAd(CaulyCloseAd caulyCloseAd, int i, String s) {}

            // 종료 광고 왼쪽 버튼 클릭 시
            @Override
            public void onLeftClicked(CaulyCloseAd caulyCloseAd) {}

            // 종료 광고 오른쪽 버튼 클릭 시
            @Override
            public void onRightClicked(CaulyCloseAd caulyCloseAd) { finish(); }

            // 광고 클릭으로 앱을 벗어 날 시
            @Override
            public void onLeaveCloseAd(CaulyCloseAd caulyCloseAd) {}
        });
    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.main_listBtn:
                // 시간 목록
                intent = new Intent(ActivityMain.this, ActivityListView.class);
                startActivity(intent);
                break;
            case R.id.main_manualBtn:
                // 버튼 설명
                intent = new Intent(ActivityMain.this, ActivityManual.class);
                startActivity(intent);
                break;
            case R.id.main_languageBtn:
                // 언어 변경
                intent = new Intent(ActivityMain.this, ActivityLanguage.class);
                startActivity(intent);
                break;
            case R.id.main_exitBtn:
                // 종료 하기
                // 앱을 처음 설치하여 실행할 때, 필요한 리소스를 다운받았는지 여부.
                if (closeAd.isModuleLoaded()) {
                    // 종료 광고 띄움
                    closeAd.show(this);
                } else {
                    // 광고에 필요한 리소스를 한번만  다운받는데 실패했을 때 앱의 종료팝업 구현
                    showDefaultClosePopup();
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(closeAd!=null) closeAd.resume(this);  // 종료 광고 구현 시 반드시!! 호출
    }

    // Back Key가 눌러졌을 때, CloseAd 호출
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {  // Back 키이면
            // 앱을 처음 설치하여 실행할 때, 필요한 리소스를 다운받았는지 여부.
            if (closeAd.isModuleLoaded()) {
                // 종료 광고 띄움
                closeAd.show(this);
            } else {
                // 광고에 필요한 리소스를 한번만  다운받는데 실패했을 때 앱의 종료팝업 구현
                showDefaultClosePopup();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // 기본 종료 팝업
    private void showDefaultClosePopup(){
        new AlertDialog.Builder(this).setTitle("").setMessage(getResources().getString(R.string.ad_exit))
                .setPositiveButton(getResources().getString(R.string.ad_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.ad_no),null)
                .show();
    }
}

class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SITDB.db";
    private static final int DATABASE_VERSION = 2;
    SQLiteDatabase myDb = this.getWritableDatabase();

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE sit ( Id INTEGER PRIMARY KEY" + " AUTOINCREMENT, timeName TEXT, setNum INTEGER, workTime TEXT, restTime TEXT );");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS sit");
        onCreate(db);
    }

    public void insertTime(String timeName, int setNum, String workTime, String restTime){
        myDb.execSQL("INSERT INTO sit VALUES (null, '" + timeName + "', '" + setNum + "', '" + workTime + "', '" + restTime + "');");
    }

    public void deleteTime(int id){
        myDb.delete("sit", "id = ? ", new String[]{Integer.toString(id)});
    }

    public void updateTime(int id, String timeName, int setNum, String workTime, String restTime){
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("timeName", timeName);
        contentValues.put("setNum", setNum);
        contentValues.put("workTime", workTime);
        contentValues.put("restTime", restTime);
        myDb.update("sit", contentValues, "id = ? ", new String[]{Integer.toString(id)});
    }

    // 마지막 입력 아이디값 검색
    public int getInsertId() {
        Cursor cursor = myDb.rawQuery("select * from sit", null);
        cursor.moveToLast();
        return cursor.getInt(0);
    }

    // set값 검색
    public ListViewItem getItem(int id){
        ListViewItem item = new ListViewItem();
        Cursor cursor = myDb.rawQuery("select * from sit where id=" + id, null);
        while (cursor.moveToNext()) {
            item.setId(id);
            item.setTimeName(cursor.getString(1));
            item.setSetNum(cursor.getInt(2));
            item.setWorkTime((cursor.getString(3)));
            item.setRestTime((cursor.getString(4)));
        }
        return item;
    }

    // items 세팅
    public ArrayList<ListViewItem> getItems(){
        ListViewItem item;
        ArrayList<ListViewItem> list = new ArrayList<ListViewItem>();

        Cursor cursor = myDb.rawQuery("SELECT * FROM sit", null);
        while (cursor.moveToNext()) {
            item = new ListViewItem();
            item.setId(cursor.getInt(0));
            item.setTimeName(cursor.getString(1));
            item.setSetNum(cursor.getInt(2));
            item.setWorkTime(cursor.getString(3));
            item.setRestTime(cursor.getString(4));
            list.add(item);
        }

        return list;
    }
}

