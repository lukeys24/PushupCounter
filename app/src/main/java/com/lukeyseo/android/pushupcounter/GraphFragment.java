package com.lukeyseo.android.pushupcounter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Luke on 7/10/17.
 */

public class GraphFragment extends Fragment {
    private LineChart mLineChart;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_graph, container, false);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Spinner spin = (Spinner) getActivity().findViewById(R.id.dateRangeSpinner);

        updateUI(spin.getSelectedItem().toString());
    }

    public void updateUI(String range) {
        mLineChart = (LineChart) getActivity().findViewById(R.id.graph_display);

        removeDataSet();

        addDataSet(range);

        mLineChart.invalidate();
    }

    private void addDataSet(String range) {
        // Get list of push ups in range
        PushupList pushupList = PushupList.get(getActivity(), range);

        List<Pushup> pushups = pushupList.getPushups();

        // Code to set pushup list for day, not individual entries
        Map<String, Integer> pushupMap = new HashMap<>();
        for (Pushup pushup : pushups) {
            if (!pushupMap.containsKey(pushup.getDate())) {
                pushupMap.put(pushup.getDate(), pushup.getCount());
            } else {
                pushupMap.put(pushup.getDate(), pushupMap.get(pushup.getDate()) + pushup.getCount());
            }
        }
        List<Pushup> dailyPushups = new ArrayList<Pushup>();
        for (Map.Entry<String, Integer> entry : pushupMap.entrySet()) {
            Pushup tempPush = new Pushup();
            tempPush.setDate(entry.getKey());
            tempPush.setCount(entry.getValue());

            dailyPushups.add(tempPush);
        }

        // Check whether to display entries by each individual or daily
        RadioButton buttonDaily = (RadioButton) getActivity().findViewById(R.id.radioIndividualEntry);
        if (!buttonDaily.isChecked()) {
            pushups = dailyPushups;
        }

        String[] formatedDates = new String[pushups.size()];

        // Initialize list for data entries
        List<Entry> entries = new ArrayList<>();

        if (pushups.size() > 1) {
            int i = 0;
            for (Pushup pushup : pushups) {
                entries.add(new Entry(i, pushup.getCount()));

                // Parse date so it's in the format '07-12' for month/day
                String dateParsed = pushup.getDate();
                dateParsed = dateParsed.substring(5);
                formatedDates[i] = dateParsed;

                i++;
            }

            // Set up data for plot
            LineDataSet dataSet = new LineDataSet(entries, "Label");
            LineData lineData = new LineData(dataSet);

            // Settings for x axis
            XAxis xAxis = mLineChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(false);
            xAxis.setValueFormatter(new MyXAxisValueFormatter(formatedDates));
            xAxis.setGranularity(1f);

            // Settings for right y axis
            YAxis yAxis = mLineChart.getAxisRight();
            yAxis.setDrawLabels(false);
            yAxis.setEnabled(false);

            mLineChart.setData(lineData);
            mLineChart.setDescription(null);
        } else {
            Description desc = new Description();
            desc.setText("Need at least 2 entries.");
            mLineChart.setDescription(desc);
        }

        mLineChart.notifyDataSetChanged();
    }

    private void removeDataSet() {
        LineData data = mLineChart.getData();

        if (data != null) {
            data.removeDataSet(data.getDataSetByIndex(data.getDataSetCount() - 1));

            mLineChart.notifyDataSetChanged();
            mLineChart.invalidate();
        }
    }

    private class MyXAxisValueFormatter implements IAxisValueFormatter {

        private String[] mValues;

        public MyXAxisValueFormatter(String[] values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            // "value" represents the position of the label on the axis (x or y)
            return mValues[(int) value];
        }
    }
}
