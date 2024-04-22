package com.example.pr102;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "my_database";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "my_kitties";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_BREED = "breed";
    public static final String COLUMN_COLOR = "color";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_AGE = "age";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_BREED + " TEXT, " +
                COLUMN_AGE + " INTEGER, " +
                COLUMN_COLOR + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
