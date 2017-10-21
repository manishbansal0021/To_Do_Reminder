package com.example.hp_pc.to_do_reminder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by HP-PC on 13-07-2017.
 */

public class MySqliteOpenHelper extends SQLiteOpenHelper {


    //Context context;
    public MySqliteOpenHelper(Context context){
        super(context,"todo.db",null,5);
        //this.context=context;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d("1234","onCreate: database");

        // Toast.makeText(context, "created", Toast.LENGTH_SHORT).show();
        ToDoTable.createTable(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {


        Log.d("1234","OnUpgrade:database");
        //Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show();
        ToDoTable.updateTAble(sqLiteDatabase);
    }
}
