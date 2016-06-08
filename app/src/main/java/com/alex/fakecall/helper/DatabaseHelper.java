package com.alex.fakecall.helper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.alex.fakecall.App;
import com.alex.fakecall.models.Call;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper mInstance;
    private static final String DB_NAME = "FakeCall";
    private static final int DB_VER = 1;

    private static final String TABLE_CALL = "call";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_NUMBER = "number";
    private static final String KEY_TIME = "time";
    private static final String KEY_CREATION_TIME = "creation_time";

    private static final String CREATE_TABLE_CALL = "CREATE TABLE " + TABLE_CALL + "( "
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT,"
            + KEY_NUMBER + " TEXT,"
            + KEY_TIME + " INTEGER,"
            + KEY_CREATION_TIME + " INTEGER)";

    private DatabaseHelper() {
        super(App.getInstance(), DB_NAME, null, DB_VER);
    }

    public static synchronized DatabaseHelper getInstance() {
        if (mInstance == null) {
            mInstance = new DatabaseHelper();
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

    public long addCall(Call call) {
        SQLiteDatabase db = getWritableDatabase();

        Long id = call.getId();

        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME, call.getName());
        cv.put(KEY_NUMBER, call.getNumber());
        cv.put(KEY_TIME, call.getTime());

        if (id == null) {
            cv.put(KEY_CREATION_TIME, System.currentTimeMillis());
            id = db.insert(TABLE_CALL, null, cv);
        } else {
            db.update(TABLE_CALL, cv, KEY_ID + " = ?",
                    new String[]{String.valueOf(id)});
        }
        db.close();
        return id;
    }

    public Call getLatestSavedCall() {
        SQLiteDatabase db = getReadableDatabase();

        Call call = new Call();

        String query = "SELECT * FROM " + TABLE_CALL + " ORDER BY " + KEY_CREATION_TIME + " DESC LIMIT 1";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            String name = cursor.getString(1);
            String number = cursor.getString(2);
            Long time = cursor.getLong(3);

            call.setName(name);
            call.setNumber(number);
            call.setTime(time);
        }

        cursor.close();
        db.close();
        return call;
    }
}
