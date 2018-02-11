package com.kw2.kw2.sit;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.TypefaceProvider;

import java.util.ArrayList;

public class ActivityMain extends Activity implements View.OnClickListener {

    public static Activity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TypefaceProvider.registerDefaultIconSets();
        setContentView(R.layout.activity_main);

        mainActivity = ActivityMain.this;

        BootstrapButton listBtn = (BootstrapButton) findViewById(R.id.main_listBtn);
        BootstrapButton manualBtn = (BootstrapButton) findViewById(R.id.main_manualBtn);
        BootstrapButton languageBtn = (BootstrapButton) findViewById(R.id.main_languageBtn);
        BootstrapButton exitBtn = (BootstrapButton) findViewById(R.id.main_exitBtn);

        listBtn.setOnClickListener(this);
        manualBtn.setOnClickListener(this);
        languageBtn.setOnClickListener(this);
        exitBtn.setOnClickListener(this);

    }



    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.main_listBtn:
                // 시간 목록
                intent = new Intent(getApplicationContext(), ActivityListView.class);
                startActivity(intent);
                break;
            case R.id.main_manualBtn:
                // 버튼 설명
                intent = new Intent(getApplicationContext(), ActivityManual.class);
                startActivity(intent);
                break;
            case R.id.main_languageBtn:
                // 언어 변경
                intent = new Intent(getApplicationContext(), ActivityLanguage.class);
                startActivity(intent);
                break;
            case R.id.main_exitBtn:
                // 종료 하기
                break;
        }
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

