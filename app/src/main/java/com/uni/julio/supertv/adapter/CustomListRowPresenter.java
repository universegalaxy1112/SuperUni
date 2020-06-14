package com.uni.julio.supertv.adapter;

import android.view.ViewGroup;

import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.ListRowView;
import androidx.leanback.widget.RowPresenter;

public class CustomListRowPresenter extends ListRowPresenter {
    public CustomListRowPresenter(){
        super();
    }

    @Override
    public void setExpandedRowHeight(int rowHeight) {
        super.setExpandedRowHeight(20);
    }
    @Override
    protected void onBindRowViewHolder(RowPresenter.ViewHolder holder, Object item) {
        int numRows = ((CustomListRow) item).getNumRows();
        ((ListRowPresenter.ViewHolder) holder).getGridView().setNumRows(numRows);
        super.onBindRowViewHolder(holder, item);
    }
}
