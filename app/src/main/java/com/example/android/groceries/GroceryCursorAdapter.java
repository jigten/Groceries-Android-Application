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
 * Created by ujigt on 6/22/2017.
 */

public class GroceryCursorAdapter extends CursorAdapter {

    public GroceryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView groceryName = (TextView) view.findViewById(R.id.name);
        TextView groceryTotal = (TextView) view.findViewById(R.id.total);
        TextView groceryQuantity = (TextView) view.findViewById(R.id.quantity);

        String name = cursor.getString(cursor.getColumnIndexOrThrow(GroceryEntry.COLUMN_GROCERY_NAME));
        float total = cursor.getFloat(cursor.getColumnIndexOrThrow(GroceryEntry.COLUMN_GROCERY_TOTAL));
        float quantity = cursor.getFloat(cursor.getColumnIndexOrThrow(GroceryEntry.COLUMN_GROCERY_QUANTITY));

        groceryName.setText(name);
        groceryTotal.setText("Total: " + String.valueOf(total) + " Nu/-");
        groceryQuantity.setText("Quantity: " + String.valueOf(quantity) + " Kg");

    }
}
