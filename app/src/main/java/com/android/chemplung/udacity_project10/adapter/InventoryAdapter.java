package com.android.chemplung.udacity_project10.adapter;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.chemplung.udacity_project10.DetailActivity;
import com.android.chemplung.udacity_project10.R;
import com.android.chemplung.udacity_project10.data.InventoryContract.InventoryEntry;


/**
 * Created by NUSNAFIF on 11/5/2016.
 */

public class InventoryAdapter extends CursorAdapter {
    private static final String TAG = "CursorAdapter";
    private Context mContext;

    public InventoryAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        this.mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.product_item_detail, parent, false);
    }

    /**
     * Set the views of the list item to values from the cursor
     */
    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        // Get the textviews
        TextView txtName = (TextView) view.findViewById(R.id.product_name_text);
        TextView txtDescription = (TextView) view.findViewById(R.id.product_description_text);
        TextView txtPrice = (TextView) view.findViewById(R.id.product_price);
        TextView txtQuantity = (TextView) view.findViewById(R.id.product_qty_available);

        // get the values
        String name = cursor.getString(cursor.getColumnIndex(InventoryEntry.COLUMN_NAME));
        String description = cursor.getString(cursor.getColumnIndex(InventoryEntry.COLUMN_DESCRIPTION));
        double price = cursor.getDouble(cursor.getColumnIndex(InventoryEntry.COLUMN_PRICE));
        String quantity = String.valueOf(cursor.getInt(cursor.getColumnIndex(InventoryEntry.COLUMN_QUANTITY)));

        // set the text
        txtName.setText(name);
        txtDescription.setText(description);
        txtPrice.setText(String.format("RM %.2f", price));
        txtQuantity.setText(quantity);

        // get the id of the item
        final long id = cursor.getLong(cursor.getColumnIndex(InventoryEntry.COLUMN_ID));

        // get the button to sell an item
        Button btnSell = (Button) view.findViewById(R.id.qty_sell_button);
        // set the "Tag" to be the cursor position so we know which button got clicked
        btnSell.setTag(id);
        // Decrease the quantity
        btnSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id = (long)v.getTag();
                AdjustQuantity(id, -1);
            }
        });

        // set the "Tag" to the cursor position
        txtName.setTag(id);
        txtDescription.setTag(id);

        // set the listener that will open the detail view
        txtName.setOnClickListener(detailClickListener);
        txtDescription.setOnClickListener(detailClickListener);
    }

    /**
     * Detail view
     */
    private View.OnClickListener detailClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            long id = (long) v.getTag();
            Intent detailIntent = new Intent(mContext, DetailActivity.class);
            Uri detailUri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, id);
            detailIntent.setData(detailUri);
            mContext.startActivity(detailIntent);
        }
    };

    /**
     * Update the quantity
     *
     * @param id  ID of the product in the table
     * @param byVol how many to add or subtract
     */
    private void AdjustQuantity(long id, int byVol) {
        Uri updateUri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, id);

        // first, get the current quantity
        Cursor c = mContext.getContentResolver().query(updateUri,
                new String[]{InventoryEntry.COLUMN_QUANTITY}, null, null, null);
        int qty=-1;
        if (c.moveToFirst()){
            qty = c.getInt(c.getColumnIndex(InventoryEntry.COLUMN_QUANTITY));
        }
        else{
            Log.e(TAG, "Could not find product in database");
            return;
        }
        int newQty = qty + byVol;
        if (newQty<0){
            Log.e(TAG, "Can not sell more than available quantity");
            Toast.makeText(mContext, "Quantity cannot go below zero", Toast.LENGTH_SHORT).show();
            return;
        }
        ContentValues values = new ContentValues();
        values.put(InventoryEntry.COLUMN_QUANTITY, newQty);
        mContext.getContentResolver().update(updateUri, values, null, null);
    }

}