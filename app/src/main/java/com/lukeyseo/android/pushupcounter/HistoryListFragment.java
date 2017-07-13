package com.lukeyseo.android.pushupcounter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Luke on 7/11/17.
 */

public class HistoryListFragment extends Fragment {
    private RecyclerView mPushupRecyclerView;
    private PushupAdapter mAdapter;

    // View holder for Recycler View to display each individual pushup
    private class PushupHolder extends RecyclerView.ViewHolder {
        private TextView mCountTextView;
        private TextView mDateTextView;
        private Pushup mPushup;

        public void bind(Pushup pushup) {
            mPushup = pushup;

            mCountTextView.setText(Integer.toString(mPushup.getCount()));
            mDateTextView.setText(mPushup.getDate());
        }

        public PushupHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_pushup, parent, false));

            mCountTextView = (TextView) itemView.findViewById(R.id.pushup_count);
            mDateTextView = (TextView) itemView.findViewById(R.id.pushup_date);
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

        updateUI();

        return v;
    }

    private void updateUI() {
        PushupList pushupList = PushupList.get(getActivity(), "");
        List<Pushup> pushups = pushupList.getPushups();

        mAdapter = new PushupAdapter(pushups);
        mPushupRecyclerView.setAdapter(mAdapter);
    }

}
