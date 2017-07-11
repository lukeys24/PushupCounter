package com.lukeyseo.android.pushupcounter;

import java.util.Date;

/**
 * Created by Luke on 7/10/17.
 */

public class Pushup {
    private Date mDate;
    private int mCount;

    public Pushup() {

    }

    public Date getDate() {
        return mDate;
    }

    public int getCount() {
        return mCount;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

    public void setCount(int mCount) {
        this.mCount = mCount;
    }

}
