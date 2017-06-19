package com.example.android.groceries;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.groceries.data.GroceryContract.GroceryEntry;
import com.example.android.groceries.data.GroceryDbHelper;

public class EditorActivity extends AppCompatActivity {

    private EditText mNameEditText;
    private EditText mQuantityEditText;
    private EditText mPriceEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Add/Edit Grocery Item");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Find all relevant views we will need to read user input
        mNameEditText = (EditText) findViewById(R.id.edit_grocery_name);
        mPriceEditText = (EditText) findViewById(R.id.edit_grocery_price);
        mQuantityEditText = (EditText) findViewById(R.id.edit_grocery_quantity);
//
//        mTotalButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                float priceFloat = Float.valueOf(mPriceEditText.getText().toString().trim());
//                float quantityFloat = Float.valueOf(mQuantityEditText.getText().toString().trim());
//                float total = priceFloat * quantityFloat;
//                mTotalTextView.setText(String.valueOf(total));
//            }
//        });
    }

    private void insertGrocery() {
        String nameString = mNameEditText.getText().toString().trim();
        float priceFloat = Float.valueOf(mPriceEditText.getText().toString().trim());
        float quantityFloat = Float.valueOf(mQuantityEditText.getText().toString().trim());
        float totalFloat = priceFloat * quantityFloat;

        GroceryDbHelper mDbHelper = new GroceryDbHelper(this);
        // get the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where the column names are keys
        ContentValues values = new ContentValues();
        values.put(GroceryEntry.COLUMN_GROCERY_NAME, nameString);
        values.put(GroceryEntry.COLUMN_GROCERY_PRICE, priceFloat);
        values.put(GroceryEntry.COLUMN_GROCERY_QUANTITY, quantityFloat);
        values.put(GroceryEntry.COLUMN_GROCERY_TOTAL, totalFloat);

        long newRowId;
        newRowId = db.insert(
                GroceryEntry.TABLE_NAME,
                null,
                values
        );

        if (newRowId == -1) {
            Toast.makeText(this, "Error saving grocery item", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Grocery item saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                insertGrocery();
                // Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
