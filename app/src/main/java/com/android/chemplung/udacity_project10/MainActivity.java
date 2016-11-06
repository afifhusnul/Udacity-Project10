package com.android.chemplung.udacity_project10;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.chemplung.udacity_project10.adapter.InventoryAdapter;
import com.android.chemplung.udacity_project10.data.InventoryContract.InventoryEntry;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "Main Activity";
    private InventoryAdapter adapter;
    private static final int LOADER_REFERENCE = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btn_main_new_product);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newProductIntent = new Intent(getApplicationContext(), ProductActivity.class);
                startActivity(newProductIntent);
            }
        });
        adapter = new InventoryAdapter(this, null, 0);
        ListView productListView = (ListView) findViewById(R.id.list_product);
        View emptyView = findViewById(R.id.empty_view);
        productListView.setEmptyView(emptyView);
        productListView.setAdapter(adapter);

        getSupportLoaderManager().initLoader(LOADER_REFERENCE, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.main_menu_insert_dummy_data):
                CreateDummyData();
                return true;
            case (R.id.main_menu_delete_all):
                showDeleteConfirmationDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Dialog for delete confirmation in DB
     */
    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage("Delete all data in the database?");
        alertBuilder.setPositiveButton("Delete All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAllProducts();
            }
        });
        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    /**
     * Delete all data in the DB and display a message with the result
     */
    private void deleteAllProducts() {
        int numRowsDeleted = getContentResolver().delete(InventoryEntry.CONTENT_URI, null, null);
        if (numRowsDeleted > 0) {
            Toast.makeText(this, "All product deleted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error delete product", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Create some data in the table
     */
    private void CreateDummyData() {

        ContentValues values = new ContentValues();

        // create some products from Aperture Science
        values.put(InventoryEntry.COLUMN_NAME, getString(R.string.product_name1));
        values.put(InventoryEntry.COLUMN_DESCRIPTION, getString(R.string.product_description1));
        values.put(InventoryEntry.COLUMN_SUPPLIER_NAME, getString(R.string.product_supplier_name1));
        values.put(InventoryEntry.COLUMN_SUPPLIER_EMAIL, getString(R.string.product_supplier_email1));
        values.put(InventoryEntry.COLUMN_QUANTITY, 100);
        values.put(InventoryEntry.COLUMN_PRICE, 5.99);
        Uri newProductUri = getContentResolver().insert(InventoryEntry.CONTENT_URI, values);

        values.put(InventoryEntry.COLUMN_NAME, getString(R.string.product_name2));
        values.put(InventoryEntry.COLUMN_DESCRIPTION, getString(R.string.product_description2));
        values.put(InventoryEntry.COLUMN_SUPPLIER_NAME, getString(R.string.product_supplier_name2));
        values.put(InventoryEntry.COLUMN_SUPPLIER_EMAIL, getString(R.string.product_supplier_email2));
        values.put(InventoryEntry.COLUMN_QUANTITY, 88);
        values.put(InventoryEntry.COLUMN_PRICE, 7);
        newProductUri = getContentResolver().insert(InventoryEntry.CONTENT_URI, values);

        values.put(InventoryEntry.COLUMN_NAME, getString(R.string.product_name3));
        values.put(InventoryEntry.COLUMN_DESCRIPTION, getString(R.string.product_description3));
        values.put(InventoryEntry.COLUMN_SUPPLIER_NAME, getString(R.string.product_supplier_name3));
        values.put(InventoryEntry.COLUMN_SUPPLIER_EMAIL, getString(R.string.product_supplier_email3));
        values.put(InventoryEntry.COLUMN_QUANTITY, 89);
        values.put(InventoryEntry.COLUMN_PRICE, 8.50);
        newProductUri = getContentResolver().insert(InventoryEntry.CONTENT_URI, values);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == LOADER_REFERENCE) {

            Uri uri = InventoryEntry.CONTENT_URI;
            String[] columns = new String[]{
                    InventoryEntry.COLUMN_ID,
                    InventoryEntry.COLUMN_NAME,
                    InventoryEntry.COLUMN_DESCRIPTION,
                    InventoryEntry.COLUMN_QUANTITY,
                    InventoryEntry.COLUMN_PRICE};
            return new CursorLoader(this,
                    uri, columns,
                    null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (adapter != null) {
            adapter.swapCursor(null);
        }
    }

    private Uri getDrawableResourceUri(int resource) {
        Uri result = Uri.parse(
                ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                        getResources().getResourcePackageName(resource) + "/" +
                        getResources().getResourceTypeName(resource) + "/" +
                        getResources().getResourceEntryName(resource));
        return result;
    }
}