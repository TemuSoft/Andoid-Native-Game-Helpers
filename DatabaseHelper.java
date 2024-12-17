package com.c0cktai1.dr55ms;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "database";
    private static final int DATABASE_VERSION = 1;

    // Table name and columns
    public static final String TABLE_MAIN = "alcohol_detail";
    public static final String TABLE_ARTICLE = "article";
    public static final String TABLE_INGREDIENT = "ingredients";
    public static final String COLUMN_ID = "_id";
    public static final String TYPE = "type";
    public static final String TITLE = "title";
    public static final String DIS = "disc";
    public static final String STATUS = "time";
    public static final String IMAGE = "image";
    public static final String OWNER = "owner";
    public static final String ING_ID = "_id";
    public static final String TYPE_ID = "type";
    public static final String ING_DIS = "name";
    public static final String TYPE_ALCOHOL = "Alcohol";
    public static final String TYPE_NON_ALCOHOL = "Non-Alcohol";


    // Create table SQL statement
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_MAIN + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    TITLE + " TEXT," +
                    TYPE + " TEXT," +
                    DIS + " TEXT," +
                    STATUS + " TEXT,"+
                    IMAGE + " TEXT,"+
                    OWNER + " TEXT)";

    private static final String SQL_CREATE_TABLE0 =
            "CREATE TABLE " + TABLE_ARTICLE + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    TITLE + " TEXT," +
                    DIS + " TEXT," +
                    IMAGE + " TEXT,"+
                    OWNER + " TEXT)";

    private static final String SQL_CREATE_TABLE1 =
            "CREATE TABLE " + TABLE_INGREDIENT + " (" +
                    ING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    TYPE_ID + " TEXT," +
                    ING_DIS + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
        db.execSQL(SQL_CREATE_TABLE0);
        db.execSQL(SQL_CREATE_TABLE1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed, this will delete all data
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MAIN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENT);

        // Recreate table
        onCreate(db);
    }
}
