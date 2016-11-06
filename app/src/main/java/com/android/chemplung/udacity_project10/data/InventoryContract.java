package com.android.chemplung.udacity_project10.data;

import android.content.ContentResolver;
import android.net.Uri;

/**
 * Created by NUSNAFIF on 11/5/2016.
 */

public final class InventoryContract {
    public static final String CONTENT_AUTHORITY = "com.android.chemplung.udacity_project10";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_PRODUCTS = "inventory";

    private InventoryContract() {    }

    /**
     * Products Table
     */
    public static final class InventoryEntry {
        public static final String TABLE_NAME = "inventory";

        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_SUPPLIER_NAME = "supplier_name";
        public static final String COLUMN_SUPPLIER_EMAIL = "supplier_email";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_QUANTITY = "quantity";

        // elements for ContentProvider
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCTS);
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
                + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;
        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
                + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;
    }
}
