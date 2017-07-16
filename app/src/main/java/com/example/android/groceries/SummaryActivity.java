package com.example.android.groceries;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.android.groceries.data.GroceryContract.GroceryEntry;
import com.example.android.groceries.data.GroceryContract.HistoryEntry;

public class SummaryActivity extends AppCompatActivity {

    String summaryPath = "summary";

    private Uri summaryUri = Uri.withAppendedPath(HistoryEntry.CONTENT_URI, summaryPath);

    TextView mSummaryTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        mSummaryTextView = (TextView) findViewById(R.id.tv_grocery_summary);
        getSummary();
    }

    private void getSummary() {
        mSummaryTextView.setText("Grocery item: " + "   " + "Total: (Nu.)" +"\n");

        Cursor cursor = getContentResolver().query(
                summaryUri,
                null,
                null,
                null,
                null );

        try {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(GroceryEntry.COLUMN_GROCERY_NAME));
                float total = cursor.getFloat(cursor.getColumnIndexOrThrow(GroceryEntry.COLUMN_GROCERY_TOTAL));
                mSummaryTextView.append(name + "   " + String.valueOf(total) + "\n");
            }
        } finally {
            cursor.close();
        }
    }
}
