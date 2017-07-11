package com.lukeyseo.android.pushupcounter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);


        FragmentManager fm = getSupportFragmentManager();

        // Check if fragment already exists
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = new CountFragment();

            // Creates and commits a fragment transaction
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }
}
