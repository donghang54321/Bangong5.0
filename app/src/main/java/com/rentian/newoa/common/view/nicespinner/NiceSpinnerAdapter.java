package com.rentian.newoa.common.view.nicespinner;

import android.content.Context;

import java.util.List;

/**
 * @author angelo.marchesin
 */

public class NiceSpinnerAdapter<T> extends NiceSpinnerBaseAdapter {

    private final List<T> mItems;

    public NiceSpinnerAdapter(Context context, List<T> items) {
        super(context);
        mItems = items;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public T getItem(int position) {
        if (position >= mSelectedIndex) {
            return mItems.get(position );
        } else {
            return mItems.get(position-1);
        }
    }
    @Override
    public T getItemInDataset(int position) {
        return mItems.get(position);
    }
}