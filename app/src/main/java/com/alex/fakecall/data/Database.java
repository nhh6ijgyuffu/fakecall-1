package com.alex.fakecall.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.alex.fakecall.models.Call;

import java.util.ArrayList;
import java.util.List;


public class Database extends SQLiteOpenHelper {
    private static final String DB_NAME = "FakeCall";

    private static final int DB_VERSION = 1;

    private static final String TABLE_CALL = "call";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_NUMBER = "number";
    private static final String KEY_TIME = "time";

    private static final String CREATE_TABLE_CALL = "CREATE TABLE " + TABLE_CALL + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT,"
            + KEY_NUMBER + " TEXT,"
            + KEY_TIME + " INTEGER" + ")";

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CALL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CALL);
        onCreate(db);
    }

    public void addNewCall(Call call){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, call.name);
        values.put(KEY_NUMBER, call.number);
        values.put(KEY_TIME, call.alarm_time);

        db.insert(TABLE_CALL, null, values);
        db.close();
    }

    public List<Call> getAllCalls() {
        List<Call> callList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_CALL;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Call call = new Call();
                call.name = cursor.getString(1);
                call.number = cursor.getString(2);
                call.alarm_time = cursor.getLong(3);

                callList.add(call);
            } while (cursor.moveToNext());
        }
        return callList;
    }
}
