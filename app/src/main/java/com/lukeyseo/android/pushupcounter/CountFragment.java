package com.lukeyseo.android.pushupcounter;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.github.pavlospt.CircleView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Luke on 7/10/17.
 */

public class CountFragment extends Fragment implements SensorEventListener {
    private BootstrapButton mBeginButton;
    private BootstrapButton mScoreView;
    private CircleView mCircleView;
    private AwesomeTextView mScoreViewToday;

    private static final String KEY_INDEX_PUSHUPS = "indexPushups";
    private static final String KEY_INDEX_COUNTING = "indexCounting";
    private int mPushupCount = 0;
    private int mHighScore = 0;
    private boolean mCurrentlyCounting = false;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    SQLiteDatabase pushupDB = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_count, container, false);

        // Set up SensorManager/Sensor
        mSensorManager = (SensorManager) this.getActivity().getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        mBeginButton = (BootstrapButton) v.findViewById(R.id.beginButton);
        mScoreView = (BootstrapButton) v.findViewById(R.id.scoreView);
        mCircleView = (CircleView) v.findViewById(R.id.circleView);
        mScoreViewToday = (AwesomeTextView) v.findViewById(R.id.scoreViewToday);

        mBeginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beginCount(v);
            }
        });

        // Handles when we change orientations and recover previous ongoing count
        if (savedInstanceState != null) {
            mPushupCount = savedInstanceState.getInt(KEY_INDEX_PUSHUPS);
            mCurrentlyCounting = savedInstanceState.getBoolean(KEY_INDEX_COUNTING);

            if (mCurrentlyCounting) {
                mBeginButton.setText("STOP");
            } else {
                mBeginButton.setText("Begin");
            }
        }

        restoreHighscore();
        updateScore();

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_INDEX_PUSHUPS, mPushupCount);
        outState.putBoolean(KEY_INDEX_COUNTING, mCurrentlyCounting);
    }

    @Override
    public void onResume() {
        super.onResume();

        mSensorManager.registerListener(this , mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (mCurrentlyCounting && event.values[0] == 0) {
            mPushupCount++;
            updateScore();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    // Counts pushups with proximity sensor
    public void beginCount(View view) {
        // Case where we begin recording
        if (!mCurrentlyCounting) {
            mCurrentlyCounting = true;
            mBeginButton.setText("STOP");
        } else {
            // We finished recording, store data if user desires
            mCurrentlyCounting = false;
            mBeginButton.setText("Begin");

            // Variable to pass count into SweetAlertDialog
            final int passInnerCount = mPushupCount;


            new SweetAlertDialog(this.getActivity())
                    .setTitleText(mPushupCount + " Pushups!")
                    .setContentText("Do you want to save your results?")
                    .setConfirmText("Yes")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            createDatabase();
                            addData(passInnerCount);
                            sweetAlertDialog.dismissWithAnimation();

                            if (passInnerCount > mHighScore) {
                                saveHighscore(passInnerCount);
                            }

                            mPushupCount = 0;
                            updateScore();

                            new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Stored your results!")
                                    .dismiss();

                        }
                    })
                    .setCancelText("No")
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                            mPushupCount = 0;
                            updateScore();

                            sweetAlertDialog.cancel();
                        }
                    })
                    .show();
        }
    }

    // Updates text to reflect current score
    private void updateScore() {
        mScoreView.setText("Best : " + mHighScore);
        mCircleView.setTitleText(Integer.toString(mPushupCount));
        mCircleView.setShowSubtitle(false);
        updateTodayView();
    }

    // Set view for today count
    private void updateTodayView() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());

        PushupList pushupList = PushupList.get(getActivity(), "");
        List<Pushup> pushups = pushupList.getPushups();
        int todayCount = 0;

        for (Pushup pushup : pushups) {
            if (pushup.getDate().equals(date)) {
                todayCount += pushup.getCount();
            }
        }

        mScoreViewToday.setText("Today : " + todayCount);
    }

    // Saves new highscore using preference
    private void saveHighscore(int newHigh) {
        mHighScore = newHigh;
        SharedPreferences prefs = this.getActivity().getSharedPreferences("myScorePrefs", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putInt("highscore", newHigh);
        prefsEditor.apply();
    }

    // Restores highscore with prefences
    private void restoreHighscore() {
        SharedPreferences prefs = this.getActivity().getSharedPreferences("myScorePrefs", MODE_PRIVATE);
        mHighScore = prefs.getInt("highscore", 0);
    }

    // Initializes database with primary key, date and pushupCount as columns
    private void createDatabase() {
        try {
            pushupDB = this.getActivity().openOrCreateDatabase("MyPushups", MODE_PRIVATE, null);
            pushupDB.execSQL("CREATE TABLE IF NOT EXISTS pushups " +
                    "(id integer primary key, date VARCHAR, pushCount INTEGER);");

        } catch(Exception e) {
            Log.e("CONTACTS ERROR", "Error creating DB");
        }
    }

    // Adds new data to database with current date/passed in pushup count
    private void addData(int count) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());


        pushupDB.execSQL("INSERT INTO pushups (date, pushCount) VALUES ('" +
                date + "', '" + count + "');");
    }
}
