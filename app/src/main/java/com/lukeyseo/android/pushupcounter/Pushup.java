package com.lukeyseo.android.pushupcounter;

import java.util.Comparator;

/**
 * Created by Luke on 7/10/17.
 */

public class Pushup implements Comparable<Pushup> {
    private int mId;
    private String mDate;
    private int mCount;

    public Pushup() { }

    public int getId() { return mId; }

    public String getDate() {
        return mDate;
    }

    public int getCount() {
        return mCount;
    }

    public void setId(int mId) { this.mId = mId; }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public void setCount(int mCount) {
        this.mCount = mCount;
    }

    @Override
    public int compareTo(Pushup pushup) {
        return this.mDate.compareTo(pushup.getDate());
    }
}
