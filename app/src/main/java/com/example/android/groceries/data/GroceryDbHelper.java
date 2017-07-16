package com.example.android.groceries.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.groceries.data.GroceryContract.GroceryEntry;

import static com.example.android.groceries.data.GroceryContract.HistoryEntry;

/**
 * Created by ujigt on 6/19/2017.
 */

public class GroceryDbHelper extends SQLiteOpenHelper {

    // If we change the database schema we have to increment the DATABASE_VERSION
    public static final int DATABASE_VERSION = 4;
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
                + GroceryEntry.COLUMN_GROCERY_TOTAL + " REAL NOT NULL DEFAULT 0, "
                + GroceryEntry.COLUMN_GROCERY_DATE + " DATETIME DEFAULT CURRENT_DATE);";

        String SQL_ENTRY_HISTORIES_TABLE = "CREATE TABLE " + HistoryEntry.TABLE_NAME + "("
                + HistoryEntry._ID + " INTEGER, "
                + HistoryEntry.COLUMN_GROCERY_NAME + " TEXT NOT NULL, "
                + HistoryEntry.COLUMN_GROCERY_PRICE + " REAL NOT NULL, "
                + HistoryEntry.COLUMN_GROCERY_QUANTITY + " REAL NOT NULL, "
                + HistoryEntry.COLUMN_GROCERY_TOTAL + " REAL NOT NULL DEFAULT 0, "
                + HistoryEntry.COLUMN_GROCERY_DATE + " DATETIME NOT NULL);";

        db.execSQL(SQL_ENTRY_PETS_TABLE);
        db.execSQL(SQL_ENTRY_HISTORIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if(oldVersion == 1 && newVersion == 2) {
            db.execSQL("drop table groceries");

            String SQL_ENTRY_PETS_TABLE = "CREATE TABLE " + GroceryEntry.TABLE_NAME + "("
                    + GroceryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + GroceryEntry.COLUMN_GROCERY_NAME + " TEXT NOT NULL, "
                    + GroceryEntry.COLUMN_GROCERY_PRICE + " REAL NOT NULL, "
                    + GroceryEntry.COLUMN_GROCERY_QUANTITY + " REAL NOT NULL, "
                    + GroceryEntry.COLUMN_GROCERY_TOTAL + " REAL NOT NULL DEFAULT 0, "
                    + GroceryEntry.COLUMN_GROCERY_DATE + " DATETIME DEFAULT CURRENT_DATE);";

            db.execSQL(SQL_ENTRY_PETS_TABLE);

        } else if (oldVersion == 2 && newVersion == 3) {
                db.execSQL("drop table groceries");

                String SQL_ENTRY_GROCERIES_TABLE = "CREATE TABLE " + GroceryEntry.TABLE_NAME + "("
                        + GroceryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + GroceryEntry.COLUMN_GROCERY_NAME + " TEXT NOT NULL, "
                        + GroceryEntry.COLUMN_GROCERY_PRICE + " REAL NOT NULL, "
                        + GroceryEntry.COLUMN_GROCERY_QUANTITY + " REAL NOT NULL, "
                        + GroceryEntry.COLUMN_GROCERY_TOTAL + " REAL NOT NULL DEFAULT 0, "
                        + GroceryEntry.COLUMN_GROCERY_DATE + " DATETIME DEFAULT CURRENT_DATE);";

                String SQL_ENTRY_HISTORIES_TABLE = "CREATE TABLE " + HistoryEntry.TABLE_NAME + "("
                        + HistoryEntry._ID + " INTEGER PRIMARY KEY, "
                        + HistoryEntry.COLUMN_GROCERY_NAME + " TEXT NOT NULL, "
                        + HistoryEntry.COLUMN_GROCERY_PRICE + " REAL NOT NULL, "
                        + HistoryEntry.COLUMN_GROCERY_QUANTITY + " REAL NOT NULL, "
                        + HistoryEntry.COLUMN_GROCERY_TOTAL + " REAL NOT NULL DEFAULT 0, "
                        + HistoryEntry.COLUMN_GROCERY_DATE + " DATETIME NOT NULL);";

                db.execSQL(SQL_ENTRY_GROCERIES_TABLE);
                db.execSQL(SQL_ENTRY_HISTORIES_TABLE);

        } else if (oldVersion == 3 && newVersion == 4) {
            db.execSQL("drop table if exists groceries");
            db.execSQL("drop table if exists histories");

            String SQL_ENTRY_GROCERIES_TABLE = "CREATE TABLE " + GroceryEntry.TABLE_NAME + "("
                    + GroceryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + GroceryEntry.COLUMN_GROCERY_NAME + " TEXT NOT NULL, "
                    + GroceryEntry.COLUMN_GROCERY_PRICE + " REAL NOT NULL, "
                    + GroceryEntry.COLUMN_GROCERY_QUANTITY + " REAL NOT NULL, "
                    + GroceryEntry.COLUMN_GROCERY_TOTAL + " REAL NOT NULL DEFAULT 0, "
                    + GroceryEntry.COLUMN_GROCERY_DATE + " DATETIME DEFAULT CURRENT_DATE);";

            String SQL_ENTRY_HISTORIES_TABLE = "CREATE TABLE " + HistoryEntry.TABLE_NAME + "("
                    + HistoryEntry._ID + " INTEGER, "
                    + HistoryEntry.COLUMN_GROCERY_NAME + " TEXT NOT NULL, "
                    + HistoryEntry.COLUMN_GROCERY_PRICE + " REAL NOT NULL, "
                    + HistoryEntry.COLUMN_GROCERY_QUANTITY + " REAL NOT NULL, "
                    + HistoryEntry.COLUMN_GROCERY_TOTAL + " REAL NOT NULL DEFAULT 0, "
                    + HistoryEntry.COLUMN_GROCERY_DATE + " DATETIME NOT NULL);";

            db.execSQL(SQL_ENTRY_GROCERIES_TABLE);
            db.execSQL(SQL_ENTRY_HISTORIES_TABLE);
        }
    }
}
