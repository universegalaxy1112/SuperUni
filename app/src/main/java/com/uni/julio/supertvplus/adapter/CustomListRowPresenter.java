package com.uni.julio.supertvplus.adapter;

import androidx.leanback.widget.ListRowPresenter;
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
