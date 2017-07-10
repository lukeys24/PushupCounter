package com.lukeyseo.android.pushupcounter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.beardedhen.androidbootstrap.BootstrapButton;

public class MainActivity extends AppCompatActivity {
    private BootstrapButton mCountButton;
    private BootstrapButton mHistoryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setReferences();


        // Listener for starting pushup counter activity
        mCountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CountActivity.class);
                startActivity(intent);
            }
        });

        // Listener for starting history activity
        mHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });
        
    }

    // Sets the references for buttons
    private void setReferences() {
        mCountButton = (BootstrapButton) findViewById(R.id.start_pushup);
        mHistoryButton = (BootstrapButton) findViewById(R.id.start_history);
    }
}

