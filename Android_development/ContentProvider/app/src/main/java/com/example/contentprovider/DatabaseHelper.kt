package com.example.contentprovider

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "inventory.db"
        const val DATABASE_VERSION = 1

        const val TABLE_NAME = "inventory"
        const val COLUMN_ID = "_id"
        const val COLUMN_ITEM_NAME = "item_name"
        const val COLUMN_QUANTITY = "quantity"
        const val COLUMN_PRICE = "price"

        private const val CREATE_TABLE =
            "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_ITEM_NAME TEXT NOT NULL, " +
                    "$COLUMN_QUANTITY INTEGER NOT NULL, " +
                    "$COLUMN_PRICE REAL NOT NULL);"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}
