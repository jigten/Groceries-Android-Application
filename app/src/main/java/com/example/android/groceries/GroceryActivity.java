package com.example.android.groceries;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.groceries.data.GroceryContract.GroceryEntry;

public class GroceryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int GROCERY_LOADER = 0;

    GroceryCursorAdapter mCursorAdapeter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery);

        // Setup FAB to open EditorActivity
        com.github.clans.fab.FloatingActionButton fabAdd = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fabAdd);
        com.github.clans.fab.FloatingActionButton fabSave = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fabSave);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroceryActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "TODO: Grocery basket saved", Toast.LENGTH_SHORT).show();
            }
        });

        final ListView groceryListView = (ListView) findViewById(R.id.groceriesLV);

        View emptyView = findViewById(R.id.empty_view);
        groceryListView.setEmptyView(emptyView);

        mCursorAdapeter = new GroceryCursorAdapter(this, null);
        groceryListView.setAdapter(mCursorAdapeter);

        groceryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GroceryActivity.this, EditorActivity.class);

                Uri currentGroceryUri = ContentUris.withAppendedId(GroceryEntry.CONTENT_URI, id);
                intent.setData(currentGroceryUri);

                startActivity(intent);
            }
        });

        getSupportLoaderManager().initLoader(GROCERY_LOADER, null, this);
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
            case R.id.action_summary_report:
                Intent summaryIntent = new Intent(this, SummaryActivity.class);
                startActivity(summaryIntent);
                return true;
            case R.id.action_show_history:
                Intent historyIntent = new Intent(this, HistoryActivity.class);
                startActivity(historyIntent);
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllGroceries();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllGroceries() {
        int rowsDeleted = getContentResolver().delete(GroceryEntry.CONTENT_URI, null, null);
        Toast.makeText(this, "Succesfully deleted all groceries", Toast.LENGTH_SHORT).show();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                GroceryEntry._ID,
                GroceryEntry.COLUMN_GROCERY_NAME,
                GroceryEntry.COLUMN_GROCERY_QUANTITY,
                GroceryEntry.COLUMN_GROCERY_TOTAL
        };

        return new CursorLoader(this,
                GroceryEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapeter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapeter.swapCursor(null);
    }
}
