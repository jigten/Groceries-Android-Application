package com.example.android.groceries;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.groceries.data.GroceryContract.GroceryEntry;

/**
 * Created by ujigt on 7/12/2017.
 */

public class HistoryCursorAdapter extends CursorAdapter {

    public HistoryCursorAdapter (Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.history_list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView dateTV = (TextView) view.findViewById(R.id.date);

        String date = cursor.getString(cursor.getColumnIndexOrThrow(GroceryEntry.COLUMN_GROCERY_DATE));

        dateTV.setText(date);
    }
}
