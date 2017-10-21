package com.example.hp_pc.to_do_reminder;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
//import android.support.annotation.LayoutRes;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by HP-PC on 13-07-2017.
 */

public class ToDoAdapter extends ArrayAdapter {


    private Context context;
    private int layres;
    private ArrayList<ToDoPojo> arrayList;
    private LayoutInflater inflater;

    public ToDoAdapter(Context context,int resource,ArrayList<ToDoPojo> objects) {
        super(context, resource, objects);
        this.context=context;
        this.layres=resource;
        this.arrayList=objects;

        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position,View convertView, ViewGroup parent) {
        View view=inflater.inflate(layres,null);

        final EditText titleTextView=view.findViewById(R.id.titleTextView);
        final TextView circleTextView=view.findViewById(R.id.circleTextView);
        final TextView dateTextView=view.findViewById(R.id.dateTextView);
        final TextView timeTextView=view.findViewById(R.id.timeTextView);
        ImageView editbtn=view.findViewById(R.id.editbtn);
        ImageView deletebtn=view.findViewById(R.id.deletebtn);
        final TextView date=view.findViewById(R.id.datebtn);
        final TextView time3=view.findViewById(R.id.timebtn);

        final ToDoPojo pojo=arrayList.get(position);

        titleTextView.setEnabled(false);
        dateTextView.setEnabled(false);
        timeTextView.setEnabled(false);

        final Calendar cal=Calendar.getInstance();

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rowID=pojo.getId();
                String selection=ToDoTable.ID + " = '"+rowID+"'";

                MySqliteOpenHelper mySqliteOpenHelper=new MySqliteOpenHelper(context);
                SQLiteDatabase db=mySqliteOpenHelper.getWritableDatabase();
                int i=ToDoTable.delete(db,selection);

                if(i>0)
                    Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(context, "Not delete", Toast.LENGTH_SHORT).show();

                db.close();
                MainActivity mainActivityRef= (MainActivity) context;

                mainActivityRef.fetchValuesFromDatabase();
            }
        });



        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog=new Dialog(context);
                dialog.setContentView(R.layout.dialog_box);

                final EditText edittextd=dialog.findViewById(R.id.edittextd);
                final Button datebtnd=dialog.findViewById(R.id.datebtnd);
                final Button timebtnd=dialog.findViewById(R.id.timebtnd);
                final Button updatebtn=dialog.findViewById(R.id.updatebtn);



                datebtnd.setText(dateTextView.getText().toString());
                timebtnd.setText(timeTextView.getText().toString());
                edittextd.setText(titleTextView.getText().toString());

                datebtnd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatePickerDialog datePickerDialog =new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                datebtnd.setText(day+"-"+(month+1)+"-"+year);
                                dateTextView.setText(day+"-"+(month)+"-"+year);

                            }
                        },cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));

                        datePickerDialog.show();

                    }
                });
                timebtnd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TimePickerDialog timePickerDialog=new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int Minute) {

                                timebtnd.setText(""+hour+":"+Minute);
                                timeTextView.setText(timebtnd.getText().toString());

                            }
                        },cal.get(Calendar.HOUR),cal.get(Calendar.MINUTE),false);
                        timePickerDialog.show();
                    }
                });

                updatebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String enteredTodoText=edittextd.getText().toString();
                        String dateSelectedByUser=dateTextView.getText().toString();
                        String timeSelectedByUser=timeTextView.getText().toString();



                        String req = dateSelectedByUser;
                        String[] arr = req.split("-",4);

                        String[] tim=timeSelectedByUser.split(":",2);


                        Calendar calender = Calendar.getInstance();
                        long time=calender.getTimeInMillis();


                        int year = calender.get(Calendar.YEAR);
                        int month = calender.get(Calendar.MONTH);
                        int day = calender.get(Calendar.DAY_OF_MONTH);
                        int hour = calender.get(Calendar.HOUR);
                        int min = calender.get(Calendar.MINUTE);

                        int y = year, m = month, d = day,h=hour,min1=min;
                        try {
                            y = Integer.parseInt(arr[2]);
                            m = Integer.parseInt(arr[1]);
                            d = Integer.parseInt(arr[0]);
                            h=Integer.parseInt(tim[0]);
                            min1=Integer.parseInt(tim[1]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        Calendar cal=Calendar.getInstance();
                        cal.set(y,m,d,h,min1);

                        long time2=cal.getTimeInMillis();
                        if(time>time2)
                            Toast.makeText(context, "Backdate is not allowed", Toast.LENGTH_SHORT).show();
                        else {
//                            date.setText(dateSelectedByUser.toString());
  //                          time3.setText(timeSelectedByUser.toString());
    //                        titleTextView.setText(edittextd.getText().toString());
                            //dateTextView.setText(datebtnd.getText().toString());
                            //timeTextView.setText(timebtnd.getText().toString());
                            MySqliteOpenHelper mySqliteOpenHelper = new MySqliteOpenHelper(context);
                            SQLiteDatabase db = mySqliteOpenHelper.getWritableDatabase();

                            ContentValues cv = new ContentValues();
                            cv.put(ToDoTable.TITLE, enteredTodoText);
                            cv.put(ToDoTable.DATE, dateTextView.getText().toString());
                            cv.put(ToDoTable.TIME, timeTextView.getText().toString());

                            String selection = ToDoTable.ID + " = '" + pojo.getId() + "'";

                            int l = ToDoTable.update(db, cv, selection);

                            if (l > 0) {
                                Toast.makeText(context, "updated" + (time2 - time), Toast.LENGTH_SHORT).show();


                                Intent i=new Intent();
                                i.setAction("r.a.b");

                                i.putExtra("title",enteredTodoText);
                                PendingIntent pendingIntent=PendingIntent.getBroadcast(context,0,i,0);
                                AlarmManager manager= (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                                manager.set(AlarmManager.RTC,System.currentTimeMillis()+time2- time,pendingIntent);
                            }
                            else
                                Toast.makeText(context, "Not update", Toast.LENGTH_SHORT).show();

                            db.close();
                            MainActivity mainActivityRef = (MainActivity) context;

                            mainActivityRef.fetchValuesFromDatabase();
                            //mainActivityRef.setAlarm();

                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        titleTextView.setText(pojo.getTitle());
        timeTextView.setText(pojo.getTime());
        dateTextView.setText(pojo.getDate());
        circleTextView.setText((""+pojo.getTitle().charAt(0)).toUpperCase());

        return view;
    }

}
