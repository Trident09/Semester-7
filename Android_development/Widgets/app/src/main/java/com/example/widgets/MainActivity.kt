package com.example.widgets

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Calendar

// Array of images to cycle through
private val images = arrayOf(
    R.drawable.image_pressed,
    R.drawable.image_focussed,
    R.drawable.sample_image // Default image
)
private var currentImageIndex = 0

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // References to Widgets
        val textView: TextView = findViewById(R.id.textView)
        val button: Button = findViewById(R.id.button)
        val imageView: ImageView = findViewById(R.id.imageView)
        val progressBar: ProgressBar = findViewById(R.id.progressBar)
        val editText: EditText = findViewById(R.id.editText)
        val calendarView: CalendarView = findViewById(R.id.calendarView)
        val dateTimeButton: Button = findViewById(R.id.dateTimeButton)
        val toggleImageButton: Button = findViewById(R.id.toggleImageButton)
        val listView: ListView = findViewById(R.id.listView)

        // Set up the button to toggle images
        toggleImageButton.setOnClickListener {
            // Cycle to the next image
            currentImageIndex = (currentImageIndex + 1) % images.size
            imageView.setImageResource(images[currentImageIndex])
        }

        // Sample Data for RecyclerView
        val listItems = listOf("Item 1", "Item 2", "Item 3", "Item 4")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems)
        listView.adapter = adapter


        // Button Click Action
        button.setOnClickListener {
            val name = editText.text.toString()
            progressBar.visibility = View.VISIBLE
            textView.text = "Loading... Please wait."

            // Simulate a task
            button.postDelayed({
                progressBar.visibility = View.GONE
                textView.text = if (name.isNotEmpty()) "Hello, $name!" else "Please enter your name."
            }, 2000) // 2-second delay
        }

        // CalendarView Date Selection
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            textView.text = "Selected Date: $dayOfMonth/${month + 1}/$year"
        }

        // DateTime Picker
        dateTimeButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            DatePickerDialog(this, { _, y, m, d ->
                TimePickerDialog(this, { _, h, min ->
                    textView.text = "Selected DateTime: $d/${m + 1}/$y $h:$min"
                }, hour, minute, true).show()
            }, year, month, day).show()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
