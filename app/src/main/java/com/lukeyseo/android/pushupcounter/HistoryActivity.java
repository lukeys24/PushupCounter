package com.lukeyseo.android.pushupcounter;

import android.content.res.Configuration;
import android.support.v4.app.*;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.beardedhen.androidbootstrap.BootstrapButton;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);


        FragmentManager fm = getSupportFragmentManager();

        // Check if fragment already exists
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = new com.lukeyseo.android.pushupcounter.ListFragment();

            // Creates and commits a fragment transaction
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }


        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

        }
    }
}
