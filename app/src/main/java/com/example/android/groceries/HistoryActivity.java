package com.example.android.groceries;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.groceries.data.GroceryContract.GroceryEntry;
import com.example.android.groceries.data.GroceryContract.HistoryEntry;

public class HistoryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    String historyPath = "history";
    private Uri historyUri = Uri.withAppendedPath(HistoryEntry.CONTENT_URI, historyPath);
    HistoryCursorAdapter mCursorAdapter;
    private static final int HISTORY_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        final ListView historyLV = (ListView) findViewById(R.id.historyLV);
        mCursorAdapter = new HistoryCursorAdapter(this, null);
        historyLV.setAdapter(mCursorAdapter);

        historyLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HistoryActivity.this, HistoryShowActivity.class);

                TextView tv = (TextView) view.findViewById(R.id.date);

                String selectedDate = tv.getText().toString();

                Uri currentDateUri = Uri.withAppendedPath(HistoryEntry.CONTENT_URI, selectedDate);
                intent.setData(currentDateUri);

                startActivity(intent);
            }
        });

        getSupportLoaderManager().initLoader(HISTORY_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                GroceryEntry._ID,
                GroceryEntry.COLUMN_GROCERY_DATE
        };

        String selection = "(" + GroceryEntry.COLUMN_GROCERY_DATE + " NOT NULL) GROUP BY (" + GroceryEntry.COLUMN_GROCERY_DATE + ")";

        return new CursorLoader(this,
                historyUri,
                projection,
                selection,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
