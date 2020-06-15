package com.uni.julio.supertvplus.adapter;

import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ObjectAdapter;

public class CustomListRow extends ListRow {
    private CharSequence mContentDescription;
    private static final String TAG = CustomListRow.class.getSimpleName();
    private int mNumRows = 1;

    public CustomListRow(HeaderItem header, ObjectAdapter adapter) {
        super(header, adapter);
    }

    public CustomListRow(long id, HeaderItem header, ObjectAdapter adapter){
        super(id,header,adapter);
    }
    public void setNumRows(int numRows) {
        mNumRows = numRows;
    }

    public int getNumRows() {
        return mNumRows;
    }
    @Override
    public CharSequence getContentDescription() {
        if (mContentDescription != null) {
            return mContentDescription;
        }
        final HeaderItem headerItem = getHeaderItem();
        if (headerItem != null) {
            CharSequence contentDescription = headerItem.getContentDescription();
            if (contentDescription != null) {
                return contentDescription;
            }
            return headerItem.getName();
        }
        return null;
    }

    public void setContentDescription(CharSequence contentDescription) {
        mContentDescription = contentDescription;
    }
}
