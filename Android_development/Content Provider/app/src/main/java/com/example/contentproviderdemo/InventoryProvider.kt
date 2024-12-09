package com.example.contentproviderdemo

import android.content.*
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri

class InventoryProvider : ContentProvider() {

    companion object {
        const val PROVIDER_NAME = "com.example.contentproviderdemo.InventoryProvider"
        private const val URL = "content://$PROVIDER_NAME/inventory"
        val CONTENT_URI: Uri = Uri.parse(URL)

        const val INVENTORY = 1
        const val INVENTORY_ID = 2

        val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(PROVIDER_NAME, "inventory", INVENTORY)
            addURI(PROVIDER_NAME, "inventory/#", INVENTORY_ID)
        }

        val INVENTORY_PROJECTION_MAP: Map<String, String>? = null

        const val TABLE_NAME = DatabaseHelper.TABLE_NAME
        const val COLUMN_ID = DatabaseHelper.COLUMN_ID
        const val COLUMN_ITEM_NAME = DatabaseHelper.COLUMN_ITEM_NAME
        const val COLUMN_QUANTITY = DatabaseHelper.COLUMN_QUANTITY
        const val COLUMN_PRICE = DatabaseHelper.COLUMN_PRICE
    }

    private lateinit var db: SQLiteDatabase

    override fun onCreate(): Boolean {
        val context = context ?: return false
        val dbHelper = DatabaseHelper(context)
        db = dbHelper.writableDatabase
        return db.isOpen
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri {
        val rowID = db.insert(TABLE_NAME, null, values)
        if (rowID > 0) {
            val uriResult = ContentUris.withAppendedId(CONTENT_URI, rowID)
            context?.contentResolver?.notifyChange(uriResult, null)
            return uriResult
        } else {
            throw SQLException("Failed to add a record into $uri")
        }
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        val qb = SQLiteQueryBuilder()
        qb.tables = TABLE_NAME

        when (uriMatcher.match(uri)) {
            INVENTORY -> qb.projectionMap = INVENTORY_PROJECTION_MAP
            INVENTORY_ID -> qb.appendWhere("$COLUMN_ID = ${uri.lastPathSegment}")
            else -> throw IllegalArgumentException("Unknown URI $uri")
        }

        val cursor = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder ?: COLUMN_ITEM_NAME)
        cursor.setNotificationUri(context?.contentResolver, uri)
        return cursor
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val count = when (uriMatcher.match(uri)) {
            INVENTORY -> db.delete(TABLE_NAME, selection, selectionArgs)
            INVENTORY_ID -> {
                val id = uri.lastPathSegment
                db.delete(
                    TABLE_NAME,
                    "$COLUMN_ID = $id" + if (!selection.isNullOrEmpty()) " AND ($selection)" else "",
                    selectionArgs
                )
            }
            else -> throw IllegalArgumentException("Unknown URI $uri")
        }
        context?.contentResolver?.notifyChange(uri, null)
        return count
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        val count = when (uriMatcher.match(uri)) {
            INVENTORY -> db.update(TABLE_NAME, values, selection, selectionArgs)
            INVENTORY_ID -> {
                val id = uri.lastPathSegment
                db.update(
                    TABLE_NAME,
                    values,
                    "$COLUMN_ID = $id" + if (!selection.isNullOrEmpty()) " AND ($selection)" else "",
                    selectionArgs
                )
            }
            else -> throw IllegalArgumentException("Unknown URI $uri")
        }
        context?.contentResolver?.notifyChange(uri, null)
        return count
    }

    override fun getType(uri: Uri): String {
        return when (uriMatcher.match(uri)) {
            INVENTORY -> "vnd.android.cursor.dir/vnd.$PROVIDER_NAME.inventory"
            INVENTORY_ID -> "vnd.android.cursor.item/vnd.$PROVIDER_NAME.inventory"
            else -> throw IllegalArgumentException("Unsupported URI: $uri")
        }
    }
}
