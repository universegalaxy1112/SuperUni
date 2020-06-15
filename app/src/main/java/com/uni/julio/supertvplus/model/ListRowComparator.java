package com.uni.julio.supertvplus.model;

import androidx.leanback.widget.ListRow;

import java.util.Comparator;

public class ListRowComparator implements Comparator<ListRow> {
    public int compare(ListRow o1, ListRow o2) {
        return (int) (o1.getId() - o2.getId());
    }
}
