package com.kw2.kw2.sit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityMain extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button listBtn = (Button) findViewById(R.id.main_listBtn);
        Button manualBtn = (Button) findViewById(R.id.main_manualBtn);
        Button languageBtn = (Button) findViewById(R.id.main_languageBtn);

        listBtn.setOnClickListener(this);
        manualBtn.setOnClickListener(this);
        languageBtn.setOnClickListener(this);
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

    // 마지막 입력 아이디값 검색
    public int getInsertId() {
        Cursor cursor = myDb.rawQuery("select * from sit", null);
        cursor.moveToLast();
        return cursor.getInt(0);
    }
}

