package com.lukeyseo.android.pushupcounter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.AwesomeTextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Luke on 7/11/17.
 */

public class HistoryListFragment extends Fragment {
    private RecyclerView mPushupRecyclerView;
    private PushupAdapter mAdapter;

    // View holder for Recycler View to display each individual pushup
    private class PushupHolder extends RecyclerView.ViewHolder {
        private AwesomeTextView mCountTextView;
        private AwesomeTextView mDateTextView;
        private Pushup mPushup;

        public void bind(Pushup pushup) {
            mPushup = pushup;

            mCountTextView.setText(Integer.toString(mPushup.getCount()));
            mDateTextView.setText(mPushup.getDate());

        }

        public PushupHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_pushup, parent, false));

            mCountTextView = (AwesomeTextView) itemView.findViewById(R.id.pushup_count);
            mDateTextView = (AwesomeTextView) itemView.findViewById(R.id.pushup_date);
        }
    }

    private class PushupAdapter extends RecyclerView.Adapter<PushupHolder> {
        private List<Pushup> mPushups;

        public PushupAdapter(List<Pushup> pushups) {
            mPushups = pushups;
        }

        @Override
        public PushupHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new PushupHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(PushupHolder holder, int position) {
            Pushup pushup = mPushups.get(position);
            holder.bind(pushup);
        }

        @Override
        public int getItemCount() {
            return mPushups.size();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history_list, container, false);

        mPushupRecyclerView = (RecyclerView) v.findViewById(R.id.pushup_recycler_view);

        mPushupRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Spinner spin = (Spinner) getActivity().findViewById(R.id.dateRangeSpinner);

        updateUI(spin.getSelectedItem().toString());

        return v;
    }

    public void updateUI(String range) {
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
        Collections.sort(dailyPushups);

        // Check whether to display entries by each individual or daily
        RadioButton buttonDaily = (RadioButton) getActivity().findViewById(R.id.radioIndividualEntry);
        if (buttonDaily.isChecked()) {
            mAdapter = new PushupAdapter(pushups);
        } else {
            mAdapter = new PushupAdapter(dailyPushups);
        }

        mPushupRecyclerView.setAdapter(mAdapter);
    }

}
