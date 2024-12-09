package com.example.inventorymanager

import android.content.ContentValues
import android.database.Cursor
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.contentproviderdemo.InventoryProvider
import com.example.contentproviderdemo.ui.theme.ContentProviderDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ContentProviderDemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    InventoryManagerUI(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun InventoryManagerUI(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var itemName by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    Column(modifier = modifier.padding(16.dp)) {
        OutlinedTextField(
            value = itemName,
            onValueChange = { itemName = it },
            label = { Text("Item Name") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = quantity,
            onValueChange = { quantity = it },
            label = { Text("Quantity") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Price") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                if (itemName.isNotEmpty() && quantity.isNotEmpty() && price.isNotEmpty()) {
                    val values = ContentValues().apply {
                        put(InventoryProvider.COLUMN_ITEM_NAME, itemName)
                        put(InventoryProvider.COLUMN_QUANTITY, quantity.toInt())
                        put(InventoryProvider.COLUMN_PRICE, price.toDouble())
                    }
                    val uri = context.contentResolver.insert(InventoryProvider.CONTENT_URI, values)
                    if (uri != null) {
                        Toast.makeText(context, "Item Added", Toast.LENGTH_SHORT).show()
                        itemName = ""
                        quantity = ""
                        price = ""
                    } else {
                        Toast.makeText(context, "Error Adding Item", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text("Add Item")
        }
        Button(
            onClick = {
                val cursor: Cursor? = context.contentResolver.query(
                    InventoryProvider.CONTENT_URI,
                    arrayOf(
                        InventoryProvider.COLUMN_ID,
                        InventoryProvider.COLUMN_ITEM_NAME,
                        InventoryProvider.COLUMN_QUANTITY,
                        InventoryProvider.COLUMN_PRICE
                    ),
                    null, null, null
                )
                cursor?.let {
                    val builder = StringBuilder()
                    while (it.moveToNext()) {
                        val id = it.getInt(it.getColumnIndexOrThrow(InventoryProvider.COLUMN_ID))
                        val name = it.getString(it.getColumnIndexOrThrow(InventoryProvider.COLUMN_ITEM_NAME))
                        val qty = it.getInt(it.getColumnIndexOrThrow(InventoryProvider.COLUMN_QUANTITY))
                        val prc = it.getDouble(it.getColumnIndexOrThrow(InventoryProvider.COLUMN_PRICE))
                        builder.append("$id. $name - Quantity: $qty, Price: $prc\n")
                    }
                    result = builder.toString()
                    it.close()
                } ?: run {
                    result = "No Items Found"
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text("Retrieve Inventory")
        }
        Text(
            text = result,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun InventoryManagerPreview() {
    ContentProviderDemoTheme() {
        InventoryManagerUI()
    }
}
