package com.lukeyseo.android.pushupcounter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;
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

        createDatabase(context);

        // Get query we will use for
        String query = "SELECT * FROM pushups" + getRangeQuery(dateRange);

        storeEntries(context, query);
    }

    private void createDatabase(Context context) {
        try {
            pushupDB = context.openOrCreateDatabase("MyPushups", MODE_PRIVATE, null);
            pushupDB.execSQL("CREATE TABLE IF NOT EXISTS pushups " +
                    "(id integer primary key, date VARCHAR, pushCount INTEGER);");

        } catch(Exception e) {
            Log.e("CONTACTS ERROR", "Error creating DB");
        }
    }

    private String getRangeQuery(String range) {
        String dateToday = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        if (range.equals("Past Week")) {
            return " WHERE date BETWEEN '" + getPastDateString(-7) + "' AND '" + dateToday + "'";
        } else if (range.equals("Past Month")) {
            return " WHERE date BETWEEN '" + getPastDateString(-30) + "' AND '" + dateToday + "'";
        } else if (range.equals("Past Year")) {
            return " WHERE date BETWEEN '" + getPastDateString(-365) + "' AND '" + dateToday + "'";
        }
        // Return all records
        return "";
    }

    private void storeEntries(Context context, String query) {
        Cursor cursor = pushupDB.rawQuery(query, null);

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

    private String getPastDateString(int days) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(pastDate(days));
    }

    private Date pastDate(int days) {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }

    public List<Pushup> getPushups() {
        return mPushups;
    }
}
