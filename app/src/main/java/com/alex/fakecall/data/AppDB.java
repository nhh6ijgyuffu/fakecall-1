package com.alex.fakecall.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.alex.fakecall.FakeCallApp;
import com.alex.fakecall.models.Call;

import java.util.ArrayList;
import java.util.List;

public class AppDB extends SQLiteOpenHelper {
    private static AppDB mInstance;
    private static final String DB_NAME = "FakeCall";
    private static final int DB_VER = 1;

    private static final String TABLE_CALL = "call";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_NUMBER = "number";
    private static final String KEY_TIME = "time";
    private static final String KEY_IS_ALARMED = "is_alarmed";
    private static final String KEY_CREATION_TIME = "creation_time";

    private static final String CREATE_TABLE_CALL = "CREATE TABLE " + TABLE_CALL + "( "
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT,"
            + KEY_NUMBER + " TEXT,"
            + KEY_TIME + " INTEGER,"
            + KEY_IS_ALARMED + " INTEGER,"
            + KEY_CREATION_TIME + " INTEGER);";

    private AppDB() {
        super(FakeCallApp.getInstance(), DB_NAME, null, DB_VER);
    }

    public static synchronized AppDB getInstance() {
        if (mInstance == null) {
            mInstance = new AppDB();
        }
        return mInstance;
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


    public List<Call> getAllCalls() {
        SQLiteDatabase db = getReadableDatabase();
        List<Call> list = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_CALL + " ORDER BY " + KEY_TIME + " DESC;";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Call call = new Call();
                call.setId(cursor.getLong(0));
                call.setName(cursor.getString(1));
                call.setNumber(cursor.getString(2));
                call.setTime(cursor.getLong(3));
                call.setAlarmed(cursor.getInt(4) == 1);
                list.add(call);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Call> getAllPendingCalls() {
        SQLiteDatabase db = getReadableDatabase();
        List<Call> list = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_CALL + " WHERE " + KEY_IS_ALARMED + " = '0' "
                + " ORDER BY " + KEY_TIME + " DESC;";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Call call = new Call();
                call.setId(cursor.getLong(0));
                call.setName(cursor.getString(1));
                call.setNumber(cursor.getString(2));
                call.setTime(cursor.getLong(3));
                call.setAlarmed(cursor.getInt(4) == 1);
                list.add(call);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public long addCall(Call call) {
        SQLiteDatabase db = getWritableDatabase();
        long id;

        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME, call.getName());
        cv.put(KEY_NUMBER, call.getNumber());
        cv.put(KEY_IS_ALARMED, call.isAlarmed() ? 1 : 0);
        cv.put(KEY_TIME, call.getTime());
        cv.put(KEY_CREATION_TIME, System.currentTimeMillis());

        id = db.insert(TABLE_CALL, null, cv);
        db.close();

        return id;
    }

    public int updateCall(Call call) {
        SQLiteDatabase db = getWritableDatabase();
        int a;

        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME, call.getName());
        cv.put(KEY_NUMBER, call.getNumber());
        cv.put(KEY_IS_ALARMED, call.isAlarmed() ? 1 : 0);
        cv.put(KEY_TIME, call.getTime());

        a = db.update(TABLE_CALL, cv, KEY_ID + " = ?",
                new String[]{String.valueOf(call.getId())});
        db.close();
        return a;
    }

    public void deleteCall(long id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_CALL, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }
}
