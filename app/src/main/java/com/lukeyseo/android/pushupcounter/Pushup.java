package com.lukeyseo.android.pushupcounter;

import java.util.Date;

/**
 * Created by Luke on 7/10/17.
 */

public class Pushup {
    private Date mDate;
    private int mCount;
    private double mTime;

    public Pushup() {

    }

    public Date getDate() {
        return mDate;
    }

    public int getCount() {
        return mCount;
    }

    public double getTime() {
        return mTime;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

    public void setCount(int mCount) {
        this.mCount = mCount;
    }

    public void setTime(double mTime) {
        this.mTime = mTime;
    }
}
