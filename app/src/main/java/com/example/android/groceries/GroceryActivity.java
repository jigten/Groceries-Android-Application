package com.example.android.groceries;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

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

        Cursor cursor = getContentResolver().query(GroceryEntry.CONTENT_URI, project, null, null, null);

        ListView groceryLV = (ListView) findViewById(R.id.groceriesLV);

        View emptyView = findViewById(R.id.empty_view);
        groceryLV.setEmptyView(emptyView);

        GroceryCursorAdapter adapter = new GroceryCursorAdapter(this, cursor);

        groceryLV.setAdapter(adapter);
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

        Uri newUri = getContentResolver().insert(GroceryEntry.CONTENT_URI, values);
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
