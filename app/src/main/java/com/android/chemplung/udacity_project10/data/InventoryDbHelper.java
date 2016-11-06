package com.android.chemplung.udacity_project10.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.chemplung.udacity_project10.data.InventoryContract.InventoryEntry;

/**
 * Created by NUSNAFIF on 11/5/2016.
 */

public class InventoryDbHelper extends SQLiteOpenHelper {

    /**
     * Inventory Database name
     */
    private static final String DB_NAME = "inventory.db";
    /**
     * Inventory Database version
     */
    private static final int DB_VERSION = 1;
    /**
     * SQL code to create products table
     */
    private static final String SQL_CREATE_PRODUCTS_TABLE = "CREATE TABLE " +
            InventoryEntry.TABLE_NAME + "(" +
            InventoryEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            InventoryEntry.COLUMN_NAME + " TEXT NOT NULL, " +
            InventoryEntry.COLUMN_DESCRIPTION + " TEXT, " +
            InventoryEntry.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL," +
            InventoryEntry.COLUMN_SUPPLIER_EMAIL + " TEXT NOT NULL, " +
            InventoryEntry.COLUMN_PRICE + " REAL NOT NULL, " +
            InventoryEntry.COLUMN_QUANTITY + " INTEGER NOT NULL); ";
    /**
     * SQL code to delete products table
     */
    private static final String SQL_DELETE_PRODUCTS_TABLE =
            "DROP TABLE IF EXISTS " + InventoryEntry.TABLE_NAME + ";";
    /**
     * Create a new instance of the {@link InventoryDbHelper}
     *
     * @param context application context
     */
    public InventoryDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_PRODUCTS_TABLE);
        onCreate(db);
    }
}
