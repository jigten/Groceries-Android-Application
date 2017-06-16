package com.example.android.groceries;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditorActivity extends AppCompatActivity {

    private EditText mNameEditText;
    private EditText mQuantityEditText;
    private EditText mPriceEditText;
    private TextView mTotalTextView;
    private Button mTotalButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Add/Edit Grocery Item");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Find all relevant views we will need to read user input
        mNameEditText = (EditText) findViewById(R.id.edit_grocery_name);
        mPriceEditText = (EditText) findViewById(R.id.edit_grocery_price);
        mQuantityEditText = (EditText) findViewById(R.id.edit_grocery_quantity);
        mTotalTextView = (TextView) findViewById(R.id.text_view_total);
        mTotalButton = (Button) findViewById(R.id.butotn_calculate_total);
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
                // Do nothing for now
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
