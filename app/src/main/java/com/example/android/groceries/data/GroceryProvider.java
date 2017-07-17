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
import com.example.android.groceries.data.GroceryContract.HistoryEntry;

/**
 * Created by ujigt on 6/21/2017.
 */

public class GroceryProvider extends ContentProvider {
    /** Tag for the log messages */
    public static final String LOG_TAG = ContentProvider.class.getSimpleName();

    private static final int GROCERIES = 100;
    private static final int GROCERY_ID = 101;
    private static final int GROCERY_SUMMARY = 102;
    private static final int GROCERY_HISTORY = 103;
    private static final int GROCERY_SAVE = 104;
    private static final int GROCERY_SHOW_HISTORY = 105;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(GroceryContract.CONTENT_AUTHORITY, GroceryContract.PATH_GROCERIES, GROCERIES);
        sUriMatcher.addURI(GroceryContract.CONTENT_AUTHORITY, GroceryContract.PATH_GROCERIES + "/#", GROCERY_ID);
        sUriMatcher.addURI(GroceryContract.CONTENT_AUTHORITY, GroceryContract.PATH_HISTORIES + "/summary", GROCERY_SUMMARY);
        sUriMatcher.addURI(GroceryContract.CONTENT_AUTHORITY, GroceryContract.PATH_GROCERIES + "/save", GROCERY_SAVE);
        sUriMatcher.addURI(GroceryContract.CONTENT_AUTHORITY, GroceryContract.PATH_HISTORIES + "/history", GROCERY_HISTORY);
        sUriMatcher.addURI(GroceryContract.CONTENT_AUTHORITY, GroceryContract.PATH_HISTORIES + "/*", GROCERY_SHOW_HISTORY);
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
            case GROCERY_SUMMARY:
                cursor = database.rawQuery("SELECT name, SUM(total) AS total FROM histories GROUP BY name ORDER BY total DESC", null);
                break;
            case GROCERY_HISTORY:
                // cursor = database.rawQuery("SELECT DISTINCT created_at FROM groceries", null);
                cursor = database.query(HistoryEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case GROCERY_SAVE:
                cursor = database.rawQuery("INSERT INTO histories SELECT * FROM groceries", null);
                break;
            case GROCERY_SHOW_HISTORY:
                cursor = database.query(HistoryEntry.TABLE_NAME,
                        new String[] {HistoryEntry._ID, HistoryEntry.COLUMN_GROCERY_NAME, HistoryEntry.COLUMN_GROCERY_QUANTITY, HistoryEntry.COLUMN_GROCERY_TOTAL},
                        "created_at = ?",
                        new String[]{String.valueOf(uri.getLastPathSegment())},
                        null,
                        null,
                        null);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
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
        String name = values.getAsString(GroceryEntry.COLUMN_GROCERY_NAME);
        if(name == null) {
            throw new IllegalArgumentException("Grocert requires a name");
        }

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

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case GROCERIES:
                rowsDeleted = database.delete(GroceryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case GROCERY_ID:
                selection = GroceryEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(GroceryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if(rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case GROCERIES:
                return updateGrocery(uri, values, selection, selectionArgs);
            case GROCERY_ID:
                selection = GroceryEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return updateGrocery(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateGrocery(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if(values.containsKey(GroceryEntry.COLUMN_GROCERY_NAME)) {
            String name = values.getAsString(GroceryEntry.COLUMN_GROCERY_NAME);
            if(name == null) {
                throw new IllegalArgumentException("Grocery requies a name");
            }
        }

        if(values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsUpdated = database.update(GroceryEntry.TABLE_NAME, values, selection, selectionArgs);

        if( rowsUpdated != 0 ) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
