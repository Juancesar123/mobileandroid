package com.garuda.hcmobile.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import com.garuda.hcmobile.BuildConfig;
import com.garuda.hcmobile.model.User;
import com.garuda.hcmobile.util.MySQLiteHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MBP on 1/9/15.
 */
public class UserDAO {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,MySQLiteHelper.COLUMN_USERNAME ,
            MySQLiteHelper.COLUMN_PASSWORD,
            MySQLiteHelper.COLUMN_NOPEG,
            MySQLiteHelper.COLUMN_EMAIL,
            MySQLiteHelper.COLUMN_PHOTO,
            MySQLiteHelper.COLUMN_USER_POSITION_KEY,
            MySQLiteHelper.COLUMN_USER_POSITION_NAME,
            MySQLiteHelper.COLUMN_USER_ORG_UNIT,
            MySQLiteHelper.COLUMN_USER_UNIT_SHORT,
            MySQLiteHelper.COLUMN_USER_UNIT_STEXT,
            MySQLiteHelper.COLUMN_USER_NAMA};

    public UserDAO(Context context) {
        dbHelper = new MySQLiteHelper(context);
        open();
    }

    public void open()   {

            database = dbHelper.getWritableDatabase();
            //Log.i("db", "call db helper");

    }

    public void close() {
        dbHelper.close();
    }

    public User insertUser(String username, String password, String nopeg,String email) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_USERNAME, username);
        values.put(MySQLiteHelper.COLUMN_PASSWORD, password);
        values.put(MySQLiteHelper.COLUMN_NOPEG,nopeg );
        values.put(MySQLiteHelper.COLUMN_EMAIL,email);
        long insertId = database.insert(MySQLiteHelper.TABLE_AUTHENTICATE, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_AUTHENTICATE,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        User user = cursorToUser(cursor);
        cursor.close();
        return user;
    }

    public void deleteUser(User user) {
        long id = user.getId();
        System.out.println("user deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_AUTHENTICATE, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public void updatePassword(long id, String password){
        ContentValues data=new ContentValues();
        data.put(MySQLiteHelper.COLUMN_PASSWORD, password);
        database.update(MySQLiteHelper.TABLE_AUTHENTICATE, data, "_id=" + id, null);
    }

    public void updateUser(long id, String username){
        ContentValues data=new ContentValues();
        data.put(MySQLiteHelper.COLUMN_USERNAME, username);
        database.update(MySQLiteHelper.TABLE_AUTHENTICATE, data, "_id=" + id, null);
    }

    public void updateProfile(long id,String photo,String positionKey,String orgUnit,String positionName,String unit_short,String unit_stext,String nama){
        ContentValues data=new ContentValues();
        data.put(MySQLiteHelper.COLUMN_PHOTO, photo);
        data.put(MySQLiteHelper.COLUMN_USER_POSITION_KEY, positionKey);
        data.put(MySQLiteHelper.COLUMN_USER_ORG_UNIT, orgUnit);
        data.put(MySQLiteHelper.COLUMN_USER_POSITION_NAME, positionName);
        data.put(MySQLiteHelper.COLUMN_USER_UNIT_SHORT, unit_short);
        data.put(MySQLiteHelper.COLUMN_USER_UNIT_STEXT, unit_stext);
        data.put(MySQLiteHelper.COLUMN_USER_NAMA, nama);
        database.update(MySQLiteHelper.TABLE_AUTHENTICATE, data, "_id=" + id, null);
    }


    public User getUser() {
        List<User> users = new ArrayList<User>();
        //Log.i("userd","database" + String.valueOf(database == null));
        Cursor cursor = database.query(MySQLiteHelper.TABLE_AUTHENTICATE,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            User user = cursorToUser(cursor);
            users.add(user);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        //Log.i("userd size",users.size()+" user");
        if(users.size() > 0){
            //Log.i("userloc 0 ",users.get(0).toString());
            return users.get(0);
        }
        return null;
    }

    private User cursorToUser(Cursor cursor) {
        User user = new User();
        user.setId(cursor.getLong(0));//ID
        user.setUser(cursor.getString(1));//USERNAME
        user.setPassword(cursor.getString(2));//PASSWORD
        user.setNopeg(cursor.getString(3));
        user.setEmail(cursor.getString(4));
        user.setPhoto(cursor.getString(5));
        user.setPosition_key(cursor.getString(6));
        user.setPosition_name(cursor.getString(7));
        user.setOrg_unit(cursor.getString(8));
        user.setUnit_short(cursor.getString(9));
        user.setUnit_stext(cursor.getString(10));
        user.setNama(cursor.getString(11));
        //Log.i("getCursor",user.toString());
        return user;
    }

    public static String getAppUsername(){
        StringBuilder builder = new StringBuilder();
        builder.append("android : ").append(Build.VERSION.RELEASE);

        Field[] fields = Build.VERSION_CODES.class.getFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            int fieldValue = -1;

            try {
                fieldValue = field.getInt(new Object());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            if (fieldValue == Build.VERSION.SDK_INT) {
                builder.append("|").append(fieldName).append("|");
                builder.append("|").append(fieldValue);
            }
        }
        return BuildConfig.APPLICATION_ID+"|"+ BuildConfig.VERSION_NAME+"|"+ BuildConfig.VERSION_CODE+"|"+builder.toString();
    }



}
