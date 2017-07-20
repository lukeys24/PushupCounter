package com.lukeyseo.android.pushupcounter;

import android.support.v4.app.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.beardedhen.androidbootstrap.BootstrapButton;

public class HistoryActivity extends AppCompatActivity {
    private BootstrapButton mListButton;
    private BootstrapButton mGraphButton;
    private Spinner mDateRange;
    private Button mIndividualButton;
    private Button mDailyButton;
    private Fragment fragmentDisplay;
    private FragmentManager fm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        mListButton = (BootstrapButton) findViewById(R.id.list_button);
        mGraphButton = (BootstrapButton) findViewById(R.id.graph_button);
        mDateRange = (Spinner) findViewById(R.id.dateRangeSpinner);
        mDailyButton = (Button) findViewById(R.id.radioDailyEntry);
        mIndividualButton = (Button) findViewById(R.id.radioIndividualEntry);
        addItemsToSpinner();
        setFragment();

        mDateRange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String word = parent.getItemAtPosition(position).toString();

                updateFragment(word);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mDailyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFragment(mDateRange.getSelectedItem().toString());
            }
        });

        mIndividualButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFragment(mDateRange.getSelectedItem().toString());
            }
        });

    }

    private void updateFragment(String word) {
        Fragment frag = fm.findFragmentById(R.id.fragment_container);

        if (frag instanceof  HistoryListFragment) {
            if (word.equals("All Time")) {
                ((HistoryListFragment) frag).updateUI("");
            } else if (word.equals("Past Week")) {
                ((HistoryListFragment) frag).updateUI(word);
            } else if (word.equals("Past Month")) {
                ((HistoryListFragment) frag).updateUI(word);
            } else if (word.equals("Past Year")) {
                ((HistoryListFragment) frag).updateUI(word);
            }
        } else if (frag instanceof  GraphFragment) {
            if (word.equals("All Time")) {
                ((GraphFragment) frag).updateUI("");
            } else if (word.equals("Past Week")) {
                ((GraphFragment) frag).updateUI(word);
            } else if (word.equals("Past Month")) {
                ((GraphFragment) frag).updateUI(word);
            } else if (word.equals("Past Year")) {
                ((GraphFragment) frag).updateUI(word);
            }
        }
    }

    private void addItemsToSpinner() {
        ArrayAdapter<CharSequence> dateRangeSpinnerAdapter =
                ArrayAdapter.createFromResource(this, R.array.viewOptions, android.R.layout.simple_spinner_item);

        dateRangeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mDateRange.setAdapter(dateRangeSpinnerAdapter);
    }

    private void setFragment() {
        fm = getSupportFragmentManager();

        fragmentDisplay = fm.findFragmentById(R.id.fragment_container);

        // Check if fragment already exists
        if (fragmentDisplay == null) {
            fragmentDisplay = new HistoryListFragment();

            // Creates and commits a fragment transaction
            fm.beginTransaction().add(R.id.fragment_container, fragmentDisplay, "LIST_FRAGMENT").commit();
            updateFragment(mDateRange.getSelectedItem().toString());
        }

        mListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Remove previous fragment and add new HistoryListFragment
                fm.beginTransaction().remove(fragmentDisplay).commit();

                fragmentDisplay = new HistoryListFragment();

                fm.beginTransaction().add(R.id.fragment_container, fragmentDisplay, "LIST_FRAGMENT").commit();
            }
        });

        mGraphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Remove previous fragment and add new GraphFragment
                fm.beginTransaction().remove(fragmentDisplay).commit();

                fragmentDisplay = new GraphFragment();

                fm.beginTransaction().add(R.id.fragment_container, fragmentDisplay, "GRAPH_FRAGMENT").commit();
            }
        });
    }
}
