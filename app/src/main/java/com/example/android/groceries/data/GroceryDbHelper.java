package com.example.android.groceries.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.groceries.data.GroceryContract.GroceryEntry;

/**
 * Created by ujigt on 6/19/2017.
 */

public class GroceryDbHelper extends SQLiteOpenHelper {

    // If we change the database schema we have to increment the DATABASE_VERSION
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Groceries.db";

    public GroceryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a string that contains the SQL statement to create the groceries table
        String SQL_ENTRY_PETS_TABLE = "CREATE TABLE " + GroceryEntry.TABLE_NAME + "("
                + GroceryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + GroceryEntry.COLUMN_GROCERY_NAME + " TEXT NOT NULL, "
                + GroceryEntry.COLUMN_GROCERY_PRICE + " REAL NOT NULL, "
                + GroceryEntry.COLUMN_GROCERY_QUANTITY + " REAL NOT NULL, "
                + GroceryEntry.COLUMN_GROCERY_TOTAL + " REAL NOT NULL DEFAULT 0);";

        db.execSQL(SQL_ENTRY_PETS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
