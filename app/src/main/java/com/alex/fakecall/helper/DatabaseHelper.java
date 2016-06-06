package com.alex.fakecall.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper mInstance;

    private static final String DB_NAME = "FakeCall";
    private static final int DB_VER = 1;

    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    public static synchronized DatabaseHelper getInstance(Context c){
        if(mInstance == null){
            mInstance = new DatabaseHelper(c);
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
