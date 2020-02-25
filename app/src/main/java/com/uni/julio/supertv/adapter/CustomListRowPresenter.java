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
    protected RowPresenter.ViewHolder createRowViewHolder(ViewGroup parent) {
        RowPresenter.ViewHolder viewHolder=super.createRowViewHolder(parent);
        return super.createRowViewHolder(parent);
    }

}
