package com.brightyu.androidcommon.ui.widget.pickview.adapter;

public interface PickerAdapter<T> {
    /**
     * Gets items count
     *
     * @return the count of wheel items
     */
    int getCount();

    /**
     * Gets a wheel item by index.
     */
    T getItem(int position);

    /**
     * Gets show data
     */
    String getItemText(int position);
}
