package com.example.android.groceries.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.groceries.data.GroceryContract.GroceryEntry;

/**
 * Created by ujigt on 6/21/2017.
 */

public class GroceryProvider extends ContentProvider {
    /** Tag for the log messages */
    public static final String LOG_TAG = ContentProvider.class.getSimpleName();

    private static final int GROCERIES = 100;
    private static final int GROCERY_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(GroceryContract.CONTENT_AUTHORITY, GroceryContract.PATH_GROCERIES, GROCERIES);
        sUriMatcher.addURI(GroceryContract.CONTENT_AUTHORITY, GroceryContract.PATH_GROCERIES + "/#", GROCERY_ID);
    }

    private GroceryDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new GroceryDbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);

        switch (match) {
            case GROCERIES:
                cursor = database.query(GroceryEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case GROCERY_ID:
                selection = GroceryEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = database.query(GroceryEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case GROCERIES:
                return insertGrocery(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    public Uri insertGrocery(Uri uri, ContentValues values) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long id = db.insert(
                GroceryEntry.TABLE_NAME,
                null,
                values
        );
        if(id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for: " + uri);
            return null;
        }
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
