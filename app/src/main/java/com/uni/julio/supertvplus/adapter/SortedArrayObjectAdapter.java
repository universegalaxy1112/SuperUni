package com.uni.julio.supertvplus.adapter;


import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.Presenter;

import java.util.Comparator;
import java.util.TreeSet;

public class SortedArrayObjectAdapter extends ArrayObjectAdapter {
    private TreeSet<Object> mSortedItems;

    public SortedArrayObjectAdapter(Comparator comparator, Presenter presenter) {
        super(presenter);
        this.mSortedItems = new TreeSet<>(comparator);
    }


    @Override
    public void add(Object item) {
        this.mSortedItems.add(item);
        super.add(this.mSortedItems.headSet(item).size(), item);
    }
    @Override
    public boolean remove(Object item) {
        this.mSortedItems.remove(item);
        return super.remove(item);
    }
    @Override
    public void clear() {
        this.mSortedItems.clear();
        super.clear();
    }
}
