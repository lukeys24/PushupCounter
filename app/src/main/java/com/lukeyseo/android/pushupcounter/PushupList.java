package com.lukeyseo.android.pushupcounter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Luke on 7/11/17.
 */

public class PushupList {

    private static PushupList sPushupList;
    private List<Pushup> mPushups;
    private SQLiteDatabase pushupDB;

    public static PushupList get(Context context) {
        if (sPushupList == null) {
            sPushupList = new PushupList(context);
        }

        return sPushupList;
    }

    private PushupList(Context context) {
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

        Cursor cursor = pushupDB.rawQuery("SELECT * FROM pushups", null);

        int idColumn = cursor.getColumnIndex("id");
        int dateColumn = cursor.getColumnIndex("date");
        int pushColumn = cursor.getColumnIndex("pushCount");

        cursor.moveToFirst();


        // Checks we at least have 1 result
        if (cursor != null && (cursor.getColumnCount() > 0)) {
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
}
