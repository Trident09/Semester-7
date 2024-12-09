package com.example.contentprovider

import android.content.ContentValues
import android.database.Cursor
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val itemName = findViewById<EditText>(R.id.itemName)
        val quantity = findViewById<EditText>(R.id.quantity)
        val price = findViewById<EditText>(R.id.price)
        val addItemButton = findViewById<Button>(R.id.addItemButton)
        val viewItemsButton = findViewById<Button>(R.id.viewItemsButton)
        val resultView = findViewById<TextView>(R.id.resultView)

        addItemButton.setOnClickListener {
            val name = itemName.text.toString()
            val qty = quantity.text.toString()
            val prc = price.text.toString()

            if (name.isEmpty() || qty.isEmpty() || prc.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val values = ContentValues().apply {
                put(InventoryProvider.COLUMN_ITEM_NAME, name)
                put(InventoryProvider.COLUMN_QUANTITY, qty.toInt())
                put(InventoryProvider.COLUMN_PRICE, prc.toDouble())
            }
            contentResolver.insert(InventoryProvider.CONTENT_URI, values)
            Toast.makeText(this, "Item Added", Toast.LENGTH_SHORT).show()

            itemName.text.clear()
            quantity.text.clear()
            price.text.clear()
        }

        viewItemsButton.setOnClickListener {
            val cursor: Cursor? = contentResolver.query(
                InventoryProvider.CONTENT_URI,
                null, null, null, null
            )
            val builder = StringBuilder()

            cursor?.let {
                while (it.moveToNext()) {
                    val id = it.getInt(it.getColumnIndexOrThrow(InventoryProvider.COLUMN_ID))
                    val name = it.getString(it.getColumnIndexOrThrow(InventoryProvider.COLUMN_ITEM_NAME))
                    val qty = it.getInt(it.getColumnIndexOrThrow(InventoryProvider.COLUMN_QUANTITY))
                    val prc = it.getDouble(it.getColumnIndexOrThrow(InventoryProvider.COLUMN_PRICE))
                    builder.append("$id. $name - Quantity: $qty, Price: $prc\n")
                }
                it.close()
            }
            resultView.text = builder.toString()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}