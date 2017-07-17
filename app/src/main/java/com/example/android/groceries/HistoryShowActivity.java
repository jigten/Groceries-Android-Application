package com.example.android.groceries;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

public class HistoryShowActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private Uri mCurrentDateUri;
    private static final int GROCERY_LOADER = 0;
    GroceryCursorAdapter mCursorAdapeter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_show);

        Intent intent = getIntent();
        mCurrentDateUri = intent.getData();

        final ListView groceryListView = (ListView) findViewById(R.id.showLV);

        mCursorAdapeter = new GroceryCursorAdapter(this, null);
        groceryListView.setAdapter(mCursorAdapeter);

        getSupportLoaderManager().initLoader(GROCERY_LOADER, null, this);

        Cursor cursor = getContentResolver().query(mCurrentDateUri, null, null, null, null);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                mCurrentDateUri,
                null,
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
