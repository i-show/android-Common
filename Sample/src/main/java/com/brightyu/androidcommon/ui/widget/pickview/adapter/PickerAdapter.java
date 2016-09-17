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
     * Gets maximum item length. It is used to determine the wheel width.
     * If -1 is returned there will be used the default wheel width.
     */
    int indexOf(T item);

    String getItemText(int position);
}
