package com.example.android.groceries.data;

import android.provider.BaseColumns;

/**
 * Created by ujigt on 6/16/2017.
 */

public class GroceryContract {

    private GroceryContract() {}

    public static final class GroceryEntry implements BaseColumns {
        public final static String TABLE_NAME = "groceries";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_GROCERY_NAME = "name";
        public final static String COLUMN_GROCERY_QUANTITY = "quantity";
        public final static String COLUMN_GROCERY_PRICE = "price";
        public final static String COLUMN_GROCERY_TOTAL = "total";

    }
}
