package com.example.android.groceries;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.groceries.data.GroceryContract.GroceryEntry;
import com.example.android.groceries.data.GroceryDbHelper;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_GROCERY_LOADER = 0;
    private Uri mCurrentGroceryUri;

    private EditText mNameEditText;
    private EditText mQuantityEditText;
    private EditText mPriceEditText;
    private TextView mDateTextView;

    private boolean mGroceryHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mGroceryHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        mCurrentGroceryUri = intent.getData();

        if(mCurrentGroceryUri == null) {
            setTitle("Add a grocery item");
            invalidateOptionsMenu();
        } else {
            setTitle("Edit grocery item");
            getSupportLoaderManager().initLoader(EXISTING_GROCERY_LOADER, null, this);
        }

        // Find all relevant views we will need to read user input
        mNameEditText = (EditText) findViewById(R.id.edit_grocery_name);
        mPriceEditText = (EditText) findViewById(R.id.edit_grocery_price);
        mQuantityEditText = (EditText) findViewById(R.id.edit_grocery_quantity);
        mDateTextView = (TextView) findViewById(R.id.tv_grocery_date);

        mNameEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mQuantityEditText.setOnTouchListener(mTouchListener);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if(mCurrentGroceryUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
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

        if(mCurrentGroceryUri == null) {
            Uri newUri = getContentResolver().insert(GroceryEntry.CONTENT_URI, values);
            Toast.makeText(this, "Added new grocery item", Toast.LENGTH_SHORT).show();
        } else {
            int rowAffected = getContentResolver().update(mCurrentGroceryUri, values, null, null);
            Toast.makeText(this, "Updated grocery item", Toast.LENGTH_SHORT).show();
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
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                if(!mGroceryHasChanged) {
                    NavUtils.navigateUpFromSameTask(this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                showUnsavedChangesDialogBox(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete this grocery item?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteGrocery();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteGrocery() {
        if(mCurrentGroceryUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentGroceryUri, null, null);

            if(rowsDeleted == 0) {
                Toast.makeText(this, "Error with deleting grocery item", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Grocery item deleted", Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        if(!mGroceryHasChanged) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }};
        showUnsavedChangesDialogBox(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Since the editor shows all pet attributes, define a projection that contains
        // all columns from the pet table
        String[] projection = {
                GroceryEntry._ID,
                GroceryEntry.COLUMN_GROCERY_NAME,
                GroceryEntry.COLUMN_GROCERY_QUANTITY,
                GroceryEntry.COLUMN_GROCERY_PRICE,
                GroceryEntry.COLUMN_GROCERY_DATE};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentGroceryUri,         // Query the content URI for the current pet
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(GroceryEntry.COLUMN_GROCERY_NAME);
            int quantityColumnIndex = cursor.getColumnIndex(GroceryEntry.COLUMN_GROCERY_QUANTITY);
            int priceColumnIndex = cursor.getColumnIndex(GroceryEntry.COLUMN_GROCERY_PRICE);
            int dateColumnIndex = cursor.getColumnIndex(GroceryEntry.COLUMN_GROCERY_DATE);

            String name = cursor.getString(nameColumnIndex);
            float quantity = cursor.getFloat(quantityColumnIndex);
            float price = cursor.getFloat(priceColumnIndex);
            String groceryDate = cursor.getString(dateColumnIndex);
            mNameEditText.setText(name);
            mPriceEditText.setText(String.valueOf(price));
            mQuantityEditText.setText(String.valueOf(quantity));
            mDateTextView.setText(groceryDate);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNameEditText.setText("");
        mPriceEditText.setText("");
        mQuantityEditText.setText("");
    }

    private void showUnsavedChangesDialogBox(DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Discard your changes and quit editing?");
        builder.setPositiveButton("Discard", discardButtonClickListener);
        builder.setNegativeButton("Keep Editing", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
