package com.example.hp_pc.to_do_reminder;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by HP-PC on 13-07-2017.
 */

public class ToDoTable {
    public static final String TABLE_NAME="todo_tb1";
    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String DATE = "date";
    public static final String TIME = "time";

    public static final String TABLE_CREATE_QUERY="CREATE TABLE `todo_tb1` (\n" +
            "\t`id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`title`\tTEXT,\n" +
            "\t`date`\tTEXT,\n" +
            "\t`time`\tTEXT\n" +
            ");";
    public static void createTable(SQLiteDatabase db)
    {
        db.execSQL(TABLE_CREATE_QUERY);
        Log.d("1234","createTable();");
    }
    public static void updateTAble(SQLiteDatabase db)
    {
        String updateQuery="drop table if exists "+TABLE_NAME;
        db.execSQL(updateQuery);
        Log.d("1234","updateTable");
        createTable(db);
    }

    public static long insert(SQLiteDatabase db, ContentValues cv)
    {
        return db.insert(TABLE_NAME,null,cv);
    }
    public static Cursor select(SQLiteDatabase db, String selection)
    {

        return db.query(TABLE_NAME,null,selection,null,null,null,null,null);
    }
    public static int delete(SQLiteDatabase db,String selection)
    {
        return db.delete(TABLE_NAME,selection,null);
    }
    public static int update(SQLiteDatabase db,ContentValues cv,String selection)
    {
        return db.update(TABLE_NAME,cv,selection,null);
    }
}
