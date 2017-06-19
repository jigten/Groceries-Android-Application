package com.example.android.groceries;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.groceries.data.GroceryContract.GroceryEntry;
import com.example.android.groceries.data.GroceryDbHelper;


public class GroceryActivity extends AppCompatActivity {
    GroceryDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroceryActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        mDbHelper = new GroceryDbHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    private  void displayDatabaseInfo() {
        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity
        GroceryDbHelper mDbHelper = new GroceryDbHelper(this);

        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] project = {
                GroceryEntry._ID,
                GroceryEntry.COLUMN_GROCERY_NAME,
                GroceryEntry.COLUMN_GROCERY_PRICE,
                GroceryEntry.COLUMN_GROCERY_QUANTITY,
                GroceryEntry.COLUMN_GROCERY_TOTAL
        };

        Cursor cursor = db.query(
                GroceryEntry.TABLE_NAME,
                project,
                null,
                null,
                null,
                null,
                null
        );

        TextView displayView = (TextView) findViewById(R.id.text_view_grocery);

        try {
            // Create a header in the TextView that looks like this:
            //
            // The Groceries table contains <number of rows in the Cursor> groceries
            // _id - name - price - quantity - total
            //
            // In the while loop below, iterate through the rows of the cursor and display
            // the information from each column in this order
            displayView.setText("The Groceries table contains " + cursor.getCount() + " groceries.\n\n");
            displayView.append(GroceryEntry._ID + " - " + GroceryEntry.COLUMN_GROCERY_NAME + " - " + GroceryEntry.COLUMN_GROCERY_PRICE +
            " - " + GroceryEntry.COLUMN_GROCERY_QUANTITY + " - " + GroceryEntry.COLUMN_GROCERY_TOTAL + "\n");

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(GroceryEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(GroceryEntry.COLUMN_GROCERY_NAME);
            int priceColumnIndex = cursor.getColumnIndex(GroceryEntry.COLUMN_GROCERY_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(GroceryEntry.COLUMN_GROCERY_QUANTITY);
            int totalColumnIndex = cursor.getColumnIndex(GroceryEntry.COLUMN_GROCERY_TOTAL);

            // Iterate through all the returned rows in the cursor
            while(cursor.moveToNext()) {
                // Use that index to extract the String/Int/Real value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                float currentPrice = cursor.getFloat(priceColumnIndex);
                float currentQuantity = cursor.getFloat(quantityColumnIndex);
                float currentTotal = cursor.getFloat(totalColumnIndex);

                // Display the values from each column of the current row in the cursor to the TextView
                displayView.append("\n" + currentID + " - " + currentName + " - " + currentPrice + " - " + currentQuantity + " - " + currentTotal);
            }

        } finally {
            // Always close the cursor when you're done reading from it. This releases all the
            // resources and makes it invalid preventing memory leaks and performance issues
            cursor.close();
        }
    }

    private void insertGrocery() {
        // get the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where the column names are keys
        ContentValues values = new ContentValues();
        values.put(GroceryEntry.COLUMN_GROCERY_NAME, "Chillies");
        values.put(GroceryEntry.COLUMN_GROCERY_PRICE, 150.50);
        values.put(GroceryEntry.COLUMN_GROCERY_QUANTITY, 3.5);
        values.put(GroceryEntry.COLUMN_GROCERY_TOTAL, 526.75);

        long newRowId;
        newRowId = db.insert(
                GroceryEntry.TABLE_NAME,
                null,
                values
        );
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_grocery.xml file
        // This adds menu items to the app bar
        getMenuInflater().inflate(R.menu.menu_grocery, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertGrocery();
                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
