package com.example.contentprovider

import android.content.*
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri

class InventoryProvider : ContentProvider() {

    companion object {
        const val PROVIDER_NAME = "com.example.inventorymanager.InventoryProvider"
        const val URL = "content://$PROVIDER_NAME/inventory"
        val CONTENT_URI: Uri = Uri.parse(URL)

        const val INVENTORY = 1
        const val INVENTORY_ID = 2

        const val TABLE_NAME = DatabaseHelper.TABLE_NAME
        const val COLUMN_ID = DatabaseHelper.COLUMN_ID
        const val COLUMN_ITEM_NAME = DatabaseHelper.COLUMN_ITEM_NAME
        const val COLUMN_QUANTITY = DatabaseHelper.COLUMN_QUANTITY
        const val COLUMN_PRICE = DatabaseHelper.COLUMN_PRICE

        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(PROVIDER_NAME, "inventory", INVENTORY)
            addURI(PROVIDER_NAME, "inventory/#", INVENTORY_ID)
        }
    }

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(): Boolean {
        dbHelper = DatabaseHelper(context!!)
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val qb = SQLiteQueryBuilder()
        qb.tables = TABLE_NAME

        when (uriMatcher.match(uri)) {
            INVENTORY_ID -> qb.appendWhere("$COLUMN_ID = ${uri.lastPathSegment}")
        }

        val db = dbHelper.readableDatabase
        val cursor = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder)
        cursor.setNotificationUri(context?.contentResolver, uri)
        return cursor
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val db = dbHelper.writableDatabase
        val id = db.insert(TABLE_NAME, null, values)
        if (id > 0) {
            context?.contentResolver?.notifyChange(uri, null)
            return ContentUris.withAppendedId(CONTENT_URI, id)
        }
        throw SQLException("Failed to insert row into $uri")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val db = dbHelper.writableDatabase
        val rowsDeleted = db.delete(TABLE_NAME, selection, selectionArgs)
        context?.contentResolver?.notifyChange(uri, null)
        return rowsDeleted
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        val db = dbHelper.writableDatabase
        val rowsUpdated = db.update(TABLE_NAME, values, selection, selectionArgs)
        context?.contentResolver?.notifyChange(uri, null)
        return rowsUpdated
    }

    override fun getType(uri: Uri): String {
        return when (uriMatcher.match(uri)) {
            INVENTORY -> "vnd.android.cursor.dir/$PROVIDER_NAME.inventory"
            INVENTORY_ID -> "vnd.android.cursor.item/$PROVIDER_NAME.inventory"
            else -> throw IllegalArgumentException("Unsupported URI: $uri")
        }
    }
}
