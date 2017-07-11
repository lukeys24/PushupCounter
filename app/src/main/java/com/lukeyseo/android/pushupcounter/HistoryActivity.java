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
        setFragments();


    }

    private void setFragments() {
        FragmentManager fm = getSupportFragmentManager();

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Check if fragment already exists
            Fragment fragment = fm.findFragmentById(R.id.fragment_container);

            if (fragment == null) {
                fragment = new com.lukeyseo.android.pushupcounter.HistoryListFragment();

                // Creates and commits a fragment transaction
                fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
            }
        } else {
            Fragment fragment_list = fm.findFragmentById(R.id.list_fragment);
            Fragment fragment_graph  = fm.findFragmentById(R.id.graph_fragment);

            // Check if fragment already exists
            if (fragment_list == null) {
                fragment_list = new com.lukeyseo.android.pushupcounter.HistoryListFragment();

                // Creates and commits a fragment transaction
                fm.beginTransaction().add(R.id.list_fragment, fragment_list).commit();
            }

            if (fragment_graph == null) {
                fragment_graph = new com.lukeyseo.android.pushupcounter.GraphFragment();

                // Creates and commits a fragment transaction
                fm.beginTransaction().add(R.id.graph_fragment, fragment_graph).commit();
            }
        }
    }
}
