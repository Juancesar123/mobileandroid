package com.garuda.hcmobile.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import com.garuda.hcmobile.BuildConfig;
import com.garuda.hcmobile.model.Menu;
import com.garuda.hcmobile.util.MySQLiteHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MBP on 1/9/15.
 */
public class MenuDAO {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_MENU_CODE,
            MySQLiteHelper.COLUMN_MENU_NAME,
            MySQLiteHelper.COLUMN_MENU_ICON,
            MySQLiteHelper.COLUMN_MENU_STATUS};

    public MenuDAO(Context context) {
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

    public Menu insertMenu(int menuCode, String menuName, int menuIcon,int menuStatus) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_MENU_CODE, menuCode);
        values.put(MySQLiteHelper.COLUMN_MENU_NAME, menuName);
        values.put(MySQLiteHelper.COLUMN_MENU_ICON,menuIcon );
        values.put(MySQLiteHelper.COLUMN_MENU_STATUS,menuStatus);
        long insertId = database.insert(MySQLiteHelper.TABLE_MENU, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_MENU,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Menu menu= cursorToMenu(cursor);
        cursor.close();
        return menu;
    }

    public void deleteMenu(Menu menu) {
        long id = menu.getId();
        System.out.println("menu deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_MENU, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public void clearMenus(){
        List<Menu> menus = getMenus();
        for(int i=0;i<menus.size();i++){
            deleteMenu(menus.get(i));
        }
    }


    public List<Menu> getMenus() {
        List<Menu> menus= new ArrayList<Menu>();
        //Log.i("menu","database" + String.valueOf(database == null));
        Cursor cursor = database.query(MySQLiteHelper.TABLE_MENU,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Menu menu = cursorToMenu(cursor);
            menus.add(menu);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return menus;
        //if(menus.size() > 0){
//            return menus.get(0);
//        }
//        return null;
    }

    private Menu cursorToMenu(Cursor cursor) {
        Menu menu = new Menu();
        menu.setId(cursor.getLong(0));
        menu.setCode(cursor.getInt(1));
        menu.setName(cursor.getString(2));
        menu.setIcon(cursor.getInt(3));
        menu.setStatus(cursor.getInt(4));
        return menu;
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
