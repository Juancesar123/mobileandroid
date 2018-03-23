package com.garuda.hcmobile.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_AUTHENTICATE = "user";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_NOPEG = "nopeg";
    public static final String COLUMN_EMAIL= "email";
    public static final String COLUMN_PHOTO= "photo";
    public static final String COLUMN_USER_POSITION_KEY= "position_key";
    public static final String COLUMN_USER_POSITION_NAME= "position_name";
    public static final String COLUMN_USER_ORG_UNIT= "org_unit";
    public static final String COLUMN_USER_UNIT_SHORT= "unit_short";
    public static final String COLUMN_USER_UNIT_STEXT= "unit_stext";
    public static final String COLUMN_USER_NAMA= "nama";

    public static final String TABLE_LOCATION = "location";
    public static final String COLUMN_PLACE = "place";
    public static final String COLUMN_ISPICKUP = "ispickup";
    public static final String COLUMN_NAME = "name";

    public static final String TABLE_MENU= "menu";
    public static final String COLUMN_MENU_CODE= "menu_code";
    public static final String COLUMN_MENU_NAME = "menu_name";
    public static final String COLUMN_MENU_ICON = "menu_icon";
    public static final String COLUMN_MENU_STATUS = "menu_status";



    private static final String DATABASE_NAME = "dmehome.db";
    private static final int DATABASE_VERSION = 2;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_AUTHENTICATE + "(" + COLUMN_ID
            + " integer primary key autoincrement, "
            + COLUMN_USERNAME + " text not null , "
            + COLUMN_PASSWORD +" text not null, "
            + COLUMN_NOPEG + " text not null , "
            + COLUMN_EMAIL+ " text not null ,"
            + COLUMN_PHOTO+ " text , "
            + COLUMN_USER_POSITION_KEY + " text , "
            + COLUMN_USER_POSITION_NAME+ " text , "
            + COLUMN_USER_ORG_UNIT + " text , "
            + COLUMN_USER_UNIT_SHORT+ " text , "
            + COLUMN_USER_UNIT_STEXT+ " text  , "
            + COLUMN_USER_NAMA+ " text  "
            + ");";

    private static final String DATABASE_CREATE2 = " create table "
            + TABLE_LOCATION + "(" + COLUMN_ID
            + " integer primary key autoincrement, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_PLACE  + " text not null, "
            + COLUMN_ISPICKUP + " int not null );" ;

    private static final String DATABASE_CREATE3 = " create table "
            + TABLE_MENU + "(" + COLUMN_ID
            + " integer primary key autoincrement, "
            + COLUMN_MENU_CODE + " INT not null, "
            + COLUMN_MENU_NAME  + " text not null, "
            + COLUMN_MENU_ICON + " int not null, "
            + COLUMN_MENU_STATUS + " int not null );" ;

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        //Log.i("db","oncreate user db");
        database.execSQL(DATABASE_CREATE);
        //database.execSQL(DATABASE_CREATE2);
        database.execSQL(DATABASE_CREATE3);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        */
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUTHENTICATE);
        onCreate(db);
    }

}
