package com.lukeyseo.android.pushupcounter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Luke on 7/11/17.
 */

public class PushupList {

    private List<Pushup> mPushups;
    private SQLiteDatabase pushupDB;

    public static PushupList get(Context context, String dateRange) {
        return new PushupList(context, dateRange);
    }

    private PushupList(Context context, String dateRange) {
        mPushups = new ArrayList<>();

        try {
            pushupDB = context.openOrCreateDatabase("MyPushups", MODE_PRIVATE, null);
            pushupDB.execSQL("CREATE TABLE IF NOT EXISTS pushups " +
                    "(id integer primary key, date VARCHAR, pushCount INTEGER);");
            ////Check if DB exists
            //File database = getApplicationContext().getDatabasePath("MyPushups.db");
            //if (!database.exists()) {
            //    Toast.makeText(this, "Database Created", Toast.LENGTH_LONG).show();
            //} else {
            //    Toast.makeText(this, "Database Missing", Toast.LENGTH_LONG).show();
            //}*/

        } catch(Exception e) {
            Log.e("CONTACTS ERROR", "Error creating DB");
        }

        String dateToday = new SimpleDateFormat("yyyy-MM-dd").format(new Date());


        String dateR =  " WHERE date BETWEEN '2017-07-10' AND '2017-07-11'";
        dateR = " WHERE date BETWEEN '" + getPastWeekString() + "' AND '" + dateToday + "'";


        Cursor cursor = pushupDB.rawQuery("SELECT * FROM pushups" + dateR, null);

        int idColumn = cursor.getColumnIndex("id");
        int dateColumn = cursor.getColumnIndex("date");
        int pushColumn = cursor.getColumnIndex("pushCount");

        // Checks we at least have 1 result
        if (cursor.moveToFirst() && (cursor.getColumnCount() > 0)) {
            do {
                Pushup pushup = new Pushup();

                pushup.setId(cursor.getInt(idColumn));
                pushup.setDate(cursor.getString(dateColumn));
                pushup.setCount(cursor.getInt(pushColumn));

                mPushups.add(pushup);

            } while (cursor.moveToNext());

        } else {
            Toast.makeText(context, "No Results to Show", Toast.LENGTH_LONG).show();
        }
    }

    public List<Pushup> getPushups() {
        return mPushups;
    }

    private String getPastWeekString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(pastWeek());
    }

    private Date pastWeek() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        return cal.getTime();
    }
}
