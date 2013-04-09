package org.tadpoleweibo.widget;

import java.util.ArrayList;

public class PageList<T> {
    public int totalCount = 0;
    public ArrayList<T> records;
    public int nextStartIndex;
    public int prevPage;
}