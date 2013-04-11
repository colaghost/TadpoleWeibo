package org.tadpoleweibo.widget;

import java.util.ArrayList;

public class PageList<T> {
    public int total_number = 0;
    public ArrayList<T> records;
    public int next_cursor;
    public int previous_cursor;
}