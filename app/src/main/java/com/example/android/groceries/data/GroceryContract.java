package com.example.android.groceries.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ujigt on 6/16/2017.
 */

public class GroceryContract {
    public static final String CONTENT_AUTHORITY = "com.example.android.groceries";
    public static final String PATH_GROCERIES = "groceries";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY );

    private GroceryContract() {}

    public static final class GroceryEntry implements BaseColumns {
        // The Content URI to access the pet data in the provider
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_GROCERIES);

        public final static String TABLE_NAME = "groceries";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_GROCERY_NAME = "name";
        public final static String COLUMN_GROCERY_QUANTITY = "quantity";
        public final static String COLUMN_GROCERY_PRICE = "price";
        public final static String COLUMN_GROCERY_TOTAL = "total";
        public final static String COLUMN_GROCERY_DATE = "created_at";

    }
}
