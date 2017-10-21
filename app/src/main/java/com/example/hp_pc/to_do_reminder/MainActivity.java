package com.example.hp_pc.to_do_reminder;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private Button datebtn,timebtn,addbtn;
    private ListView listView;
    private ArrayList<ToDoPojo> arrayList=new ArrayList<>();
    private ToDoAdapter adapter;
    private String dateSelectedByUser="",timeSelectedByUser="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        methodListener();

        adapter=new ToDoAdapter(this,R.layout.list_item_view,arrayList);
        listView.setAdapter(adapter);
        fetchValuesFromDatabase();
    }

    void fetchValuesFromDatabase() {
        arrayList.clear();
        MySqliteOpenHelper mySqliteOpenHelper= new MySqliteOpenHelper(this);
        SQLiteDatabase db=mySqliteOpenHelper.getReadableDatabase();

        Cursor cursor=ToDoTable.select(db,null);

        if(cursor!=null)
        {
            while (cursor.moveToNext())
            {
                int id=cursor.getInt(0);
                String title=cursor.getString(1);
                String date=cursor.getString(2);
                String time=cursor.getString(3);

                ToDoPojo toDoPojo=new ToDoPojo();
                toDoPojo.setId(""+id);
                toDoPojo.setTitle(title);
                toDoPojo.setDate(date);
                toDoPojo.setTime(time);

                arrayList.add(toDoPojo);
            }
            adapter.notifyDataSetChanged();
        }
        cursor.close();
        db.close();
    }

    private void init() {
        addbtn= (Button) findViewById(R.id.addbtn);
        editText= (EditText) findViewById(R.id.editText);
        datebtn= (Button) findViewById(R.id.datebtn);
        timebtn= (Button) findViewById(R.id.timebtn);
        listView= (ListView) findViewById(R.id.listView);
    }

    private void methodListener() {

        datebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate();
            }
        });

        timebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTime();
            }
        });

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });


    }

    private void add() {
        String enteredText = editText.getText().toString();
        if (enteredText.equals(""))
            editText.setError("Required");
        else if(datebtn.getText().toString().equals("DATE"))
            Toast.makeText(this, "Enter Date", Toast.LENGTH_SHORT).show();
        else if(timebtn.getText().toString().equals("TIME"))
            Toast.makeText(this, "Enter Time", Toast.LENGTH_SHORT).show();
        else {

            Calendar calender = Calendar.getInstance();
            int year = calender.get(Calendar.YEAR);
            int month = calender.get(Calendar.MONTH);
            int day = calender.get(Calendar.DAY_OF_MONTH);
            int hour = calender.get(Calendar.HOUR);
            int min = calender.get(Calendar.MINUTE);


            String req = dateSelectedByUser;
            String[] arr = req.split("-", 4);

            int y = year, m = month, d = day;
            try {
                y = Integer.parseInt(arr[2]);
                m = Integer.parseInt(arr[1]);
                d = Integer.parseInt(arr[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (y < year) {
                Toast.makeText(this, "Backdate is not allowed", Toast.LENGTH_SHORT).show();
            } else if (y == year) {
                if (m < month)
                    Toast.makeText(this, "Backdate is not allowed", Toast.LENGTH_SHORT).show();
                else if (m == month) {
                    if (d < day)
                        Toast.makeText(this, "Backdate is not allowed", Toast.LENGTH_SHORT).show();
                    else {


                        MySqliteOpenHelper mySqliteOpenHelper =
                                new MySqliteOpenHelper(this);
                        SQLiteDatabase db = mySqliteOpenHelper.getWritableDatabase();

                        ContentValues cv = new ContentValues();
                        cv.put(ToDoTable.TITLE, enteredText);
                        cv.put(ToDoTable.DATE, dateSelectedByUser);// System time is in table
                        cv.put(ToDoTable.TIME, timeSelectedByUser);

                        long l = ToDoTable.insert(db,cv);
                        if (l > 0)
                        {
                            Toast.makeText(this, "inserted", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(this, "not inserted", Toast.LENGTH_SHORT).show();
                        }
                        db.close();

                        fetchValuesFromDatabase();
                        setAlarm();
                        dateSelectedByUser="";
                        timeSelectedByUser="";
                        // editText.setText("");
                        datebtn.setText("DATE");
                        timebtn.setText("TIME");

                    }
                } else {


                    MySqliteOpenHelper mySqliteOpenHelper =
                            new MySqliteOpenHelper(this);
                    SQLiteDatabase db = mySqliteOpenHelper.getWritableDatabase();

                    ContentValues cv = new ContentValues();
                    cv.put(ToDoTable.TITLE, enteredText);
                    cv.put(ToDoTable.DATE, dateSelectedByUser);
                    cv.put(ToDoTable.TIME, timeSelectedByUser);

                    long l = ToDoTable.insert(db,cv);
                    if (l > 0)
                    {
                        Toast.makeText(this, "inserted", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(this, "not inserted", Toast.LENGTH_SHORT).show();
                    }
                    db.close();

                    fetchValuesFromDatabase();
                    setAlarm();
                    dateSelectedByUser="";
                    timeSelectedByUser="";
                    //editText.setText("");
                    datebtn.setText("DATE");
                    timebtn.setText("TIME");

                }

            } else {


                MySqliteOpenHelper mySqliteOpenHelper =
                        new MySqliteOpenHelper(this);
                SQLiteDatabase db = mySqliteOpenHelper.getWritableDatabase();

                ContentValues cv = new ContentValues();
                cv.put(ToDoTable.TITLE, enteredText);
                cv.put(ToDoTable.DATE, dateSelectedByUser);
                cv.put(ToDoTable.TIME, timeSelectedByUser);

                long l = ToDoTable.insert(db,cv);
                if (l > 0)
                {
                    Toast.makeText(this, "inserted", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(this, "not inserted", Toast.LENGTH_SHORT).show();
                }
                db.close();

                fetchValuesFromDatabase();

                setAlarm();
                dateSelectedByUser="";
                timeSelectedByUser="";
                //editText.setText("");
                datebtn.setText("DATE");
                timebtn.setText("TIME");

            }
        }
    }

     void setAlarm() {


        Intent i=new Intent();
        i.setAction("r.a.b");
        i.putExtra("title",editText.getText().toString());
        editText.setText("");

        Calendar cal=Calendar.getInstance();

        Calendar c=Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR);
        int min = cal.get(Calendar.MINUTE);


        String req = dateSelectedByUser;
        String[] arr = req.split("-", 4);

        String time=timeSelectedByUser;
        String[] tr=time.split(":",2);

        int y = year, m = month, d = day,h=hour,mi=min;
        try {
            y = Integer.parseInt(arr[2]);
            m = Integer.parseInt(arr[1]);
            d = Integer.parseInt(arr[0]);
            h=Integer.parseInt(tr[0]);
            mi=Integer.parseInt(tr[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        cal.set(y,m,d,h,mi);

       //  datebtn.setText("DATE");
         //timebtn.setText("TIME");
        long k=cal.getTimeInMillis()-c.getTimeInMillis();
        Toast.makeText(this, ""+k, Toast.LENGTH_SHORT).show();
        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,0,i,0);

        AlarmManager manager= (AlarmManager) getSystemService(ALARM_SERVICE);
        manager.set(AlarmManager.RTC,System.currentTimeMillis()+k,pendingIntent);

    }


    private void setTime() {
        Calendar cal=Calendar.getInstance();
        TimePickerDialog timePickerDialog=new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int Minute) {

                timebtn.setText(hour+":"+Minute);
                timeSelectedByUser=hour+":"+Minute;


            }
        },cal.get(Calendar.HOUR),cal.get(Calendar.MINUTE),false);
        timePickerDialog.show();
    }

    private void setDate() {

        Calendar cal=Calendar.getInstance();
        DatePickerDialog datePickerDialog =new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                datebtn.setText(day+"-"+(month+1)+"-"+year);
                dateSelectedByUser=day+"-"+month+"-"+year;

            }
        },cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }
}
